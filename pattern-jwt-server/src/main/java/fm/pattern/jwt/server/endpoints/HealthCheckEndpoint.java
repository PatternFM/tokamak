/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fm.pattern.jwt.server.endpoints;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fm.pattern.commons.rest.HealthCheckRepresentation;
import fm.pattern.commons.rest.HealthChecksRepresentation;
import fm.pattern.jwt.server.service.DatabaseHealthCheckService;

@RestController
public class HealthCheckEndpoint extends Endpoint {

	private final DatabaseHealthCheckService databaseHealthCheckService;

	@Autowired
	public HealthCheckEndpoint(DatabaseHealthCheckService databaseHealthCheckService) {
		this.databaseHealthCheckService = databaseHealthCheckService;
	}

	@RequestMapping(value = "/health", method = GET, produces = "application/json")
	public HealthChecksRepresentation get() {
		HealthChecksRepresentation healthChecksRepresentation = new HealthChecksRepresentation();
		HealthCheckRepresentation databaseHealthCheck = databaseHealthCheck();
		healthChecksRepresentation.add(databaseHealthCheck);
		healthChecksRepresentation.setStatus(databaseHealthCheck.getStatus());
		return healthChecksRepresentation;
	}

	private HealthCheckRepresentation databaseHealthCheck() {
		HealthCheckRepresentation representation = new HealthCheckRepresentation();
		representation.setService("database");
		if (!databaseHealthCheckService.isHealthy()) {
			representation.setStatus("ERROR");
			representation.setMessage("Unable to ping database instance.");
		}
		else {
			representation.setStatus("OK");
		}
		return representation;
	}

}
