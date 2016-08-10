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
package org.jspare.forvertx.web.handler;

import java.lang.reflect.Method;
import java.util.Set;

import org.jspare.forvertx.web.middleware.Middleware;

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
	private String path;
	private int order;
	private boolean pathRegex;
	private String httpMethod;
	private String consumes;
	private String produces;
	private HandlerType handlerType;
	private Set<Middleware> before;
	private Set<Middleware> after;

	@Override
	protected Object clone() throws CloneNotSupportedException {

		return super.clone();
	}
}