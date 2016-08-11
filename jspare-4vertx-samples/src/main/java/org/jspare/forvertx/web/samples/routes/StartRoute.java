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
package org.jspare.forvertx.web.samples.routes;

import org.jspare.forvertx.web.handling.Handling;
import org.jspare.forvertx.web.mapping.handlers.Handler;
import org.jspare.forvertx.web.mapping.method.Get;

@Get("/start")
public class StartRoute extends Handling {
	
	@Handler(order=0)
	public void route1() {
		
		response.setChunked(true);
		response.write("route 1\n");
		routingContext.next();
	}

	@Handler(order=1)
	public void route2() {

		response.write("route 2\n");
	}
}