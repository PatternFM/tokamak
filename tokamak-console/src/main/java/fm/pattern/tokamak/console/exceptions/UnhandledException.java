package fm.pattern.tokamak.console.exceptions;

import java.util.ArrayList;
import java.util.List;

import fm.pattern.tokamak.sdk.commons.ErrorRepresentation;

public class UnhandledException extends RuntimeException {

	private static final long serialVersionUID = -7093595345324626648L;
	private final List<ErrorRepresentation> errors = new ArrayList<ErrorRepresentation>();

	public UnhandledException() {

	}

	public UnhandledException(ErrorRepresentation message) {
		this.errors.add(message);
	}

	public UnhandledException(List<ErrorRepresentation> errors) {
		this.errors.addAll(errors);
	}

	public List<ErrorRepresentation> getErrors() {
		return errors;
	}

}
