package org.jspare.forvertx.web.middleware;

import io.vertx.ext.web.RoutingContext;

public interface Middleware {
	
	void doIt(RoutingContext routingContext);
}
