package fm.pattern.jwt.server.service;

import static fm.pattern.jwt.server.PatternAssertions.assertThat;
import static fm.pattern.jwt.server.dsl.ClientDSL.client;
import static fm.pattern.jwt.server.dsl.GrantTypeDSL.grantType;
import static fm.pattern.microstructure.ResultType.NOT_FOUND;
import static fm.pattern.microstructure.ResultType.UNPROCESSABLE_ENTITY;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.Client;
import fm.pattern.jwt.server.model.GrantType;
import fm.pattern.jwt.server.security.PasswordEncodingService;
import fm.pattern.microstructure.Result;
import fm.pattern.microstructure.ResultType;

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
		Client client = client().withGrantType(null).build();

		Result<Client> result = clientService.create(client);
		assertThat(result).rejected().withType(ResultType.UNPROCESSABLE_ENTITY);
	}
	
	
	@Test
	public void shouldBeAbleToDeleteAClient() {
		Client client = client().withGrantType(grantType).thatIs().persistent().build();
		assertThat(clientService.findById(client.getId())).isNotNull();
		
		clientService.delete(client);
		assertThat(clientService.findById(client.getId())).rejected().withType(NOT_FOUND);
	}

	@Test
	public void shouldEncryptTheClientPasswordWhenCreatingAClient() {
		Client client = client().withGrantType(grantType).withClientSecret("password1234").thatIs().persistent().build();
		assertThat(client.getClientSecret()).startsWith("$2a$");
		assertThat(passwordEncodingService.matches("password1234", client.getClientSecret()));
	}

	@Test
	public void shouldBeAbleToFindAClientById() {
		Client client = client().withGrantType(grantType).thatIs().persistent().build();
		assertThat(clientService.findById(client.getId()).getInstance()).isEqualTo(client);
	}

	@Test
	public void shouldNotBeAbleToFindAClientByIdIfTheClientIdentifierIsNull() {
		assertThat(clientService.findById(null)).rejected().withType(UNPROCESSABLE_ENTITY).withMessage("The client id to retrieve cannot be null or empty.");
		assertThat(clientService.findById("")).rejected().withType(UNPROCESSABLE_ENTITY).withMessage("The client id to retrieve cannot be null or empty.");
		assertThat(clientService.findById("  ")).rejected().withType(UNPROCESSABLE_ENTITY).withMessage("The client id to retrieve cannot be null or empty.");
	}

	@Test
	public void shouldNotBeAbleToFindAClientByIdIfTheClientIdentifierDoesNotExist() {
		assertThat(clientService.findById("csrx")).rejected().withType(NOT_FOUND).withMessage("No such client id: csrx");
	}

	@Test
	public void shouldBeAbleToFindAClientByUsername() {
		Client client = client().withGrantType(grantType).thatIs().persistent().build();
		assertThat(clientService.findByClientId(client.getClientId()).getInstance()).isEqualTo(client);
	}

	@Test
	public void shouldNotBeAbleToFindAClientByClientIdIfTheClientIdIsNullOrEmpty() {
		assertThat(clientService.findByClientId(null)).rejected().withType(UNPROCESSABLE_ENTITY).withMessage("The client id to retrieve cannot be null or empty.");
		assertThat(clientService.findByClientId("")).rejected().withType(UNPROCESSABLE_ENTITY).withMessage("The client id to retrieve cannot be null or empty.");
		assertThat(clientService.findByClientId("  ")).rejected().withType(UNPROCESSABLE_ENTITY).withMessage("The client id to retrieve cannot be null or empty.");
	}

	@Test
	public void shouldNotBeAbleToFindAClientByClienIdIfTheClientIdDoesNotExist() {
		assertThat(clientService.findByClientId("csrx")).rejected().withType(NOT_FOUND).withMessage("No such client id: csrx");
	}

}
