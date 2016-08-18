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

import static org.jspare.core.container.Environment.my;

import java.lang.reflect.Parameter;

import org.apache.commons.lang.StringUtils;
import org.jspare.core.exception.SerializationException;
import org.jspare.core.serializer.Json;
import org.jspare.forvertx.web.handling.Handling;
import org.jspare.forvertx.web.handling.HandlingFactory;
import org.jspare.forvertx.web.mapping.handling.Model;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class DefaultHandler implements Handler<RoutingContext> {

	private final HandlerData handlerData;

	@Override
	public void handle(RoutingContext routingContext) {

		try {

			handlerData.before().forEach(middleware -> middleware.doIt(routingContext));

			// Inject Request and Response if is Available
			Object newInstance = my(HandlingFactory.class).instantiate(handlerData.clazz());

			if (newInstance instanceof Handling) {

				((Handling) newInstance).setRequest(routingContext.request());
				((Handling) newInstance).setResponse(routingContext.response());
				((Handling) newInstance).setRoutingContext(routingContext);
			}

			Object[] parameters = new Object[handlerData.method().getParameterCount()];
			int i = 0;
			for (Parameter parameter : handlerData.method().getParameters()) {

				parameters[i] = resolveParameter(parameter, routingContext);
				i++;
			}

			handlerData.method().invoke(newInstance, parameters);

			handlerData.after().forEach(middleware -> middleware.doIt(routingContext));

			if (!routingContext.response().ended()) {

				routingContext.response().end();
			}

		} catch (Throwable t) {

			while (t.getCause() != null) {

				t = t.getCause();
			}
			log.info("Error: {}", handlerData.toStringLine());
			log.error(t.getMessage(), t);
			routingContext.response().setStatusCode(500).end(t.toString());
		}
	}

	private Object resolveParameter(Parameter parameter, RoutingContext routingContext) {

		if (parameter.getType().equals(RoutingContext.class)) {

			return routingContext;
		}
		if (parameter.getType().equals(HttpServerRequest.class)) {

			return routingContext.request();
		}

		if (parameter.getType().equals(HttpServerResponse.class)) {

			return routingContext.response();
		}
		if (StringUtils.isNotEmpty(routingContext.request().getParam(parameter.getName()))) {

			return routingContext.request().getParam(parameter.getName());
		}

		if (parameter.getType().getPackage().getName().endsWith(".model") || parameter.getType().isAnnotationPresent(Model.class)
				|| parameter.isAnnotationPresent(Model.class)) {

			try {
				if (routingContext.getBody() == null) {

					return null;
				}
				return my(Json.class).fromJSON(routingContext.getBody().toString(), parameter.getType());
			} catch (SerializationException e) {

				log.warn("Invalid content of body for class [{}] on parameter [{}]", parameter.getClass(), parameter.getName());
				return null;
			}
		}
		if (parameter.isAnnotationPresent(org.jspare.forvertx.web.mapping.handling.Parameter.class)) {

			String parameterName = parameter.getAnnotation(org.jspare.forvertx.web.mapping.handling.Parameter.class).value();
			return routingContext.request().getParam(parameterName);
		}
		if (parameter.isAnnotationPresent(org.jspare.forvertx.web.mapping.handling.Header.class)) {

			String headerName = parameter.getAnnotation(org.jspare.forvertx.web.mapping.handling.Header.class).value();
			return routingContext.request().getHeader(headerName);
		}

		return null;
	}
}