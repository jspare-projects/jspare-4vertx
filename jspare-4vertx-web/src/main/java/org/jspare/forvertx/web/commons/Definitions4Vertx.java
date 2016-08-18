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
import org.jspare.forvertx.web.mapping.handlers.BlockingHandler;
import org.jspare.forvertx.web.mapping.handlers.FailureHandler;
import org.jspare.forvertx.web.mapping.handlers.Handler;
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

public interface Definitions4Vertx extends Definitions {

	Class<?>[] HTTP_METHODS_TYPES = new Class<?>[] { Connect.class, Delete.class, Get.class, Head.class, Options.class, Other.class,
			Path.class, Post.class, Put.class, Trace.class };

	Class<?>[] HANDLERS_TYPES = new Class<?>[] { Handler.class, FailureHandler.class, BlockingHandler.class };

	/** The Constant ROUTES_PACKAGE */
	String ROUTES_PACKAGE = ".routes";

	/** The Constant SERVER_PORT_KEY. */
	String SERVER_PORT_KEY = "server.default.port";

	int SERVER_DEFAULT_BODY_SIZE = -1;

	/** The default port. */
	int SERVER_PORT_DEFAULT = 8080;

	/** The certificate enable. */
	String SSL_ENABLE = "ssl.server";

	/** The certificate keystore key. */
	String SSL_KEYSTORE_KEY = "ssl.keystore.path";

	/** The certificate keystore path. */
	String SSL_KEYSTORE_PATH = String.format("%s%s%s", "certificate", File.separator, "certificate.keystore.jks");

	/** The certificate keystore password key. */
	String SSL_KEYSTORE_PASSWORD_KEY = "ssl.keystore.password";

	/** The certificate keystore password. */
	String SSL_KEYSTORE_PASSWORD = "";
}