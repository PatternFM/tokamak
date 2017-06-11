package fm.pattern.jwt.spec.endpoints;

import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static fm.pattern.tokamak.sdk.dsl.AccessTokenDSL.token;
import static fm.pattern.tokamak.sdk.dsl.AccountDSL.account;
import static fm.pattern.tokamak.sdk.dsl.AudienceDSL.audience;
import static fm.pattern.tokamak.sdk.dsl.AuthorityDSL.authority;
import static fm.pattern.tokamak.sdk.dsl.ClientDSL.client;
import static fm.pattern.tokamak.sdk.dsl.RoleDSL.role;
import static fm.pattern.tokamak.sdk.dsl.ScopeDSL.scope;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import fm.pattern.jwt.spec.AcceptanceTest;
import fm.pattern.tokamak.sdk.ClientCredentials;
import fm.pattern.tokamak.sdk.ClientsClient;
import fm.pattern.tokamak.sdk.JwtClientProperties;
import fm.pattern.tokamak.sdk.TokensClient;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.AccessTokenRepresentation;
import fm.pattern.tokamak.sdk.model.AccountRepresentation;
import fm.pattern.tokamak.sdk.model.AudienceRepresentation;
import fm.pattern.tokamak.sdk.model.AuthorityRepresentation;
import fm.pattern.tokamak.sdk.model.ClientRepresentation;
import fm.pattern.tokamak.sdk.model.RoleRepresentation;
import fm.pattern.tokamak.sdk.model.ScopeRepresentation;

public class JWTAcceptanceTest extends AcceptanceTest {

	private TokensClient tokensClient = new TokensClient(JwtClientProperties.getEndpoint());
	private ClientsClient clientsClient = new ClientsClient(JwtClientProperties.getEndpoint());

	private String password = "password12345";

	private AccountRepresentation account;
	private RoleRepresentation role;
	private AccessTokenRepresentation token;

	@Before
	public void before() {
		this.token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();
		this.role = role().thatIs().persistent(token).build();
		this.account = account().withPassword(password).withRoles(role).thatIs().persistent(token).build();
	}

	@Test
	public void shouldBeAbleToProduceAJWTTokenForClients() throws Exception {
		AuthorityRepresentation authority1 = authority().thatIs().persistent(token).build();
		AuthorityRepresentation authority2 = authority().thatIs().persistent(token).build();

		AudienceRepresentation audience1 = audience().thatIs().persistent(token).build();
		AudienceRepresentation audience2 = audience().thatIs().persistent(token).build();

		ScopeRepresentation scope1 = scope().thatIs().persistent(token).build();
		ScopeRepresentation scope2 = scope().thatIs().persistent(token).build();

		ClientRepresentation representation = client().withToken(token).withAudiences(audience1, audience2).withScopes(scope1, scope2).withAuthorities(authority1, authority2).withClientSecret("testClientSecret").withGrantTypes("client_credentials", "password", "refresh_token").build();
		assertThat(clientsClient.create(representation, this.token.getAccessToken())).accepted().withResponseCode(201);

		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(new ClientCredentials(representation.getClientId(), "testClientSecret"));
		assertThat(response).accepted();

		DecodedJWT jwt = JWT.decode(response.getInstance().getAccessToken());

		assertThat(jwt.getAlgorithm()).isEqualTo("RS256");

		assertThat(jwt.getIssuer()).isEqualTo(ISSUER);
		assertThat(jwt.getExpiresAt()).isNotNull();
		assertThat(jwt.getId()).isNotNull();
		assertThat(jwt.getAudience()).containsOnly(audience1.getName().toLowerCase(), audience2.getName().toLowerCase());
		assertThat(jwt.getSubject()).isEqualTo(representation.getClientId());

		List<String> clientAuthorities = jwt.getClaim("client_authorities").asList(String.class);
		assertThat(clientAuthorities).containsOnly(authority1.getName(), authority2.getName());

		List<String> userRoles = jwt.getClaim("authorities").asList(String.class);
		assertThat(userRoles).isNull();

		List<String> scopes = jwt.getClaim("scope").asList(String.class);
		assertThat(scopes).containsOnly(scope1.getName(), scope2.getName());

		String clientId = jwt.getClaim("client_id").asString();
		assertThat(clientId).containsSequence(representation.getClientId());
	}

	@Test
	public void shouldBeAbleToProduceAJWTTokenForUsers() throws Exception {
		AuthorityRepresentation authority1 = authority().thatIs().persistent(token).build();
		AuthorityRepresentation authority2 = authority().thatIs().persistent(token).build();

		AudienceRepresentation audience1 = audience().thatIs().persistent(token).build();
		AudienceRepresentation audience2 = audience().thatIs().persistent(token).build();

		ScopeRepresentation scope1 = scope().thatIs().persistent(token).build();
		ScopeRepresentation scope2 = scope().thatIs().persistent(token).build();

		ClientRepresentation representation = client().withToken(token).withAudiences(audience1, audience2).withScopes(scope1, scope2).withAuthorities(authority1, authority2).withClientSecret("testClientSecret").withGrantTypes("client_credentials", "password", "refresh_token").build();
		assertThat(clientsClient.create(representation, this.token.getAccessToken())).accepted().withResponseCode(201);

		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(new ClientCredentials(representation.getClientId(), "testClientSecret"), account.getUsername(), password);
		assertThat(response).accepted();

		DecodedJWT jwt = JWT.decode(response.getInstance().getAccessToken());

		assertThat(jwt.getAlgorithm()).isEqualTo("RS256");

		assertThat(jwt.getIssuer()).isEqualTo(ISSUER);
		assertThat(jwt.getExpiresAt()).isNotNull();
		assertThat(jwt.getId()).isNotNull();
		assertThat(jwt.getAudience()).containsOnly(audience1.getName().toLowerCase(), audience2.getName().toLowerCase());
		assertThat(jwt.getSubject()).isEqualTo(account.getId());

		List<String> clientAuthorities = jwt.getClaim("client_authorities").asList(String.class);
		assertThat(clientAuthorities).containsOnly(authority1.getName(), authority2.getName());

		List<String> userRoles = jwt.getClaim("authorities").asList(String.class);
		assertThat(userRoles).containsExactly(role.getName());

		List<String> scopes = jwt.getClaim("scope").asList(String.class);
		assertThat(scopes).containsOnly(scope1.getName(), scope2.getName());

		String clientId = jwt.getClaim("client_id").asString();
		assertThat(clientId).containsSequence(representation.getClientId());
	}

}
