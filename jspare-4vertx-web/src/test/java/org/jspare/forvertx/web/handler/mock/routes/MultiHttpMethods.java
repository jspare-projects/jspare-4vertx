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
package org.jspare.forvertx.web.handler.mock.routes;

import org.jspare.forvertx.web.handling.Handling;
import org.jspare.forvertx.web.mapping.handlers.Handler;
import org.jspare.forvertx.web.mapping.method.All;
import org.jspare.forvertx.web.mapping.method.Connect;
import org.jspare.forvertx.web.mapping.method.Delete;
import org.jspare.forvertx.web.mapping.method.Get;
import org.jspare.forvertx.web.mapping.method.Head;
import org.jspare.forvertx.web.mapping.method.Options;
import org.jspare.forvertx.web.mapping.method.Other;
import org.jspare.forvertx.web.mapping.method.Path;
import org.jspare.forvertx.web.mapping.method.Post;
import org.jspare.forvertx.web.mapping.method.Put;
import org.jspare.forvertx.web.mapping.method.Trace;

public class MultiHttpMethods extends Handling {
	@All
	@Connect
	@Delete
	@Get
	@Post
	@Head
	@Options
	@Other
	@Path
	@Put
	@Trace
	@Handler
	public void handler1() {
	}

	@All
	@Connect
	@Delete
	@Get
	@Post
	@Head
	@Options
	@Other
	@Path
	@Put
	@Trace
	@Handler
	public void handler2() {
	}
}