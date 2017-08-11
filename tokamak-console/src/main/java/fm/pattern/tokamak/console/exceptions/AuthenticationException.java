package fm.pattern.tokamak.console.exceptions;

import java.util.ArrayList;
import java.util.List;

import fm.pattern.tokamak.sdk.commons.ErrorRepresentation;

public class AuthenticationException extends RuntimeException {

	private static final long serialVersionUID = -7022235345324655548L;
	private final List<ErrorRepresentation> errors = new ArrayList<ErrorRepresentation>();

	public AuthenticationException() {

	}

	public AuthenticationException(ErrorRepresentation message) {
		this.errors.add(message);
	}

	public AuthenticationException(List<ErrorRepresentation> errors) {
		this.errors.addAll(errors);
	}

	public List<ErrorRepresentation> getErrors() {
		return errors;
	}

}
