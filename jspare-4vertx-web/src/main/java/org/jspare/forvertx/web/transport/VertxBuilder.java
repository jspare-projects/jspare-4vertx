package org.jspare.forvertx.web.transport;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.jspare.forvertx.web.handler.HandlerData;
import org.jspare.forvertx.web.middleware.Middleware;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class VertxBuilder {

	private String name;

	private VertxOptions vertxOptions;

	private HttpServerOptions httpServerOptions;

	private Set<Class<?>> routeSet;

	private Set<Middleware> afterMiddlewaresSet;

	private Set<Middleware> beforeMiddlewaresSet;

	private boolean conventions = true;

	private int port;

	private Vertx vertx;

	private HttpServer httpServer;

	private Router router;

	private Object source;

	private VertxBuilder() {

		vertxOptions = new VertxOptions();
		httpServerOptions = new HttpServerOptions();
		routeSet = new HashSet<>();
		afterMiddlewaresSet = new HashSet<>();
		beforeMiddlewaresSet = new HashSet<>();
	}

	public static VertxBuilder create(Object source) {

		return new VertxBuilder().source(source);
	}

	public static VertxBuilder create() {

		return new VertxBuilder();
	}

	public VertxBuilder addRoute(Class<?> clazz) {

		this.routeSet.add(clazz);
		return this;
	}

	public VertxBuilder addAfterMiddleware(Middleware middleware) {

		this.afterMiddlewaresSet.add(middleware);
		return this;
	}

	public VertxBuilder addBeforeMiddleware(Middleware middleware) {

		this.beforeMiddlewaresSet.add(middleware);
		return this;
	}

	public VertxTransporter build() {

		if (conventions && source != null) {

			List<HandlerData> handlers = VertxRoutesBuilder.look4defaultHandlers(source);

		}
		if(port == 0){
			
		}		
		

		return new VertxTransporter(this.name, vertx(), httpServer(), router());
	}

	public String name() {

		if (StringUtils.isEmpty(name))
			this.name = UUID.randomUUID().toString();
		return name;
	}

	private HttpServer httpServer() {

		if (httpServer == null)
			httpServer = vertx().createHttpServer(httpServerOptions);
		return httpServer;
	}

	private Router router() {

		if (router == null)
			router = Router.router(vertx());
		return router;
	}

	private Vertx vertx() {

		if (vertx == null)
			Vertx.vertx(vertxOptions);
		return vertx;
	}
}