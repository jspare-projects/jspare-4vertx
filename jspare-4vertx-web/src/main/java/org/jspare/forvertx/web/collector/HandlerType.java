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

import org.jspare.forvertx.web.mapping.handlers.BlockingHandler;
import org.jspare.forvertx.web.mapping.handlers.FailureHandler;
import org.jspare.forvertx.web.mapping.handlers.Handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum HandlerType {

	HANDLER(Handler.class), BLOCKING_HANDLER(BlockingHandler.class), FAILURE_HANDLER(FailureHandler.class);

	@Getter
	private Class<?> handlerClass;
}