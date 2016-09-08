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
package org.jspare.forvertx.web.auth;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jspare.core.commons.Definitions;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import lombok.SneakyThrows;

public abstract class BasicAuthProvider implements AuthProvider {

	protected static final String DEFAULT_ERROR = "Unauthorized";
	protected static final String PASSWORD = "password";
	protected static final String USERNAME = "username";
	protected static final String AUTHORIZATION = "authorization";
	protected static final String BASIC = "Basic";
	protected static final String SPACE = " ";

	@Override
	public void authenticate(JsonObject authData, Handler<AsyncResult<User>> handler) {

		// Get authorization token
		String username = authData.getString(USERNAME);
		String password = authData.getString(PASSWORD);

		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {

			handler.handle(Future.failedFuture(DEFAULT_ERROR));
			return;
		}

		doAuthenticate(handler, username, password);
	}

	@Override
	@SneakyThrows(IOException.class)
	public JsonObject provideAuthdata(RoutingContext ctx) {

		JsonObject authData = new JsonObject();
		String authorization = ctx.request().getHeader(AUTHORIZATION);
		String[] splited = StringUtils.defaultIfEmpty(authorization, StringUtils.EMPTY).split(SPACE);
		if (splited.length == 2 && BASIC.equals(splited[0])) {

			String token = IOUtils.toString(java.util.Base64.getDecoder().decode(splited[1]), Definitions.DEFAULT_CHARSET.name());
			String[] tSplited = token.split(":");
			authData.put(USERNAME, tSplited[0]);
			authData.put(PASSWORD, tSplited[1]);
		} else {

			ctx.response().putHeader("WWW-Authenticate", "Basic");
		}
		return authData;
	}

	protected abstract void doAuthenticate(Handler<AsyncResult<User>> handler, String username, String password);
}