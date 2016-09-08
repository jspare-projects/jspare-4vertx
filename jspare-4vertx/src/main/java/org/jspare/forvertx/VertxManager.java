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
package org.jspare.forvertx;

import org.jspare.core.container.Component;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * The Interface VertxManager.
 */
@Component
public interface VertxManager {

	/** The default vertx options json path. */
	String DEFAULT_VERTX_OPTIONS_JSON_PATH = "vertxOptions.json";

	/**
	 * Clustered vertx.
	 *
	 * @param handler
	 *            the handler
	 */
	void clusteredVertx(Handler<Vertx> handler);

	/**
	 * Load vertx options.
	 *
	 * @return the vertx manager
	 */
	VertxManager loadVertxOptions();

	/**
	 * Load vertx options.
	 *
	 * @param path
	 *            the path
	 * @return the vertx manager
	 */
	VertxManager loadVertxOptions(String path);

	/**
	 * Vertx.
	 *
	 * @return the vertx
	 */
	Vertx vertx();

	/**
	 * Vertx options.
	 *
	 * @param vertxOptions
	 *            the vertx options
	 * @return the vertx manager
	 */
	VertxManager vertxOptions(VertxOptions vertxOptions);
}
