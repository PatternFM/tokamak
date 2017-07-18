package fm.pattern.jwt.spec.security;

import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static fm.pattern.tokamak.sdk.dsl.AccessTokenDSL.token;
import static fm.pattern.tokamak.sdk.dsl.ClientDSL.client;
import static fm.pattern.tokamak.sdk.dsl.RoleDSL.role;

import org.junit.Before;
import org.junit.Test;

import fm.pattern.jwt.spec.AcceptanceTest;
import fm.pattern.tokamak.sdk.JwtClientProperties;
import fm.pattern.tokamak.sdk.RolesClient;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.AccessTokenRepresentation;
import fm.pattern.tokamak.sdk.model.ClientRepresentation;
import fm.pattern.tokamak.sdk.model.RoleRepresentation;
import fm.pattern.tokamak.sdk.model.RolesRepresentation;

public class RolesEndpointSecurityTest extends AcceptanceTest {

	private RolesClient rolesClient = new RolesClient(JwtClientProperties.getEndpoint());

	private String secret = "sadfsdSDFJS776$#";

	private ClientRepresentation client;
	private AccessTokenRepresentation token;

	@Before
	public void before() {
		this.token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();
		this.client = client().withClientSecret(secret).withScopes("clients:read").withGrantTypes("client_credentials", "refresh_token").thatIs().persistent(token).build();
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToCreateARole() {
		Result<RoleRepresentation> response = rolesClient.create(role().build(), null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToUpdateARole() {
		Result<RoleRepresentation> response = rolesClient.update(role().withId("abc").build(), null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToDeleteARole() {
		Result<RoleRepresentation> response = rolesClient.delete("123456", null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToFindARoleById() {
		Result<RoleRepresentation> response = rolesClient.findById("12345", null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToFindARoleByName() {
		Result<RoleRepresentation> response = rolesClient.findByName("name", null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToListRoles() {
		Result<RolesRepresentation> response = rolesClient.list(null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToCreateARole() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), secret).thatIs().persistent().build();

		Result<RoleRepresentation> response = rolesClient.create(role().build(), t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToUpdateARole() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), secret).thatIs().persistent().build();

		Result<RoleRepresentation> response = rolesClient.update(role().withId("12345").build(), t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToDeleteARole() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), secret).thatIs().persistent().build();

		Result<RoleRepresentation> response = rolesClient.delete("12345", t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToFindARoleById() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), secret).thatIs().persistent().build();

		Result<RoleRepresentation> response = rolesClient.findById("12345", t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToFindARoleByName() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), secret).thatIs().persistent().build();

		Result<RoleRepresentation> response = rolesClient.findByName("username", t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToListRoles() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), secret).thatIs().persistent().build();

		Result<RolesRepresentation> response = rolesClient.list(t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

}
