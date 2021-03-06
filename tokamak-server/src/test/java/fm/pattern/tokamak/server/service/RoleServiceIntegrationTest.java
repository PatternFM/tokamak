package fm.pattern.tokamak.server.service;

import static fm.pattern.tokamak.server.PatternAssertions.assertThat;
import static fm.pattern.tokamak.server.dsl.AccountDSL.account;
import static fm.pattern.tokamak.server.dsl.RoleDSL.role;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.Account;
import fm.pattern.tokamak.server.model.Role;
import fm.pattern.tokamak.server.repository.Cache;
import fm.pattern.valex.EntityNotFoundException;
import fm.pattern.valex.ResourceConflictException;
import fm.pattern.valex.Result;
import fm.pattern.valex.UnprocessableEntityException;

public class RoleServiceIntegrationTest extends IntegrationTest {

	@Autowired
	@Qualifier("accountCache")
	private Cache cache;

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
		Role role = role().save();
		role.setName("first");

		assertThat(roleService.update(role)).accepted();

		Role found = roleService.findById(role.getId()).getInstance();
		assertThat(found.getName()).isEqualTo("first");
	}

	@Test
	public void shouldFlushTheAccountCacheWhenARoleIsUpdated() {
		Account account = account().save();

		assertThat(cache.get("accounts:id:" + account.getId(), Account.class)).isNotNull();
		assertThat(roleService.update(role().save())).accepted();
		assertThat(cache.get("accounts:id:" + account.getId(), Account.class)).isNull();
	}

	@Test
	public void shouldNotBeAbleToUpdateARoleIfTheRoleIsInvalid() {
		Role role = role().save();
		role.setName(null);

		assertThat(roleService.update(role)).rejected().withMessage("A role name is required.");
	}

	@Test
	public void shouldBeAbleToDeleteARole() {
		Role role = role().save();

		assertThat(roleService.findById(role.getId())).accepted();
		assertThat(roleService.delete(role)).accepted();
		assertThat(roleService.findById(role.getId())).rejected();
	}

	@Test
	public void shouldNotBeAbleToDeleteARoleIfTheRoleIsInvalid() {
		Role role = role().save();
		role.setId(null);
		assertThat(roleService.delete(role)).rejected().withError("ENT-0001", "An id is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToDeleteAnRoleIfTheRoleIsBeingUsedByOtherAccounts() {
		Role role = role().save();
		account().withRole(role).save();

		Result<Role> result = roleService.delete(role);
		assertThat(result).rejected().withError("ROL-0007", "This role cannot be deleted, 1 account is linked to this role.", ResourceConflictException.class);
	}

	@Test
	public void shouldBeAbleToFindARoleById() {
		Role role = role().save();

		Result<Role> result = roleService.findById(role.getId());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(role);
	}

	@Test
	public void shouldNotBeAbleToFindARoleByIdIfTheRoleIdIsNullOrEmpty() {
		assertThat(roleService.findById(null)).rejected().withError("ROL-0005", "A role id is required.", UnprocessableEntityException.class);
		assertThat(roleService.findById("")).rejected().withError("ROL-0005", "A role id is required.", UnprocessableEntityException.class);
		assertThat(roleService.findById("  ")).rejected().withError("ROL-0005", "A role id is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToFindARoleByIdIfTheRoleIdDoesNotExist() {
		assertThat(roleService.findById("csrx")).rejected().withError("SYS-0001", "No such role id: csrx", EntityNotFoundException.class);
	}

	@Test
	public void shouldBeAbleToFindARoleByName() {
		Role role = role().save();

		Result<Role> result = roleService.findByName(role.getName());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(role);
	}

	@Test
	public void shouldNotBeAbleToFindARoleByNameIfTheRoleNameIsNullOrEmpty() {
		assertThat(roleService.findByName(null)).rejected().withError("ROL-0001", "A role name is required.", UnprocessableEntityException.class);
		assertThat(roleService.findByName("")).rejected().withError("ROL-0001", "A role name is required.", UnprocessableEntityException.class);
		assertThat(roleService.findByName("  ")).rejected().withError("ROL-0001", "A role name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToFindARoleByNameIfTheRoleNameDoesNotExist() {
		assertThat(roleService.findByName("csrx")).rejected().withError("ROL-0008", "No such role name: csrx", EntityNotFoundException.class);
	}

	@Test
	public void shouldBeAbleToListAllRoles() {
		range(0, 5).forEach(i -> role().save());

		Result<List<Role>> result = roleService.list();
		assertThat(result).accepted();
		assertThat(result.getInstance().size()).isGreaterThanOrEqualTo(5);
	}

}
