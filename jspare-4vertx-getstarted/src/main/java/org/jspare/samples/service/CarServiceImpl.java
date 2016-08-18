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
package org.jspare.samples.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspare.samples.model.Car;

public class CarServiceImpl implements CarService {

	private final static Map<Integer, Car> CARS = new HashMap<>();

	static {

		CARS.put(1, new Car(1, "CAR 1"));
		CARS.put(2, new Car(2, "CAR 2"));
	}

	@Override
	public void save(Car car) {

		CARS.put(car.getId(), car);
	}

	@Override
	public Car findById(int id) {

		return CARS.get(id);
	}

	@Override
	public List<Car> list() {

		return new ArrayList<>(CARS.values());
	}

}