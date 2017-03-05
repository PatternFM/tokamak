package fm.pattern.jwt.server.validation;

import static fm.pattern.jwt.server.dsl.RoleDSL.role;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.Role;
import fm.pattern.validation.ResourceConflictException;
import fm.pattern.validation.UnprocessableEntityException;

public class RoleValidationTest extends IntegrationTest {

	@Test
	public void shouldBeAbleToCreateAValidRole() {
		onCreate(role().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToCreateARoleWhenTheRoleNameIsNotProvided() {
		onCreate(role().withName(null).build()).rejected().withError("ROL-0001", "A role name is required.", UnprocessableEntityException.class);
		onCreate(role().withName("").build()).rejected().withError("ROL-0001", "A role name is required.", UnprocessableEntityException.class);
		onCreate(role().withName("  ").build()).rejected().withError("ROL-0001", "A role name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateARoleWhenTheRoleNameIsGreaterThan128Characters() {
		onCreate(role().withName(randomAlphabetic(129)).build()).rejected().withError("ROL-0002", "A role name cannot be greater than 128 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateARoleWhenTheRoleDescriptionIsGreaterThan255Characters() {
		onCreate(role().withDescription(randomAlphabetic(256)).build()).rejected().withError("ROL-0004", "A role description cannot be greater than 255 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateARoleWhenTheRoleNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);
		role().withName(name).thatIs().persistent().build();

		onCreate(role().withName(name).build()).rejected().withError("ROL-0003", "This role name is already in use.", ResourceConflictException.class);
	}

	@Test
	public void shouldBeAbleToUpdateAValidRole() {
		onUpdate(role().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateARoleWhenTheRoleNameIsNotProvided() {
		onUpdate(role().withName(null).build()).rejected().withError("ROL-0001", "A role name is required.", UnprocessableEntityException.class);
		onUpdate(role().withName("").build()).rejected().withError("ROL-0001", "A role name is required.", UnprocessableEntityException.class);
		onUpdate(role().withName("  ").build()).rejected().withError("ROL-0001", "A role name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateARoleWhenTheRoleNameIsGreaterThan128Characters() {
		onUpdate(role().withName(randomAlphabetic(129)).build()).rejected().withError("ROL-0002", "A role name cannot be greater than 128 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateARoleWhenTheRoleDescriptionIsGreaterThan255Characters() {
		onUpdate(role().withDescription(randomAlphabetic(256)).build()).rejected().withError("ROL-0004", "A role description cannot be greater than 255 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateARoleWhenTheRoleNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);
		role().withName(name).thatIs().persistent().build();

		Role role = role().thatIs().persistent().build();
		role.setName(name);

		onUpdate(role).rejected().withError("ROL-0003", "This role name is already in use.", ResourceConflictException.class);
	}

}
