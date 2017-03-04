package fm.pattern.jwt.server.service;

import static fm.pattern.jwt.server.PatternAssertions.assertThat;
import static fm.pattern.jwt.server.dsl.RoleDSL.role;
import static fm.pattern.microstructure.ResultType.NOT_FOUND;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.Role;
import fm.pattern.microstructure.Result;

public class RoleServiceIntegrationTest extends IntegrationTest {

	@Autowired
	private RoleService roleService;

	@Test
	public void shouldBeAbleToCreateARole() {
		Role role = new Role("user");

		Result<Role> result = roleService.create(role);
		assertThat(result).accepted();

		Role created = result.getInstance();
		assertThat(created.getName()).isEqualTo(role.getName());
		assertThat(created.getId()).isNotNull();
		assertThat(created.getCreated()).isNotNull();
		assertThat(created.getUpdated()).isNotNull();
		assertThat(created.getCreated()).isEqualTo(created.getUpdated());
	}

	@Test
	public void shouldNotBeAbleToCreateARoleIfTheRoleIsInvalid() {
		Role role = role().withName(null).build();
		assertThat(roleService.create(role)).rejected().withMessage("A role name is required.");
	}

	@Test
	public void shouldBeAbleToUpdateARole() {
		Role role = role().thatIs().persistent().build();
		role.setName("first");

		assertThat(roleService.update(role)).accepted();

		Role found = roleService.findById(role.getId()).getInstance();
		assertThat(found.getName()).isEqualTo("first");
	}

	@Test
	public void shouldNotBeAbleToUpdateARoleIfTheRoleIsInvalid() {
		Role role = role().thatIs().persistent().build();
		role.setName(null);

		assertThat(roleService.update(role)).rejected().withMessage("A role name is required.");
	}

	@Test
	public void shouldBeAbleToDeleteARole() {
		Role role = role().thatIs().persistent().build();
		assertThat(roleService.findById(role.getId())).accepted();

		assertThat(roleService.delete(role)).accepted();
		assertThat(roleService.findById(role.getId())).rejected();
	}

	@Test
	public void shouldBeAbleToFindARoleById() {
		Role role = role().thatIs().persistent().build();

		Result<Role> result = roleService.findById(role.getId());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(role);
	}

	@Test
	public void shouldNotBeAbleToFindARoleByIdIfTheRoleIdIsNullOrEmpty() {
		assertThat(roleService.findById(null)).rejected().withMessage("The role id to retrieve cannot be null or empty.");
		assertThat(roleService.findById("")).rejected().withMessage("The role id to retrieve cannot be null or empty.");
		assertThat(roleService.findById("  ")).rejected().withMessage("The role id to retrieve cannot be null or empty.");
	}

	@Test
	public void shouldNotBeAbleToFindARoleByIdIfTheRoleIdDoesNotExist() {
		assertThat(roleService.findById("csrx")).rejected().withType(NOT_FOUND).withMessage("No such role id: csrx");
	}

	@Test
	public void shouldBeAbleToFindARoleByName() {
		Role role = role().thatIs().persistent().build();

		Result<Role> result = roleService.findByName(role.getName());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(role);
	}

	@Test
	public void shouldNotBeAbleToFindARoleByNameIfTheRoleNameIsNullOrEmpty() {
		assertThat(roleService.findByName(null)).rejected().withMessage("The role name to retrieve cannot be null or empty.");
		assertThat(roleService.findByName("")).rejected().withMessage("The role name to retrieve cannot be null or empty.");
		assertThat(roleService.findByName("  ")).rejected().withMessage("The role name to retrieve cannot be null or empty.");
	}

	@Test
	public void shouldNotBeAbleToFindARoleByNameIfTheRoleNameDoesNotExist() {
		assertThat(roleService.findByName("csrx")).rejected().withMessage("No such role name: csrx");
	}

	@Test
	public void shouldBeAbleToListAllRoles() {
		range(0, 5).forEach(i -> role().thatIs().persistent().build());

		Result<List<Role>> result = roleService.list();
		assertThat(result).accepted();
		assertThat(result.getInstance().size()).isGreaterThanOrEqualTo(5);
	}

}
