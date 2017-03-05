package fm.pattern.jwt.server.validation;

import static fm.pattern.jwt.server.dsl.AuthorityDSL.authority;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.Authority;
import fm.pattern.validation.ResourceConflictException;
import fm.pattern.validation.UnprocessableEntityException;

public class AuthorityValidationTest extends IntegrationTest {

	@Test
	public void shouldBeAbleToCreateAValidAuthority() {
		onCreate(authority().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToCreateAnAuthorityWhenTheAuthorityNameIsNotProvided() {
		onCreate(authority().withName(null).build()).rejected().withError("ATH-0001", "An authority name is required.", UnprocessableEntityException.class);
		onCreate(authority().withName("").build()).rejected().withError("ATH-0001", "An authority name is required.", UnprocessableEntityException.class);
		onCreate(authority().withName("  ").build()).rejected().withError("ATH-0001", "An authority name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAnAuthorityWhenTheAuthorityNameIsGreaterThan128Characters() {
		onCreate(authority().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withError("ATH-0002", "An authority name cannot be greater than 128 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAnAuthorityWhenTheAuthorityDescriptionIsGreaterThan255Characters() {
		onCreate(authority().withDescription(RandomStringUtils.randomAlphabetic(256)).build()).rejected().withError("ATH-0004", "An authority description cannot be greater than 255 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAnAuthorityWhenTheAuthorityNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);

		authority().withName(name).thatIs().persistent().build();
		onCreate(authority().withName(name).build()).rejected().withError("ATH-0003", "This authority name is already in use.", ResourceConflictException.class);
	}

	@Test
	public void shouldBeAbleToUpdateAValidAuthority() {
		onUpdate(authority().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAuthorityWhenTheAuthorityNameIsNotProvided() {
		onUpdate(authority().withName(null).build()).rejected().withError("ATH-0001", "An authority name is required.", UnprocessableEntityException.class);
		onUpdate(authority().withName("").build()).rejected().withError("ATH-0001", "An authority name is required.", UnprocessableEntityException.class);
		onUpdate(authority().withName("  ").build()).rejected().withError("ATH-0001", "An authority name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAuthorityWhenTheAuthorityNameIsGreaterThan128Characters() {
		onUpdate(authority().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withError("ATH-0002", "An authority name cannot be greater than 128 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAuthorityWhenTheAuthorityDescriptionIsGreaterThan255Characters() {
		onUpdate(authority().withDescription(RandomStringUtils.randomAlphabetic(256)).build()).rejected().withError("ATH-0004", "An authority description cannot be greater than 255 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAuthorityWhenTheAuthorityNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);
		authority().withName(name).thatIs().persistent().build();

		Authority authority = authority().thatIs().persistent().build();
		authority.setName(name);

		onUpdate(authority).rejected().withError("ATH-0003", "This authority name is already in use.", ResourceConflictException.class);
	}

}
