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
package org.jspare.forvertx.web.collector;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jspare.forvertx.web.auth.AuthProvider;
import org.jspare.forvertx.web.handler.BodyEndHandler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
public class HandlerData implements Cloneable {

	private Class<?> clazz;
	private Method method;
	private String patch = StringUtils.EMPTY;
	private int order;
	private boolean pathRegex;
	private String httpMethod;
	private String consumes;
	private String produces;
	private HandlerType handlerType;
	private Class<? extends Handler<RoutingContext>> routeHandler;
	private List<BodyEndHandler> bodyEndHandler;
	private AuthProvider authProvider;
	private boolean auth;
	private boolean skipAuthorities;
	private String autority;
	private HandlerDocumentation documentation;

	public String toStringLine() {
		StringBuilder line = new StringBuilder();
		line.append(String.format("[%s.%s]", clazz().getSimpleName(), method().getName()));
		line.append(String.format("[%s]", handlerType));
		if (StringUtils.isNotEmpty(httpMethod())) {
			line.append(String.format("[%s] ", httpMethod()));
		}
		if (StringUtils.isNotEmpty(patch())) {
			line.append(String.format("[%s] ", patch()));
		} else {
			line.append(String.format("[%s] ", clazz().getSimpleName())).append(String.format("[%s] ", method().getName()));
		}
		return line.toString();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		return super.clone();
	}
}