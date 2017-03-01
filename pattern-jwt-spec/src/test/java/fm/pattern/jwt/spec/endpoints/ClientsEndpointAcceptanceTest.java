package fm.pattern.jwt.spec.endpoints;

import static fm.pattern.jwt.sdk.dsl.AccessTokenDSL.token;
import static fm.pattern.jwt.sdk.dsl.AuthorityDSL.authority;
import static fm.pattern.jwt.sdk.dsl.ClientDSL.client;
import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.StrictAssertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import fm.pattern.commons.rest.JwtClientProperties;
import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.ClientsClient;
import fm.pattern.jwt.sdk.model.AccessTokenRepresentation;
import fm.pattern.jwt.sdk.model.AuthorityRepresentation;
import fm.pattern.jwt.sdk.model.ClientRepresentation;
import fm.pattern.jwt.spec.AcceptanceTest;

public class ClientsEndpointAcceptanceTest extends AcceptanceTest {

	private ClientsClient clientsClient = new ClientsClient(JwtClientProperties.getEndpoint());

	private AccessTokenRepresentation token;

	@Before
	public void before() {
		this.token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();
	}

	@Test
	public void shouldBeAbleToCreateAClient() {
		AuthorityRepresentation authority1 = authority().thatIs().persistent(token).build();
		AuthorityRepresentation authority2 = authority().thatIs().persistent(token).build();

		ClientRepresentation representation = client().withToken(token).withAuthorities(authority1, authority2).withScopes("accounts:read", "accounts:create").withGrantTypes("password", "refresh_token").build();

		Result<ClientRepresentation> response = clientsClient.create(representation, this.token.getAccessToken());
		assertThat(response).accepted().withResponseCode(201);

		ClientRepresentation created = response.getInstance();
		assertThat(created.getId()).isNotNull();
		assertThat(created.getId()).startsWith("cli_");
		assertThat(created.getCreated()).isNotNull();
		assertThat(created.getUpdated()).isNotNull();
		assertThat(created.getCreated()).isEqualTo(created.getUpdated());
		assertThat(created.getClientId()).isEqualTo(representation.getClientId());
		assertThat(created.getClientSecret()).isNull();

		assertThat(created.getAccessTokenValiditySeconds()).isEqualTo(600);
		assertThat(created.getRefreshTokenValiditySeconds()).isEqualTo(6000);

		assertThat(created.getScopes()).hasSize(2);
		assertThat(created.getScopes()).extracting("name").contains("accounts:read", "accounts:create");

		assertThat(created.getGrantTypes()).hasSize(2);
		assertThat(created.getGrantTypes()).extracting("name").contains("password", "refresh_token");

		assertThat(created.getAuthorities()).hasSize(2);
		assertThat(created.getAuthorities()).extracting("name").contains(authority1.getName(), authority2.getName());
	}

	@Test
	public void shouldNotBeAbleToCreateAnInvalidClient() {
		ClientRepresentation representation = client().withToken(token).build();

		Result<ClientRepresentation> response = clientsClient.create(representation, this.token.getAccessToken());
		assertThat(response).rejected().withResponseCode(422).withDescription("A client requires at least one grant type.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWithAClientIdThatIsAlreadyInUse() {
		ClientRepresentation representation = client().withGrantTypes("password", "refresh_token").thatIs().persistent(token).build();
		representation.setClientSecret("client_secret");

		Result<ClientRepresentation> response = clientsClient.create(representation, this.token.getAccessToken());
		assertThat(response).rejected().withResponseCode(422).withDescription("This client id is already in use.");
	}

	@Test
	public void shouldBeAbleToUpdateAClient() {
		AuthorityRepresentation authority1 = authority().thatIs().persistent(token).build();
		AuthorityRepresentation authority2 = authority().thatIs().persistent(token).build();

		ClientRepresentation client = client().withAuthorities(authority1).withScopes("accounts:read", "accounts:create").withGrantTypes("password", "refresh_token").thatIs().persistent(token).build();
		pause(1000);

		client.getAuthorities().add(authority2);
		client.getScopes().clear();
		client.setAccessTokenValiditySeconds(100);
		client.setRefreshTokenValiditySeconds(100);

		Result<ClientRepresentation> result = clientsClient.update(client, token.getAccessToken());

		ClientRepresentation updated = result.getInstance();
		assertThat(updated.getId()).startsWith("cli_");
		assertThat(updated.getCreated()).isNotNull();
		assertThat(updated.getUpdated()).isNotNull();
		assertThat(updated.getCreated()).isEqualTo(client.getCreated());
		assertThat(updated.getCreated()).isBefore(updated.getUpdated());
		assertThat(updated.getUpdated()).isAfter(client.getUpdated());
		assertThat(updated.getAccessTokenValiditySeconds()).isEqualTo(100);
		assertThat(updated.getRefreshTokenValiditySeconds()).isEqualTo(100);
		assertThat(updated.getAuthorities()).hasSize(2);
		assertThat(updated.getScopes()).isEmpty();

	}

	@Test
	public void shouldNotBeAbleToUpdateAnInvalidClient() {
		ClientRepresentation client = client().withScopes("accounts:read", "accounts:create").withGrantTypes("password", "refresh_token").thatIs().persistent(token).build();

		client.getScopes().clear();
		client.getGrantTypes().clear();

		Result<ClientRepresentation> result = clientsClient.update(client, token.getAccessToken());
		assertThat(result).rejected().withResponseCode(422);
	}

	@Test
	public void shouldBeAbleToDeleteAClient() {
		ClientRepresentation client = client().withGrantTypes("password", "refresh_token").thatIs().persistent(token).build();

		Result<ClientRepresentation> result = clientsClient.delete(client.getId(), token.getAccessToken());
		assertThat(result).accepted().withResponseCode(204);

		assertThat(clientsClient.findById(client.getId(), token.getAccessToken())).rejected().withResponseCode(404);
	}

	@Test
	public void shouldBeAbleToFindAnClientById() {
		ClientRepresentation persistent = client().withGrantTypes("password", "refresh_token").thatIs().persistent(token).build();
		Result<ClientRepresentation> response = clientsClient.findById(persistent.getId(), token.getAccessToken());

		assertThat(response).accepted().withResponseCode(200);
		assertThat(response.getInstance().getId()).isEqualTo(persistent.getId());
	}

	@Test
	public void shouldReturnA404WhenAnClientWithTheSpecifiedIdCannotBeFound() {
		Result<ClientRepresentation> response = clientsClient.findById("abcdefg", token.getAccessToken());
		assertThat(response).rejected().withResponseCode(404).withDescription("No such client id: abcdefg");
	}

}
