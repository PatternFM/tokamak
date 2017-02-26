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
