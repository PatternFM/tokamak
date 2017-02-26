package fm.pattern.jwt.server.validation;

import static fm.pattern.jwt.server.dsl.AccountDSL.account;
import static fm.pattern.microstructure.ResultType.UNPROCESSABLE_ENTITY;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.Account;

public class AccountValidationTest extends IntegrationTest {

	@Test
	public void shouldBeAbleToCreateAValidAccount() {
		onCreate(account().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfTheUsernameIsAlreadyInUse() {
		account().withUsername("use@email.com").thatIs().persistent().build();
		onCreate(account().withUsername("use@email.com").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.username.conflict").withDescription("This account username is already in use.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfTheUsernameIsNotProvided() {
		onCreate(account().withUsername(null).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.username.required").withDescription("An account username is required.");
		onCreate(account().withUsername("").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.username.required").withDescription("An account username is required.");
		onCreate(account().withUsername("  ").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.username.required").withDescription("An account username is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfTheUsernameIsLessThan3Characters() {
		onCreate(account().withUsername(randomAlphabetic(2)).build()).rejected().withType(UNPROCESSABLE_ENTITY).withDescription("An account username must be between 3 and 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfTheUsernameIsGreaterThan128Characters() {
		onCreate(account().withUsername(randomAlphabetic(129)).build()).rejected().withType(UNPROCESSABLE_ENTITY).withDescription("An account username must be between 3 and 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfThePasswordIsNotProvided() {
		onCreate(account().withPassword(null).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.password.required").withDescription("An account password is required.");
		onCreate(account().withPassword("").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.password.required").withDescription("An account password is required.");
		onCreate(account().withPassword("  ").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.password.required").withDescription("An account password is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfThePasswordIsGreaterThan255Characters() {
		onCreate(account().withPassword(RandomStringUtils.randomNumeric(256)).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.password.size").withDescription("An account password must be between 8 and 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfThePasswordIsLessThenEightCharacters() {
		onCreate(account().withPassword(RandomStringUtils.randomNumeric(7)).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.password.size").withDescription("An account password must be between 8 and 255 characters.");
	}

	@Test
	public void shouldBeAbleToUpdateAValidAccount() {
		onUpdate(account().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfTheUsernameIsAlreadyInUse() {
		account().withUsername("first").thatIs().persistent().build();

		Account updated = account().withUsername("second").thatIs().persistent().build();
		updated.setUsername("first");

		onUpdate(updated).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.username.conflict").withDescription("This account username is already in use.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfTheUsernameIsNotPresent() {
		onUpdate(account().withUsername(null).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.username.required").withDescription("An account username is required.");
		onUpdate(account().withUsername("").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.username.required").withDescription("An account username is required.");
		onUpdate(account().withUsername("  ").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.username.required").withDescription("An account username is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfTheUsernameIsLessThan3Characters() {
		onUpdate(account().withUsername(randomAlphabetic(2)).build()).rejected().withType(UNPROCESSABLE_ENTITY).withDescription("An account username must be between 3 and 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfTheUsernameIsGreaterThan128Characters() {
		onUpdate(account().withUsername(randomAlphabetic(129)).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.username.size").withDescription("An account username must be between 3 and 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfThePasswordIsNotPresent() {
		onUpdate(account().withPassword(null).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.password.required").withDescription("An account password is required.");
		onUpdate(account().withPassword("").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.password.required").withDescription("An account password is required.");
		onUpdate(account().withPassword("  ").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.password.required").withDescription("An account password is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfThePasswordIsNotProvided() {
		onUpdate(account().withPassword(null).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.password.required").withDescription("An account password is required.");
		onUpdate(account().withPassword("").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.password.required").withDescription("An account password is required.");
		onUpdate(account().withPassword("  ").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.password.required").withDescription("An account password is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfThePasswordIsGreaterThan255Characters() {
		onUpdate(account().withPassword(RandomStringUtils.randomNumeric(256)).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.password.size").withDescription("An account password must be between 8 and 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfThePasswordIsLessThenEightCharacters() {
		onUpdate(account().withPassword(RandomStringUtils.randomNumeric(7)).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("account.password.size").withDescription("An account password must be between 8 and 255 characters.");
	}

}
