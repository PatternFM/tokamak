package fm.pattern.jwt.spec.security;

import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static fm.pattern.tokamak.sdk.dsl.AccessTokenDSL.token;
import static fm.pattern.tokamak.sdk.dsl.ClientDSL.client;

import org.junit.Before;
import org.junit.Test;

import fm.pattern.jwt.spec.AcceptanceTest;
import fm.pattern.tokamak.sdk.GrantTypesClient;
import fm.pattern.tokamak.sdk.JwtClientProperties;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.AccessTokenRepresentation;
import fm.pattern.tokamak.sdk.model.GrantTypeRepresentation;
import fm.pattern.tokamak.sdk.model.GrantTypesRepresentation;
import fm.pattern.tokamak.sdk.model.ClientRepresentation;

public class GrantTypesEndpointSecurityTest extends AcceptanceTest {

	private static final String CLIENT_SECRET = "client_secret";

	private GrantTypesClient grantTypesClient = new GrantTypesClient(JwtClientProperties.getEndpoint());

	private ClientRepresentation client;
	private AccessTokenRepresentation token;

	@Before
	public void before() {
		this.token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();
		this.client = client().withClientSecret(CLIENT_SECRET).withScopes("clients:read").withGrantTypes("client_credentials", "refresh_token").thatIs().persistent(token).build();
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToFindAGrantTypeById() {
		Result<GrantTypeRepresentation> response = grantTypesClient.findById("12345", null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToFindAGrantTypeByName() {
		Result<GrantTypeRepresentation> response = grantTypesClient.findByName("name", null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToListGrantTypes() {
		Result<GrantTypesRepresentation> response = grantTypesClient.list(null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateGrantTypeShouldNotBeAbleToFindAGrantTypeById() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), CLIENT_SECRET).thatIs().persistent().build();

		Result<GrantTypeRepresentation> response = grantTypesClient.findById("12345", t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateGrantTypeShouldNotBeAbleToFindAGrantTypeByName() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), CLIENT_SECRET).thatIs().persistent().build();

		Result<GrantTypeRepresentation> response = grantTypesClient.findByName("username", t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateGrantTypeShouldNotBeAbleToListGrantTypes() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), CLIENT_SECRET).thatIs().persistent().build();

		Result<GrantTypesRepresentation> response = grantTypesClient.list(t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

}
