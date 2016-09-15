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
import java.util.Collection;

import org.jspare.core.serializer.Json;

import lombok.AllArgsConstructor;


/**
 * The Class ArrayModelParser.
 *
 * @param <T> the generic type
 */
@AllArgsConstructor
public class ArrayModelParser<T> implements ParameterizedType {

	/** The collection class. */
	private Class<?> collectionClass;

	/** The value. */
	private Class<?> value;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.ParameterizedType#getActualTypeArguments()
	 */
	@Override
	public Type[] getActualTypeArguments() {

		return new Type[] { value };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.ParameterizedType#getOwnerType()
	 */
	@Override
	public Type getOwnerType() {

		return collectionClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.ParameterizedType#getRawType()
	 */
	@Override
	public Type getRawType() {

		return collectionClass;
	}

	/**
	 * To list.
	 *
	 * @param <T> the generic type
	 * @param json the json
	 * @param collection the collection
	 * @param typeClass the type class
	 * @return the collection
	 */
	public static <T> Collection<T> toList(String json, Class<? extends Collection<?>> collection, Class<T> typeClass) {

		return my(Json.class).fromJSON(json, new ArrayModelParser<T>(collection, typeClass));
	}
}
