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

import static org.jspare.core.container.Environment.my;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.jspare.core.serializer.Json;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import lombok.Setter;

public abstract class Handling {

	@Setter
	protected HttpServerRequest request;

	@Setter
	protected HttpServerResponse response;

	@Setter
	protected RoutingContext routingContext;

	/**
	 * Bad gateway.
	 */
	public void badGateway() {

		status(HttpResponseStatus.BAD_GATEWAY);
	}

	/**
	 * Bad request.
	 */
	public void badRequest() {

		status(HttpResponseStatus.BAD_REQUEST);
	}

	/**
	 * Bad request.
	 *
	 * @param object
	 *            the object
	 */
	public void badRequest(Object object) {

		String content = my(Json.class).toJSON(object);
		status(HttpResponseStatus.BAD_REQUEST);
		contentType("application/json");
		response.setChunked(true);
		response.write(content, StandardCharsets.UTF_8.name());
	}

	/**
	 * Bad request.
	 *
	 * @param content
	 *            the content
	 */
	public void badRequest(String content) {

		status(HttpResponseStatus.BAD_REQUEST);
		response.setChunked(true);
		response.write(content, StandardCharsets.UTF_8.name());
	}

	/**
	 * Conflict.
	 */
	public void conflict() {

		status(HttpResponseStatus.CONFLICT);
	}

	/**
	 * Conflict.
	 *
	 * @param object
	 *            the object
	 */
	public void conflict(Object object) {

		String content = my(Json.class).toJSON(object);
		status(HttpResponseStatus.CONFLICT);
		contentType("application/json");
		response.setChunked(true);
		response.write(content, StandardCharsets.UTF_8.name());

	}

	/**
	 * Conflict.
	 *
	 * @param content
	 *            the content
	 */
	public void conflict(String content) {

		status(HttpResponseStatus.BAD_REQUEST);
		response.setChunked(true);
		response.write(content, StandardCharsets.UTF_8.name());
	}

	public HttpServerResponse contentType(String contentType) {
		response.putHeader("content-type", contentType);
		return response;
	}

	/**
	 * Error.
	 */
	public void error() {

		status(HttpResponseStatus.BAD_REQUEST);
	}

	/**
	 * Error.
	 *
	 * @param e
	 *            the e
	 */
	public void error(Exception e) {

		status(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		contentType("application/json");
		response.setChunked(true);
		response.write(e.getMessage());
	}

	/**
	 * Error.
	 *
	 * @param content
	 *            the content
	 */
	public void error(String content) {

		status(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		response.setChunked(true);
		response.write(content, StandardCharsets.UTF_8.name());
	}

	/**
	 * Forbidden.
	 */
	public void forbidden() {

		status(HttpResponseStatus.FORBIDDEN);
	}

	/**
	 * Forbidden.
	 *
	 * @param object
	 *            the object
	 */
	public void forbidden(Object object) {

		String content = my(Json.class).toJSON(object);
		status(HttpResponseStatus.FORBIDDEN);
		contentType("application/json");
		response.setChunked(true);
		response.write(content, StandardCharsets.UTF_8.name());
	}

	/**
	 * Forbidden.
	 *
	 * @param content
	 *            the content
	 */
	public void forbidden(String content) {

		status(HttpResponseStatus.BAD_REQUEST);
		response.setChunked(true);
		response.write(content, StandardCharsets.UTF_8.name());
	}

	public Optional<String> getHeader(String name) {

		return Optional.ofNullable(request.getHeader(name));
	}

	public String getParameter(String name) {

		return request.getParam(name);
	}

	/**
	 * No content.
	 */
	public void noContent() {

		status(HttpResponseStatus.NO_CONTENT);
	}

	/**
	 * Not acceptable.
	 */
	public void notAcceptable() {

		status(HttpResponseStatus.NOT_ACCEPTABLE);
	}

	/**
	 * Not found.
	 */
	public void notFound() {

		status(HttpResponseStatus.NOT_FOUND);
	}

	/**
	 * Not implemented.
	 */
	public void notImplemented() {

		status(HttpResponseStatus.NOT_IMPLEMENTED);
	}

	/**
	 * Pre condition failed.
	 */
	public void preConditionFailed() {

		status(HttpResponseStatus.PRECONDITION_FAILED);
	}

	/**
	 * Pre condition failed.
	 *
	 * @param object
	 *            the object
	 */
	public void preConditionFailed(Object object) {

		String content = my(Json.class).toJSON(object);
		status(HttpResponseStatus.PRECONDITION_FAILED);
		contentType("application/json");
		response.setChunked(true);
		response.write(content, StandardCharsets.UTF_8.name());
	}

	/**
	 * Pre condition failed.
	 *
	 * @param content
	 *            the content
	 */
	public void preConditionFailed(String content) {

		status(HttpResponseStatus.PRECONDITION_FAILED);
		response.setChunked(true);
		response.write(content, StandardCharsets.UTF_8.name());
	}

	public HttpServerResponse status(HttpResponseStatus status) {
		response.setStatusCode(status.code());
		response.setStatusMessage(status.reasonPhrase());
		return response;
	}

	/**
	 * Success.
	 */
	public void success() {

		status(HttpResponseStatus.OK);
	}

	/**
	 * Success.
	 *
	 * @param object
	 *            the object
	 */
	public void success(Object object) {

		String content = my(Json.class).toJSON(object);
		status(HttpResponseStatus.OK);
		contentType("application/json");
		response.setChunked(true);
		response.write(content, StandardCharsets.UTF_8.name());
	}

	/**
	 * Success.
	 *
	 * @param content
	 *            the content
	 */
	public void success(String content) {

		if (my(Json.class).isValidJson(content)) {
			contentType("application/json");
		}

		response.setChunked(true);
		status(HttpResponseStatus.OK).write(content);
	}

	/**
	 * Unauthorized.
	 */
	public void unauthorized() {

		status(HttpResponseStatus.UNAUTHORIZED);
	}

	/**
	 * Unauthorized.
	 *
	 * @param object
	 *            the object
	 */
	public void unauthorized(Object object) {

		String content = my(Json.class).toJSON(object);
		contentType("application/json");
		status(HttpResponseStatus.UNAUTHORIZED);
		response.setChunked(true);
		response.write(content, StandardCharsets.UTF_8.name());
	}

	/**
	 * Unauthorized.
	 *
	 * @param content
	 *            the content
	 */
	public void unauthorized(String content) {

		status(HttpResponseStatus.UNAUTHORIZED);
		response.setChunked(true);
		response.write(content, StandardCharsets.UTF_8.name());
	}

	/**
	 * Unvailable.
	 */
	public void unvailable() {

		status(HttpResponseStatus.SERVICE_UNAVAILABLE);
	}

	protected String body() {

		return routingContext.getBodyAsString();
	}
}