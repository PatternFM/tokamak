package fm.pattern.jwt.server.validation;

import static fm.pattern.jwt.server.dsl.ScopeDSL.scope;

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
		onCreate(scope().withName(null).build()).rejected().withCode("SCO-0001").withMessage("A scope name is required.");
		onCreate(scope().withName("").build()).rejected().withCode("SCO-0001").withMessage("A scope name is required.");
		onCreate(scope().withName("  ").build()).rejected().withCode("SCO-0001").withMessage("A scope name is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAScopeWhenTheScopeNameIsGreaterThan128Characters() {
		onCreate(scope().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withCode("SCO-0002").withMessage("A scope name cannot be greater than 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAScopeWhenTheScopeNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);
		scope().withName(name).thatIs().persistent().build();

		onCreate(scope().withName(name).build()).rejected().withCode("SCO-0003").withMessage("This scope name is already in use.");
	}

	@Test
	public void shouldNotBeAbleToCreateAScopeWhenTheScopeDescriptionIsGreaterThan255Characters() {
		onCreate(scope().withDescription(RandomStringUtils.randomAlphabetic(256)).build()).rejected().withCode("SCO-0004").withMessage("A scope description cannot be greater than 255 characters.");
	}

	@Test
	public void shouldBeAbleToUpdateAValidScope() {
		onUpdate(scope().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateAScopeWhenTheScopeNameIsNotProvided() {
		onUpdate(scope().withName(null).build()).rejected().withCode("SCO-0001").withMessage("A scope name is required.");
		onUpdate(scope().withName("").build()).rejected().withCode("SCO-0001").withMessage("A scope name is required.");
		onUpdate(scope().withName("  ").build()).rejected().withCode("SCO-0001").withMessage("A scope name is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAScopeWhenTheScopeNameIsGreaterThan128Characters() {
		onUpdate(scope().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withCode("SCO-0002").withMessage("A scope name cannot be greater than 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAScopeWhenTheScopeDescriptionIsGreaterThan255Characters() {
		onUpdate(scope().withDescription(RandomStringUtils.randomAlphabetic(256)).build()).rejected().withCode("SCO-0004").withMessage("A scope description cannot be greater than 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAScopeWhenTheScopeNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);
		scope().withName(name).thatIs().persistent().build();

		Scope scope = scope().thatIs().persistent().build();
		scope.setName(name);

		onUpdate(scope).rejected().withCode("SCO-0003").withMessage("This scope name is already in use.");
	}

}
