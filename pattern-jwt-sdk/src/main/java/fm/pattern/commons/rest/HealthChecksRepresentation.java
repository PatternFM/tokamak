package fm.pattern.commons.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HealthChecksRepresentation extends Representation {

	private String status;
	private Long timestamp = new Date().getTime();
	private List<HealthCheckRepresentation> checks = new ArrayList<HealthCheckRepresentation>();

	public HealthChecksRepresentation() {

	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public void add(HealthCheckRepresentation healthCheck) {
		this.checks.add(healthCheck);
	}

	public List<HealthCheckRepresentation> getChecks() {
		return checks;
	}

	public void setChecks(List<HealthCheckRepresentation> checks) {
		this.checks = checks;
	}

}
