package fm.pattern.jwt.server.validation;

import static fm.pattern.jwt.server.dsl.ScopeDSL.scope;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.Scope;
import fm.pattern.valex.ResourceConflictException;
import fm.pattern.valex.UnprocessableEntityException;

public class ScopeValidationTest extends IntegrationTest {

	@Test
	public void shouldBeAbleToCreateAValidScope() {
		onCreate(scope().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToCreateAScopeWhenTheScopeNameIsNotProvided() {
		onCreate(scope().withName(null).build()).rejected().withError("SCO-0001", "A scope name is required.", UnprocessableEntityException.class);
		onCreate(scope().withName("").build()).rejected().withError("SCO-0001", "A scope name is required.", UnprocessableEntityException.class);
		onCreate(scope().withName("  ").build()).rejected().withError("SCO-0001", "A scope name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAScopeWhenTheScopeNameIsGreaterThan128Characters() {
		onCreate(scope().withName(randomAlphabetic(129)).build()).rejected().withError("SCO-0002", "A scope name cannot be greater than 128 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAScopeWhenTheScopeNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);
		scope().withName(name).thatIs().persistent().build();

		onCreate(scope().withName(name).build()).rejected().withError("SCO-0003", "This scope name is already in use.", ResourceConflictException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAScopeWhenTheScopeDescriptionIsGreaterThan255Characters() {
		onCreate(scope().withDescription(randomAlphabetic(256)).build()).rejected().withError("SCO-0004", "A scope description cannot be greater than 255 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldBeAbleToUpdateAValidScope() {
		onUpdate(scope().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateAScopeWhenTheScopeNameIsNotProvided() {
		onUpdate(scope().withName(null).build()).rejected().withError("SCO-0001", "A scope name is required.", UnprocessableEntityException.class);
		onUpdate(scope().withName("").build()).rejected().withError("SCO-0001", "A scope name is required.", UnprocessableEntityException.class);
		onUpdate(scope().withName("  ").build()).rejected().withError("SCO-0001", "A scope name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAScopeWhenTheScopeNameIsGreaterThan128Characters() {
		onUpdate(scope().withName(randomAlphabetic(129)).build()).rejected().withError("SCO-0002", "A scope name cannot be greater than 128 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAScopeWhenTheScopeDescriptionIsGreaterThan255Characters() {
		onUpdate(scope().withDescription(randomAlphabetic(256)).build()).rejected().withError("SCO-0004", "A scope description cannot be greater than 255 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAScopeWhenTheScopeNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);
		scope().withName(name).thatIs().persistent().build();

		Scope scope = scope().thatIs().persistent().build();
		scope.setName(name);

		onUpdate(scope).rejected().withError("SCO-0003", "This scope name is already in use.", ResourceConflictException.class);
	}

}
