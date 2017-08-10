package fm.pattern.tokamak.console;

import java.util.ArrayList;
import java.util.List;

import fm.pattern.tokamak.sdk.commons.ErrorRepresentation;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = -11353433548L;
	private final List<ErrorRepresentation> errors = new ArrayList<ErrorRepresentation>();

	public BadRequestException() {

	}

	public BadRequestException(ErrorRepresentation message) {
		this.errors.add(message);
	}

	public BadRequestException(List<ErrorRepresentation> errors) {
		this.errors.addAll(errors);
	}

	public List<ErrorRepresentation> getErrors() {
		return errors;
	}

}
