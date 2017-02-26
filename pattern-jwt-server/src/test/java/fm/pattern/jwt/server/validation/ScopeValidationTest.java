package fm.pattern.jwt.server.validation;

import static fm.pattern.jwt.server.dsl.ScopeDSL.scope;
import static fm.pattern.microstructure.ResultType.UNPROCESSABLE_ENTITY;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.Scope;

public class ScopeValidationTest extends IntegrationTest {

	@Test
	public void shouldBeAbleToCreateAValidScope() {
		onCreate(scope().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToCreateAScopeWhenTheScopeNameIsNotProvided() {
		onCreate(scope().withName(null).build()).rejected().withCode("scope.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("A scope name is required.");
		onCreate(scope().withName("").build()).rejected().withCode("scope.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("A scope name is required.");
		onCreate(scope().withName("  ").build()).rejected().withCode("scope.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("A scope name is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAScopeWhenTheScopeNameIsGreaterThan128Characters() {
		onCreate(scope().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withCode("scope.name.size").withType(UNPROCESSABLE_ENTITY).withDescription("A scope name cannot be greater than 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAScopeWhenTheScopeNameAlredyExists() {
		scope().withName("first").thatIs().persistent().build();
		onCreate(scope().withName("first").build()).rejected().withCode("scope.name.conflict").withType(UNPROCESSABLE_ENTITY).withDescription("This scope name is already in use.");
	}

	@Test
	public void shouldBeAbleToUpdateAValidScope() {
		onUpdate(scope().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateAScopeWhenTheScopeNameIsNotProvided() {
		onUpdate(scope().withName(null).build()).rejected().withCode("scope.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("A scope name is required.");
		onUpdate(scope().withName("").build()).rejected().withCode("scope.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("A scope name is required.");
		onUpdate(scope().withName("  ").build()).rejected().withCode("scope.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("A scope name is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAScopeWhenTheScopeNameIsGreaterThan128Characters() {
		onUpdate(scope().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withCode("scope.name.size").withType(UNPROCESSABLE_ENTITY).withDescription("A scope name cannot be greater than 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAScopeWhenTheScopeNameAlredyExists() {
		scope().withName("first").thatIs().persistent().build();

		Scope second = scope().withName("second").thatIs().persistent().build();
		second.setName("first");
		
		onUpdate(second).rejected().withCode("scope.name.conflict").withType(UNPROCESSABLE_ENTITY).withDescription("This scope name is already in use.");
	}

}
