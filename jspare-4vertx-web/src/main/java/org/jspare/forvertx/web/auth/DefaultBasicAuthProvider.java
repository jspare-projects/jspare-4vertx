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


import static org.jspare.core.container.Environment.my;

import java.io.IOException;

import org.jspare.core.loader.ResourceLoader;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultBasicAuthProvider extends BasicAuthProvider {

	private static final String ROLE = "role";
	private static final String DEFAULT_RESOURCE = "security/users.json";
	
	private final String resource;
	
	public DefaultBasicAuthProvider() {

		this.resource = DEFAULT_RESOURCE;
	}
	
	public DefaultBasicAuthProvider(String resource) {

		this.resource = resource;
	}

	@Override
	protected void doAuthenticate(Handler<AsyncResult<User>> handler, String username, String password) {
		try {
			String users = my(ResourceLoader.class).readFileToString(this.resource);
			JsonObject data = new JsonObject(users);

			if (data.containsKey(username) && data.getJsonObject(username).getString(PASSWORD).equals(password)) {

				handler.handle(Future.succeededFuture(new AbstractUser() {

					@Override
					public JsonObject principal() {
						return data.getJsonObject(username);
					}

					@Override
					public void setAuthProvider(io.vertx.ext.auth.AuthProvider arg0) {
					}

					@Override
					protected void doIsPermitted(String authority, Handler<AsyncResult<Boolean>> resultPermitted) {

						resultPermitted.handle(Future.succeededFuture(principal().getJsonArray(ROLE).contains(authority)));
					}
				}));
			}

		} catch (IOException e) {

			log.warn(DEFAULT_ERROR);
		}

		handler.handle(Future.failedFuture(DEFAULT_ERROR));
	}
}