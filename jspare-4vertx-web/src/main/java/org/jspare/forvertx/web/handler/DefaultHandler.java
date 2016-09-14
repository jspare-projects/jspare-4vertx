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
import java.lang.reflect.Type;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.jspare.core.exception.SerializationException;
import org.jspare.core.serializer.Json;
import org.jspare.forvertx.web.collector.HandlerData;
import org.jspare.forvertx.web.handling.Handling;
import org.jspare.forvertx.web.handling.HandlingFactory;
import org.jspare.forvertx.web.mapping.handling.ArrayModel;
import org.jspare.forvertx.web.mapping.handling.ArrayModelParser;
import org.jspare.forvertx.web.mapping.handling.MapModel;
import org.jspare.forvertx.web.mapping.handling.MapModelParser;
import org.jspare.forvertx.web.mapping.handling.Model;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j

/**
 * Instantiates a new default handler.
 *
 * @param handlerData
 *            the handler data
 */
@AllArgsConstructor
public class DefaultHandler implements Handler<RoutingContext> {

	/** The handler data. */
	private final HandlerData handlerData;

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.vertx.core.Handler#handle(java.lang.Object)
	 */
	@Override
	public void handle(RoutingContext routingContext) {

		try {

			// Inject Request and Response if is Available
			Object newInstance = my(HandlingFactory.class).instantiate(handlerData.clazz());

			// If Route is handling by abstract Handling inject some resources
			if (newInstance instanceof Handling) {

				((Handling) newInstance).setReq(routingContext.request());
				((Handling) newInstance).setRes(routingContext.response());
				((Handling) newInstance).setCtx(routingContext);
			}

			// Prepare parameters to call method of route
			Object[] parameters = new Object[handlerData.method().getParameterCount()];
			int i = 0;
			for (Parameter parameter : handlerData.method().getParameters()) {

				parameters[i] = resolveParameter(parameter, routingContext);
				i++;
			}

			// Wrap bodyEndHandler to share routingContext
			handlerData.bodyEndHandler().forEach(h -> routingContext.addBodyEndHandler(event -> {

				h.handle(routingContext);
			}));

			// Check Authentication
			if (handlerData.auth() && handlerData.authProvider() != null) {

				handleAuthentication(routingContext, newInstance, parameters);
				return;
			}

			if (handlerData.auth() && handlerData.authProvider() == null) {

				log.warn("AuthProvider is null, ignoring Authentication and Authorization");
			}

			// Call method of handler data
			handlerData.method().invoke(newInstance, parameters);

		} catch (Throwable t) {

			catchInvoke(routingContext, t);
		}
	}

	/**
	 * Catch invoke.
	 *
	 * @param routingContext
	 *            the routing context
	 * @param t
	 *            the t
	 */
	protected void catchInvoke(RoutingContext routingContext, Throwable t) {
		// Any server error return internal server error
		while (t.getCause() != null) {

			t = t.getCause();
		}
		log.info("Error: {}", handlerData.toStringLine());
		log.error(t.getMessage(), t);
		routingContext.response().setStatusCode(500).end(t.toString());
	}

	/**
	 * Handle authentication.
	 *
	 * @param routingContext
	 *            the routing context
	 * @param newInstance
	 *            the new instance
	 * @param parameters
	 *            the parameters
	 */
	protected void handleAuthentication(RoutingContext routingContext, Object newInstance, Object[] parameters) {

		JsonObject authData = handlerData.authProvider().provideAuthdata(routingContext);
		handlerData.authProvider().authenticate(authData, authenticationResult -> {

			try {

				if (authenticationResult.succeeded()) {

					routingContext.setUser(authenticationResult.result());
					if (!handlerData.skipAuthorities() && StringUtils.isNotEmpty(handlerData.autority())) {

						handleAuthorization(routingContext, newInstance, parameters);
						return;
					}
					handlerData.method().invoke(newInstance, parameters);
					return;
				}

				if (!routingContext.response().ended()) {

					log.debug("AuthValidation failed, returning unauthorized status code");
					sendStatus(routingContext, HttpResponseStatus.UNAUTHORIZED);
				}

			} catch (Throwable t) {

				catchInvoke(routingContext, t);
			}
		});
	}

	/**
	 * Handle authorization.
	 *
	 * @param routingContext
	 *            the routing context
	 * @param newInstance
	 *            the new instance
	 * @param parameters
	 *            the parameters
	 */
	protected void handleAuthorization(RoutingContext routingContext, Object newInstance, Object[] parameters) {
		routingContext.user().isAuthorised(handlerData.autority(), authorizationResult -> {

			if (authorizationResult.succeeded() && authorizationResult.result()) {

				try {

					handlerData.method().invoke(newInstance, parameters);
					return;
				} catch (Throwable t) {

					catchInvoke(routingContext, t);
				}
			}

			sendStatus(routingContext, HttpResponseStatus.FORBIDDEN);
		});
	}

	/**
	 * Resolve parameter.
	 *
	 * @param parameter
	 *            the parameter
	 * @param routingContext
	 *            the routing context
	 * @return the object
	 */
	@SuppressWarnings("unchecked")
	protected Object resolveParameter(Parameter parameter, RoutingContext routingContext) {

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

		if (parameter.isAnnotationPresent(ArrayModel.class)) {

			ArrayModel am = parameter.getAnnotation(ArrayModel.class);
			Class<? extends Collection<?>> collection = (Class<? extends Collection<?>>) am.collectionClass();
			Class<?> clazz = am.value();
			return ArrayModelParser.toList(routingContext.getBody().toString(), collection, clazz);
		}

		if (parameter.isAnnotationPresent(MapModel.class)) {

			MapModel mm = parameter.getAnnotation(MapModel.class);
			Class<?> mapClazz = (Class<? extends Collection<?>>) mm.mapClass();
			Class<?> key = mm.key();
			Class<?> value = mm.value();
			return MapModelParser.toMap(routingContext.getBody().toString(), mapClazz, key, value);
		}

		if (parameter.getType().getPackage().getName().endsWith(".model") || parameter.getType().isAnnotationPresent(Model.class)
				|| parameter.isAnnotationPresent(Model.class)) {

			try {
				if (routingContext.getBody() == null) {

					return null;
				}
				return my(Json.class).fromJSON(routingContext.getBody().toString(), parameter.getType());
			} catch (SerializationException e) {

				log.debug("Invalid content of body for class [{}] on parameter [{}]", parameter.getClass(), parameter.getName());
				return null;
			}
		}
		if (parameter.isAnnotationPresent(org.jspare.forvertx.web.mapping.handling.Parameter.class)) {

			String parameterName = parameter.getAnnotation(org.jspare.forvertx.web.mapping.handling.Parameter.class).value();
			// Test types
			Type typeOfParameter = parameter.getType();
			if (typeOfParameter.equals(Integer.class)) {
				return Integer.parseInt(routingContext.request().getParam(parameterName));
			}
			if (typeOfParameter.equals(Double.class)) {
				return Double.parseDouble(routingContext.request().getParam(parameterName));
			}
			if (typeOfParameter.equals(Long.class)) {
				return Long.parseLong(routingContext.request().getParam(parameterName));
			}
			return routingContext.request().getParam(parameterName);
		}
		if (parameter.isAnnotationPresent(org.jspare.forvertx.web.mapping.handling.Header.class)) {

			String headerName = parameter.getAnnotation(org.jspare.forvertx.web.mapping.handling.Header.class).value();
			return routingContext.request().getHeader(headerName);
		}

		return null;
	}

	/**
	 * Send status.
	 *
	 * @param routingContext
	 *            the routing context
	 * @param status
	 *            the status
	 */
	protected void sendStatus(RoutingContext routingContext, HttpResponseStatus status) {
		routingContext.response().setStatusCode(status.code()).setStatusMessage(status.reasonPhrase()).end(status.reasonPhrase());
	}
}