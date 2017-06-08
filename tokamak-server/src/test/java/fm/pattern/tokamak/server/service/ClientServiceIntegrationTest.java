package fm.pattern.tokamak.server.service;

import static fm.pattern.tokamak.server.PatternAssertions.assertThat;
import static fm.pattern.tokamak.server.dsl.ClientDSL.client;
import static fm.pattern.tokamak.server.dsl.GrantTypeDSL.grantType;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.Client;
import fm.pattern.tokamak.server.model.GrantType;
import fm.pattern.tokamak.server.security.PasswordEncodingService;
import fm.pattern.tokamak.server.service.ClientService;
import fm.pattern.valex.EntityNotFoundException;
import fm.pattern.valex.Result;
import fm.pattern.valex.UnprocessableEntityException;

public class ClientServiceIntegrationTest extends IntegrationTest {

	@Autowired
	private ClientService clientService;

	@Autowired
	private PasswordEncodingService passwordEncodingService;

	private GrantType grantType;

	@Before
	public void before() {
		this.grantType = grantType().thatIs().persistent().build();
	}

	@Test
	public void shouldBeAbleToCreateAClient() {
		Client client = client().withGrantType(grantType).build();

		Result<Client> result = clientService.create(client);
		assertThat(result).accepted();

		Client created = result.getInstance();
		assertThat(created.getId()).isNotNull();
		assertThat(created.getCreated()).isNotNull();
		assertThat(created.getUpdated()).isNotNull();
		assertThat(created.getCreated()).isEqualTo(client.getUpdated());
		assertThat(created.getId()).isNotNull();
		assertThat(created.getClientId()).isNotNull();
		assertThat(created.getClientSecret()).isNotNull();
	}

	@Test
	public void shouldNotBeAbleToCreateAnInvalidClient() {
		Client client = client().build();

		Result<Client> result = clientService.create(client);
		assertThat(result).rejected().withMessage("A client requires at least one grant type.");
	}

	@Test
	public void shouldBeAbleToDeleteAClient() {
		Client client = client().withGrantType(grantType).thatIs().persistent().build();
		assertThat(clientService.findById(client.getId())).isNotNull();

		assertThat(clientService.delete(client)).accepted();
		assertThat(clientService.findById(client.getId())).rejected();
	}

	@Test
	public void shouldEncryptTheClientPasswordWhenCreatingAClient() {
		Client client = client().withGrantType(grantType).withClientSecret("password1234").thatIs().persistent().build();
		assertThat(client.getClientSecret()).startsWith("$2a$");
		assertThat(passwordEncodingService.matches("password1234", client.getClientSecret())).isTrue();
	}

	@Test
	public void shouldBeAbleToFindAClientById() {
		Client client = client().withGrantType(grantType).thatIs().persistent().build();
		assertThat(clientService.findById(client.getId()).getInstance()).isEqualTo(client);
	}

	@Test
	public void shouldNotBeAbleToFindAClientByIdIfTheClientIdIsNull() {
		assertThat(clientService.findById(null)).rejected().withError("CLI-0007", "A client id is required.", UnprocessableEntityException.class);
		assertThat(clientService.findById("")).rejected().withError("CLI-0007","A client id is required.", UnprocessableEntityException.class);
		assertThat(clientService.findById("  ")).rejected().withError("CLI-0007","A client id is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToFindAClientByIdIfTheClientIdDoesNotExist() {
		assertThat(clientService.findById("csrx")).rejected().withError("SYS-0001", "No such client id: csrx", EntityNotFoundException.class);
	}

	@Test
	public void shouldBeAbleToFindAClientByUsername() {
		Client client = client().withGrantType(grantType).thatIs().persistent().build();
		assertThat(clientService.findByClientId(client.getClientId()).getInstance()).isEqualTo(client);
	}

	@Test
	public void shouldNotBeAbleToFindAClientByClientIdIfTheClientIdIsNullOrEmpty() {
		assertThat(clientService.findByClientId(null)).rejected().withError("CLI-0001", "A client id is required.", UnprocessableEntityException.class);;
		assertThat(clientService.findByClientId("")).rejected().withError("CLI-0001", "A client id is required.", UnprocessableEntityException.class);
		assertThat(clientService.findByClientId("  ")).rejected().withError("CLI-0001", "A client id is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToFindAClientByClienIdIfTheClientIdDoesNotExist() {
		assertThat(clientService.findByClientId("csrx")).rejected().withError("CLI-0009", "No such client id: csrx", EntityNotFoundException.class);
	}

}
