package org.jspare.forvertx.web.transport;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.vertx.core.http.HttpServer;

public class VertxHolderImpl implements VertxHolder {
	
	private final ConcurrentMap<String, VertxTransporter> transports = new ConcurrentHashMap<>();

	@Override
	public VertxTransporter get(String deploymentId) {

		return this.transports.get(deploymentId);
	}

	@Override
	public void registry(VertxTransporter vertxTransport) {
		
		
		this.transports.put(vertxTransport.getDeploymentId(), vertxTransport);
	}

	@Override
	public void release() {

		this.transports.values().stream().map(VertxTransporter::httpServer).forEach(HttpServer::close);
		this.transports.clear();
	}

	@Override
	public void release(String deploymentId) {

		VertxTransporter transport = this.transports.remove(deploymentId);
		if(transport != null){
			
			transport.httpServer().close();
		}
	}
}