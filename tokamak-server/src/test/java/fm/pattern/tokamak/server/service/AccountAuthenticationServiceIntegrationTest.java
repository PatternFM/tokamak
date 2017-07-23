package fm.pattern.tokamak.server.service;

import static fm.pattern.tokamak.server.dsl.AccountDSL.account;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.Account;
import fm.pattern.tokamak.server.service.AccountAuthenticationService;

public class AccountAuthenticationServiceIntegrationTest extends IntegrationTest {

	@Autowired
	private AccountAuthenticationService authenticationService;

	@Test
	public void shouldBeAbleToFindAnAccountByUsername() {
		account().withUsername("username@email.com").save();
		UserDetails authenticatedUser = authenticationService.loadUserByUsername("username@email.com");
		assertThat(authenticatedUser).isNotNull();
	}

	@Test(expected = UsernameNotFoundException.class)
	public void shouldThrowAnExceptionIfTheAccountIsLocked() {
		Account account = account().withUsername("username@email.com").save();
		assertThat(account.isLocked()).isFalse();

		Account lockedAccount = account().withUsername("locked@email.com").thatIs().locked().and().persistent().build();
		assertThat(lockedAccount.isLocked()).isTrue();

		authenticationService.loadUserByUsername("inacuser@email.com");
	}

	@Test(expected = UsernameNotFoundException.class)
	public void shouldThrowAnExceptionIfTheUsernameIsEmpty() {
		authenticationService.loadUserByUsername("");
	}

	@Test(expected = UsernameNotFoundException.class)
	public void shouldThrowAnExceptionIfTheUsernameIsNull() {
		authenticationService.loadUserByUsername(null);
	}

	@Test(expected = UsernameNotFoundException.class)
	public void shouldThrowAnExceptionIfTheUsernameDoesNotMatchAnExistingUser() {
		authenticationService.loadUserByUsername("alsidfjlasdfi");
	}

}
