package org.jspare.forvertx.web.handler.mock;

import org.jspare.forvertx.web.transport.VertxBuilder;

public class BootstrapMock {
	
	public static BootstrapMock instance(){
		
		return new BootstrapMock();
	}
	
	public void load(){
		
		VertxBuilder.create(this);
	}
}