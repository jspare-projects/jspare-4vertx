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
package org.jspare.forvertx.web.commons;

import java.io.File;

import org.jspare.core.commons.Definitions;

public interface ApplicationDefinitions extends Definitions {

	/** The Constant SERVER_PORT_KEY. */
	String SERVER_PORT_KEY = "server.default.port";

	/** The default port. */
	int SERVER_PORT_DEFAULT = 8080;

	/** The certificate enable. */
	String CERTIFICATE_ENABLE = "certificate.enable";

	/** The certificate keystore key. */
	String CERTIFICATE_KEYSTORE_KEY = "certificate.keystore.path";

	/** The certificate keystore path. */
	String CERTIFICATE_KEYSTORE_PATH = String.format("%s%s%s", "certificate", File.separator, "certificate.keystore.jks");

	/** The certificate keystore password key. */
	String CERTIFICATE_KEYSTORE_PASSWORD_KEY = "certificate.keystore.password";

	/** The certificate keystore password. */
	String CERTIFICATE_KEYSTORE_PASSWORD = "password";

	/** The transaction time to live key. */
	String TRANSACTION_TIME_TO_LIVE_KEY = "server.transaction.live";

	/** The transaction time to live default. */
	Long TRANSACTION_TIME_TO_LIVE_DEFAULT = 5l;

	/** The yield enable key. */
	String YIELD_ENABLE_KEY = "server.yield.enable";
}