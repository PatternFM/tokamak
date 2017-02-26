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
	public void shouldNotBeAbleToCreateAAuthorityWhenTheAuthorityNameIsNotProvided() {
		onCreate(authority().withName(null).build()).rejected().withCode("authority.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("An authority name is required.");
		onCreate(authority().withName("").build()).rejected().withCode("authority.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("An authority name is required.");
		onCreate(authority().withName("  ").build()).rejected().withCode("authority.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("An authority name is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAAuthorityWhenTheAuthorityNameIsGreaterThan128Characters() {
		onCreate(authority().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withCode("authority.name.size").withType(UNPROCESSABLE_ENTITY).withDescription("An authority name cannot be greater than 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAAuthorityWhenTheAuthorityNameAlredyExists() {
		authority().withName("first").thatIs().persistent().build();
		onCreate(authority().withName("first").build()).rejected().withCode("authority.name.conflict").withType(UNPROCESSABLE_ENTITY).withDescription("This authority name is already in use.");
	}

	@Test
	public void shouldBeAbleToUpdateAValidAuthority() {
		onUpdate(authority().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateAAuthorityWhenTheAuthorityNameIsNotProvided() {
		onUpdate(authority().withName(null).build()).rejected().withCode("authority.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("An authority name is required.");
		onUpdate(authority().withName("").build()).rejected().withCode("authority.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("An authority name is required.");
		onUpdate(authority().withName("  ").build()).rejected().withCode("authority.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("An authority name is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAAuthorityWhenTheAuthorityNameIsGreaterThan128Characters() {
		onUpdate(authority().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withCode("authority.name.size").withType(UNPROCESSABLE_ENTITY).withDescription("An authority name cannot be greater than 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAAuthorityWhenTheAuthorityNameAlredyExists() {
		authority().withName("first").thatIs().persistent().build();

		Authority second = authority().withName("second").thatIs().persistent().build();
		second.setName("first");

		onUpdate(second).rejected().withCode("authority.name.conflict").withType(UNPROCESSABLE_ENTITY).withDescription("This authority name is already in use.");
	}

}
