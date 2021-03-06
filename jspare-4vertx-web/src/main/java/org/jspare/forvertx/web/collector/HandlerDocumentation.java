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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspare.forvertx.web.mapping.documentation.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data

/*
 * (non-Javadoc)
 * 
 * @see java.lang.Object#hashCode()
 */
@EqualsAndHashCode
@Accessors(fluent = true)

/**
 * Instantiates a new handler documentation.
 */
@NoArgsConstructor

/**
 * Instantiates a new handler documentation.
 *
 * @param description
 *            the description
 * @param status
 *            the status
 * @param queryParameters
 *            the query parameters
 * @param requestSchema
 *            the request schema
 * @param responseSchema
 *            the response schema
 */
@AllArgsConstructor
public class HandlerDocumentation {

	/** The description. */
	private String description;

	/** The status. */
	private List<ResponseStatus> status;

	/** The query parameters. */
	private List<QueryParameter> queryParameters;

	/** The request schema. */
	private Map<String, String> requestSchema;

	/** The response schema. */
	private Map<String, String> responseSchema;

	/**
	 * Request schema.
	 *
	 * @param clazz
	 *            the clazz
	 */
	public void requestSchema(Class<?> clazz) {
		this.requestSchema = buildSchema(clazz);
	}

	/**
	 * Response schema.
	 *
	 * @param clazz
	 *            the clazz
	 */
	public void responseSchema(Class<?> clazz) {
		this.responseSchema = buildSchema(clazz);
	}

	/**
	 * Builds the schema.
	 *
	 * @param clazz
	 *            the clazz
	 * @return the map
	 */
	private Map<String, String> buildSchema(Class<?> clazz) {
		Map<String, String> schema = new HashMap<>();
		extractFields(clazz, schema);
		if (clazz.getSuperclass() != null) {

			extractFields(clazz.getSuperclass(), schema);
		}
		return schema;
	}

	protected void extractFields(Class<?> clazz, Map<String, String> schema) {
		for (Field f : clazz.getDeclaredFields()) {
			if (!Modifier.isTransient(f.getModifiers())) {

				schema.put(f.getName(), f.getType().getSimpleName());
			}
		}
	}

	@Data
	public static class QueryParameter {

		public QueryParameter(org.jspare.forvertx.web.mapping.documentation.QueryParameter aQueryParameter) {
			this.key = aQueryParameter.key();
			this.description = aQueryParameter.description();
		}

		private String key;
		private String description;
	}

	@Data
	@AllArgsConstructor
	public static class ResponseStatus {

		public ResponseStatus(Status status) {
			this.code = status.code();
			this.description = status.description();
		}

		private int code;
		private String description;
	}
}