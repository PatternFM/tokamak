package fm.pattern.jwt.server.security;

import static fm.pattern.jwt.server.dsl.ClientDSL.client;
import static fm.pattern.jwt.server.dsl.GrantTypeDSL.grantType;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.Client;
import fm.pattern.jwt.server.model.GrantType;

public class ClientAuthenticationServiceIntegrationTest extends IntegrationTest {

	@Autowired
	private ClientAuthenticationService clientAuthenticationService;

	private GrantType grantType;

	@Before
	public void before() {
		this.grantType = grantType().thatIs().persistent().build();
	}

	@Test
	public void shouldBeAbleToFindAnInternalClientByUsername() {
		Client client = client().withGrantType(grantType).thatIs().persistent().build();
		ClientDetails clientDetails = clientAuthenticationService.loadClientByClientId(client.getClientId());

		assertThat(clientDetails).isNotNull();
		assertThat(clientDetails.getAccessTokenValiditySeconds()).isNull();
		assertThat(clientDetails.getRefreshTokenValiditySeconds()).isNull();
	}

	@Test
	public void shouldBeAbleToFindAnAdminClientByUsername() {
		Client client = client().withGrantType(grantType).thatIs().persistent().build();
		ClientDetails clientDetails = clientAuthenticationService.loadClientByClientId(client.getClientId());

		assertThat(clientDetails).isNotNull();
		assertThat(clientDetails.getAccessTokenValiditySeconds()).isNull();
		assertThat(clientDetails.getRefreshTokenValiditySeconds()).isNull();
	}

	@Test
	public void shouldBeAbleToFindATrustedClientByUsername() {
		Client client = client().withGrantType(grantType).thatIs().persistent().build();
		ClientDetails clientDetails = clientAuthenticationService.loadClientByClientId(client.getClientId());
		assertThat(clientDetails).isNotNull();
	}

	@Test(expected = UsernameNotFoundException.class)
	public void shouldReturnNullWhenTheClientIdIsNull() {
		assertThat(clientAuthenticationService.loadClientByClientId(null)).isNull();
	}

	@Test(expected = UsernameNotFoundException.class)
	public void shouldReturnNullWhenTheClientIdIEmpty() {
		assertThat(clientAuthenticationService.loadClientByClientId("")).isNull();
	}

	@Test(expected = UsernameNotFoundException.class)
	public void shouldReturnNullWhenTheClientIdIsInvalid() {
		assertThat(clientAuthenticationService.loadClientByClientId("foobard")).isNull();
	}

}
