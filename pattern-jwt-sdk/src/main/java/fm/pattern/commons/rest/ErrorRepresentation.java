package fm.pattern.commons.rest;

public class ErrorRepresentation extends Representation {

	private String code;
	private String description;
	private String property;

	public ErrorRepresentation() {

	}

	public ErrorRepresentation(String code, String description) {
		this.code = code;
		this.description = description;
		this.property = null;
	}

	public ErrorRepresentation(String code, String description, String property) {
		this.code = code;
		this.description = description;
		this.property = property;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public String getProperty() {
		return property;
	}

}
