package fm.pattern.tokamak.server.validation;

import static fm.pattern.tokamak.server.dsl.AccountDSL.account;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.Account;
import fm.pattern.valex.ResourceConflictException;
import fm.pattern.valex.UnprocessableEntityException;

public class AccountValidationTest extends IntegrationTest {

	@Test
	public void shouldBeAbleToCreateAValidAccount() {
		onCreate(account().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfTheUsernameIsAlreadyInUse() {
		String username = RandomStringUtils.randomAlphanumeric(15);

		account().withUsername(username).save();
		onCreate(account().withUsername(username).build()).rejected().withError("ACC-0003", "This account username is already in use.", ResourceConflictException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfTheUsernameIsNotProvided() {
		onCreate(account().withUsername(null).build()).rejected().withError("ACC-0001", "An account username is required.", UnprocessableEntityException.class);
		onCreate(account().withUsername("").build()).rejected().withError("ACC-0001", "An account username is required.", UnprocessableEntityException.class);
		onCreate(account().withUsername("  ").build()).rejected().withError("ACC-0001", "An account username is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfTheUsernameIsLessThan3Characters() {
		onCreate(account().withUsername(randomAlphabetic(2)).build()).rejected().withError("ACC-0002", "An account username must be between 3 and 128 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfTheUsernameIsGreaterThan128Characters() {
		onCreate(account().withUsername(randomAlphabetic(129)).build()).rejected().withError("ACC-0002", "An account username must be between 3 and 128 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfThePasswordIsNotProvided() {
		onCreate(account().withPassword(null).build()).rejected().withError("ACC-0004", "An account password is required.", UnprocessableEntityException.class);
		onCreate(account().withPassword("").build()).rejected().withError("ACC-0004", "An account password is required.", UnprocessableEntityException.class);
		onCreate(account().withPassword("  ").build()).rejected().withError("ACC-0004", "An account password is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfThePasswordIsGreaterThan255Characters() {
		onCreate(account().withPassword(RandomStringUtils.randomNumeric(256)).build()).rejected().withError("ACC-0005", "An account password must be less than 255 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldBeAbleToUpdateAValidAccount() {
		onUpdate(account().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfTheUsernameIsAlreadyInUse() {
		String username = RandomStringUtils.randomAlphanumeric(15);
		account().withUsername(username).save();

		Account account = account().save();
		account.setUsername(username);

		onUpdate(account).rejected().withError("ACC-0003", "This account username is already in use.", ResourceConflictException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfTheUsernameIsNotPresent() {
		onUpdate(account().withUsername(null).build()).rejected().withError("ACC-0001", "An account username is required.", UnprocessableEntityException.class);
		onUpdate(account().withUsername("").build()).rejected().withError("ACC-0001", "An account username is required.", UnprocessableEntityException.class);
		onUpdate(account().withUsername("  ").build()).rejected().withError("ACC-0001", "An account username is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfTheUsernameIsLessThan3Characters() {
		onUpdate(account().withUsername(randomAlphabetic(2)).build()).rejected().withError("ACC-0002", "An account username must be between 3 and 128 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfTheUsernameIsGreaterThan128Characters() {
		onUpdate(account().withUsername(randomAlphabetic(129)).build()).rejected().withError("ACC-0002", "An account username must be between 3 and 128 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfThePasswordIsNotPresent() {
		onUpdate(account().withPassword(null).build()).rejected().withError("ACC-0004", "An account password is required.", UnprocessableEntityException.class);
		onUpdate(account().withPassword("").build()).rejected().withError("ACC-0004", "An account password is required.", UnprocessableEntityException.class);
		onUpdate(account().withPassword("  ").build()).rejected().withError("ACC-0004", "An account password is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfThePasswordIsNotProvided() {
		onUpdate(account().withPassword(null).build()).rejected().withError("ACC-0004", "An account password is required.", UnprocessableEntityException.class);
		onUpdate(account().withPassword("").build()).rejected().withError("ACC-0004", "An account password is required.", UnprocessableEntityException.class);
		onUpdate(account().withPassword("  ").build()).rejected().withError("ACC-0004", "An account password is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAccountIfThePasswordIsGreaterThan255Characters() {
		onUpdate(account().withPassword(RandomStringUtils.randomNumeric(256)).build()).rejected().withError("ACC-0005", "An account password must be less than 255 characters.", UnprocessableEntityException.class);
	}

}
