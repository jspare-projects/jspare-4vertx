package org.jspare.forvertx.web.mapping.handling;

import static org.jspare.core.container.Environment.my;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import org.jspare.core.serializer.Json;

import lombok.AllArgsConstructor;

/**
 * Instantiates a new map model parser.
 *
 * @param mapClazz
 *            the map clazz
 * @param key
 *            the key
 * @param value
 *            the value
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
	 * @param <K>
	 *            the key type
	 * @param <V>
	 *            the value type
	 * @param json
	 *            the json
	 * @param mapClazz
	 *            the map clazz
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @return the map
	 */
	public static <K, V> Map<K, V> toMap(String json, Class<?> mapClazz, Class<K> key, Class<V> value) {

		return my(Json.class).fromJSON(json, new MapModelParser<K, V>(mapClazz, key, value));
	}
}
