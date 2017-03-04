package fm.pattern.jwt.server.validation;

import static fm.pattern.jwt.server.dsl.AccountDSL.account;
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
		String username = RandomStringUtils.randomAlphanumeric(15);

		account().withUsername(username).thatIs().persistent().build();
		onCreate(account().withUsername(username).build()).rejected().withCode("ACC-0003").withMessage("This account username is already in use.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfTheUsernameIsNotProvided() {
		onCreate(account().withUsername(null).build()).rejected().withCode("ACC-0001").withMessage("An account username is required.");
		onCreate(account().withUsername("").build()).rejected().withCode("ACC-0001").withMessage("An account username is required.");
		onCreate(account().withUsername("  ").build()).rejected().withCode("ACC-0001").withMessage("An account username is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfTheUsernameIsLessThan3Characters() {
		onCreate(account().withUsername(randomAlphabetic(2)).build()).rejected().withCode("ACC-0002").withMessage("An account username must be between 3 and 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfTheUsernameIsGreaterThan128Characters() {
		onCreate(account().withUsername(randomAlphabetic(129)).build()).rejected().withCode("ACC-0002").withMessage("An account username must be between 3 and 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfThePasswordIsNotProvided() {
		onCreate(account().withPassword(null).build()).rejected().withCode("ACC-0004").withMessage("An account password is required.");
		onCreate(account().withPassword("").build()).rejected().withCode("ACC-0004").withMessage("An account password is required.");
		onCreate(account().withPassword("  ").build()).rejected().withCode("ACC-0004").withMessage("An account password is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfThePasswordIsGreaterThan255Characters() {
		onCreate(account().withPassword(RandomStringUtils.randomNumeric(256)).build()).rejected().withCode("ACC-0005").withMessage("An account password must be between 8 and 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfThePasswordIsLessThenEightCharacters() {
		onCreate(account().withPassword(RandomStringUtils.randomNumeric(7)).build()).rejected().withCode("ACC-0005").withMessage("An account password must be between 8 and 255 characters.");
	}

	@Test
	public void shouldBeAbleToUpdateAValidAccount() {
		onUpdate(account().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfTheUsernameIsAlreadyInUse() {
		String username = RandomStringUtils.randomAlphanumeric(15);
		account().withUsername(username).thatIs().persistent().build();

		Account account = account().thatIs().persistent().build();
		account.setUsername(username);

		onUpdate(account).rejected().withCode("ACC-0003").withMessage("This account username is already in use.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfTheUsernameIsNotPresent() {
		onUpdate(account().withUsername(null).build()).rejected().withCode("ACC-0001").withMessage("An account username is required.");
		onUpdate(account().withUsername("").build()).rejected().withCode("ACC-0001").withMessage("An account username is required.");
		onUpdate(account().withUsername("  ").build()).rejected().withCode("ACC-0001").withMessage("An account username is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfTheUsernameIsLessThan3Characters() {
		onUpdate(account().withUsername(randomAlphabetic(2)).build()).rejected().withCode("ACC-0002").withMessage("An account username must be between 3 and 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfTheUsernameIsGreaterThan128Characters() {
		onUpdate(account().withUsername(randomAlphabetic(129)).build()).rejected().withCode("ACC-0002").withMessage("An account username must be between 3 and 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfThePasswordIsNotPresent() {
		onUpdate(account().withPassword(null).build()).rejected().withCode("ACC-0004").withMessage("An account password is required.");
		onUpdate(account().withPassword("").build()).rejected().withCode("ACC-0004").withMessage("An account password is required.");
		onUpdate(account().withPassword("  ").build()).rejected().withCode("ACC-0004").withMessage("An account password is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfThePasswordIsNotProvided() {
		onUpdate(account().withPassword(null).build()).rejected().withCode("ACC-0004").withMessage("An account password is required.");
		onUpdate(account().withPassword("").build()).rejected().withCode("ACC-0004").withMessage("An account password is required.");
		onUpdate(account().withPassword("  ").build()).rejected().withCode("ACC-0004").withMessage("An account password is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfThePasswordIsGreaterThan255Characters() {
		onUpdate(account().withPassword(RandomStringUtils.randomNumeric(256)).build()).rejected().withCode("ACC-0005").withMessage("An account password must be between 8 and 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfThePasswordIsLessThenEightCharacters() {
		onUpdate(account().withPassword(RandomStringUtils.randomNumeric(7)).build()).rejected().withCode("ACC-0005").withMessage("An account password must be between 8 and 255 characters.");
	}

}
