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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jspare.forvertx.web.collector.HandlerCollector;
import org.jspare.forvertx.web.collector.HandlerData;
import org.jspare.forvertx.web.transporter.Transporter;

public class AbstractCollectorTest {

	protected Transporter.TransporterBuilder builder = Transporter.builder();

	protected List<HandlerData> collect(Class<?> clazz) {
		List<HandlerData> handlers = new ArrayList<>(HandlerCollector.collect(builder.build(), clazz));
		Collections.sort(handlers, (o1, o2) -> o1.patch().compareTo(o2.patch()));
		return handlers;
	}
}
