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
/**
 * Copyright 2016 Senior Sistemas.
 *
 * Software sob Medida
 *
 */
package org.jspare.forvertx.web.transporter;

import static org.jspare.core.container.Environment.CONFIG;
import static org.jspare.core.container.Environment.my;
import static org.jspare.core.scanner.ComponentScanner.ALL_SCAN_QUOTE;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.DEFAULT_HTTP_OPTIONS_JSON_PATH;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.FILE_UPLOADS_PATH;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.ROUTES_PACKAGE;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.SERVER_PORT_AUTO_KEY;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.SERVER_PORT_DEFAULT;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.SERVER_PORT_KEY;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.SSL_ENABLE;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.SSL_KEYSTORE_KEY;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.SSL_KEYSTORE_PASSWORD;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.SSL_KEYSTORE_PASSWORD_KEY;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.SSL_KEYSTORE_PATH;
import static org.jspare.forvertx.web.commons.Definitions4Vertx.START_PORT_SCAN;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jspare.core.loader.ResourceLoader;
import org.jspare.core.scanner.ComponentScanner;
import org.jspare.forvertx.VertxManager;
import org.jspare.forvertx.web.auth.AuthProvider;
import org.jspare.forvertx.web.collector.HandlerCollector;
import org.jspare.forvertx.web.collector.HandlerData;
import org.jspare.forvertx.web.commons.TransporterUtils;
import org.jspare.forvertx.web.exceptions.HttpServerListenException;
import org.jspare.forvertx.web.handler.BodyEndHandler;
import org.jspare.forvertx.web.handler.DefaultHandler;
import org.jspare.forvertx.web.handler.HandlerWrapper;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerOptionsConverter;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j

/*
 * (non-Javadoc)
 *
 * @see java.lang.Object#toString()
 */
@Builder
public class Transporter extends AbstractVerticle {

	/**
	 * Builder.
	 *
	 * @return the transporter builder
	 */
	@SneakyThrows(IOException.class)
	public static TransporterBuilder builder() {

		// Prepare httpServerOptions default
		HttpServerOptions httpServerOptions = new HttpServerOptions().setTcpKeepAlive(true).setReuseAddress(true);
		if (my(ResourceLoader.class).exist(DEFAULT_HTTP_OPTIONS_JSON_PATH)) {

			String content = my(ResourceLoader.class).readFileToString(DEFAULT_HTTP_OPTIONS_JSON_PATH);
			if (StringUtils.isNotEmpty(content)) {

				HttpServerOptionsConverter.fromJson(Json.decodeValue(content, JsonObject.class), httpServerOptions);
			}
		}

		return new TransporterBuilder().httpServerOptions(httpServerOptions);
	}

	/**
	 * Builder.
	 *
	 * @param source4convetions
	 *            the source 4 convetions
	 * @return the transporter builder
	 */
	public static TransporterBuilder builder(Object source4convetions) {

		return builder().source4conventions(source4convetions);
	}
	
	/** The name. */
	@Getter
	private String name;

	/**
	 * Gets the port used by http server.
	 *
	 * @return the port
	 */
	@Getter
	private int port;

	/*
	 * (non-Javadoc)
	 *
	 * @see io.vertx.core.AbstractVerticle#getVertx()
	 */
	@Getter
	private Vertx vertx;

	/**
	 * Gets the http server.
	 *
	 * @return the http server
	 */
	@Getter
	private HttpServer httpServer;

	/**
	 * Gets the router.
	 *
	 * @return the router
	 */
	@Getter
	private Router router;

	/**
	 * Gets the source 4 conventions.
	 *
	 * @return the source 4 conventions
	 */
	@Getter
	private Object source4conventions;

	/**
	 * Gets the http server options.
	 *
	 * @return the http server options
	 */
	@Getter
	private HttpServerOptions httpServerOptions = new HttpServerOptions().setTcpKeepAlive(true).setReuseAddress(true);

	/**
	 * Gets the routes class <br>
	 * The Routes can receive any mapped class to be wraped on one potential
	 * route.
	 *
	 * @return the routes
	 */
	@Getter
	@Singular("addRoute")
	private List<Class<?>> routes;
	
	/**
	 * Gets the handlers. <br>
	 * This handlers define the middleware before each call of route on http
	 * server. For all requests this handlers will be called. <br>
	 * Note: All of this handlers has setted with order MIN integer value, for
	 * be called before any handler
	 *
	 * @return the handlers
	 */
	@Getter
	@Singular("addHandler")
	private List<Handler<RoutingContext>> handlers;

	/** The route handler.
	 *
	 *	Define the default routeHandler any request
	 *
	 * @return the route handler
	 *  /*/
	@Getter
	private Class<? extends Handler<RoutingContext>> routeHandler;
	
	@Getter
	private boolean ignoreBodyHandler;
	
	/** The body handler.
	 *
	 *	Define the default bodyHandler for requests
	 *
	 * @return the body handler
	 *  /*/
	@Getter
	private Handler<RoutingContext> bodyHandler;
	
	/**
	 * Gets the default body end handlers.
	 *
	 * This attribute set for any request one defaultBodyHandler.
	 *
	 * @return the default body end handlers
	 */
	@Getter
	@Singular("addDefaultBodyEndHandler")
	private List<BodyEndHandler> defaultBodyEndHandlers;

	@Getter
	private AuthProvider authProvider;

	/**
	 * Instantiates a new transporter.
	 *
	 * @param port
	 *            the port
	 * @param vertx
	 *            the vertx
	 * @param httpServer
	 *            the http server
	 * @param router
	 *            the router
	 * @param source4conventions
	 *            the source 4 conventions
	 * @param httpServerOptions
	 *            the http server options
	 * @param routes
	 *            the routes
	 * @param handlers
	 *            the handlers
	 * @param defaultBodyEndHandlers
	 *            the default body end handlers
	 */
	public Transporter(String name, int port, Vertx vertx, HttpServer httpServer, Router router, Object source4conventions,
			HttpServerOptions httpServerOptions, List<Class<?>> routes, List<Handler<RoutingContext>> handlers, Class<? extends Handler<RoutingContext>> routeHandler, boolean ignoreBodyHandler, Handler<RoutingContext> bodyHandler,
			List<BodyEndHandler> defaultBodyEndHandlers, AuthProvider authProvider) {
		super();
		this.name = name;
		this.port = port;
		this.vertx = vertx;
		this.httpServer = httpServer;
		this.router = router;
		this.source4conventions = source4conventions;
		this.httpServerOptions = httpServerOptions;
		this.routes = routes;
		this.handlers = handlers;
		this.routeHandler = routeHandler;
		this.ignoreBodyHandler = ignoreBodyHandler;
		this.bodyHandler = bodyHandler;
		this.defaultBodyEndHandlers = defaultBodyEndHandlers;
		this.authProvider = authProvider;
		build();
	}

	/**
	 * Listen.
	 */
	public void listen() {

		listen((server) -> {

			if (server.failed()) {

				log.error("Address port in use: [{}]", port);
				throw new HttpServerListenException(server.cause());
			}
			log.info(StringUtils.repeat("#", 50));
			log.info("# Tranposrter name [{}]", StringUtils.defaultIfEmpty(this.name, "DEFAULT"));
			log.info("# Vert.x httpServer started at: 127.0.0.1:{}", port);
			log.info(StringUtils.repeat("#", 50));
		});
	}

	/**
	 * Listen.
	 *
	 * @param result
	 *            the result
	 */
	public void listen(Handler<AsyncResult<HttpServer>> result) {

		// Prepare http server to receive all routes
		httpServer.requestHandler(router::accept);

		// Start http server
		httpServer.listen(result);
	}

	/**
	 * Builds the.
	 */
	private void build() {

		// Initialize Vert.x if not setted
		if (vertx == null) {

			vertx = my(VertxManager.class).vertx();
		}

		// Set default port if not setted to transporter
		if (port == 0) {

			if (Boolean.valueOf(CONFIG.get(SERVER_PORT_AUTO_KEY, Boolean.FALSE))) {

				port = findForAvailablePort();
			} else {

				port = Integer.valueOf(CONFIG.get(SERVER_PORT_KEY, SERVER_PORT_DEFAULT));
			}
		}
		httpServerOptions.setPort(port);

		// Prepare SSL if available
		if (!httpServerOptions.isSsl() && Boolean.valueOf(CONFIG.get(SSL_ENABLE, Boolean.FALSE))) {

			httpServerOptions.setSsl(true).setKeyStoreOptions(new JksOptions().setPath(CONFIG.get(SSL_KEYSTORE_KEY, SSL_KEYSTORE_PATH))
					.setPassword(CONFIG.get(SSL_KEYSTORE_PASSWORD_KEY, SSL_KEYSTORE_PASSWORD)));
		}

		// Initialize Router if not setted
		if (router == null) {

			router = Router.router(vertx);
		}
		
		//Create or define the defaut route handler
		if(this.routeHandler == null){
			
			this.routeHandler = DefaultHandler.class;
		}

		// Create default Handler to handle file uploads and body
		if(this.bodyHandler == null){
			
			this.bodyHandler = BodyHandler.create(FILE_UPLOADS_PATH);
		}
		
		if(!ignoreBodyHandler){
			
			router.route().handler(bodyHandler);
		}

		// Collect all Middleware Handlers
		handlers.forEach(h -> HandlerWrapper.prepareHandler(router, h));

		// Collect all handlers adding to router
		collectHandlers().forEach(hd -> HandlerWrapper.prepareHandler(router, hd));

		// Initialize http server if not setted
		if (httpServer == null) {

			httpServer = vertx.createHttpServer(httpServerOptions);
		}

		// Deploy this verticle on Vert.x
		vertx.deployVerticle(this);
	}

	/**
	 * Collect convention handlers.
	 *
	 * @return the list
	 */
	private List<HandlerData> collectConventionHandlers() {

		List<HandlerData> handlers = new ArrayList<>();
		String cpackage = source4conventions.getClass().getPackage().getName().concat(ROUTES_PACKAGE).concat(ALL_SCAN_QUOTE);
		my(ComponentScanner.class).scanAndExecute(cpackage, clazzName -> {

			try {

				Class<?> clazz = Class.forName(clazzName);
				handlers.addAll(HandlerCollector.collect(this, clazz));
			} catch (Exception e) {

				log.warn("Cannot collect route class [{}]", clazzName, e);
			}
		});

		return handlers;
	}

	/**
	 * Collect handlers.
	 *
	 * @return the list
	 */
	private List<HandlerData> collectHandlers() {

		List<HandlerData> handlers = new ArrayList<>();
		handlers.addAll(collectRoutesHandlers());
		if (source4conventions != null) {

			handlers.addAll(collectConventionHandlers());
		}
		return handlers;
	}

	/**
	 * Collect routes handlers.
	 *
	 * @return the sets the
	 */
	private Set<HandlerData> collectRoutesHandlers() {

		Set<HandlerData> handlers = new HashSet<>();
		routes.forEach(clazz -> handlers.addAll(HandlerCollector.collect(this, clazz)));
		return handlers;
	}

	/**
	 * Find for available port.
	 *
	 * @return the int
	 */
	private int findForAvailablePort() {

		int port = START_PORT_SCAN;
		boolean isAvailable = false;

		while (!isAvailable) {
			isAvailable = TransporterUtils.available(port);
			if (!isAvailable) {

				port++;
			}
		}
		return port;
	}
}