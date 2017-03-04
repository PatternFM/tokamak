package fm.pattern.jwt.server.service;

import static fm.pattern.jwt.server.PatternAssertions.assertThat;
import static fm.pattern.jwt.server.dsl.AccountDSL.account;
import static fm.pattern.microstructure.ResultType.NOT_FOUND;
import static fm.pattern.microstructure.ResultType.UNPROCESSABLE_ENTITY;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.Account;
import fm.pattern.jwt.server.security.PasswordEncodingService;
import fm.pattern.microstructure.Result;
import fm.pattern.microstructure.ResultType;

public class AccountServiceIntegrationTest extends IntegrationTest {

	@Autowired
	private AccountService accountService;

	@Autowired
	private PasswordEncodingService passwordEncodingService;

	@Test
	public void shouldBeAbleToCreateAnAccount() {
		Account account = account().withUsername("email@address.com").withPassword("password").build();

		Result<Account> result = accountService.create(account);
		assertThat(result).accepted();

		Account created = result.getInstance();
		assertThat(created.getId()).isNotNull();
		assertThat(created.getCreated()).isNotNull();
		assertThat(created.getUpdated()).isNotNull();
		assertThat(created.getCreated()).isEqualTo(created.getUpdated());
		assertThat(created.isLocked()).isFalse();
		assertThat(created.getUsername()).isEqualTo("email@address.com");
		assertThat(created.getPassword()).isNotNull();
		assertThat(passwordEncodingService.matches("password", created.getPassword()));
	}

	@Test
	public void shouldNotBeAbleToCreateAnInvalidAccount() {
		Account account = account().withUsername(null).withPassword("password").build();
		assertThat(accountService.create(account)).rejected().withType(UNPROCESSABLE_ENTITY);
	}

	@Test
	public void shouldBeAbleToUpdateAnAccount() {
		Account account = account().thatIs().persistent().build();
		account.setLocked(true);

		Result<Account> result = accountService.update(account);
		assertThat(result).accepted();
		assertThat(result.getInstance().isLocked()).isTrue();
	}

	@Test
	public void shouldNotBeAbleToUpdateAnInvalidAccount() {
		Account account = account().thatIs().persistent().build();
		account.setLocked(true);
		account.setUsername(null);

		Result<Account> result = accountService.update(account);
		assertThat(result).rejected().withType(ResultType.UNPROCESSABLE_ENTITY);
	}

	@Test
	public void shouldBeAbleToDeleteAnAccount() {
		Account account = account().thatIs().persistent().build();
		assertThat(accountService.findById(account.getId())).accepted();

		assertThat(accountService.delete(account)).accepted();
		assertThat(accountService.findById(account.getId())).rejected().withType(NOT_FOUND).withMessage("No such account id: " + account.getId());
	}

	@Test
	public void shouldEncryptTheAccountPasswordBeforeSavingTheAccount() {
		Account account = account().withPassword("password1234").thatIs().persistent().build();
		assertThat(account.getPassword()).startsWith("$2a$");
		assertThat(passwordEncodingService.matches("password1234", account.getPassword()));
	}

	@Test
	public void shouldBeAbleToFindAnAccountById() {
		Account account = account().thatIs().persistent().build();

		Result<Account> result = accountService.findById(account.getId());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(account);
	}

	@Test
	public void shouldNotBeAbleToFindAnAccountByIdIfTheAccountIdIsNullOrEmpty() {
		assertThat(accountService.findById(null)).rejected().withType(UNPROCESSABLE_ENTITY).withMessage("The account id to retrieve cannot be null or empty.");
		assertThat(accountService.findById("")).rejected().withType(UNPROCESSABLE_ENTITY).withMessage("The account id to retrieve cannot be null or empty.");
		assertThat(accountService.findById("  ")).rejected().withType(UNPROCESSABLE_ENTITY).withMessage("The account id to retrieve cannot be null or empty.");
	}

	@Test
	public void shouldNotBeAbleToFindAnAccountByIdIfTheAccountIdDoesNotExist() {
		assertThat(accountService.findById("csrx")).rejected().withType(NOT_FOUND).withMessage("No such account id: csrx");
	}

	@Test
	public void shouldBeAbleToFindAnAccountByUsername() {
		Account account = account().thatIs().persistent().build();

		Result<Account> result = accountService.findByUsername(account.getUsername());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(account);
	}

	@Test
	public void shouldNotBeAbleToFindAnAccountByUsernameIfTheUsernameIsNull() {
		assertThat(accountService.findByUsername(null)).rejected().withType(UNPROCESSABLE_ENTITY).withMessage("The account username to retrieve cannot be null or empty.");
	}

	@Test
	public void shouldNotBeAbleToFindAnAccountByIdIfTheEmailAddressIsInvalid() {
		assertThat(accountService.findByUsername("csrx")).rejected().withType(NOT_FOUND).withMessage("No such username: csrx");
	}

	@Test
	public void shouldBeAbleToUpdateAPassword() {
		String currentPassword = "myOLDPassword";
		String newPassword = "myNEWPassword";
		String username = "test@email.com";

		Account account = account().withUsername(username).withPassword(currentPassword).thatIs().persistent().build();
		assertThat(accountService.updatePassword(account, currentPassword, newPassword)).accepted();

		assertAccountHasPassword(username, newPassword);
	}

	@Test
	public void shouldNotBeAbleToUpdateAPasswordWhenTheNewPasswordIsNotProvided() {
		String oldPassword = "myOLDPassword";
		String email = "test@email.com";

		Account account = account().withUsername(email).withPassword(oldPassword).thatIs().persistent().build();
		assertThat(accountService.updatePassword(account, oldPassword, null)).rejected().withMessage("Your new password must be provided.");
		assertThat(accountService.updatePassword(account, oldPassword, "")).rejected().withMessage("Your new password must be provided.");
		assertThat(accountService.updatePassword(account, oldPassword, "  ")).rejected().withMessage("Your new password must be provided.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAPasswordWhenTheNewPasswordIsLessThanEightCharacters() {
		String oldPassword = "myOLDPassword";
		String email = "test@email.com";

		Account account = account().withUsername(email).withPassword(oldPassword).thatIs().persistent().build();
		assertThat(accountService.updatePassword(account, oldPassword, "abc")).rejected().withMessage("Your new password must be between 8 and 50 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAPasswordWhenTheNewPasswordIsGreaterThan50Characters() {
		String oldPassword = "myOLDPassword";
		String email = "test@email.com";

		Account account = account().withUsername(email).withPassword(oldPassword).thatIs().persistent().build();
		assertThat(accountService.updatePassword(account, oldPassword, RandomStringUtils.randomAlphabetic(51))).rejected().withMessage("Your new password must be between 8 and 50 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAPasswordWhenTheCurrentPasswordIsNotProvided() {
		String oldPassword = "myOLDPassword";
		String email = "test@email.com";

		Account account = account().withUsername(email).withPassword(oldPassword).thatIs().persistent().build();
		assertThat(accountService.updatePassword(account, null, "ABC")).rejected().withMessage("Your current password must be provided.");
		assertThat(accountService.updatePassword(account, "", "ABC")).rejected().withMessage("Your current password must be provided.");
		assertThat(accountService.updatePassword(account, "  ", "ABC")).rejected().withMessage("Your current password must be provided.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAPasswordWhenTheProvidedPasswordDoesNotMatchTheExistingPassword() {
		String oldPassword = "myOLDPassword";
		String email = "test@email.com";

		Account account = account().withUsername(email).withPassword(oldPassword).thatIs().persistent().build();
		assertThat(accountService.updatePassword(account, "invalid", "ABC")).rejected().withMessage("The password you provided does not match your current password. Please try again.");
	}

	private void assertAccountHasPassword(String email, String expectedPassword) {
		String accountPassword = accountService.findByUsername(email).getInstance().getPassword();
		passwordEncodingService.matches(expectedPassword, accountPassword);
	}

}
