package fm.pattern.jwt.spec.security;

import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static fm.pattern.tokamak.sdk.dsl.AccessTokenDSL.token;
import static fm.pattern.tokamak.sdk.dsl.AuthorityDSL.authority;
import static fm.pattern.tokamak.sdk.dsl.ClientDSL.client;

import org.junit.Before;
import org.junit.Test;

import fm.pattern.jwt.spec.AcceptanceTest;
import fm.pattern.tokamak.sdk.AuthoritiesClient;
import fm.pattern.tokamak.sdk.JwtClientProperties;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.AccessTokenRepresentation;
import fm.pattern.tokamak.sdk.model.AuthoritiesRepresentation;
import fm.pattern.tokamak.sdk.model.AuthorityRepresentation;
import fm.pattern.tokamak.sdk.model.ClientRepresentation;

public class AuthoritiesEndpointSecurityTest extends AcceptanceTest {

	private AuthoritiesClient authoritiesClient = new AuthoritiesClient(JwtClientProperties.getEndpoint());

	private ClientRepresentation client;
	private AccessTokenRepresentation token;

	@Before
	public void before() {
		this.token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();
		this.client = client().withClientSecret("client_secret").withScopes("clients:read").withGrantTypes("client_credentials", "refresh_token").thatIs().persistent(token).build();
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToCreateAnAuthority() {
		Result<AuthorityRepresentation> response = authoritiesClient.create(authority().build(), null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToUpdateAnAuthority() {
		Result<AuthorityRepresentation> response = authoritiesClient.update(authority().withId("abc").build(), null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToDeleteAnAuthority() {
		Result<AuthorityRepresentation> response = authoritiesClient.delete("123456", null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToFindAnAuthorityById() {
		Result<AuthorityRepresentation> response = authoritiesClient.findById("12345", null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToFindAnAuthorityByName() {
		Result<AuthorityRepresentation> response = authoritiesClient.findByName("name", null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToListAuthorities() {
		Result<AuthoritiesRepresentation> response = authoritiesClient.list(null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToCreateAnAuthority() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), "client_secret").thatIs().persistent().build();

		Result<AuthorityRepresentation> response = authoritiesClient.create(authority().build(), t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToUpdateAnAuthority() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), "client_secret").thatIs().persistent().build();

		Result<AuthorityRepresentation> response = authoritiesClient.update(authority().withId("12345").build(), t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToDeleteAnAuthority() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), "client_secret").thatIs().persistent().build();

		Result<AuthorityRepresentation> response = authoritiesClient.delete("12345", t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToFindAnAuthorityById() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), "client_secret").thatIs().persistent().build();

		Result<AuthorityRepresentation> response = authoritiesClient.findById("12345", t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToFindAnAuthorityByName() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), "client_secret").thatIs().persistent().build();

		Result<AuthorityRepresentation> response = authoritiesClient.findByName("username", t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToListAuthorities() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), "client_secret").thatIs().persistent().build();

		Result<AuthoritiesRepresentation> response = authoritiesClient.list(t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

}
