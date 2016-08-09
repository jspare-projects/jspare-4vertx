package org.jspare.forvertx.web.transport;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

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

	private boolean conventions = true;
	
	private int port;
	
	private Vertx vertx;
	
	private HttpServer httpServer;
	
	private Router router;


	private VertxBuilder() {
		
		vertxOptions = new VertxOptions();
		httpServerOptions = new HttpServerOptions();
	}

	public static VertxBuilder create() {

		return new VertxBuilder();
	}

	public VertxTransporter build(){
		
		if(conventions){
			
			//TODO map all convention on build vertxTransporter
		}
		
		return new VertxTransporter(this.name, vertx(), httpServer(), router());
	}

	public String name(){
		
		if(StringUtils.isEmpty(name)) this.name = UUID.randomUUID().toString();
		return name;
	}
	
	private HttpServer httpServer() {
		
		if(httpServer == null) httpServer = vertx().createHttpServer(httpServerOptions);
		return httpServer;
	}
	
	private Router router(){
		
		if(router == null) router = Router.router(vertx());
		return router;
	}
	
	private Vertx vertx() {
		
		if(vertx == null) Vertx.vertx(vertxOptions);
		return vertx;
	}
}