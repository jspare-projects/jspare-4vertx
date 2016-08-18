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
package org.jspare.samples.routes;

import java.util.List;

import org.jspare.core.container.Inject;
import org.jspare.forvertx.web.handling.Handling;
import org.jspare.forvertx.web.mapping.handlers.Handler;
import org.jspare.forvertx.web.mapping.handling.Parameter;
import org.jspare.forvertx.web.mapping.method.Get;
import org.jspare.forvertx.web.mapping.method.Post;
import org.jspare.samples.model.Car;
import org.jspare.samples.service.CarService;

public class CarRoutes extends Handling {

	@Inject
	private CarService service;

	@Handler
	@Get("/cars")
	public void list() {

		List<Car> list = service.list();
		success(list);
	}

	@Handler
	@Post("/cars")
	public void save(Car car) {

		service.save(car);
		success();
	}

	@Handler
	@Get("/cars/:id")
	public void find(@Parameter("id") String id) {

		if (!isValidId(id)) {
			badRequest();
			return;
		}

		Car car = service.findById(Integer.valueOf(id));
		success(car);
	}

	private boolean isValidId(String id) {
		try {
			Integer.valueOf(id);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}