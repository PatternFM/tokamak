package fm.pattern.jwt.spec.security;

import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static fm.pattern.tokamak.sdk.dsl.AccessTokenDSL.token;
import static fm.pattern.tokamak.sdk.dsl.ClientDSL.client;

import org.junit.Before;
import org.junit.Test;

import fm.pattern.jwt.spec.AcceptanceTest;
import fm.pattern.tokamak.sdk.ClientsClient;
import fm.pattern.tokamak.sdk.JwtClientProperties;
import fm.pattern.tokamak.sdk.commons.CriteriaRepresentation;
import fm.pattern.tokamak.sdk.commons.PaginatedListRepresentation;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.AccessTokenRepresentation;
import fm.pattern.tokamak.sdk.model.ClientRepresentation;

public class ClientsEndpointSecurityTest extends AcceptanceTest {

	private static final String CLIENT_SECRET = "as4dfSDFSw33sfsdf%%^";

	private ClientsClient clientsClient = new ClientsClient(JwtClientProperties.getEndpoint());

	private ClientRepresentation client;
	private AccessTokenRepresentation token;

	@Before
	public void before() {
		this.token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();
		this.client = client().withClientSecret(CLIENT_SECRET).withScopes("accounts:read").withGrantTypes("client_credentials", "refresh_token").thatIs().persistent(token).build();
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToCreateAClient() {
		Result<ClientRepresentation> response = clientsClient.create(client().build(), null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToUpdateAClient() {
		Result<ClientRepresentation> response = clientsClient.update(client().build(), null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToDeleteAClient() {
		Result<ClientRepresentation> response = clientsClient.delete("123456", null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToFindAClientById() {
		Result<ClientRepresentation> response = clientsClient.findById("12345", null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToListClients() {
		Result<PaginatedListRepresentation<ClientRepresentation>> response = clientsClient.list(new CriteriaRepresentation(), null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToCreateAClient() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), CLIENT_SECRET).thatIs().persistent().build();

		Result<ClientRepresentation> response = clientsClient.create(client().build(), t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToUpdateAClient() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), CLIENT_SECRET).thatIs().persistent().build();

		Result<ClientRepresentation> response = clientsClient.update(client().withId("12345").build(), t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToDeleteAClient() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), CLIENT_SECRET).thatIs().persistent().build();

		Result<ClientRepresentation> response = clientsClient.delete("12345", t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToFindAClientById() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), CLIENT_SECRET).thatIs().persistent().build();

		Result<ClientRepresentation> response = clientsClient.findById("12345", t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToListClients() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), CLIENT_SECRET).thatIs().persistent().build();

		Result<PaginatedListRepresentation<ClientRepresentation>> response = clientsClient.list(new CriteriaRepresentation(), t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

}
