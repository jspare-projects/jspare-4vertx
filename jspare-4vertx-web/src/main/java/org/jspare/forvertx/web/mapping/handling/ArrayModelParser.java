package org.jspare.forvertx.web.mapping.handling;

import static org.jspare.core.container.Environment.my;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import org.jspare.core.serializer.Json;

import lombok.AllArgsConstructor;

/**
 * Instantiates a new array model parser.
 *
 * @param collectionClass
 *            the collection class
 * @param value
 *            the value
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
	 * @param <T>
	 *            the generic type
	 * @param json
	 *            the json
	 * @param collection
	 *            the collection
	 * @param typeClass
	 *            the type class
	 * @return the collection
	 */
	public static <T> Collection<T> toList(String json, Class<? extends Collection<?>> collection, Class<T> typeClass) {

		return my(Json.class).fromJSON(json, new ArrayModelParser<T>(collection, typeClass));
	}
}
