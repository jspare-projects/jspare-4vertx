package org.jspare.forvertx.web.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jspare.forvertx.web.TestUtils;

public class AbstarctCollectorTest {
	
	protected List<HandlerData> collect(Class<?> clazz) {
		List<HandlerData> handlers = new ArrayList<>(HandlerCollector.collect(clazz, TestUtils.emptySet(), TestUtils.emptySet()));
		Collections.sort(handlers, (o1, o2) -> o1.patch().compareTo(o2.patch()));
		return handlers;
	}
}
