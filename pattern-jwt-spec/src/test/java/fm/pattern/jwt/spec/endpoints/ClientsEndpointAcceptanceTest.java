package fm.pattern.jwt.spec.endpoints;

import static fm.pattern.jwt.sdk.dsl.AccessTokenDSL.token;
import static fm.pattern.jwt.sdk.dsl.AuthorityDSL.authority;
import static fm.pattern.jwt.sdk.dsl.ClientDSL.client;
import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.StrictAssertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.ClientsClient;
import fm.pattern.jwt.sdk.model.AccessTokenRepresentation;
import fm.pattern.jwt.sdk.model.AuthorityRepresentation;
import fm.pattern.jwt.sdk.model.ClientRepresentation;
import fm.pattern.jwt.spec.AcceptanceTest;

public class ClientsEndpointAcceptanceTest extends AcceptanceTest {

	private ClientsClient client = new ClientsClient("http://localhost:9600");

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

		Result<ClientRepresentation> response = client.create(representation, this.token.getAccessToken());
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

}
