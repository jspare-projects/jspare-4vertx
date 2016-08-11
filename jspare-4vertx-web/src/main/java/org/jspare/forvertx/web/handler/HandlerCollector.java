/*
 * Copyright 2016 JSpare.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.jspare.forvertx.web.handler;

import static org.jspare.forvertx.web.commons.Definitions4Vertx.HANDLERS_TYPES;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.HTTP_METHODS_TYPES;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.jspare.core.exception.InfraRuntimeException;
import org.jspare.forvertx.web.mapping.content.Consumes;
import org.jspare.forvertx.web.mapping.content.Produces;
import org.jspare.forvertx.web.mapping.handlers.BlockingHandler;
import org.jspare.forvertx.web.mapping.handlers.FailureHandler;
import org.jspare.forvertx.web.mapping.handlers.Handler;
import org.jspare.forvertx.web.mapping.middlewares.Before;
import org.jspare.forvertx.web.mapping.subrouter.IgnoreSubRouter;
import org.jspare.forvertx.web.mapping.subrouter.SubRouter;
import org.jspare.forvertx.web.middleware.Middleware;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HandlerCollector {

	public static Collection<HandlerData> collect(Class<?> clazz, Set<Middleware> beforeMiddleware, Set<Middleware> afterMiddleware) {

		Set<HandlerData> collectedHandlers = new HashSet<>();

		List<Annotation> httpMethodsAnnotations = new ArrayList<>(getHttpMethodsPresents(clazz));

		for (Method method : clazz.getDeclaredMethods()) {

			if (!isHandler(method)) {

				continue;
			}

			final List<Annotation> handlerHttpMethodsAnnotations = new ArrayList<>();
			handlerHttpMethodsAnnotations.addAll(httpMethodsAnnotations);

			String consumes = method.isAnnotationPresent(Consumes.class) ? method.getAnnotation(Consumes.class).value() : StringUtils.EMPTY;
			String produces = method.isAnnotationPresent(Produces.class) ? method.getAnnotation(Produces.class).value() : StringUtils.EMPTY;

			HandlerData defaultHandlerData = new HandlerData().clazz(clazz).method(method).consumes(consumes).produces(produces);
			defaultHandlerData.before(beforeMiddleware);
			defaultHandlerData.before().addAll(collectBeforeMiddlewares(method));
			defaultHandlerData.after(afterMiddleware);
			defaultHandlerData.after().addAll(collectAfterMiddlewares(method));

			if (hasHttpMethodsPresents(method)) {

				handlerHttpMethodsAnnotations.clear();
				handlerHttpMethodsAnnotations.addAll(getHttpMethodsPresents(method));
			}

			getHandlersPresents(method).forEach(handlerType -> {

				try {
					
					//Extract order from Handler, all Hanlder having order() method
					int order = annotationMethod(handlerType, "order");

					HandlerData handlerData = (HandlerData) defaultHandlerData.clone();
					handlerData.order(order);
					
					if (isHandlerAnnotation(handlerType, Handler.class)) {

						handlerData.handlerType(HandlerType.DEFAULT);
					} else if (isHandlerAnnotation(handlerType, FailureHandler.class)) {

						handlerData.handlerType(HandlerType.DEFAULT);
					} else if (isHandlerAnnotation(handlerType, BlockingHandler.class)) {

						handlerData.handlerType(HandlerType.BLOCKING);
					}

					if (handlerHttpMethodsAnnotations.isEmpty()) {

						collectedHandlers.add(handlerData);
					} else {

						collectedHandlers.addAll(collectByMethods(handlerData, handlerHttpMethodsAnnotations));
					}

				} catch (Exception e) {

					log.warn("Ignoring handler class {} method {} - {}", clazz.getName(), method.getName(), e.getMessage());
				}
			});
		}
		return collectedHandlers;
	}

	protected static boolean isHandlerAnnotation(Annotation handlerType, Class<?> element) {
		return handlerType.annotationType().equals(element);
	}

	@SuppressWarnings("unchecked")
	private static <T> T annotationMethod(Annotation annotation, String methodRef) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method method = annotation.annotationType().getMethod(methodRef);
		return (T) method.invoke(annotation);
	}

	private static Collection<? extends Middleware> collectAfterMiddlewares(Method method) {
		if (!method.isAnnotationPresent(Before.class)) {

			return Collections.emptyList();
		}
		Before before = method.getAnnotation(Before.class);
		return Arrays.asList(before.value()).stream().map(clazz -> {

			try {

				return (Middleware) clazz.newInstance();
			} catch (Exception e) {
				return null;
			}
		}).collect(Collectors.toList());
	}

	private static Collection<Middleware> collectBeforeMiddlewares(Method method) {

		if (!method.isAnnotationPresent(Before.class)) {

			return Collections.emptyList();
		}
		Before before = method.getAnnotation(Before.class);
		return Arrays.asList(before.value()).stream().map(clazz -> {

			try {

				return (Middleware) clazz.newInstance();
			} catch (Exception e) {

				throw new InfraRuntimeException(String.format("Cannot load handler of method %s invalid middleware %s", method.getName(), clazz.getName()), e);
			}
		}).collect(Collectors.toList());
	}
	
	private static Collection<HandlerData> collectByMethods(HandlerData handlerSource, List<Annotation> httpMethodsAnnotations) {

		return httpMethodsAnnotations.stream().map(annotationHttpMethod -> {

			try {

				HandlerData handlerData = (HandlerData) handlerSource.clone();

				String prefix = StringUtils.EMPTY;
				String path =  annotationMethod(annotationHttpMethod, "value");
				boolean isRegexPath = annotationMethod(annotationHttpMethod, "regex");

				if (handlerData.clazz().isAnnotationPresent(SubRouter.class) && !handlerData.method().isAnnotationPresent(IgnoreSubRouter.class)) {

					prefix = handlerData.clazz().getAnnotation(SubRouter.class).value();
				}
				handlerData.patch(String.format("%s%s", prefix, path));
				handlerData.pathRegex(isRegexPath);
				handlerData.httpMethod(annotationHttpMethod.annotationType().getSimpleName().toUpperCase());

				return handlerData;
			} catch (Exception e) {

				log.warn("Ignoring handler class {} method {} - {}", handlerSource.clazz().getName(), handlerSource.method().getName(), e);
			}
			return null;
		}).collect(Collectors.toList());
	}
	
	@SuppressWarnings("unchecked")
	private static List<Annotation> getHandlersPresents(Method method) {

		return Arrays.asList(HANDLERS_TYPES).stream().filter(handlerType -> method.isAnnotationPresent((Class<? extends Annotation>) handlerType))
				.map(handlerType -> method.getAnnotation((Class<? extends Annotation>) handlerType)).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	private static List<Annotation> getHttpMethodsPresents(AnnotatedElement element) {
		List<Class<?>> filteredClazz = new ArrayList<>();
		filteredClazz.addAll(Arrays.asList(HTTP_METHODS_TYPES));
		filteredClazz.removeIf(clazzHttpMethodType -> !element.isAnnotationPresent((Class<? extends Annotation>) clazzHttpMethodType));
		return filteredClazz.stream().map(httpMethodClazz -> element.getAnnotation((Class<? extends Annotation>) httpMethodClazz)).collect(Collectors.toList());
	}

	private static boolean hasHttpMethodsPresents(Method method) {

		return getHttpMethodsPresents(method).stream().filter(annotation -> method.isAnnotationPresent(annotation.annotationType())).count() >= 1;
	}

	private static boolean isHandler(Method method) {

		return getHandlersPresents(method).stream().filter(handlerAnnotation -> method.isAnnotationPresent(handlerAnnotation.annotationType())).count() >= 1;
	}
}