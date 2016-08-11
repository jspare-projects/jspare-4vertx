/*
 * Copyright 2016 JSpare.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.jspare.forvertx.web.handler;

import org.apache.commons.lang.StringUtils;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HandlerWrapper {

	public static void prepareHandler(Router router, HandlerData data) {
		
		log.info("Mapping handler: {}", data.toStringLine());

		Route route = router.route().order(data.order());

		if(StringUtils.isNotEmpty(data.httpMethod())){
			
			setMethod(data, route);
		}
		if(StringUtils.isNotEmpty(data.patch())){

			setPatch(data, route);
		}
		if(StringUtils.isNotEmpty(data.consumes())){
		
			setConsumes(data, route);
		}
		if(StringUtils.isNotEmpty(data.produces())){
			
			setProduces(data, route);
		}
		setHandler(data, route);
	}

	protected static void setConsumes(HandlerData data, Route route) {
		route.consumes(data.consumes());
	}

	protected static void setHandler(HandlerData data, Route route) {

		if(HandlerType.DEFAULT.equals(data.handlerType())){
			
			route.handler(new DefaultHandler(data));
		}else if(HandlerType.FAILURE.equals(data.handlerType())){
			
			route.failureHandler(new DefaultHandler(data));
		}else if(HandlerType.BLOCKING.equals(data.handlerType())){
			
			route.blockingHandler(new DefaultHandler(data));
		}
	}

	protected static void setMethod(HandlerData data, Route route) {
		route.method(HttpMethod.valueOf(data.httpMethod()));
	}

	protected static void setPatch(HandlerData data, Route route) {
		if(data.pathRegex()){
			
			route.pathRegex(data.patch());
		}else{
			
			route.path(data.patch());
		}
	}
	
	protected static void setProduces(HandlerData data, Route route) {
		route.produces(data.produces());
	}
}