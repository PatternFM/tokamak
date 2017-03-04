package fm.pattern.jwt.server.validation;

import static fm.pattern.jwt.server.dsl.AuthorityDSL.authority;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.Authority;

public class AuthorityValidationTest extends IntegrationTest {

	@Test
	public void shouldBeAbleToCreateAValidAuthority() {
		onCreate(authority().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToCreateAnAuthorityWhenTheAuthorityNameIsNotProvided() {
		onCreate(authority().withName(null).build()).rejected().withCode("ATH-0001").withMessage("An authority name is required.");
		onCreate(authority().withName("").build()).rejected().withCode("ATH-0001").withMessage("An authority name is required.");
		onCreate(authority().withName("  ").build()).rejected().withCode("ATH-0001").withMessage("An authority name is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAuthorityWhenTheAuthorityNameIsGreaterThan128Characters() {
		onCreate(authority().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withCode("ATH-0002").withMessage("An authority name cannot be greater than 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAuthorityWhenTheAuthorityDescriptionIsGreaterThan255Characters() {
		onCreate(authority().withDescription(RandomStringUtils.randomAlphabetic(256)).build()).rejected().withCode("ATH-0004").withMessage("An authority description cannot be greater than 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAuthorityWhenTheAuthorityNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);
		
		authority().withName(name).thatIs().persistent().build();
		onCreate(authority().withName(name).build()).rejected().withCode("ATH-0003").withMessage("This authority name is already in use.");
	}

	@Test
	public void shouldBeAbleToUpdateAValidAuthority() {
		onUpdate(authority().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAuthorityWhenTheAuthorityNameIsNotProvided() {
		onUpdate(authority().withName(null).build()).rejected().withCode("ATH-0001").withMessage("An authority name is required.");
		onUpdate(authority().withName("").build()).rejected().withCode("ATH-0001").withMessage("An authority name is required.");
		onUpdate(authority().withName("  ").build()).rejected().withCode("ATH-0001").withMessage("An authority name is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAuthorityWhenTheAuthorityNameIsGreaterThan128Characters() {
		onUpdate(authority().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withCode("ATH-0002").withMessage("An authority name cannot be greater than 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAuthorityWhenTheAuthorityDescriptionIsGreaterThan255Characters() {
		onUpdate(authority().withDescription(RandomStringUtils.randomAlphabetic(256)).build()).rejected().withCode("ATH-0004").withMessage("An authority description cannot be greater than 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAuthorityWhenTheAuthorityNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);
		authority().withName(name).thatIs().persistent().build();

		Authority authority = authority().thatIs().persistent().build();
		authority.setName(name);

		onUpdate(authority).rejected().withCode("ATH-0003").withMessage("This authority name is already in use.");
	}

}
