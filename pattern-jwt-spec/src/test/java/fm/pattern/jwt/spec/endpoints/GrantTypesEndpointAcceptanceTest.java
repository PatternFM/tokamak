package fm.pattern.jwt.spec.endpoints;

import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static fm.pattern.tokamak.sdk.dsl.AccessTokenDSL.token;
import static org.assertj.core.api.StrictAssertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fm.pattern.jwt.spec.AcceptanceTest;
import fm.pattern.tokamak.sdk.GrantTypesClient;
import fm.pattern.tokamak.sdk.JwtClientProperties;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.AccessTokenRepresentation;
import fm.pattern.tokamak.sdk.model.GrantTypeRepresentation;

public class GrantTypesEndpointAcceptanceTest extends AcceptanceTest {

	private final GrantTypesClient client = new GrantTypesClient(JwtClientProperties.getEndpoint());

	private AccessTokenRepresentation token;

	@Before
	public void before() {
		this.token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();
	}

	@Test
	public void shouldBeAbleToFindAGrantTypeById() {
		List<GrantTypeRepresentation> types = client.list(token.getAccessToken()).getInstance().getGrantTypes();

		for (GrantTypeRepresentation representation : types) {
			Result<GrantTypeRepresentation> result = client.findById(representation.getId(), token.getAccessToken());
			assertThat(result).accepted().withResponseCode(200);
			assertThat(result.getInstance()).isEqualToComparingFieldByField(representation);
		}
	}

	@Test
	public void shouldReturnA404WhenAGrantTypeWithTheSpecifiedIdCannotBeFound() {
		Result<GrantTypeRepresentation> result = client.findById("scp_123", token.getAccessToken());
		assertThat(result).rejected().withResponseCode(404).withMessage("No such granttype id: scp_123");
	}

	@Test
	public void shouldBeAbleToFindAGrantTypeByName() {
		List<GrantTypeRepresentation> types = client.list(token.getAccessToken()).getInstance().getGrantTypes();

		for (GrantTypeRepresentation representation : types) {
			Result<GrantTypeRepresentation> result = client.findByName(representation.getName(), token.getAccessToken());
			assertThat(result).accepted().withResponseCode(200);
			assertThat(result.getInstance()).isEqualToComparingFieldByField(representation);
		}
	}

	@Test
	public void shouldReturnA404WhenAGrantTypeWithTheSpecifiedNameCannotBeFound() {
		Result<GrantTypeRepresentation> result = client.findByName("gnt_123", token.getAccessToken());
		assertThat(result).rejected().withResponseCode(404).withMessage("No such grant type name: gnt_123");
	}

}
