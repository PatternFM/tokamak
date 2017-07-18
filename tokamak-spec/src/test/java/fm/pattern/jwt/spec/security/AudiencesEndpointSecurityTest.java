package fm.pattern.jwt.spec.security;

import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static fm.pattern.tokamak.sdk.dsl.AccessTokenDSL.token;
import static fm.pattern.tokamak.sdk.dsl.AudienceDSL.audience;
import static fm.pattern.tokamak.sdk.dsl.ClientDSL.client;

import org.junit.Before;
import org.junit.Test;

import fm.pattern.jwt.spec.AcceptanceTest;
import fm.pattern.tokamak.sdk.AudiencesClient;
import fm.pattern.tokamak.sdk.JwtClientProperties;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.AccessTokenRepresentation;
import fm.pattern.tokamak.sdk.model.AudienceRepresentation;
import fm.pattern.tokamak.sdk.model.AudiencesRepresentation;
import fm.pattern.tokamak.sdk.model.ClientRepresentation;

public class AudiencesEndpointSecurityTest extends AcceptanceTest {

	private static final String CLIENT_SECRET = "as4dfSDFSw33sfsdf%%^";

	private AudiencesClient audiencesClient = new AudiencesClient(JwtClientProperties.getEndpoint());

	private ClientRepresentation client;
	private AccessTokenRepresentation token;

	@Before
	public void before() {
		this.token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();
		this.client = client().withClientSecret(CLIENT_SECRET).withScopes("clients:read").withGrantTypes("client_credentials", "refresh_token").thatIs().persistent(token).build();
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToCreateAnAudience() {
		Result<AudienceRepresentation> response = audiencesClient.create(audience().build(), null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToUpdateAnAudience() {
		Result<AudienceRepresentation> response = audiencesClient.update(audience().withId("abc").build(), null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToDeleteAnAudience() {
		Result<AudienceRepresentation> response = audiencesClient.delete("123456", null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToFindAnAudienceById() {
		Result<AudienceRepresentation> response = audiencesClient.findById("12345", null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToFindAnAudienceByName() {
		Result<AudienceRepresentation> response = audiencesClient.findByName("name", null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToListAudiences() {
		Result<AudiencesRepresentation> response = audiencesClient.list(null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateAudienceShouldNotBeAbleToCreateAnAudience() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), CLIENT_SECRET).thatIs().persistent().build();

		Result<AudienceRepresentation> response = audiencesClient.create(audience().build(), t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateAudienceShouldNotBeAbleToUpdateAnAudience() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), CLIENT_SECRET).thatIs().persistent().build();

		Result<AudienceRepresentation> response = audiencesClient.update(audience().withId("12345").build(), t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateAudienceShouldNotBeAbleToDeleteAnAudience() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), CLIENT_SECRET).thatIs().persistent().build();

		Result<AudienceRepresentation> response = audiencesClient.delete("12345", t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateAudienceShouldNotBeAbleToFindAnAudienceById() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), CLIENT_SECRET).thatIs().persistent().build();

		Result<AudienceRepresentation> response = audiencesClient.findById("12345", t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateAudienceShouldNotBeAbleToFindAnAudienceByName() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), CLIENT_SECRET).thatIs().persistent().build();

		Result<AudienceRepresentation> response = audiencesClient.findByName("username", t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateAudienceShouldNotBeAbleToListAudiences() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), CLIENT_SECRET).thatIs().persistent().build();

		Result<AudiencesRepresentation> response = audiencesClient.list(t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

}
