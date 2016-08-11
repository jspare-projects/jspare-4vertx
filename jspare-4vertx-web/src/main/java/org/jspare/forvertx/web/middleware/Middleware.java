package org.jspare.forvertx.web.middleware;

import io.vertx.ext.web.RoutingContext;

@FunctionalInterface
public interface Middleware {
	
	void doIt(RoutingContext routingContext);
}