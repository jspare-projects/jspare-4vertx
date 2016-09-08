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
package org.jspare.forvertx.web.transporter;

import org.jspare.core.container.Component;
import org.jspare.forvertx.web.exceptions.UnavailableTransportException;


/**
 * The Holder.
 *
 * Responsible for hold the httpServer of the application.
 *
 */
@Component
public interface TransporterHolder {

	/**
	 * Gets the.
	 *
	 * @param deploymentId
	 *            the deployment id
	 * @return the http server
	 */
	Transporter get(int port);

	/**
	 * Registry.
	 *
	 * @param deploymentId
	 *            the deployment id
	 * @param httpServer
	 *            the http server
	 */
	void registry(Transporter vertxTransport) throws UnavailableTransportException;

	/**
	 * Release all server allocated on {@link TransporterHolder}.
	 */
	void release();

	/**
	 * Release.
	 *
	 * @param deploymentId
	 *            the deployment id
	 */
	void release(int port);
}