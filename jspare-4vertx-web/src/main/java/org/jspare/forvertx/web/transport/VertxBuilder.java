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
package org.jspare.forvertx.web.transport;

import static org.jspare.core.container.Environment.CONFIG;
import static org.jspare.core.container.Environment.my;
import static org.jspare.core.scanner.ComponentScanner.ALL_SCAN_QUOTE;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.ROUTES_PACKAGE;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.SERVER_PORT_DEFAULT;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.SERVER_PORT_KEY;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.SSL_ENABLE;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.SSL_KEYSTORE_KEY;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.SSL_KEYSTORE_PASSWORD;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.SSL_KEYSTORE_PASSWORD_KEY;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.SSL_KEYSTORE_PATH;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.jspare.core.scanner.ComponentScanner;
import org.jspare.forvertx.web.handler.HandlerCollector;
import org.jspare.forvertx.web.handler.HandlerData;
import org.jspare.forvertx.web.handler.HandlerWrapper;
import org.jspare.forvertx.web.middleware.Middleware;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Data
@Accessors(fluent = true)
@Slf4j
public class VertxBuilder {

	private String name;

	private VertxOptions vertxOptions;

	private HttpServerOptions httpServerOptions;

	private List<Class<?>> routeSet;

	private Set<Middleware> afterMiddlewareSet;

	private Set<Middleware> beforeMiddlewareSet;

	private boolean conventions = true;

	private int port;

	private Vertx vertx;

	private HttpServer httpServer;

	private Router router;

	private Object source;

	private VertxBuilder() {

		vertxOptions = new VertxOptions();
		httpServerOptions = new HttpServerOptions().setTcpKeepAlive(true).setReuseAddress(true);
		routeSet = new ArrayList<>();
		afterMiddlewareSet = new HashSet<>();
		beforeMiddlewareSet = new HashSet<>();
	}

	public static VertxBuilder create() {

		return new VertxBuilder();
	}

	public static VertxBuilder create(Object source) {

		return new VertxBuilder().source(source);
	}

	public VertxBuilder addAfterMiddleware(Middleware middleware) {

		this.afterMiddlewareSet.add(middleware);
		return this;
	}

	public VertxBuilder addBeforeMiddleware(Middleware middleware) {

		this.beforeMiddlewareSet.add(middleware);
		return this;
	}

	public VertxBuilder addRoute(Class<?> clazz) {

		this.routeSet.add(clazz);
		return this;
	}

	public VertxTransporter build() {

		collectHandlers().forEach(hd -> HandlerWrapper.prepareHandler(router(), hd));

		if (port == 0) {

			port = Integer.valueOf(CONFIG.get(SERVER_PORT_KEY, SERVER_PORT_DEFAULT));
		}
		if(!httpServerOptions.isSsl() && Boolean.valueOf(CONFIG.get(SSL_ENABLE, Boolean.FALSE))){
			
			httpServerOptions.setSsl(true).setKeyStoreOptions(
					   new JksOptions()
					   	.setPath(CONFIG.get(SSL_KEYSTORE_KEY, SSL_KEYSTORE_PATH))
					   	.setPassword(CONFIG.get(SSL_KEYSTORE_PASSWORD_KEY, SSL_KEYSTORE_PASSWORD)));
		}

		return new VertxTransporter(name(), vertx(), httpServer(), router());
	}

	public String name() {

		if (StringUtils.isEmpty(name))
			this.name = UUID.randomUUID().toString();
		return name;
	}

	protected HttpServer httpServer() {

		if (httpServer == null)
			httpServer = vertx().createHttpServer(httpServerOptions);
		return httpServer;
	}

	protected Router router() {

		if (router == null)
			router = Router.router(vertx());
		return router;
	}

	protected Vertx vertx() {

		if (vertx == null)
			vertx = Vertx.vertx(vertxOptions);
		return vertx;
	}

	private List<HandlerData> collectConventionHandlers() {

		List<HandlerData> handlers = new ArrayList<>();
		String cpackage = source.getClass().getPackage().getName().concat(ROUTES_PACKAGE).concat(ALL_SCAN_QUOTE);
		my(ComponentScanner.class).scanAndExecute(cpackage, (clazzName) -> {

			try {

				Class<?> clazz = Class.forName((String) clazzName[0]);
				handlers.addAll(HandlerCollector.collect(clazz, beforeMiddlewareSet, afterMiddlewareSet));
			} catch (Exception e) {

				log.warn("Cannot collect route class [{}]", clazzName[0], e);
			}
			return Void.TYPE;
		});

		return handlers;
	}

	private List<HandlerData> collectHandlers() {

		List<HandlerData> handlers = new ArrayList<>();
		handlers.addAll(collectRouteSetHandlers());
		if (conventions && source != null) {

			handlers.addAll(collectConventionHandlers());
		}
		return handlers;
	}

	private Set<HandlerData> collectRouteSetHandlers() {

		Set<HandlerData> handlers = new HashSet<>();
		routeSet.forEach(clazz -> handlers.addAll(HandlerCollector.collect(clazz, beforeMiddlewareSet, afterMiddlewareSet)));
		return handlers;
	}
}