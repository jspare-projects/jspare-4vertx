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
package org.jspare.forvertx.web.exceptions;

import org.jspare.core.exception.InfraException;

/**
 * The Class FilterException.
 *
 * @author pflima
 * @since 05/10/2015
 */
public class MiddlewareException extends InfraException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new filter exception.
	 *
	 * @param message
	 *            the message
	 */
	public MiddlewareException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new filter exception.
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public MiddlewareException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new filter exception.
	 *
	 * @param cause
	 *            the cause
	 */
	public MiddlewareException(Throwable cause) {
		super(cause);
	}
}