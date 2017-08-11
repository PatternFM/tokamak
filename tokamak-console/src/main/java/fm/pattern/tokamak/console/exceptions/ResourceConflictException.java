package fm.pattern.tokamak.console.exceptions;

import java.util.ArrayList;
import java.util.List;

import fm.pattern.tokamak.sdk.commons.ErrorRepresentation;

public class ResourceConflictException extends RuntimeException {

	private static final long serialVersionUID = -7735345324657785548L;
	private final List<ErrorRepresentation> errors = new ArrayList<ErrorRepresentation>();

	public ResourceConflictException() {

	}

	public ResourceConflictException(ErrorRepresentation message) {
		this.errors.add(message);
	}

	public ResourceConflictException(List<ErrorRepresentation> errors) {
		this.errors.addAll(errors);
	}

	public List<ErrorRepresentation> getErrors() {
		return errors;
	}

}
