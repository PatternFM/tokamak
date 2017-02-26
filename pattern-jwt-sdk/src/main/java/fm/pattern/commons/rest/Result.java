package fm.pattern.commons.rest;

import java.util.ArrayList;
import java.util.List;

public class Result<T> {

	private T instance;
	private Integer responseCode;
	private List<ErrorRepresentation> errors = new ArrayList<ErrorRepresentation>();

	public static <T> Result<T> accept(Integer responseCode, T instance) {
		return new Result<T>(responseCode, instance, new ArrayList<ErrorRepresentation>());
	}

	public static <T> Result<T> reject(Integer responseCode, T instance, List<ErrorRepresentation> errors) {
		return new Result<T>(responseCode, instance, errors);
	}

	public Result() {

	}

	private Result(Integer responseCode, T instance, List<ErrorRepresentation> errors) {
		this.responseCode = responseCode;
		this.instance = instance;
		this.errors.addAll(errors);
	}

	public T getInstance() {
		return instance;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public List<ErrorRepresentation> getErrors() {
		return errors;
	}

	public boolean accepted() {
		return getErrors().isEmpty();
	}

	public boolean rejected() {
		return !getErrors().isEmpty();
	}

}
