package fm.pattern.jwt.server.validation;

import static fm.pattern.jwt.server.dsl.AuthorityDSL.authority;
import static fm.pattern.microstructure.ResultType.UNPROCESSABLE_ENTITY;

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
		onCreate(authority().withName(null).build()).rejected().withCode("authority.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("An authority name is required.");
		onCreate(authority().withName("").build()).rejected().withCode("authority.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("An authority name is required.");
		onCreate(authority().withName("  ").build()).rejected().withCode("authority.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("An authority name is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAuthorityWhenTheAuthorityNameIsGreaterThan128Characters() {
		onCreate(authority().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withCode("authority.name.size").withType(UNPROCESSABLE_ENTITY).withDescription("An authority name cannot be greater than 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAuthorityWhenTheAuthorityDescriptionIsGreaterThan255Characters() {
		onCreate(authority().withDescription(RandomStringUtils.randomAlphabetic(256)).build()).rejected().withCode("authority.description.size").withType(UNPROCESSABLE_ENTITY).withDescription("An authority description cannot be greater than 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAuthorityWhenTheAuthorityNameAlredyExists() {
		authority().withName("first").thatIs().persistent().build();
		onCreate(authority().withName("first").build()).rejected().withCode("authority.name.conflict").withType(UNPROCESSABLE_ENTITY).withDescription("This authority name is already in use.");
	}

	@Test
	public void shouldBeAbleToUpdateAValidAuthority() {
		onUpdate(authority().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAuthorityWhenTheAuthorityNameIsNotProvided() {
		onUpdate(authority().withName(null).build()).rejected().withCode("authority.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("An authority name is required.");
		onUpdate(authority().withName("").build()).rejected().withCode("authority.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("An authority name is required.");
		onUpdate(authority().withName("  ").build()).rejected().withCode("authority.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("An authority name is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAuthorityWhenTheAuthorityNameIsGreaterThan128Characters() {
		onUpdate(authority().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withCode("authority.name.size").withType(UNPROCESSABLE_ENTITY).withDescription("An authority name cannot be greater than 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAuthorityWhenTheAuthorityDescriptionIsGreaterThan255Characters() {
		onUpdate(authority().withDescription(RandomStringUtils.randomAlphabetic(256)).build()).rejected().withCode("authority.description.size").withType(UNPROCESSABLE_ENTITY).withDescription("An authority description cannot be greater than 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAuthorityWhenTheAuthorityNameAlredyExists() {
		authority().withName("first").thatIs().persistent().build();

		Authority second = authority().withName("second").thatIs().persistent().build();
		second.setName("first");

		onUpdate(second).rejected().withCode("authority.name.conflict").withType(UNPROCESSABLE_ENTITY).withDescription("This authority name is already in use.");
	}

}
