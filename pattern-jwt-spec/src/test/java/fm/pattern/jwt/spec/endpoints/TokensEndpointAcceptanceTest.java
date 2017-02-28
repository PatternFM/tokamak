package fm.pattern.jwt.spec.endpoints;

import static fm.pattern.jwt.sdk.dsl.AccessTokenDSL.token;
import static fm.pattern.jwt.sdk.dsl.AccountDSL.account;
import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static org.assertj.core.api.StrictAssertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import fm.pattern.commons.rest.JwtClientProperties;
import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.ClientCredentials;
import fm.pattern.jwt.sdk.TokensClient;
import fm.pattern.jwt.sdk.UserCredentials;
import fm.pattern.jwt.sdk.model.AccessTokenRepresentation;
import fm.pattern.jwt.sdk.model.AccountRepresentation;
import fm.pattern.jwt.spec.AcceptanceTest;

public class TokensEndpointAcceptanceTest extends AcceptanceTest {

	private TokensClient tokensClient = new TokensClient(JwtClientProperties.getEndpoint());

	private String password = "password12345";
	private AccountRepresentation account;
	private AccessTokenRepresentation token;

	@Before
	public void before() {
		this.token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();

		this.password = "password12345";
		this.account = account().withPassword(password).thatIs().persistent(token).build();
	}

	@Test
	public void theServerShouldReturnAnAccessTokenButNotRefreshTokenWhenUsingTheClientCredentialsGrantType() {
		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS);
		assertThat(response).accepted();

		AccessTokenRepresentation token = response.getInstance();
		assertThat(token.getAccessToken()).isNotEmpty();
		assertThat(token.getRefreshToken()).isNull();
		assertThat(token.getExpiresIn()).isNotNull();
		assertThat(token.getTokenType()).isEqualTo("bearer");
	}

	@Test
	public void aClientShouldNotBeAbleToGetAnAccessTokenUsingAClientCredentialsGrantTypeWhenTheClientIdIsInvalid() {
		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(new ClientCredentials("invalid_client_id", TEST_CLIENT_SECRET));
		assertThat(response).rejected().withResponseCode(401).withDescription(BAD_CREDENTIALS);
	}

	@Test
	public void aClientShouldNotBeAbleToGetAnAccessTokenUsingAClientCredentialsGrantTypeWhenTheClientSecretIsInvalid() {
		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(new ClientCredentials(TEST_CLIENT_ID, "invalid_client_secret"));
		assertThat(response).rejected().withResponseCode(401).withDescription(BAD_CREDENTIALS);
	}

	@Test
	public void theServerShouldReturnAnAccessTokenAndRefreshTokenWhenUsingThePasswordGrantType() {
		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS, account.getUsername(), password);
		assertThat(response).accepted();

		AccessTokenRepresentation token = response.getInstance();
		assertThat(token.getAccessToken()).isNotEmpty();
		assertThat(token.getExpiresIn()).isNotNull();
		assertThat(token.getTokenType()).isEqualTo("bearer");
		assertThat(token.getRefreshToken()).isNotNull();
	}

	@Test
	public void aClientShouldNotBeAbleToGetAnAccessTokenUsingAResourceOwnerGrantTypeWhenTheClientIdIsInvalid() {
		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(new ClientCredentials("invalid_client_id", TEST_CLIENT_SECRET), new UserCredentials(account.getUsername(), password));
		assertThat(response).rejected().withResponseCode(401).withDescription(BAD_CREDENTIALS);
	}

	@Test
	public void aClientShouldNotBeAbleToGetAnAccessTokenUsingAResourceOwnerGrantTypeWhenTheClientSecretIsInvalid() {
		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(new ClientCredentials(TEST_CLIENT_ID, "invalid_client_secret"), new UserCredentials(account.getUsername(), password));
		assertThat(response).rejected().withResponseCode(401).withDescription(BAD_CREDENTIALS);
	}

	@Test
	public void aClientShouldNotBeAbleToGetAnAccessTokenUsingAResourceOwnerGrantTypeWhenTheUsernameIsInvalid() {
		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS, new UserCredentials("invalid_username", password));
		assertThat(response).rejected().withResponseCode(400).withDescription(BAD_CREDENTIALS);
	}

	@Test
	public void aClientShouldNotBeAbleToGetAnAccessTokenUsingAResourceOwnerGrantTypeWhenThePasswordIsInvalid() {
		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS, new UserCredentials(account.getUsername(), "invalid_password"));
		assertThat(response).rejected().withResponseCode(400).withDescription(BAD_CREDENTIALS);
	}

	@Test
	public void shouldBeAbleToGetAnAccessTokenUsingARefreshTokenGrantType() {
		Result<AccessTokenRepresentation> accessTokenResponse = tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS, account.getUsername(), password);
		assertThat(accessTokenResponse).accepted();
		AccessTokenRepresentation originalAccessToken = accessTokenResponse.getInstance();

		Result<AccessTokenRepresentation> refreshTokenResponse = tokensClient.refreshAccessToken(TEST_CLIENT_CREDENTIALS, originalAccessToken);
		assertThat(refreshTokenResponse).accepted();

		AccessTokenRepresentation refreshedAccessToken = refreshTokenResponse.getInstance();
		assertThat(refreshedAccessToken.getAccessToken()).isNotEqualTo(originalAccessToken.getAccessToken());
	}

	@Test
	public void shouldBeNotAbleToGetAnAccessTokenUsingARefreshTokenGrantTypeWhenTheClientIdIsInvalid() {
		Result<AccessTokenRepresentation> accessTokenResponse = tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS, new UserCredentials(account.getUsername(), password));
		assertThat(accessTokenResponse).accepted();

		Result<AccessTokenRepresentation> refreshTokenResponse = tokensClient.refreshAccessToken(new ClientCredentials("invalid_client_id", TEST_CLIENT_SECRET), accessTokenResponse.getInstance());
		assertThat(refreshTokenResponse).withResponseCode(401).withDescription(BAD_CREDENTIALS);
	}

	@Test
	public void shouldBeNotAbleToGetAnAccessTokenUsingARefreshTokenGrantTypeWhenTheClientPasswordIsInvalid() {
		Result<AccessTokenRepresentation> accessTokenResponse = tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS, new UserCredentials(account.getUsername(), password));
		assertThat(accessTokenResponse).accepted();

		Result<AccessTokenRepresentation> refreshTokenResponse = tokensClient.refreshAccessToken(new ClientCredentials(TEST_CLIENT_ID, "invalid_password"), accessTokenResponse.getInstance());
		assertThat(refreshTokenResponse).rejected().withResponseCode(401).withDescription(BAD_CREDENTIALS);
	}

}
