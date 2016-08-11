package org.jspare.forvertx.web.handler;

import static org.jspare.forvertx.web.commons.Definitions4Vertx.HTTP_METHODS_TYPES;

import java.util.List;

import org.jspare.forvertx.web.handler.mock.routes.MultiHandlers;
import org.jspare.forvertx.web.handler.mock.routes.MultiHttpMethods;
import org.jspare.forvertx.web.handler.mock.routes.MultiRoutes;
import org.junit.Assert;
import org.junit.Test;

public class HandlerCollectorTest extends AbstarctCollectorTest {
	
	@Test
	public void collectRouteSetTest(){
		
		List<HandlerData> handlers = collect(MultiRoutes.class);
		
		Assert.assertEquals(2, handlers.size());
		
		HandlerData handler1 = handlers.get(0);
		HandlerData handler2 = handlers.get(1);

		Assert.assertEquals(MultiRoutes.class, handler1.clazz());
		Assert.assertEquals("handler1", handler1.method().getName());
		Assert.assertEquals("/multiRoutes/1", handler1.patch());
		Assert.assertEquals("GET", handler1.httpMethod());
		Assert.assertEquals(HandlerType.DEFAULT, handler1.handlerType());
		Assert.assertEquals(0, handler1.order());
		Assert.assertTrue(handler1.after().isEmpty());
		Assert.assertTrue(handler1.before().isEmpty());
		
		Assert.assertEquals("/multiRoutes/2", handler2.patch());
		Assert.assertEquals(HandlerType.BLOCKING, handler2.handlerType());
		Assert.assertEquals("handler2", handler2.method().getName());
		Assert.assertEquals(1, handler2.order());
		Assert.assertTrue(handler2.pathRegex());
		Assert.assertEquals("*/*", handler2.consumes());
		Assert.assertEquals("text/plain", handler2.produces());
		Assert.assertTrue(handler2.after().isEmpty());
		Assert.assertTrue(handler2.before().isEmpty());
	}

	@Test
	public void multiHandlersTest(){

		List<HandlerData> handlers = collect(MultiHandlers.class);
		Assert.assertEquals(3, handlers.size());		
	}
	
	@Test
	public void multiHttpMethodsTest(){

		List<HandlerData> handlers = collect(MultiHttpMethods.class);
		Assert.assertEquals(HTTP_METHODS_TYPES.length * 2, handlers.size());		
	}
}
