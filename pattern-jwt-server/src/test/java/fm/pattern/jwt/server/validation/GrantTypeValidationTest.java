package fm.pattern.jwt.server.validation;

import static fm.pattern.jwt.server.dsl.GrantTypeDSL.grantType;
import static fm.pattern.microstructure.ResultType.UNPROCESSABLE_ENTITY;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.GrantType;

public class GrantTypeValidationTest extends IntegrationTest {

	@Test
	public void shouldBeAbleToCreateAValidGrantType() {
		onCreate(grantType().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToCreateAGrantTypeWhenTheGrantTypeNameIsNotProvided() {
		onCreate(grantType().withName(null).build()).rejected().withCode("grantType.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("A grant type name is required.");
		onCreate(grantType().withName("").build()).rejected().withCode("grantType.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("A grant type name is required.");
		onCreate(grantType().withName("  ").build()).rejected().withCode("grantType.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("A grant type name is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAGrantTypeWhenTheGrantTypeNameIsGreaterThan128Characters() {
		onCreate(grantType().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withCode("grantType.name.size").withType(UNPROCESSABLE_ENTITY).withDescription("A grant type name cannot be greater than 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAGrantTypeWhenTheGrantTypeNameAlredyExists() {
		grantType().withName("first").thatIs().persistent().build();
		onCreate(grantType().withName("first").build()).rejected().withCode("grantType.name.conflict").withType(UNPROCESSABLE_ENTITY).withDescription("This grant type name is already in use.");
	}

	@Test
	public void shouldBeAbleToUpdateAValidGrantType() {
		onUpdate(grantType().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateAGrantTypeWhenTheGrantTypeNameIsNotProvided() {
		onUpdate(grantType().withName(null).build()).rejected().withCode("grantType.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("A grant type name is required.");
		onUpdate(grantType().withName("").build()).rejected().withCode("grantType.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("A grant type name is required.");
		onUpdate(grantType().withName("  ").build()).rejected().withCode("grantType.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("A grant type name is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAGrantTypeWhenTheGrantTypeNameIsGreaterThan128Characters() {
		onUpdate(grantType().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withCode("grantType.name.size").withType(UNPROCESSABLE_ENTITY).withDescription("A grant type name cannot be greater than 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAGrantTypeWhenTheGrantTypeNameAlredyExists() {
		grantType().withName("first").thatIs().persistent().build();

		GrantType second = grantType().withName("second").thatIs().persistent().build();
		second.setName("first");

		onUpdate(second).rejected().withCode("grantType.name.conflict").withType(UNPROCESSABLE_ENTITY).withDescription("This grant type name is already in use.");
	}

}
