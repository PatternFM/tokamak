package fm.pattern.jwt.spec.endpoints;

import static fm.pattern.jwt.sdk.dsl.AccessTokenDSL.token;
import static fm.pattern.jwt.sdk.dsl.RoleDSL.role;
import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static org.assertj.core.api.StrictAssertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.RolesClient;
import fm.pattern.jwt.sdk.model.AccessTokenRepresentation;
import fm.pattern.jwt.sdk.model.RoleRepresentation;
import fm.pattern.jwt.spec.AcceptanceTest;

public class RolesEndpointAcceptanceTest extends AcceptanceTest {

	private final RolesClient client = new RolesClient("http://localhost:9600");

	private AccessTokenRepresentation token;

	@Before
	public void before() {
		this.token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();
	}

	@Test
	public void shouldBeAbleToCreateARole() {
		RoleRepresentation role = role().build();

		Result<RoleRepresentation> result = client.create(role, token.getAccessToken());
		assertThat(result).accepted().withResponseCode(201);

		RoleRepresentation created = result.getInstance();
		assertThat(created.getId()).startsWith("rol_");
		assertThat(created.getCreated()).isNotNull();
		assertThat(created.getUpdated()).isNotNull();
		assertThat(created.getCreated()).isEqualTo(created.getUpdated());
		assertThat(created.getName()).isEqualTo(role.getName());

	}

}
