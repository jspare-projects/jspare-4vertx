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
package org.jspare.forvertx.web.mapping.handling;

import static org.jspare.core.container.Environment.my;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import org.jspare.core.serializer.Json;

import lombok.AllArgsConstructor;

/**
 * The Class MapModelParser.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
@AllArgsConstructor
public class MapModelParser<K, V> implements ParameterizedType {

	/** The map clazz. */
	private Class<?> mapClazz;

	/** The key. */
	private Class<K> key;

	/** The value. */
	private Class<V> value;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.ParameterizedType#getActualTypeArguments()
	 */
	@Override
	public Type[] getActualTypeArguments() {

		return new Type[] { key, value };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.ParameterizedType#getOwnerType()
	 */
	@Override
	public Type getOwnerType() {

		return mapClazz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.ParameterizedType#getRawType()
	 */
	@Override
	public Type getRawType() {

		return mapClazz;
	}

	/**
	 * To map.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param json the json
	 * @param mapClazz the map clazz
	 * @param key the key
	 * @param value the value
	 * @return the map
	 */
	public static <K, V> Map<K, V> toMap(String json, Class<?> mapClazz, Class<K> key, Class<V> value) {

		return my(Json.class).fromJSON(json, new MapModelParser<K, V>(mapClazz, key, value));
	}
}
