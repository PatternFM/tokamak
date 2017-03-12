package fm.pattern.jwt.spec;

import org.assertj.core.api.Assertions;

import fm.pattern.tokamak.sdk.commons.Result;

public class PatternAssertions extends Assertions {

	public static ResultAssertions assertThat(Result<?> result) {
		return new ResultAssertions(result);
	}

}
