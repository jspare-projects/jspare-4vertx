package org.jspare.forvertx.web.transport;

import org.jspare.core.container.Component;
import org.jspare.forvertx.web.exceptions.UnavailableTransportException;

@Component
public interface VertxManager {
	
	VertxTransporter create(VertxBuilder vertxBuilder) throws UnavailableTransportException;
	
	VertxTransporter retrieve(String deploymentId);
}