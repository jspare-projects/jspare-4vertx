package org.jspare.forvertx.web.transport;

import static org.jspare.core.scanner.ComponentScanner.ALL_SCAN_QUOTE;

import java.util.List;

import org.jspare.forvertx.web.handler.HandlerData;

public class VertxRoutesBuilder {
	
	/** The controller package. */
	private static final String ROUTES_PACKAGE = ".routes";

	public static List<HandlerData> look4defaultHandlers(Object source) {
		
		String defaultRoutesPackage = source.getClass().getPackage().getName().concat(ROUTES_PACKAGE).concat(ALL_SCAN_QUOTE);
		
		
		return null;
	}
	
	public static HandlerData prepareHanlder(Class<?> clazzHandler){
		
		return null;
	}

}