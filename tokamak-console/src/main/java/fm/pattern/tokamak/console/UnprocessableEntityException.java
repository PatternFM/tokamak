package fm.pattern.tokamak.console;

import java.util.ArrayList;
import java.util.List;

import fm.pattern.tokamak.sdk.commons.ErrorRepresentation;

public class UnprocessableEntityException extends RuntimeException {

	private static final long serialVersionUID = -7333875229824655548L;
	private final List<ErrorRepresentation> errors = new ArrayList<ErrorRepresentation>();

	public UnprocessableEntityException() {

	}

	public UnprocessableEntityException(ErrorRepresentation message) {
		this.errors.add(message);
	}

	public UnprocessableEntityException(List<ErrorRepresentation> errors) {
		this.errors.addAll(errors);
	}

	public List<ErrorRepresentation> getErrors() {
		return errors;
	}

}
