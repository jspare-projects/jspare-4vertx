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

import java.lang.annotation.Annotation;
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
import org.jspare.forvertx.web.mapping.method.Connect;
import org.jspare.forvertx.web.mapping.method.Delete;
import org.jspare.forvertx.web.mapping.method.Get;
import org.jspare.forvertx.web.mapping.method.Head;
import org.jspare.forvertx.web.mapping.method.Options;
import org.jspare.forvertx.web.mapping.method.Other;
import org.jspare.forvertx.web.mapping.method.Path;
import org.jspare.forvertx.web.mapping.method.Post;
import org.jspare.forvertx.web.mapping.method.Put;
import org.jspare.forvertx.web.mapping.method.Trace;
import org.jspare.forvertx.web.mapping.middlewares.Before;
import org.jspare.forvertx.web.mapping.subrouter.IgnoreSubRouter;
import org.jspare.forvertx.web.mapping.subrouter.SubRouter;
import org.jspare.forvertx.web.middleware.Middleware;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HandlerCollector {

	public static Set<HandlerData> collect(Class<?> clazz, Set<Middleware> beforeMiddleware, Set<Middleware> afterMiddleware) {

		Set<HandlerData> collectedHandlers = new HashSet<>();
		
		List<Class<? extends Annotation>> clazzHttpMethods = new ArrayList<>(getHttpMethodsPresents(clazz)); 
		
		for (Method method : clazz.getDeclaredMethods()) {

			if (!isHandler(method)) {

				continue;
			}
			
			final List<Class<? extends Annotation>> handlerHttpMethods = new ArrayList<>();
			handlerHttpMethods.addAll(clazzHttpMethods);
			
			String consumes = method.isAnnotationPresent(Consumes.class) ? method.getAnnotation(Consumes.class).value() : StringUtils.EMPTY;
			String produces = method.isAnnotationPresent(Produces.class) ? method.getAnnotation(Produces.class).value() : StringUtils.EMPTY;

			HandlerData defaultHandlerData = new HandlerData().clazz(clazz).method(method).consumes(consumes).produces(produces);
			defaultHandlerData.before(beforeMiddleware);
			defaultHandlerData.before().addAll(collectBeforeMiddlewares(method));
			defaultHandlerData.after(afterMiddleware);
			defaultHandlerData.after().addAll(collectAfterMiddlewares(method));

			if(hasHttpMethodsPresents(method)){
				
				handlerHttpMethods.clear();
				handlerHttpMethods.addAll(getHttpMethodsPresents(method));
			} 

			getHandlersPresents(method).forEach(handlerType -> {

				try {

					HandlerData handlerData = (HandlerData) defaultHandlerData.clone();
					if (handlerType.equals(Handler.class)) {

						handlerData.handlerType(HandlerType.DEFAULT);
					} else if (handlerType.equals(FailureHandler.class)) {

						handlerData.handlerType(HandlerType.DEFAULT);
					} else if (handlerType.equals(Handler.class)) {

						handlerData.handlerType(HandlerType.BLOCKING);
					}
					
					if (handlerHttpMethods.isEmpty()) {

						collectedHandlers.add(handlerData);
					} else {

						collectedHandlers.addAll(collectByMethods(handlerData, handlerHttpMethods));
					}

				} catch (Exception e) {

					log.warn("Ignoring handler class {} method {} - {}", clazz.getName(), method.getName(), e);
				}
			});

			collectedHandlers.add(defaultHandlerData);

		}
		return collectedHandlers;
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

	private static Collection<HandlerData> collectByMethods(HandlerData handlerSource, List<Class<? extends Annotation>> clazzHttpMethods) {

		return clazzHttpMethods.stream().map(clazzHttpMethod -> {
			
			try {

				HandlerData handlerData = (HandlerData) handlerSource.clone();

				String prefix = StringUtils.EMPTY;
				String path = StringUtils.EMPTY;
				int order = 0;

				if (handlerData.getClass().isAnnotationPresent(SubRouter.class) && !handlerData.method().isAnnotationPresent(IgnoreSubRouter.class)) {

					prefix = handlerData.getClass().getAnnotation(SubRouter.class).value();
				}
				
				Annotation annotation = clazzHttpMethod.newInstance();
				Method valueMethod = annotation.annotationType().getDeclaredMethod("value");
				Method orderMethod = annotation.annotationType().getDeclaredMethod("order");
				path = (String) valueMethod.invoke(annotation);
				order = (int) valueMethod.invoke(orderMethod);

				handlerData.path(String.format("%s%s", prefix, path));
				handlerData.order(order);
				handlerData.httpMethod(annotation.getClass().getSimpleName().toUpperCase());

				return handlerData;
			} catch (Exception e) {

				log.warn("Ignoring handler class {} method {} - {}", handlerSource.clazz().getName(), handlerSource.method().getName(), e);
			}
			return null;
		}).collect(Collectors.toList());
	}

	private static List<Class<? extends Annotation>> getHandlersPresents(Method method) {

		return Arrays.asList(Handler.class, FailureHandler.class, BlockingHandler.class).stream().filter(method::isAnnotationPresent)
				.collect(Collectors.toList());
	}

	private static List<Class<? extends Annotation>> getHttpMethodsPresents(Class<?> clazz) {

		return Arrays.asList(Connect.class, Delete.class, Get.class, Head.class, Options.class, Other.class, Path.class, Post.class,
				Put.class, Trace.class).stream().filter(clazz::isAnnotationPresent).collect(Collectors.toList());
 	}
	
	private static List<Class<? extends Annotation>> getHttpMethodsPresents(Method method) {

		return Arrays.asList(Connect.class, Delete.class, Get.class, Head.class, Options.class, Other.class, Path.class, Post.class,
				Put.class, Trace.class).stream().filter(method::isAnnotationPresent).collect(Collectors.toList());

	}

	private static boolean hasHttpMethodsPresents(Method method) {

		return getHttpMethodsPresents(method).stream().filter(method::isAnnotationPresent).count() >= 1;
	}


	private static boolean isHandler(Method method) {

		return getHandlersPresents(method).stream().filter(method::isAnnotationPresent).count() >= 1;
	}
}