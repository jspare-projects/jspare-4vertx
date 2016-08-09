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
package org.jspare.forvertx.web.transaction;

import org.jspare.forvertx.web.exceptions.NoSuchCallerException;
import org.jspare.forvertx.web.handler.HandlerData;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionExecutorImpl implements TransactionExecutor {
	
	
	@Override
	public HttpServerResponse consumeResponse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doIt(HandlerData cmd, RoutingContext routingContext) throws InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasResponse() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void notifyFinish(HttpServerResponse response) throws NoSuchCallerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() throws InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCaller(Object caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void yield(HttpServerRequest request, HttpServerResponse response, String bind) {
		// TODO Auto-generated method stub

	}

}