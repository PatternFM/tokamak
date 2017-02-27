package fm.pattern.jwt.spec.endpoints;

import static fm.pattern.jwt.sdk.dsl.AccessTokenDSL.token;
import static fm.pattern.jwt.sdk.dsl.AccountDSL.account;
import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static org.assertj.core.api.StrictAssertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import fm.pattern.commons.rest.JwtClientProperties;
import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.AccountsClient;
import fm.pattern.jwt.sdk.model.AccessTokenRepresentation;
import fm.pattern.jwt.sdk.model.AccountRepresentation;
import fm.pattern.jwt.spec.AcceptanceTest;

public class AccountsEndpointAcceptanceTest extends AcceptanceTest {

	private AccountsClient accountsClient = new AccountsClient(JwtClientProperties.getEndpoint());

	private AccessTokenRepresentation token;

	@Before
	public void before() {
		this.token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();
	}

	@Test
	public void shouldBeAbleToCreateAnAccount() {
		AccountRepresentation account = account().withPassword("password").build();

		Result<AccountRepresentation> response = accountsClient.create(account, token.getAccessToken());
		assertThat(response).accepted().withResponseCode(201);

		AccountRepresentation created = response.getInstance();
		assertThat(created.getId()).isNotNull();
		assertThat(created.getId()).startsWith("acc_");
		assertThat(created.getCreated()).isNotNull();
		assertThat(created.getUpdated()).isNotNull();
		assertThat(created.getCreated()).isEqualTo(created.getUpdated());
		assertThat(created.getUsername()).isEqualTo(account.getUsername());
		assertThat(created.getPassword()).isNull();
	}

	@Test
	public void shouldNotBeAbleToCreateAnInvalidAccount() {
		AccountRepresentation account = account().withUsername(null).withPassword("password").build();
		Result<AccountRepresentation> response = accountsClient.create(account, token.getAccessToken());
		assertThat(response).rejected().withResponseCode(422).withDescription("An account username is required.");
	}

	@Test
	public void shouldBeAbleToFindAnAccountById() {
		AccountRepresentation account = account().thatIs().persistent(token).build();
		Result<AccountRepresentation> response = accountsClient.findById(account.getId(), token.getAccessToken());

		assertThat(response).accepted().withResponseCode(200);
		assertThat(response.getInstance()).isEqualToComparingFieldByField(account);
	}

	@Test
	public void shouldReturnA404WhenAnAccountWithTheSpecifiedIdCannotBeFound() {
		Result<AccountRepresentation> response = accountsClient.findById("abcdefg", token.getAccessToken());
		assertThat(response).rejected().withResponseCode(404).withDescription("No such account id: abcdefg");
	}

	@Test
	public void shouldBeAbleToFindAnAccountByUsername() {
		AccountRepresentation account = account().thatIs().persistent(token).build();
		Result<AccountRepresentation> result = accountsClient.findByUsername(account.getUsername(), token.getAccessToken());

		assertThat(result).accepted().withResponseCode(200);
		assertThat(result.getInstance()).isEqualToComparingFieldByField(account);
	}

	@Test
	public void shouldReturnA404WhenAnAccountWithTheSpecifiedUsernameCannotBeFound() {
		Result<AccountRepresentation> response = accountsClient.findByUsername("abcdefg", token.getAccessToken());
		assertThat(response).rejected().withResponseCode(404).withDescription("No such username: abcdefg");
	}

}
