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

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

public class AbstractVertxBuilder {

	private Vertx vertx;

	private HttpServer httpServer;

	private Router router;

	protected VertxOptions vertxOptions;

	protected HttpServerOptions httpServerOptions;

	public AbstractVertxBuilder() {

		vertxOptions = new VertxOptions();
		httpServerOptions = new HttpServerOptions().setTcpKeepAlive(true).setReuseAddress(true);
	}

	protected HttpServer httpServer() {

		if (httpServer == null)
			httpServer = vertx().createHttpServer(httpServerOptions);
		return httpServer;
	}

	protected Router router() {

		if (router == null)
			router = Router.router(vertx());
		return router;
	}

	protected Vertx vertx() {

		if (vertx == null)
			vertx = Vertx.vertx(vertxOptions);
		return vertx;
	}

}
