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
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import lombok.Setter;

public abstract class Handling {

	@Setter
	protected HttpServerRequest req;

	@Setter
	protected HttpServerResponse res;

	@Setter
	protected RoutingContext ctx;

	/**
	 * Bad gateway.
	 */
	public void badGateway() {

		status(HttpResponseStatus.BAD_GATEWAY);
		end();
	}

	/**
	 * Bad gateway.
	 */
	public void badGateway(String content) {

		status(HttpResponseStatus.BAD_GATEWAY);
		res.setChunked(true);
		res.write(content, StandardCharsets.UTF_8.name());
		end();
	}

	/**
	 * Bad request.
	 */
	public void badRequest() {

		status(HttpResponseStatus.BAD_REQUEST);
		end();
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
		res.setChunked(true);
		res.write(content, StandardCharsets.UTF_8.name());
		end();
	}

	/**
	 * Bad request.
	 *
	 * @param content
	 *            the content
	 */
	public void badRequest(String content) {

		status(HttpResponseStatus.BAD_REQUEST);
		res.setChunked(true);
		res.write(content, StandardCharsets.UTF_8.name());
		end();
	}

	/**
	 * Conflict.
	 */
	public void conflict() {

		status(HttpResponseStatus.CONFLICT);
		end();
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
		res.setChunked(true);
		res.write(content, StandardCharsets.UTF_8.name());
		end();
	}

	/**
	 * Conflict.
	 *
	 * @param content
	 *            the content
	 */
	public void conflict(String content) {

		status(HttpResponseStatus.BAD_REQUEST);
		res.setChunked(true);
		res.write(content, StandardCharsets.UTF_8.name());
		end();
	}

	public HttpServerResponse contentType(String contentType) {
		res.putHeader("content-type", contentType);
		return res;
	}

	public void end() {

		if (!res.ended()) {

			res.end();
		}
	}

	public void end(Buffer buffer) {

		if (!res.ended()) {

			res.end(buffer);
		}
	}

	/**
	 * Error.
	 */
	public void error() {

		status(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		end();
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
		res.setChunked(true);
		res.write(e.getMessage());
		end();
	}

	/**
	 * Error.
	 *
	 * @param content
	 *            the content
	 */
	public void error(String content) {

		status(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		res.setChunked(true);
		res.write(content, StandardCharsets.UTF_8.name());
		end();
	}

	/**
	 * Forbidden.
	 */
	public void forbidden() {

		status(HttpResponseStatus.FORBIDDEN);
		end();
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
		res.setChunked(true);
		res.write(content, StandardCharsets.UTF_8.name());
		end();
	}

	/**
	 * Forbidden.
	 *
	 * @param content
	 *            the content
	 */
	public void forbidden(String content) {

		status(HttpResponseStatus.BAD_REQUEST);
		res.setChunked(true);
		res.write(content, StandardCharsets.UTF_8.name());
		end();
	}

	public Optional<String> getHeader(String name) {

		return Optional.ofNullable(req.getHeader(name));
	}

	public String getParameter(String name) {

		return req.getParam(name);
	}

	/**
	 * No content.
	 */
	public void noContent() {

		status(HttpResponseStatus.NO_CONTENT);
		end();
	}

	/**
	 * Not acceptable.
	 */
	public void notAcceptable() {

		status(HttpResponseStatus.NOT_ACCEPTABLE);
		end();
	}

	/**
	 * Not found.
	 */
	public void notFound() {

		status(HttpResponseStatus.NOT_FOUND);
		end();
	}

	/**
	 * Not implemented.
	 */
	public void notImplemented() {

		status(HttpResponseStatus.NOT_IMPLEMENTED);
		end();
	}

	/**
	 * Pre condition failed.
	 */
	public void preConditionFailed() {

		status(HttpResponseStatus.PRECONDITION_FAILED);
		end();
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
		res.setChunked(true);
		res.write(content, StandardCharsets.UTF_8.name());
		end();
	}

	/**
	 * Pre condition failed.
	 *
	 * @param content
	 *            the content
	 */
	public void preConditionFailed(String content) {

		status(HttpResponseStatus.PRECONDITION_FAILED);
		res.setChunked(true);
		res.write(content, StandardCharsets.UTF_8.name());
		end();
	}

	public HttpServerResponse status(HttpResponseStatus status) {
		res.setStatusCode(status.code());
		res.setStatusMessage(status.reasonPhrase());
		return res;
	}

	/**
	 * Success.
	 */
	public void success() {

		status(HttpResponseStatus.OK);
		end();
	}

	/**
	 * Success using {@link Buffer}.
	 *
	 * @param buffer
	 *            the vertx buffer
	 */
	public void success(Buffer buffer) {

		status(HttpResponseStatus.OK);
		end(buffer);
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
		res.setChunked(true);
		res.write(content, StandardCharsets.UTF_8.name());
		end();
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

		res.setChunked(true);
		status(HttpResponseStatus.OK).write(content);
		end();
	}

	/**
	 * Unauthorized.
	 */
	public void unauthorized() {

		status(HttpResponseStatus.UNAUTHORIZED);
		end();
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
		res.setChunked(true);
		res.write(content, StandardCharsets.UTF_8.name());
		end();
	}

	/**
	 * Unauthorized.
	 *
	 * @param content
	 *            the content
	 */
	public void unauthorized(String content) {

		status(HttpResponseStatus.UNAUTHORIZED);
		res.setChunked(true);
		res.write(content, StandardCharsets.UTF_8.name());
		end();
	}

	/**
	 * Unvailable.
	 */
	public void unvailable() {

		status(HttpResponseStatus.SERVICE_UNAVAILABLE);
		end();
	}

	protected String body() {

		return ctx.getBodyAsString();
	}
}