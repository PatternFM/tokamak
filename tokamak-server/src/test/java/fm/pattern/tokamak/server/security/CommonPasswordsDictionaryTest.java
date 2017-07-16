package fm.pattern.tokamak.server.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

public class CommonPasswordsDictionaryTest {

	@Test
	public void shouldBeAbleToListTenThousandOfTheMostCommonPasswords() {
		List<String> common = CommonPasswordsDictionary.list();
		assertThat(common).hasSize(10000);
	}

	@Test
	public void shouldBeAbleToIdentifyCommonPasswords() {
		assertThat(CommonPasswordsDictionary.contains("password")).isTrue();
		assertThat(CommonPasswordsDictionary.contains("midnight")).isTrue();
		assertThat(CommonPasswordsDictionary.contains("123456")).isTrue();
		assertThat(CommonPasswordsDictionary.contains("asdfs2342o8fhshfsd")).isFalse();
	}
	
	@Test
	public void shouldPerformACaseInsensitiveMatchWhenIdentifyingCommonPasswords() {
		assertThat(CommonPasswordsDictionary.contains("password")).isTrue();
		assertThat(CommonPasswordsDictionary.contains("PASSWORD")).isTrue();
		assertThat(CommonPasswordsDictionary.contains("PAsswoRD")).isTrue();
	}
	
	@Test
	public void shouldReturnFalseIfThePasswordToCheckIsNullOrEmpty() {
		assertThat(CommonPasswordsDictionary.contains(null)).isFalse();
		assertThat(CommonPasswordsDictionary.contains("")).isFalse();
		assertThat(CommonPasswordsDictionary.contains("  ")).isFalse();
	}
	
}
