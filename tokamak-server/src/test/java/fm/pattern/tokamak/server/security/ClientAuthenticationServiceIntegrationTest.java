package fm.pattern.tokamak.server.security;

import static fm.pattern.tokamak.server.dsl.ClientDSL.client;
import static fm.pattern.tokamak.server.dsl.GrantTypeDSL.grantType;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.Client;
import fm.pattern.tokamak.server.model.GrantType;
import fm.pattern.tokamak.server.security.ClientAuthenticationService;

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
		assertThat(clientDetails.getAccessTokenValiditySeconds()).isEqualTo(600);
		assertThat(clientDetails.getRefreshTokenValiditySeconds()).isEqualTo(3600);
	}

	@Test
	public void shouldBeAbleToFindAnAdminClientByUsername() {
		Client client = client().withGrantType(grantType).thatIs().persistent().build();
		ClientDetails clientDetails = clientAuthenticationService.loadClientByClientId(client.getClientId());

		assertThat(clientDetails).isNotNull();
		assertThat(clientDetails.getAccessTokenValiditySeconds()).isEqualTo(600);
		assertThat(clientDetails.getRefreshTokenValiditySeconds()).isEqualTo(3600);
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

	@Test
	public void shouldBeAbleToResolveTheClientFromTheThreadLocalCache() {
		Client client = client().withGrantType(grantType).thatIs().persistent().build();
		CurrentAuthenticatedClientContext.setAuthenticatedClient(new AuthenticatedClient(client));

		ClientDetails details = clientAuthenticationService.loadClientByClientId(client.getClientId());
		assertThat(details.getClientId()).isEqualTo(client.getClientId());
		
		CurrentAuthenticatedClientContext.clear();
	}

	@Test
	public void shouldResolveTheClientEvenWhenAnotherClientIStoredInCache() {
		Client client1 = client().withGrantType(grantType).thatIs().persistent().build();
		Client client2 = client().withGrantType(grantType).thatIs().persistent().build();
		
		CurrentAuthenticatedClientContext.setAuthenticatedClient(new AuthenticatedClient(client1));

		ClientDetails details = clientAuthenticationService.loadClientByClientId(client2.getClientId());
		assertThat(details.getClientId()).isEqualTo(client2.getClientId());
		
		CurrentAuthenticatedClientContext.clear();
	}
	
}
