package fm.pattern.jwt.spec.endpoints;

import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static fm.pattern.tokamak.sdk.dsl.AccessTokenDSL.token;
import static fm.pattern.tokamak.sdk.dsl.AudienceDSL.audience;
import static fm.pattern.tokamak.sdk.dsl.ClientDSL.client;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import fm.pattern.jwt.spec.AcceptanceTest;
import fm.pattern.tokamak.sdk.AudiencesClient;
import fm.pattern.tokamak.sdk.JwtClientProperties;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.AccessTokenRepresentation;
import fm.pattern.tokamak.sdk.model.AudienceRepresentation;

public class AudiencesEndpointAcceptanceTest extends AcceptanceTest {

	private final AudiencesClient client = new AudiencesClient(JwtClientProperties.getEndpoint());

	private AccessTokenRepresentation token;

	@Before
	public void before() {
		this.token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();
	}

	@Test
	public void shouldBeAbleToCreateAnAudience() {
		AudienceRepresentation audience = audience().build();

		Result<AudienceRepresentation> result = client.create(audience, token.getAccessToken());
		assertThat(result).accepted().withResponseCode(201);

		AudienceRepresentation created = result.getInstance();
		assertThat(created.getId()).startsWith("aud_");
		assertThat(created.getCreated()).isNotNull();
		assertThat(created.getUpdated()).isNotNull();
		assertThat(created.getCreated()).isEqualTo(created.getUpdated());
		assertThat(created.getName()).isEqualTo(audience.getName());
		assertThat(created.getDescription()).isEqualTo(audience.getDescription());
	}

	@Test
	public void shouldNotBeAbleToCreateAnAudienceIfTheAudienceIsInvalid() {
		AudienceRepresentation audience = audience().withName("").build();

		Result<AudienceRepresentation> result = client.create(audience, token.getAccessToken());
		assertThat(result).rejected().withResponseCode(422).withCode("AUD-0001").withMessage("An audience name is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAudienceIfTheAudienceNameIsAlreadyInUse() {
		AudienceRepresentation audience = audience().thatIs().persistent(token).build();

		Result<AudienceRepresentation> result = client.create(audience, token.getAccessToken());
		assertThat(result).rejected().withResponseCode(409).withCode("AUD-0003").withMessage("This audience name is already in use.");
	}

	@Test
	public void shouldBeAbleToUpdateAnAudience() {
		AudienceRepresentation audience = audience().thatIs().persistent(token).build();
		pause(1000);

		audience.setName(RandomStringUtils.randomAlphabetic(10));

		Result<AudienceRepresentation> result = client.update(audience, token.getAccessToken());
		assertThat(result).accepted().withResponseCode(200);

		AudienceRepresentation updated = result.getInstance();
		assertThat(updated.getId()).startsWith("aud_");
		assertThat(updated.getCreated()).isNotNull();
		assertThat(updated.getUpdated()).isNotNull();

		assertThat(updated.getCreated()).isEqualTo(audience.getCreated());
		assertThat(updated.getCreated()).isBefore(updated.getUpdated());
		assertThat(updated.getUpdated()).isAfter(audience.getUpdated());

		assertThat(updated.getName()).isEqualTo(audience.getName());
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAudienceIfTheAudienceIsInvalid() {
		AudienceRepresentation audience = audience().thatIs().persistent(token).build();
		audience.setName("");

		Result<AudienceRepresentation> result = client.update(audience, token.getAccessToken());
		assertThat(result).rejected().withResponseCode(422).withCode("AUD-0001").withMessage("An audience name is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAudienceIfTheAudienceNameIsAlreadyInUse() {
		AudienceRepresentation existing = audience().thatIs().persistent(token).build();
		AudienceRepresentation audience = audience().thatIs().persistent(token).build();
		audience.setName(existing.getName());

		Result<AudienceRepresentation> result = client.update(audience, token.getAccessToken());
		assertThat(result).rejected().withResponseCode(409).withCode("AUD-0003").withMessage("This audience name is already in use.");
	}

	@Test
	public void shouldBeAbleToDeleteAnAudience() {
		AudienceRepresentation audience = audience().thatIs().persistent(token).build();

		Result<AudienceRepresentation> result = client.delete(audience.getId(), token.getAccessToken());
		assertThat(result).accepted().withResponseCode(204);

		assertThat(client.findById(audience.getId(), token.getAccessToken())).rejected().withResponseCode(404);
	}

	@Test
	public void shouldNotBeAbleToDeleteAnAudienceIfTheAudienceIsAssociatedWithClients() {
		AudienceRepresentation audience = audience().thatIs().persistent(token).build();
		client().withAudiences(audience).withGrantTypes("password", "refresh_token").thatIs().persistent(token).build();

		Result<AudienceRepresentation> result = client.delete(audience.getId(), token.getAccessToken());
		assertThat(result).rejected().withResponseCode(409).withCode("AUD-0005").withMessage("This audience cannot be deleted, 1 client is linked to this audience.");
	}

	@Test
	public void shouldBeAbleToFindAnAudienceById() {
		AudienceRepresentation audience = audience().thatIs().persistent(token).build();

		Result<AudienceRepresentation> result = client.findById(audience.getId(), token.getAccessToken());
		assertThat(result).accepted().withResponseCode(200);
		assertThat(result.getInstance()).isEqualToComparingFieldByField(audience);
	}

	@Test
	public void shouldReturnA404WhenAnAudienceWithTheSpecifiedIdCannotBeFound() {
		Result<AudienceRepresentation> result = client.findById("aud_123", token.getAccessToken());
		assertThat(result).rejected().withResponseCode(404).withMessage("No such audience id: aud_123");
	}

	@Test
	public void shouldBeAbleToFindAnAudienceByName() {
		AudienceRepresentation audience = audience().thatIs().persistent(token).build();

		Result<AudienceRepresentation> result = client.findByName(audience.getName(), token.getAccessToken());
		assertThat(result).accepted().withResponseCode(200);
		assertThat(result.getInstance()).isEqualToComparingFieldByField(audience);
	}

	@Test
	public void shouldReturnA404WhenAnAudienceWithTheSpecifiedNameCannotBeFound() {
		Result<AudienceRepresentation> result = client.findByName("rol_123", token.getAccessToken());
		assertThat(result).rejected().withResponseCode(404).withMessage("No such audience name: rol_123");
	}

}
