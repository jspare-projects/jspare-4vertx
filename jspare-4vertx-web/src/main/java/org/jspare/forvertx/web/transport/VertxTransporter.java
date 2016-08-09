package org.jspare.forvertx.web.transport;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class VertxTransporter extends AbstractVerticle {

	@Getter
	private final String deploymentId;

	@Getter
	private final Vertx vertx;

	private final HttpServer httpServer;

	private final Router router;

	public HttpServer httpServer() {

		return this.httpServer;
	}

	public Router router() {
		return this.router;
	}
	
	public VertxTransporter remap(){
		
		return this;
	}
}