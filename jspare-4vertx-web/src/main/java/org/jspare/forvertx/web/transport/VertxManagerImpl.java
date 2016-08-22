package org.jspare.forvertx.web.transport;

import org.jspare.core.container.Inject;
import org.jspare.forvertx.web.exceptions.UnavailableTransportException;

public class VertxManagerImpl implements VertxManager {
	
	@Inject
	private VertxHolder holder;

	@Override
	public VertxTransporter create(AbstractVertxBuilder vertxBuilder) throws UnavailableTransportException{

		VertxTransporter transport = vertxBuilder.build();
		holder.registry(transport);
		return transport;
	}

	@Override
	public VertxTransporter retrieve(String deploymentId) {

		return holder.get(deploymentId);
	}
}