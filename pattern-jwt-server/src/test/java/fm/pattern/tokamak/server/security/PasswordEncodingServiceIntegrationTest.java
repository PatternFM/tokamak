package fm.pattern.tokamak.server.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.security.PasswordEncodingService;

public class PasswordEncodingServiceIntegrationTest extends IntegrationTest {

	@Autowired
	private PasswordEncodingService passwordEncodingService;

	@Test
	public void shouldBeAbleToEncodeAPassword() {
		String encodedPassword = passwordEncodingService.encode("passwor13");
		assertThat(encodedPassword).isNotNull();
	}

	@Test
	public void shouldBeAbleToMatchARawPasswordWithItsEncodedCounterpart() {
		String encodedPassword = passwordEncodingService.encode("passwor13");
		assertThat(passwordEncodingService.matches("passwor13", encodedPassword)).isTrue();
	}

	@Test
	public void shouldBeAbleToMatchARawPasswordWithItsEncodedCounterpartIfThePasswordIsIncorrect() {
		String encodedPassword = passwordEncodingService.encode("passwor13");
		assertThat(passwordEncodingService.matches("passwor12", encodedPassword)).isFalse();
	}

}
