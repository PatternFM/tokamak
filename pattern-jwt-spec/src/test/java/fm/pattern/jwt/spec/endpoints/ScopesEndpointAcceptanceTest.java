package fm.pattern.jwt.spec.endpoints;

import static fm.pattern.jwt.sdk.dsl.AccessTokenDSL.token;
import static fm.pattern.jwt.sdk.dsl.ClientDSL.client;
import static fm.pattern.jwt.sdk.dsl.ScopeDSL.scope;
import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static org.assertj.core.api.StrictAssertions.assertThat;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import fm.pattern.commons.rest.JwtClientProperties;
import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.ScopesClient;
import fm.pattern.jwt.sdk.model.AccessTokenRepresentation;
import fm.pattern.jwt.sdk.model.ScopeRepresentation;
import fm.pattern.jwt.spec.AcceptanceTest;

public class ScopesEndpointAcceptanceTest extends AcceptanceTest {

	private final ScopesClient client = new ScopesClient(JwtClientProperties.getEndpoint());

	private AccessTokenRepresentation token;

	@Before
	public void before() {
		this.token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();
	}

	@Test
	public void shouldBeAbleToCreateAScope() {
		ScopeRepresentation scope = scope().build();

		Result<ScopeRepresentation> result = client.create(scope, token.getAccessToken());
		assertThat(result).accepted().withResponseCode(201);

		ScopeRepresentation created = result.getInstance();
		assertThat(created.getId()).startsWith("scp_");
		assertThat(created.getCreated()).isNotNull();
		assertThat(created.getUpdated()).isNotNull();
		assertThat(created.getCreated()).isEqualTo(created.getUpdated());
		assertThat(created.getName()).isEqualTo(scope.getName());
		assertThat(created.getDescription()).isEqualTo(scope.getDescription());
	}

	@Test
	public void shouldNotBeAbleToCreateAScopeIfTheScopeIsInvalid() {
		ScopeRepresentation scope = scope().withName("").build();

		Result<ScopeRepresentation> result = client.create(scope, token.getAccessToken());
		assertThat(result).rejected().withResponseCode(422).withCode("SCO-0001").withMessage("A scope name is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAScopeIfTheScopeNameIsAlreadyInUse() {
		ScopeRepresentation scope = scope().thatIs().persistent(token).build();

		Result<ScopeRepresentation> result = client.create(scope, token.getAccessToken());
		assertThat(result).rejected().withResponseCode(409).withCode("SCO-0003").withMessage("This scope name is already in use.");
	}

	@Test
	public void shouldBeAbleToUpdateAScope() {
		ScopeRepresentation scope = scope().thatIs().persistent(token).build();
		pause(1000);

		scope.setName(RandomStringUtils.randomAlphabetic(10));

		Result<ScopeRepresentation> result = client.update(scope, token.getAccessToken());
		assertThat(result).accepted().withResponseCode(200);

		ScopeRepresentation updated = result.getInstance();
		assertThat(updated.getId()).startsWith("scp_");
		assertThat(updated.getCreated()).isNotNull();
		assertThat(updated.getUpdated()).isNotNull();

		assertThat(updated.getCreated()).isEqualTo(scope.getCreated());
		assertThat(updated.getCreated()).isBefore(updated.getUpdated());
		assertThat(updated.getUpdated()).isAfter(scope.getUpdated());

		assertThat(updated.getName()).isEqualTo(scope.getName());
	}

	@Test
	public void shouldNotBeAbleToUpdateAScopeIfTheScopeIsInvalid() {
		ScopeRepresentation scope = scope().thatIs().persistent(token).build();
		scope.setName("");

		Result<ScopeRepresentation> result = client.update(scope, token.getAccessToken());
		assertThat(result).rejected().withResponseCode(422).withCode("SCO-0001").withMessage("A scope name is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAScopeIfTheScopeNameIsAlreadyInUse() {
		ScopeRepresentation existing = scope().thatIs().persistent(token).build();
		ScopeRepresentation scope = scope().thatIs().persistent(token).build();
		scope.setName(existing.getName());

		Result<ScopeRepresentation> result = client.update(scope, token.getAccessToken());
		assertThat(result).rejected().withResponseCode(409).withCode("SCO-0003").withMessage("This scope name is already in use.");
	}

	@Test
	public void shouldBeAbleToDeleteAScope() {
		ScopeRepresentation scope = scope().thatIs().persistent(token).build();

		Result<ScopeRepresentation> result = client.delete(scope.getId(), token.getAccessToken());
		assertThat(result).accepted().withResponseCode(204);

		assertThat(client.findById(scope.getId(), token.getAccessToken())).rejected().withResponseCode(404);
	}

	@Test
	public void shouldNotBeAbleToDeleteAScopeIfTheScopeIsAssociatedWithClients() {
		ScopeRepresentation scope = scope().thatIs().persistent(token).build();
		client().withScopes(scope).withGrantTypes("password", "refresh_token").thatIs().persistent(token).build();

		Result<ScopeRepresentation> result = client.delete(scope.getId(), token.getAccessToken());
		assertThat(result).rejected().withResponseCode(409).withCode("SCO-0008").withMessage("This scope cannot be deleted, 1 client is linked to this scope.");
	}

	@Test
	public void shouldBeAbleToFindAScopeById() {
		ScopeRepresentation scope = scope().thatIs().persistent(token).build();

		Result<ScopeRepresentation> result = client.findById(scope.getId(), token.getAccessToken());
		assertThat(result).accepted().withResponseCode(200);
		assertThat(result.getInstance()).isEqualToComparingFieldByField(scope);
	}

	@Test
	public void shouldReturnA404WhenAScopeWithTheSpecifiedIdCannotBeFound() {
		Result<ScopeRepresentation> result = client.findById("scp_123", token.getAccessToken());
		assertThat(result).rejected().withResponseCode(404).withMessage("No such scope id: scp_123");
	}

	@Test
	public void shouldBeAbleToFindAScopeByName() {
		ScopeRepresentation scope = scope().thatIs().persistent(token).build();

		Result<ScopeRepresentation> result = client.findByName(scope.getName(), token.getAccessToken());
		assertThat(result).accepted().withResponseCode(200);
		assertThat(result.getInstance()).isEqualToComparingFieldByField(scope);
	}

	@Test
	public void shouldReturnA404WhenAScopeWithTheSpecifiedNameCannotBeFound() {
		Result<ScopeRepresentation> result = client.findByName("scp_123", token.getAccessToken());
		assertThat(result).rejected().withResponseCode(404).withMessage("No such scope name: scp_123");
	}

}
