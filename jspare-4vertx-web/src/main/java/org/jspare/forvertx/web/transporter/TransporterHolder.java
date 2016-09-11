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
	 * @param port the port
	 * @return the transporter
	 */
	Transporter get(int port);

	/**
	 * Registry.
	 *
	 * @param vertxTransport the vertx transport
	 * @throws UnavailableTransportException the unavailable transport exception
	 */
	void registry(Transporter vertxTransport) throws UnavailableTransportException;

	/**
	 * Release all server allocated on {@link TransporterHolder}.
	 */
	void release();

	/**
	 * Release.
	 *
	 * @param port the port
	 */
	void release(int port);
	
	/**
	 * Ports.
	 *
	 * @return the int[]
	 */
	int[] ports();
}