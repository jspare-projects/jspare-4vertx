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
package org.jspare.samples;

import org.jspare.core.container.Application;
import org.jspare.core.container.Inject;
import org.jspare.core.exception.InfraException;
import org.jspare.forvertx.web.transport.VertxBuilder;
import org.jspare.forvertx.web.transport.VertxManager;
import org.jspare.forvertx.web.transport.VertxTransporter;

public class Bootstrap extends Application {

	@Inject
	private VertxManager manager;

	@Override
	protected void load() {

		VertxTransporter transporter = manager.create(VertxBuilder.create(this));
		transporter.httpServer().listen(8080);
	}

	public static void main(String[] args) throws InfraException {

		new Bootstrap().start();
	}
}