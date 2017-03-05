package fm.pattern.jwt.spec;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import fm.pattern.commons.rest.ErrorRepresentation;
import fm.pattern.commons.rest.Result;

public class ResultAssertions extends AbstractAssert<ResultAssertions, Result<?>> {

	public ResultAssertions(Result<?> result) {
		super(result, ResultAssertions.class);
	}

	public static ResultAssertions assertThat(Result<?> result) {
		return new ResultAssertions(result);
	}

	public ResultAssertions and() {
		return this;
	}

	public ResultAssertions accepted() {
		Assertions.assertThat(actual.accepted()).describedAs("Expected the entity to be accepted, but had " + actual.getErrors().size() + " errors: " + Arrays.toString(actual.getErrors().toArray())).isTrue();
		return this;
	}

	public ResultAssertions rejected() {
		Assertions.assertThat(actual.rejected()).describedAs("Expected the entity to be rejected, but had " + actual.getErrors().size() + " errors: " + Arrays.toString(actual.getErrors().toArray())).isTrue();
		return this;
	}

	public ResultAssertions withResponseCode(Integer responseCode) {
		Assertions.assertThat(actual.getResponseCode()).isEqualTo(responseCode).describedAs("Expected a response code of " + responseCode + " but found " + actual.getResponseCode());
		return this;
	}

	public ResultAssertions withCode(String... codes) {
		Assertions.assertThat(codes.length).describedAs("Expected " + codes.length + " error codes but found " + actual.getErrors().size() + " instead: " + Arrays.toString(actual.getErrors().toArray())).isEqualTo(actual.getErrors().size());

		for (String code : codes) {
			Assertions.assertThat(actual.getErrors()).extracting("code").contains(code);
		}

		return this;
	}

	public ResultAssertions withMessage(String... messages) {
		Assertions.assertThat(messages.length).describedAs("Expected " + messages.length + " error descriptions but found " + actual.getErrors().size() + " instead: " + Arrays.toString(actual.getErrors().toArray())).isEqualTo(actual.getErrors().size());

		for (String message : messages) {
			Assertions.assertThat(actual.getErrors()).extracting("message").contains(message);
		}

		return this;
	}

	public ResultAssertions withError(String code, String message) {
		for (ErrorRepresentation error : actual.getErrors()) {
			if (error.getCode().equals(code) && error.getMessage().equals(message)) {
				return this;
			}
		}

		String errors = actual.getErrors().stream().map(error -> error.toString()).collect(Collectors.joining(","));
		Assertions.fail("Unable to find an error with code '" + code + "', message '" + message + ". Errors are: " + errors);
		return this;
	}

}
