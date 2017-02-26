package fm.pattern.jwt.server.validation;

import static fm.pattern.jwt.server.dsl.RoleDSL.role;
import static fm.pattern.microstructure.ResultType.UNPROCESSABLE_ENTITY;

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
		onCreate(role().withName(null).build()).rejected().withCode("role.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("A role name is required.");
		onCreate(role().withName("").build()).rejected().withCode("role.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("A role name is required.");
		onCreate(role().withName("  ").build()).rejected().withCode("role.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("A role name is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateARoleWhenTheRoleNameIsGreaterThan128Characters() {
		onCreate(role().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withCode("role.name.size").withType(UNPROCESSABLE_ENTITY).withDescription("A role name cannot be greater than 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateARoleWhenTheRoleNameAlredyExists() {
		role().withName("first").thatIs().persistent().build();
		onCreate(role().withName("first").build()).rejected().withCode("role.name.conflict").withType(UNPROCESSABLE_ENTITY).withDescription("This role name is already in use.");
	}

	@Test
	public void shouldBeAbleToUpdateAValidRole() {
		onUpdate(role().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateARoleWhenTheRoleNameIsNotProvided() {
		onUpdate(role().withName(null).build()).rejected().withCode("role.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("A role name is required.");
		onUpdate(role().withName("").build()).rejected().withCode("role.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("A role name is required.");
		onUpdate(role().withName("  ").build()).rejected().withCode("role.name.required").withType(UNPROCESSABLE_ENTITY).withDescription("A role name is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateARoleWhenTheRoleNameIsGreaterThan128Characters() {
		onUpdate(role().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withCode("role.name.size").withType(UNPROCESSABLE_ENTITY).withDescription("A role name cannot be greater than 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateARoleWhenTheRoleNameAlredyExists() {
		role().withName("first").thatIs().persistent().build();

		Role second = role().withName("second").thatIs().persistent().build();
		second.setName("first");
		
		onUpdate(second).rejected().withCode("role.name.conflict").withType(UNPROCESSABLE_ENTITY).withDescription("This role name is already in use.");
	}

}
