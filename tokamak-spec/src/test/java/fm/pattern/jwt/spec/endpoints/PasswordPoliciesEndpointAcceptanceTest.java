package fm.pattern.jwt.spec.endpoints;

import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static fm.pattern.tokamak.sdk.dsl.AccessTokenDSL.token;
import static fm.pattern.tokamak.sdk.dsl.PasswordPolicyDSL.passwordPolicy;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import fm.pattern.jwt.spec.AcceptanceTest;
import fm.pattern.tokamak.sdk.JwtClientProperties;
import fm.pattern.tokamak.sdk.PasswordPoliciesClient;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.AccessTokenRepresentation;
import fm.pattern.tokamak.sdk.model.PasswordPolicyRepresentation;

public class PasswordPoliciesEndpointAcceptanceTest extends AcceptanceTest {

	private final PasswordPoliciesClient client = new PasswordPoliciesClient(JwtClientProperties.getEndpoint());

	private AccessTokenRepresentation token;

	@Before
	public void before() {
		this.token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();
	}

	@Test
	public void shouldBeAbleToCreateAPasswordPolicy() {
		PasswordPolicyRepresentation representation = passwordPolicy().build();

		Result<PasswordPolicyRepresentation> result = client.create(representation, token.getAccessToken());
		assertThat(result).accepted().withResponseCode(201);
		assertThat(result.getInstance()).isEqualToIgnoringGivenFields(representation, "id", "created", "updated");
	}

	@Test
	public void shouldBeAbleToFindAPasswordPolicyById() {
		PasswordPolicyRepresentation representation = client.findByName("account-password-policy", token.getAccessToken()).getInstance();

		Result<PasswordPolicyRepresentation> result = client.findById(representation.getId(), token.getAccessToken());
		assertThat(result).accepted().withResponseCode(200);
		assertThat(result.getInstance()).isEqualToComparingFieldByField(representation);
	}

	@Test
	public void shouldReturnA404WhenAPasswordPolicyWithTheSpecifiedIdCannotBeFound() {
		Result<PasswordPolicyRepresentation> result = client.findById("pol_123", token.getAccessToken());
		assertThat(result).rejected().withResponseCode(404).withMessage("No such passwordpolicy id: pol_123");
	}

	@Test
	public void shouldBeAbleToFindAPasswordPolicyByName() {
		Result<PasswordPolicyRepresentation> result = client.findByName("account-password-policy", token.getAccessToken());
		assertThat(result).accepted().withResponseCode(200);
		assertThat(result.getInstance()).isEqualToComparingFieldByField(result.getInstance());
	}

	@Test
	public void shouldReturnA404WhenAPasswordPolicyWithTheSpecifiedNameCannotBeFound() {
		Result<PasswordPolicyRepresentation> result = client.findByName("pol_123", token.getAccessToken());
		assertThat(result).rejected().withResponseCode(404).withMessage("No such policy name: pol_123");
	}

}
