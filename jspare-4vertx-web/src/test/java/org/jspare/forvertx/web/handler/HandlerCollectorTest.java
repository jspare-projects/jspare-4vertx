package org.jspare.forvertx.web.handler;

import java.util.HashSet;
import java.util.Set;

import org.jspare.forvertx.web.TestUtils;
import org.jspare.forvertx.web.handler.mock.routes.MultiRoutes;
import org.jspare.forvertx.web.middleware.Middleware;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HandlerCollectorTest {
	
	@Test
	public void collectRouteSetTest(){
		
		Set<HandlerData> handlers = HandlerCollector.collect(MultiRoutes.class, TestUtils.emptySet(), TestUtils.emptySet());
		Assert.assertEquals(2, handlers.size());
	}
	
	@Test
	public void collectMockBoostrapTest(){
		
	}
	
	private Set<Middleware> emptyMiddlewares(){
		
		return new HashSet<>();
	}
	
}
