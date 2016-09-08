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

import static org.jspare.core.container.Environment.my;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.jspare.core.exception.InfraRuntimeException;
import org.jspare.core.loader.ResourceLoader;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.VertxOptionsConverter;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j
@Accessors(fluent = true)

/**
 * Instantiates a new vertx manager impl.
 *
 * @param vertxOptions
 *            the vertx options
 * @param vertx
 *            the vertx
 */
@AllArgsConstructor
public class VertxManagerImpl implements VertxManager {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jspare.forvertx.VertxManager#vertxOptions(io.vertx.core.VertxOptions)
	 */
	@Setter
	private VertxOptions vertxOptions;

	/** The vertx. */
	private Vertx vertx;

	/**
	 * Instantiates a new vertx manager impl.
	 */
	public VertxManagerImpl() {

		vertxOptions = new VertxOptions();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jspare.forvertx.VertxManager#clusteredVertx(io.vertx.core.Handler)
	 */
	@Override
	public void clusteredVertx(Handler<Vertx> handler) {

		if (vertx == null) {

			Vertx.clusteredVertx(vertxOptions, result -> {
				if (!result.succeeded()) {
					log.error("Cannot create Vert.x instance");
					throw new InfraRuntimeException(result.cause());
				}
				handler.handle(result.result());
			});
			return;
		}
		handler.handle(vertx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jspare.forvertx.VertxManager#loadVertxOptions()
	 */
	@Override
	public VertxManager loadVertxOptions() {
		loadVertxOptions(DEFAULT_VERTX_OPTIONS_JSON_PATH);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jspare.forvertx.VertxManager#loadVertxOptions(java.lang.String)
	 */
	@Override
	@SneakyThrows(IOException.class)
	public VertxManager loadVertxOptions(String path) {
		String content = my(ResourceLoader.class).readFileToString(path);
		if (StringUtils.isNotEmpty(content)) {

			VertxOptionsConverter.fromJson(io.vertx.core.json.Json.decodeValue(content, JsonObject.class), vertxOptions);
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jspare.forvertx.VertxManager#vertx()
	 */
	@Override
	public Vertx vertx() {

		if (vertx == null) {

			vertx = Vertx.vertx(vertxOptions);
		}
		return vertx;
	}
}