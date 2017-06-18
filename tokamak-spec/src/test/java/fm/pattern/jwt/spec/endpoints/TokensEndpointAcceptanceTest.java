package fm.pattern.jwt.spec.endpoints;

import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static fm.pattern.tokamak.sdk.dsl.AccessTokenDSL.token;
import static fm.pattern.tokamak.sdk.dsl.AccountDSL.account;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import fm.pattern.jwt.spec.AcceptanceTest;
import fm.pattern.tokamak.sdk.AccountsClient;
import fm.pattern.tokamak.sdk.ClientCredentials;
import fm.pattern.tokamak.sdk.JwtClientProperties;
import fm.pattern.tokamak.sdk.PreFlightClient;
import fm.pattern.tokamak.sdk.TokensClient;
import fm.pattern.tokamak.sdk.UserCredentials;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.AccessTokenRepresentation;
import fm.pattern.tokamak.sdk.model.AccountRepresentation;

public class TokensEndpointAcceptanceTest extends AcceptanceTest {

	private AccountsClient accountsClient = new AccountsClient(JwtClientProperties.getEndpoint());
	private TokensClient tokensClient = new TokensClient(JwtClientProperties.getEndpoint());
	private PreFlightClient preFlightClient = new PreFlightClient(JwtClientProperties.getEndpoint());

	private String password = "password12345";

	private AccountRepresentation account;
	private AccessTokenRepresentation token;

	@Before
	public void before() {
		this.token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();
		this.account = account().withPassword(password).thatIs().persistent(token).build();
	}

	@Test
	public void shouldBeAbleToMakeAPreFlightRequestOnTheTokensEndpoint() {
		assertThat(preFlightClient.check("/oauth/token")).accepted().withResponseCode(200);
	}

	@Test
	public void shouldNotBeAbleToUseARefreshTokenWhenAnAccessTokenIsExpected() {
		AccountRepresentation account = account().withPassword("password").withToken(token).thatIs().persistent().build();
		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS, account.getUsername(), "password");
		assertThat(response).accepted();

		AccountRepresentation account2 = account().withPassword("password").build();

		Result<AccountRepresentation> response2 = accountsClient.create(account2, response.getInstance().getRefreshToken());
		assertThat(response2).rejected().withError(401, "AUT-0001", "Encoded token is a refresh token.");
	}

	/*
	 * To run this test, you must authenticate a user at:
	 * http://localhost:9600/oauth/authorize?client_id=test-client&redirect_uri=http://localhost:8080/login&
	 * response_type=code.
	 * 
	 * Once you press the "Authorize" button a redirect will occur (to the above url) which will contain the
	 * authorization code.
	 */
	@Ignore
	public void shouldBeAbleToExchangeAValidAuthorizationCodeForAnAccessToken() {
		Result<AccessTokenRepresentation> response = tokensClient.exchange(TEST_CLIENT_CREDENTIALS, "enter_auth_code_here", "http://localhost:8080/login");
		assertThat(response).accepted();

		AccessTokenRepresentation token = response.getInstance();
		assertThat(token.getAccessToken()).isNotNull();
		assertThat(token.getRefreshToken()).isNotNull();
		assertThat(token.getExpiresIn()).isNotNull();
		assertThat(token.getTokenType()).isEqualTo(TOKEN_BEARER);
		assertThat(token.getSubject()).isEqualTo(TEST_CLIENT_ID);
		assertThat(token.getIssuer()).isEqualTo(ISSUER);
	}

	@Test
	public void theServerShouldReturnAnAccessTokenButNotRefreshTokenWhenUsingTheClientCredentialsGrantType() {
		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS);
		assertThat(response).accepted();

		AccessTokenRepresentation token = response.getInstance();
		assertThat(token.getAccessToken()).isNotNull();
		assertThat(token.getRefreshToken()).isNull();
		assertThat(token.getExpiresIn()).isNotNull();
		assertThat(token.getTokenType()).isEqualTo(TOKEN_BEARER);
		assertThat(token.getSubject()).isEqualTo(TEST_CLIENT_ID);
		assertThat(token.getIssuer()).isEqualTo(ISSUER);
		assertThat(token.getIssuedAt()).isNotNull();
	}

	@Test
	public void aClientShouldNotBeAbleToGetAnAccessTokenUsingAClientCredentialsGrantTypeWhenTheClientIdIsInvalid() {
		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(new ClientCredentials("invalid_client_id", TEST_CLIENT_SECRET));
		assertThat(response).rejected().withResponseCode(401).withMessage(BAD_CREDENTIALS);
	}

	@Test
	public void aClientShouldNotBeAbleToGetAnAccessTokenUsingAClientCredentialsGrantTypeWhenTheClientSecretIsInvalid() {
		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(new ClientCredentials(TEST_CLIENT_ID, "invalid_client_secret"));
		assertThat(response).rejected().withResponseCode(401).withMessage(BAD_CREDENTIALS);
	}

	@Test
	public void shouldNotBeAbleToIssueTokensToLockedAccounts() {
		AccountRepresentation account = account().withPassword("password").thatIs().persistent(token).build();

		assertThat(tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS, account.getUsername(), "password")).accepted().withResponseCode(200);

		account.setLocked(true);
		assertThat(accountsClient.update(account, token.getAccessToken())).accepted().withResponseCode(200);

		assertThat(tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS, account.getUsername(), "password")).rejected().withResponseCode(400);

		account.setLocked(false);
		assertThat(accountsClient.update(account, token.getAccessToken())).accepted().withResponseCode(200);

		assertThat(tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS, account.getUsername(), "password")).accepted().withResponseCode(200);
	}

	@Test
	public void theServerShouldReturnAnAccessTokenAndRefreshTokenWhenUsingThePasswordGrantType() {
		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS, account.getUsername(), password);
		assertThat(response).accepted();

		AccessTokenRepresentation token = response.getInstance();
		assertThat(token.getAccessToken()).isNotNull();
		assertThat(token.getRefreshToken()).isNotNull();
		assertThat(token.getExpiresIn()).isNotNull();
		assertThat(token.getTokenType()).isEqualTo(TOKEN_BEARER);
		assertThat(token.getIssuer()).isEqualTo(ISSUER);
		assertThat(token.getSubject()).isEqualTo(account.getId());
		assertThat(token.getIssuedAt()).isNotNull();
	}

	@Test
	public void aClientShouldNotBeAbleToGetAnAccessTokenUsingAResourceOwnerGrantTypeWhenTheClientIdIsInvalid() {
		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(new ClientCredentials("invalid_client_id", TEST_CLIENT_SECRET), new UserCredentials(account.getUsername(), password));
		assertThat(response).rejected().withResponseCode(401).withMessage(BAD_CREDENTIALS);
	}

	@Test
	public void aClientShouldNotBeAbleToGetAnAccessTokenUsingAResourceOwnerGrantTypeWhenTheClientSecretIsInvalid() {
		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(new ClientCredentials(TEST_CLIENT_ID, "invalid_client_secret"), new UserCredentials(account.getUsername(), password));
		assertThat(response).rejected().withResponseCode(401).withMessage(BAD_CREDENTIALS);
	}

	@Test
	public void aClientShouldNotBeAbleToGetAnAccessTokenUsingAResourceOwnerGrantTypeWhenTheUsernameIsInvalid() {
		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS, new UserCredentials("invalid_username", password));
		assertThat(response).rejected().withResponseCode(400).withMessage(BAD_CREDENTIALS);
	}

	@Test
	public void aClientShouldNotBeAbleToGetAnAccessTokenUsingAResourceOwnerGrantTypeWhenThePasswordIsInvalid() {
		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS, new UserCredentials(account.getUsername(), "invalid_password"));
		assertThat(response).rejected().withResponseCode(400).withMessage(BAD_CREDENTIALS);
	}

	@Test
	public void shouldBeAbleToGetAnAccessTokenUsingARefreshTokenGrantType() {
		Result<AccessTokenRepresentation> accessTokenResponse = tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS, account.getUsername(), password);
		assertThat(accessTokenResponse).accepted();

		AccessTokenRepresentation originalToken = accessTokenResponse.getInstance();

		Result<AccessTokenRepresentation> refreshTokenResponse = tokensClient.refreshAccessToken(TEST_CLIENT_CREDENTIALS, originalToken);
		assertThat(refreshTokenResponse).accepted();

		AccessTokenRepresentation refreshedToken = refreshTokenResponse.getInstance();
		assertThat(refreshedToken.getAccessToken()).isNotEqualTo(originalToken.getAccessToken());
	}

	@Test
	public void shouldNotBeAbleToPerformARefreshUsingAnAccessToken() {
		Result<AccessTokenRepresentation> accessTokenResponse = tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS, account.getUsername(), password);
		assertThat(accessTokenResponse).accepted();

		AccessTokenRepresentation originalToken = accessTokenResponse.getInstance();
		originalToken.setRefreshToken(originalToken.getAccessToken());

		Result<AccessTokenRepresentation> refreshTokenResponse = tokensClient.refreshAccessToken(TEST_CLIENT_CREDENTIALS, originalToken);
		assertThat(refreshTokenResponse).rejected().withError(401, "invalid_token", "Encoded token is not a refresh token");
	}

	@Test
	public void shouldBeNotAbleToGetAnAccessTokenUsingARefreshTokenGrantTypeWhenTheClientIdIsInvalid() {
		Result<AccessTokenRepresentation> accessTokenResponse = tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS, new UserCredentials(account.getUsername(), password));
		assertThat(accessTokenResponse).accepted();

		Result<AccessTokenRepresentation> refreshTokenResponse = tokensClient.refreshAccessToken(new ClientCredentials("invalid_client_id", TEST_CLIENT_SECRET), accessTokenResponse.getInstance());
		assertThat(refreshTokenResponse).withResponseCode(401).withMessage(BAD_CREDENTIALS);
	}

	@Test
	public void shouldBeNotAbleToGetAnAccessTokenUsingARefreshTokenGrantTypeWhenTheClientPasswordIsInvalid() {
		Result<AccessTokenRepresentation> accessTokenResponse = tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS, new UserCredentials(account.getUsername(), password));
		assertThat(accessTokenResponse).accepted();

		Result<AccessTokenRepresentation> refreshTokenResponse = tokensClient.refreshAccessToken(new ClientCredentials(TEST_CLIENT_ID, "invalid_password"), accessTokenResponse.getInstance());
		assertThat(refreshTokenResponse).rejected().withResponseCode(401).withMessage(BAD_CREDENTIALS);
	}

}
