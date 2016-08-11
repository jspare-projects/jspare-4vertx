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
package org.jspare.forvertx.web.transport;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import lombok.Getter;

public class VertxTransporter extends AbstractVerticle {

	@Getter
	private final String deploymentId;

	@Getter
	private final Vertx vertx;

	private final HttpServer httpServer;

	private final Router router;

	public VertxTransporter(String deploymentId, Vertx vertx, HttpServer httpServer, Router router) {
		this.deploymentId = deploymentId;
		this.vertx = vertx;
		this.httpServer = httpServer;
		this.router = router;
		this.vertx.deployVerticle(this);
		this.httpServer.requestHandler(router::accept);
	}

	public HttpServer httpServer() {

		return this.httpServer;
	}

	public VertxTransporter remap() {

		return this;
	}

	public Router router() {
		return this.router;
	}
}