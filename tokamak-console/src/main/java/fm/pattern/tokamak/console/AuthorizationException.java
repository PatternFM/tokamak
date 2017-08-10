package fm.pattern.tokamak.console;

import java.util.ArrayList;
import java.util.List;

import fm.pattern.tokamak.sdk.commons.ErrorRepresentation;

public class AuthorizationException extends RuntimeException {

	private static final long serialVersionUID = -7022235229824655548L;
	private final List<ErrorRepresentation> errors = new ArrayList<ErrorRepresentation>();

	public AuthorizationException() {

	}

	public AuthorizationException(ErrorRepresentation message) {
		this.errors.add(message);
	}

	public AuthorizationException(List<ErrorRepresentation> errors) {
		this.errors.addAll(errors);
	}

	public List<ErrorRepresentation> getErrors() {
		return errors;
	}

}
