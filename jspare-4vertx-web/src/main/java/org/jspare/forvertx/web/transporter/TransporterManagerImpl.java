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

import org.jspare.core.container.Inject;
import org.jspare.forvertx.web.exceptions.UnavailableTransportException;


/**
 * The Class TransporterManagerImpl.
 */
public class TransporterManagerImpl implements TransporterManager {

	/** The holder. */
	@Inject
	private TransporterHolder holder;

	/* (non-Javadoc)
	 * @see org.jspare.forvertx.web.transporter.TransporterManager#create(org.jspare.forvertx.web.transporter.Transporter.TransporterBuilder)
	 */
	@Override
	public Transporter create(Transporter.TransporterBuilder vertxBuilder) throws UnavailableTransportException {

		Transporter transport = vertxBuilder.build();
		holder.registry(transport);
		return transport;
	}

	/* (non-Javadoc)
	 * @see org.jspare.forvertx.web.transporter.TransporterManager#retrieve(int)
	 */
	@Override
	public Transporter retrieve(int port) {

		return holder.get(port);
	}

	/* (non-Javadoc)
	 * @see org.jspare.forvertx.web.transporter.TransporterManager#ports()
	 */
	@Override
	public int[] ports() {

		return holder.ports();
	}
}