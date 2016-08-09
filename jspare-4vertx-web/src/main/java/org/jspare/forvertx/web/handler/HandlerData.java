package org.jspare.forvertx.web.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@AllArgsConstructor
public class HandlerData {

	private String path;
	private String method;
	private String produces;
	private String consumes;
	private boolean pathRegex;
	private HandlerType handlerType;
}