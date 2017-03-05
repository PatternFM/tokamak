package fm.pattern.jwt.server;

import org.assertj.core.api.Assertions;

import fm.pattern.validation.Result;

public class PatternAssertions extends Assertions {

	public static ResultAssertions assertThat(Result<?> result) {
		return new ResultAssertions(result);
	}

}
