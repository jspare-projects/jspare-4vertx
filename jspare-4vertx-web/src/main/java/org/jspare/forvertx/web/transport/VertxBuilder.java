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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.jspare.core.loader.ResourceLoader;
import org.jspare.core.scanner.ComponentScanner;
import org.jspare.forvertx.web.commons.Definitions4Vertx;
import org.jspare.forvertx.web.handler.HandlerCollector;
import org.jspare.forvertx.web.handler.HandlerData;
import org.jspare.forvertx.web.handler.HandlerWrapper;
import org.jspare.forvertx.web.middleware.Middleware;

import io.vertx.core.VertxOptionsConverter;
import io.vertx.core.http.HttpServerOptionsConverter;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = false)
public class VertxBuilder extends AbstractVertxBuilder {

	private static final String DEFAULT_VERTX_OPTIONS_JSON_PATH = "vertxOptions.json";

	public static VertxBuilder create() {

		return new VertxBuilder();
	}

	public static VertxBuilder create(Object source) {

		return new VertxBuilder().source(source);
	}

	private String name;

	private List<Class<?>> routeSet;

	private Set<Middleware> afterMiddlewareSet;

	private Set<Middleware> beforeMiddlewareSet;

	private boolean conventions = true;

	private int port;

	private int bodySizeLimit = Definitions4Vertx.SERVER_DEFAULT_BODY_SIZE;

	private Object source;

	private VertxBuilder() {

		super();
		routeSet = new ArrayList<>();
		afterMiddlewareSet = new HashSet<>();
		beforeMiddlewareSet = new HashSet<>();
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

		router().route().handler(BodyHandler.create().setBodyLimit(bodySizeLimit));

		collectHandlers().forEach(hd -> HandlerWrapper.prepareHandler(router(), hd));

		if (port == 0) {

			port = Integer.valueOf(CONFIG.get(SERVER_PORT_KEY, SERVER_PORT_DEFAULT));
		}
		if (!httpServerOptions.isSsl() && Boolean.valueOf(CONFIG.get(SSL_ENABLE, Boolean.FALSE))) {

			httpServerOptions.setSsl(true).setKeyStoreOptions(new JksOptions().setPath(CONFIG.get(SSL_KEYSTORE_KEY, SSL_KEYSTORE_PATH))
					.setPassword(CONFIG.get(SSL_KEYSTORE_PASSWORD_KEY, SSL_KEYSTORE_PASSWORD)));
		}

		return new VertxTransporter(name(), vertx(), httpServer(), router());
	}

	public VertxBuilder loadHttpServerOptions() {

		loadVertxOptions(DEFAULT_VERTX_OPTIONS_JSON_PATH);
		return this;
	}

	@SneakyThrows(IOException.class)
	public VertxBuilder loadHttpServerOptions(String path) {
		String content = my(ResourceLoader.class).readFileToString(path);
		if (StringUtils.isNotEmpty(content)) {

			HttpServerOptionsConverter.fromJson(Json.decodeValue(content, JsonObject.class), httpServerOptions);
		}
		return this;
	}

	public VertxBuilder loadVertxOptions() {

		loadVertxOptions(DEFAULT_VERTX_OPTIONS_JSON_PATH);
		return this;
	}

	@SneakyThrows(IOException.class)
	public VertxBuilder loadVertxOptions(String path) {
		String content = my(ResourceLoader.class).readFileToString(path);
		if (StringUtils.isNotEmpty(content)) {

			VertxOptionsConverter.fromJson(Json.decodeValue(content, JsonObject.class), vertxOptions);
		}
		return this;
	}

	public String name() {

		if (StringUtils.isEmpty(name))
			this.name = UUID.randomUUID().toString();
		return name;
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