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
package org.jspare.samples.routes;

import org.jspare.forvertx.web.handling.Handling;
import org.jspare.forvertx.web.mapping.handlers.Handler;
import org.jspare.forvertx.web.mapping.method.Post;

import io.vertx.ext.web.RoutingContext;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UploadRoute extends Handling {

	@Setter
	private String filename;

	@Handler
	@Post("/upload")
	public void upload(RoutingContext ctx) {

		ctx.fileUploads().forEach(fu -> {

			log.info(fu.fileName());
			log.info(fu.contentType());
			log.info(fu.uploadedFileName());
			setFilename(fu.uploadedFileName());
		});

		response.setChunked(true).sendFile(filename);
	}
}