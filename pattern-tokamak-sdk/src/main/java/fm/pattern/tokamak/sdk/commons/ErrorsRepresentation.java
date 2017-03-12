package fm.pattern.tokamak.sdk.commons;

import java.util.ArrayList;
import java.util.List;

public class ErrorsRepresentation extends Representation {

	private List<ErrorRepresentation> errors = new ArrayList<ErrorRepresentation>();

	public ErrorsRepresentation() {

	}

	public ErrorsRepresentation(List<ErrorRepresentation> errors) {
		this.errors.addAll(errors);
	}

	public List<ErrorRepresentation> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorRepresentation> errors) {
		this.errors = errors;
	}

}
