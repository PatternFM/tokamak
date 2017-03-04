package fm.pattern.jwt.server.validation;

import static fm.pattern.jwt.server.dsl.RoleDSL.role;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.Role;

public class RoleValidationTest extends IntegrationTest {

	@Test
	public void shouldBeAbleToCreateAValidRole() {
		onCreate(role().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToCreateARoleWhenTheRoleNameIsNotProvided() {
		onCreate(role().withName(null).build()).rejected().withCode("ROL-0001").withMessage("A role name is required.");
		onCreate(role().withName("").build()).rejected().withCode("ROL-0001").withMessage("A role name is required.");
		onCreate(role().withName("  ").build()).rejected().withCode("ROL-0001").withMessage("A role name is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateARoleWhenTheRoleNameIsGreaterThan128Characters() {
		onCreate(role().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withCode("ROL-0002").withMessage("A role name cannot be greater than 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateARoleWhenTheRoleDescriptionIsGreaterThan255Characters() {
		onCreate(role().withDescription(RandomStringUtils.randomAlphabetic(256)).build()).rejected().withCode("ROL-0004").withMessage("A role description cannot be greater than 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateARoleWhenTheRoleNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);
		role().withName(name).thatIs().persistent().build();

		onCreate(role().withName(name).build()).rejected().withCode("ROL-0003").withMessage("This role name is already in use.");
	}

	@Test
	public void shouldBeAbleToUpdateAValidRole() {
		onUpdate(role().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateARoleWhenTheRoleNameIsNotProvided() {
		onUpdate(role().withName(null).build()).rejected().withCode("ROL-0001").withMessage("A role name is required.");
		onUpdate(role().withName("").build()).rejected().withCode("ROL-0001").withMessage("A role name is required.");
		onUpdate(role().withName("  ").build()).rejected().withCode("ROL-0001").withMessage("A role name is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateARoleWhenTheRoleNameIsGreaterThan128Characters() {
		onUpdate(role().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withCode("ROL-0002").withMessage("A role name cannot be greater than 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateARoleWhenTheRoleDescriptionIsGreaterThan255Characters() {
		onUpdate(role().withDescription(RandomStringUtils.randomAlphabetic(256)).build()).rejected().withCode("ROL-0004").withMessage("A role description cannot be greater than 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateARoleWhenTheRoleNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);
		role().withName(name).thatIs().persistent().build();

		Role role = role().thatIs().persistent().build();
		role.setName(name);

		onUpdate(role).rejected().withCode("ROL-0003").withMessage("This role name is already in use.");
	}

}
