package fm.pattern.tokamak.server;

import org.assertj.core.api.Assertions;

import fm.pattern.valex.Result;

public class PatternAssertions extends Assertions {

	public static ResultAssertions assertThat(Result<?> result) {
		return new ResultAssertions(result);
	}

	public static UtilityClassAssertions assertClass(Class<?> clazz) {
		return new UtilityClassAssertions(clazz);
	}

}
