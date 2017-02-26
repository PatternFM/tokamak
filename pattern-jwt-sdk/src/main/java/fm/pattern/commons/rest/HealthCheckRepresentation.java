package fm.pattern.commons.rest;

public class HealthCheckRepresentation extends Representation {

	private String service;
	private String status;
	private String message;

	public HealthCheckRepresentation() {

	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
