package org.jspare.forvertx.web.transport;

import org.jspare.core.container.Component;
import org.jspare.forvertx.web.exceptions.UnavailableTransportException;

/**
 * The Holder.
 * 
 * Responsible for hold the httpServer of the application.
 * 
 */
@Component
public interface VertxHolder {
	
	/**
	 * Gets the.
	 *
	 * @param deploymentId the deployment id
	 * @return the http server
	 */
	VertxTransporter get(String deploymentId);
	
	/**
	 * Registry.
	 *
	 * @param deploymentId the deployment id
	 * @param httpServer the http server
	 */
	void registry(VertxTransporter vertxTransport) throws UnavailableTransportException;
	
	/**
	 * Release all server allocated on {@link VertxHolder}.
	 */
	void release();

	/**
	 * Release.
	 *
	 * @param deploymentId the deployment id
	 */
	void release(String deploymentId);
}