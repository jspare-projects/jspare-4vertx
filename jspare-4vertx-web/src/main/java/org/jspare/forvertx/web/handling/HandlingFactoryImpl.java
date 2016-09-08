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
package org.jspare.forvertx.web.handling;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.jspare.core.container.ContainerUtils;
import org.jspare.forvertx.web.exceptions.HandlingInstantiationException;
import org.jspare.forvertx.web.exceptions.InvalidHandlingException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The Class ControllerFactoryImpl.
 *
 * @author pflima
 * @since 30/03/2016
 */
public class HandlingFactoryImpl implements HandlingFactory {

	/** The Constant instances. */
	private static final Map<HandlingKey, Object> instances = new HashMap<>();

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.jspare.server.controller.ControllerFactory#instantiate(java.lang.
	 * Class)
	 */
	@Override
	public Object instantiate(Class<?> cmdClazz) {

		HoldType scope = cmdClazz.isAnnotationPresent(Hold.class) ? cmdClazz.getAnnotation(Hold.class).value() : HoldType.REQUEST;

		HandlingKey key = new HandlingKey(cmdClazz, scope);
		if (!instances.containsKey(key)) {
			try {

				Constructor<?> constructor = cmdClazz.getConstructor();

				if (constructor == null) {
					throw new InvalidHandlingException(String.format("Cannot found default Constructor for [%s]", cmdClazz.getName()));
				}

				Object instance = constructor.newInstance();
				ContainerUtils.processInjection(cmdClazz, instance);

				if (!scope.equals(HoldType.REQUEST)) {
					instances.put(key, instance);
				}

				return instance;

			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {

				throw new HandlingInstantiationException(e);
			}
		}

		return instances.get(cmdClazz);
	}
}

@Data
@AllArgsConstructor
@EqualsAndHashCode
class HandlingKey {

	private Class<?> clazz;
	private HoldType scope;
}