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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.vertx.core.http.HttpServer;

public class TransporterHolderImpl implements TransporterHolder {

	private final ConcurrentMap<Integer, Transporter> transports = new ConcurrentHashMap<>();

	@Override
	public Transporter get(int port) {

		return transports.get(port);
	}

	@Override
	public void registry(Transporter vertxTransport) {

		transports.put(vertxTransport.getPort(), vertxTransport);
	}

	@Override
	public void release() {

		transports.values().stream().map(Transporter::getHttpServer).forEach(HttpServer::close);
		transports.clear();
	}

	@Override
	public void release(int port) {

		Transporter transport = transports.remove(port);
		if (transport != null) {

			transport.getHttpServer().close();
		}
	}
}