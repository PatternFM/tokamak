package fm.pattern.jwt.spec.endpoints;

import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static fm.pattern.tokamak.sdk.commons.CriteriaRepresentation.criteria;
import static fm.pattern.tokamak.sdk.dsl.AccessTokenDSL.token;
import static fm.pattern.tokamak.sdk.dsl.AccountDSL.account;
import static fm.pattern.tokamak.sdk.dsl.ClientDSL.client;
import static fm.pattern.tokamak.sdk.dsl.RoleDSL.role;
import static fm.pattern.tokamak.sdk.dsl.ScopeDSL.scope;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.IntStream;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import fm.pattern.jwt.spec.AcceptanceTest;
import fm.pattern.tokamak.sdk.AccountsClient;
import fm.pattern.tokamak.sdk.ClientCredentials;
import fm.pattern.tokamak.sdk.JwtClientProperties;
import fm.pattern.tokamak.sdk.TokensClient;
import fm.pattern.tokamak.sdk.UserCredentials;
import fm.pattern.tokamak.sdk.commons.PaginatedListRepresentation;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.commons.TokenHolder;
import fm.pattern.tokamak.sdk.model.AccessTokenRepresentation;
import fm.pattern.tokamak.sdk.model.AccountRepresentation;
import fm.pattern.tokamak.sdk.model.ClientRepresentation;
import fm.pattern.tokamak.sdk.model.RoleRepresentation;
import fm.pattern.tokamak.sdk.model.ScopeRepresentation;
import fm.pattern.tokamak.sdk.model.SecretsRepresentation;

public class AccountsEndpointAcceptanceTest extends AcceptanceTest {

	private AccountsClient accountsClient = new AccountsClient(JwtClientProperties.getEndpoint());
	private TokensClient tokensClient = new TokensClient(JwtClientProperties.getEndpoint());

	private static AccessTokenRepresentation token = null;

	@Before
	public void before() {
		if (token == null) {
			token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();
			TokenHolder.token(token.getAccessToken());
		}
	}

	@Test
	public void shouldBeAbleToCreateAnAccount() {
		AccountRepresentation account = account().build();

		Result<AccountRepresentation> response = accountsClient.create(account);
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
		Result<AccountRepresentation> response = accountsClient.create(account);
		assertThat(response).rejected().withResponseCode(422).withMessage("An account username is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAnAccountIfTheUsernameIsAlreadyTaken() {
		AccountRepresentation account = account().thatIs().persistent(token).build();
		account.setPassword("password");

		Result<AccountRepresentation> response = accountsClient.create(account);
		assertThat(response).rejected().withResponseCode(409).withMessage("This account username is already in use.");
	}

	@Test
	public void shouldBeAbleToUpdateAnAccount() {
		AccountRepresentation account = account().thatIs().persistent(token).build();
		pause(1000);

		RoleRepresentation role = role().thatIs().persistent(token).build();

		account.setRoles(new HashSet<RoleRepresentation>(Arrays.asList(role)));
		account.setLocked(true);

		Result<AccountRepresentation> result = accountsClient.update(account);
		assertThat(result).accepted().withResponseCode(200);

		AccountRepresentation updated = result.getInstance();
		assertThat(updated.getId()).startsWith("acc_");
		assertThat(updated.getCreated()).isNotNull();
		assertThat(updated.getUpdated()).isNotNull();

		assertThat(updated.getCreated()).isEqualTo(account.getCreated());
		assertThat(updated.getCreated()).isBefore(updated.getUpdated());
		assertThat(updated.getUpdated()).isAfter(account.getUpdated());

		assertThat(updated.getRoles()).hasSize(1);
		assertThat(updated.getRoles().iterator().next()).isEqualToComparingFieldByField(role);

		account.setRoles(new HashSet<>());

		Result<AccountRepresentation> result2 = accountsClient.update(account);
		assertThat(result2).accepted().withResponseCode(200);
		assertThat(result2.getInstance().getRoles()).isEmpty();
	}

	@Test
	public void shouldDeleteRoleAssociationsWhenUpdatingAnAccount() {
		RoleRepresentation role1 = role().thatIs().persistent(token).build();
		RoleRepresentation role2 = role().thatIs().persistent(token).build();
		RoleRepresentation role3 = role().thatIs().persistent(token).build();

		AccountRepresentation account = account().withRoles(role1, role2, role3).thatIs().persistent(token).build();
		account.setRoles(new HashSet<RoleRepresentation>(Arrays.asList(role1)));

		Result<AccountRepresentation> result = accountsClient.update(account);
		assertThat(result).accepted().withResponseCode(200);

		AccountRepresentation updated = result.getInstance();
		assertThat(updated.getRoles()).hasSize(1);
		assertThat(updated.getRoles().iterator().next()).isEqualToComparingFieldByField(role1);
	}

	@Test
	public void updatingAnAccountShouldNotUpdateTheUsernameOrPasswordOrLockedStatus() {
		AccountRepresentation account = account().withPassword("dfsfsASDFSA454$").thatIs().persistent(token).build();
		String username = account.getUsername();

		account.setLocked(true);
		account.setPassword("new_password");
		account.setUsername(RandomStringUtils.randomAlphanumeric(6));

		Result<AccountRepresentation> result = accountsClient.update(account);
		assertThat(result).accepted().withResponseCode(200);

		AccountRepresentation updated = result.getInstance();
		assertThat(updated.getId()).isEqualTo(account.getId());
		assertThat(updated.getUsername()).isEqualTo(username);
		assertThat(updated.isLocked()).isEqualTo(false);

		Result<AccessTokenRepresentation> response = tokensClient.getAccessToken(TEST_CLIENT_CREDENTIALS, new UserCredentials(username, "new_password"));
		assertThat(response).rejected().withResponseCode(400).withMessage(BAD_CREDENTIALS);
	}

	@Test
	public void anAdministratorShouldBeAbleToSetAnAccountPassword() {
		String clientSecret = "sadflasdjfsdfij324234ASDFD#$@#$";
		String accountPassword = "alsdifjls3424AA!";
		String newAccountPassword = "sldifsladfjsdl#224234AB";

		AccountRepresentation admin = account().withPassword(accountPassword).withRoles("tokamak:admin").thatIs().persistent(token).build();
		String t = token().withClient(TEST_CLIENT_CREDENTIALS).withUser(admin.getUsername(), accountPassword).thatIs().persistent().build().getAccessToken();

		AccountRepresentation account = account().withPassword(accountPassword).withRoles("tokamak:admin").thatIs().persistent(token).build();

		ScopeRepresentation scope = scope().thatIs().persistent(token).build();
		ClientRepresentation client = client().withClientSecret(clientSecret).withScopes(scope).withToken(token).withName("name").withGrantTypes("password", "refresh_token").thatIs().persistent().build();

		assertThat(accountsClient.updatePassword(account, new SecretsRepresentation(newAccountPassword), t)).accepted().withResponseCode(200);
		assertThat(tokensClient.getAccessToken(new ClientCredentials(client.getClientId(), clientSecret), new UserCredentials(account.getUsername(), newAccountPassword))).accepted().withResponseCode(200);
	}

	@Test
	public void anAdministratorShouldNotBeAbleToSetAnAccountPasswordIfThePasswordIsInvalid() {
		String accountPassword = "alsdifjls3424AA!";
		String newAccountPassword = "dl#234A";

		AccountRepresentation admin = account().withPassword(accountPassword).withRoles("tokamak:admin").thatIs().persistent(token).build();
		String t = token().withClient(TEST_CLIENT_CREDENTIALS).withUser(admin.getUsername(), accountPassword).thatIs().persistent().build().getAccessToken();

		AccountRepresentation account = account().withPassword(accountPassword).withRoles("tokamak:admin").thatIs().persistent(token).build();
		assertThat(accountsClient.updatePassword(account, new SecretsRepresentation(newAccountPassword), t)).rejected().withError(422, "PWD-0004", "The password must be at least 8 characters.");
	}

	@Test
	public void aUserShouldNotBeAbleToSetAnAccountPassword() {
		String accountPassword = "alsdifjls3424AA!";
		String newAccountPassword = "sldifsladfjsdl#224234AB";

		AccountRepresentation user = account().withPassword(accountPassword).withRoles("tokamak:user").thatIs().persistent(token).build();
		String t = token().withClient(TEST_CLIENT_CREDENTIALS).withUser(user.getUsername(), accountPassword).thatIs().persistent().build().getAccessToken();

		AccountRepresentation account = account().withPassword(accountPassword).withRoles("tokamak:user").thatIs().persistent(token).build();
		assertThat(accountsClient.updatePassword(account, new SecretsRepresentation(newAccountPassword), t)).rejected().withError(422, "PWD-1000", "The current password is required.");
	}

	@Test
	public void aUserShouldBeAbleToUpdateTheirOwnPassword() {
		String clientSecret = "sadflasdjfsdfij324234ASDFD#$@#$";
		String accountPassword = "alsdifjls3424AA!";
		String newAccountPassword = "sldifsladfjsdl#224234AB";

		AccountRepresentation account = account().withPassword(accountPassword).withRoles("tokamak:admin").thatIs().persistent(token).build();
		String t = token().withClient(TEST_CLIENT_CREDENTIALS).withUser(account.getUsername(), accountPassword).thatIs().persistent().build().getAccessToken();

		ScopeRepresentation scope = scope().thatIs().persistent(token).build();
		ClientRepresentation client = client().withClientSecret(clientSecret).withScopes(scope).withToken(token).withName("name").withGrantTypes("password", "refresh_token").thatIs().persistent().build();

		assertThat(accountsClient.updatePassword(account, new SecretsRepresentation(newAccountPassword, accountPassword), t)).accepted().withResponseCode(200);
		assertThat(tokensClient.getAccessToken(new ClientCredentials(client.getClientId(), clientSecret), new UserCredentials(account.getUsername(), newAccountPassword))).accepted().withResponseCode(200);
	}

	@Test
	public void aUserShouldNotBeAbleToUpdateTheirOwnPasswordIfTheNewPasswordIsInvalid() {
		String accountPassword = "alsdifjls3424AA!";
		String newAccountPassword = "aA2!";

		AccountRepresentation user = account().withPassword(accountPassword).withRoles("tokamak:user").thatIs().persistent(token).build();
		String t = token().withClient(TEST_CLIENT_CREDENTIALS).withUser(user.getUsername(), accountPassword).thatIs().persistent().build().getAccessToken();

		assertThat(accountsClient.updatePassword(user, new SecretsRepresentation(newAccountPassword, accountPassword), t)).rejected().withError(422, "PWD-0004", "The password must be at least 8 characters.");
	}

	@Test
	public void aUserShouldNotBeAbleToUpdateTheirOwnPasswordTheirProvidedPasswordDoesNotMatchTheCurrentPassword() {
		String accountPassword = "alsdifjls3424AA!";
		String newAccountPassword = "sldifsladfjsdl#224234AB";

		AccountRepresentation user = account().withPassword(accountPassword).withRoles("tokamak:user").thatIs().persistent(token).build();
		String t = token().withClient(TEST_CLIENT_CREDENTIALS).withUser(user.getUsername(), accountPassword).thatIs().persistent().build().getAccessToken();

		assertThat(accountsClient.updatePassword(user, new SecretsRepresentation(newAccountPassword, "somethingelse"), t)).rejected().withError(422, "PWD-1001", "The password provided does not match your current password. Please try again.");
	}

	@Test
	public void shouldBeAbleToDeleteAnAccount() {
		RoleRepresentation role1 = role().thatIs().persistent(token).build();

		account().withRoles(role1).thatIs().persistent(token).build();

		AccountRepresentation account = account().withRoles(role1).thatIs().persistent(token).build();
		Result<AccountRepresentation> result = accountsClient.delete(account.getId());
		assertThat(result).accepted().withResponseCode(204);

		assertThat(accountsClient.findById(account.getId())).rejected().withResponseCode(404);
	}

	@Test
	public void shouldBeAbleToFindAnAccountById() {
		AccountRepresentation account = account().thatIs().persistent(token).build();

		Result<AccountRepresentation> response = accountsClient.findById(account.getId());
		assertThat(response).accepted().withResponseCode(200);
		assertThat(response.getInstance()).isEqualToComparingFieldByField(account);
	}

	@Test
	public void shouldReturnA404WhenAnAccountWithTheSpecifiedIdCannotBeFound() {
		Result<AccountRepresentation> response = accountsClient.findById("abcdefg");
		assertThat(response).rejected().withResponseCode(404).withMessage("No such account id: abcdefg");
	}

	@Test
	public void shouldBeAbleToFindAnAccountByUsername() {
		AccountRepresentation account = account().thatIs().persistent(token).build();
		Result<AccountRepresentation> result = accountsClient.findByUsername(account.getUsername());

		assertThat(result).accepted().withResponseCode(200);
		assertThat(result.getInstance()).isEqualToComparingFieldByField(account);
	}

	@Test
	public void shouldReturnA404WhenAnAccountWithTheSpecifiedUsernameCannotBeFound() {
		Result<AccountRepresentation> response = accountsClient.findByUsername("abcdefg");
		assertThat(response).rejected().withResponseCode(404).withMessage("No such username: abcdefg");
	}

	@Test
	public void shouldBeAbleToListAccounts() {
		IntStream.range(1, 5).forEach(i -> account().withToken(token).thatIs().persistent().build());

		Result<PaginatedListRepresentation<AccountRepresentation>> result = accountsClient.list(criteria());
		assertThat(result).accepted().withResponseCode(200);
		assertThat(result.getInstance().getPayload().size()).isGreaterThanOrEqualTo(5);
		assertThat(result.getInstance().getCriteria().getPage()).isNotNull();
		assertThat(result.getInstance().getCriteria().getLimit()).isNotNull();

		assertThat(accountsClient.list(criteria().limit(1).page(4)).getInstance().getPayload()).hasSize(1);
	}

}
