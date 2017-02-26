package fm.pattern.jwt.spec.endpoints;

import static fm.pattern.jwt.sdk.dsl.AccessTokenDSL.token;
import static fm.pattern.jwt.sdk.dsl.AccountDSL.account;
import static fm.pattern.jwt.sdk.dsl.RoleDSL.role;
import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static org.assertj.core.api.StrictAssertions.assertThat;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.RolesClient;
import fm.pattern.jwt.sdk.model.AccessTokenRepresentation;
import fm.pattern.jwt.sdk.model.RoleRepresentation;
import fm.pattern.jwt.spec.AcceptanceTest;

public class RolesEndpointAcceptanceTest extends AcceptanceTest {

	private final RolesClient client = new RolesClient("http://localhost:9600");

	private AccessTokenRepresentation token;

	@Before
	public void before() {
		this.token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();
	}

	@Test
	public void shouldBeAbleToCreateARole() {
		RoleRepresentation role = role().build();

		Result<RoleRepresentation> result = client.create(role, token.getAccessToken());
		assertThat(result).accepted().withResponseCode(201);

		RoleRepresentation created = result.getInstance();
		assertThat(created.getId()).startsWith("rol_");
		assertThat(created.getCreated()).isNotNull();
		assertThat(created.getUpdated()).isNotNull();
		assertThat(created.getCreated()).isEqualTo(created.getUpdated());
		assertThat(created.getName()).isEqualTo(role.getName());
	}

	@Test
	public void shouldNotBeAbleToCreateARoleIfTheRoleIsInvalid() {
		RoleRepresentation role = role().withName("").build();

		Result<RoleRepresentation> result = client.create(role, token.getAccessToken());
		assertThat(result).rejected().withResponseCode(422).withCode("role.name.required").withDescription("A role name is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateARoleIfTheRoleNameIsAlreadyInUse() {
		RoleRepresentation role = role().thatIs().persistent(token).build();

		Result<RoleRepresentation> result = client.create(role, token.getAccessToken());
		assertThat(result).rejected().withResponseCode(422).withCode("role.name.conflict").withDescription("This role name is already in use.");
	}

	@Test
	public void shouldBeAbleToUpdateARole() {
		RoleRepresentation role = role().thatIs().persistent(token).build();
		pause(1000);

		role.setName(RandomStringUtils.randomAlphabetic(10));

		Result<RoleRepresentation> result = client.update(role, token.getAccessToken());
		assertThat(result).accepted().withResponseCode(200);

		RoleRepresentation updated = result.getInstance();
		assertThat(updated.getId()).startsWith("rol_");
		assertThat(updated.getCreated()).isNotNull();
		assertThat(updated.getUpdated()).isNotNull();

		assertThat(updated.getCreated()).isEqualTo(role.getCreated());
		assertThat(updated.getCreated()).isBefore(updated.getUpdated());
		assertThat(updated.getUpdated()).isAfter(role.getUpdated());

		assertThat(updated.getName()).isEqualTo(role.getName());
	}

	@Test
	public void shouldNotBeAbleToUpdateARoleIfTheRoleIsInvalid() {
		RoleRepresentation role = role().thatIs().persistent(token).build();
		role.setName("");

		Result<RoleRepresentation> result = client.update(role, token.getAccessToken());
		assertThat(result).rejected().withResponseCode(422).withCode("role.name.required").withDescription("A role name is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateARoleIfTheRoleNameIsAlreadyInUse() {
		RoleRepresentation existing = role().thatIs().persistent(token).build();
		RoleRepresentation role = role().thatIs().persistent(token).build();
		role.setName(existing.getName());

		Result<RoleRepresentation> result = client.update(role, token.getAccessToken());
		assertThat(result).rejected().withResponseCode(422).withCode("role.name.conflict").withDescription("This role name is already in use.");
	}

	@Test
	public void shouldBeAbleToDeleteARole() {
		RoleRepresentation role = role().thatIs().persistent(token).build();

		Result<RoleRepresentation> result = client.delete(role.getId(), token.getAccessToken());
		assertThat(result).accepted().withResponseCode(204);

		assertThat(client.findById(role.getId(), token.getAccessToken())).rejected().withResponseCode(404);
	}

	@Test
	public void shouldNotBeAbleToDeleteARoleIfTheRoleIsAssociatedWithAccounts() {
		RoleRepresentation role = role().thatIs().persistent(token).build();
		account().withRoles(role).thatIs().persistent(token).build();

		Result<RoleRepresentation> result = client.delete(role.getId(), token.getAccessToken());
		assertThat(result).rejected().withResponseCode(409).withCode("role.delete.conflict").withDescription("This role cannot be deleted, 1 account is linked to this role.");
	}

	@Test
	public void shouldBeAbleToFindARoleById() {
		RoleRepresentation role = role().thatIs().persistent(token).build();

		Result<RoleRepresentation> result = client.findById(role.getId(), token.getAccessToken());
		assertThat(result).accepted().withResponseCode(200);
		assertThat(result.getInstance()).isEqualToComparingFieldByField(role);
	}

	@Test
	public void shouldReturnA404WhenARoleWithTheSpecifiedIdCannotBeFound() {
		Result<RoleRepresentation> result = client.findById("rol_123", token.getAccessToken());
		assertThat(result).rejected().withResponseCode(404).withDescription("No such role id: rol_123");
	}

}
