package org.jspare.forvertx.web.samples.routes;

import org.jspare.forvertx.web.mapping.handlers.Handler;
import org.jspare.forvertx.web.mapping.method.Get;
import org.jspare.forvertx.web.mapping.subrouter.IgnoreSubRouter;
import org.jspare.forvertx.web.mapping.subrouter.SubRouter;

import io.vertx.ext.web.RoutingContext;

@SubRouter("/subRouter")
public class SampleSubRouter {
	
	@Handler
	@Get("/sub/1")
	public void sub1(RoutingContext routingContext){
		
		routingContext.response().write("/subRouter/sub/1");
		routingContext.response().end();		
	}
	
	@Handler
	@Get("/sub/2")
	public void sub2(RoutingContext routingContext){
		
		routingContext.response().write("/subRouter/sub/2");
		routingContext.response().end();
	}
	
	@IgnoreSubRouter
	@Handler
	@Get("/sub/3")
	public void sub3(RoutingContext routingContext){
		
		routingContext.response().write("sub/3");
		routingContext.response().end();
	}
}