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
package org.jspare.forvertx.web.samples;

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
	protected void initialize() {

		// The lifecycle start here.
		// Initialize your component and determine the behavior of your
		// ENVIRONMENT. On this stage is the ideal time to configure the
		// container and the environment configs.
	}

	@Override
	protected void load() {

		// Its components have not been initialized in the container, you can
		// now load and determine the behavior of your APPLICATION.
		
		VertxTransporter transporter = manager.create(VertxBuilder.create(this));
		transporter.router();
		transporter.httpServer().listen();
	}
	
	@Override
	protected void start() throws InfraException {

		// With all registered and configured is the time to start your
		// application at this time all settings are validated and configured.
		// your application will start here.
		
		// The super method need be called for execute the lifecyle of your application
		super.start();
	}

	public static void main(String[] args) throws InfraException {

		//The boostrap will be started here
		new Bootstrap().start();
	}
}