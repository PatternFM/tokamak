package fm.pattern.tokamak.server;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import fm.pattern.valex.Reportable;
import fm.pattern.valex.ReportableException;
import fm.pattern.valex.Result;

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

    public ResultAssertions withErrorCount(Integer count) {
        Assertions.assertThat(actual.getErrors()).describedAs("Expected the result to have " + count + "errors, but found " + actual.getErrors().size());
        return this;
    }

    public ResultAssertions withError(String code, String message, Class<? extends ReportableException> exception) {
        for (Reportable error : actual.getErrors()) {
            if (error.getCode().equals(code) && error.getMessage().equals(message) && error.getException().equals(exception)) {
                return this;
            }
        }

        String errors = actual.getErrors().stream().map(error -> error.toString()).collect(Collectors.joining(","));
        Assertions.fail("Unable to find an error with code '" + code + "', message '" + message + "' and class '" + exception.getCanonicalName() + "'. Errors are: " + errors);
        return this;
    }

    public ResultAssertions withCode(String code) {
        Assertions.assertThat(actual.getErrors()).extracting("code").contains(code);
        return this;
    }

    public ResultAssertions withMessage(String message) {
        Assertions.assertThat(actual.getErrors()).extracting("message").contains(message);
        return this;
    }

    public ResultAssertions withException(Class<? extends ReportableException> exception) {
        Assertions.assertThat(actual.getErrors()).extracting("exception").contains(exception);
        return this;
    }

}
