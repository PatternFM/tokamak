package fm.pattern.tokamak.server.service;

import static fm.pattern.tokamak.server.PatternAssertions.assertThat;
import static fm.pattern.tokamak.server.dsl.ClientDSL.client;
import static fm.pattern.tokamak.server.dsl.GrantTypeDSL.grantType;
import static fm.pattern.tokamak.server.dsl.ScopeDSL.scope;
import static fm.pattern.tokamak.server.pagination.Criteria.criteria;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Sets;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.Client;
import fm.pattern.tokamak.server.model.GrantType;
import fm.pattern.tokamak.server.model.Scope;
import fm.pattern.tokamak.server.security.PasswordEncodingService;
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
		this.grantType = grantType().save();
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
	public void shouldEncryptTheClientPasswordWhenCreatingAClient() {
		String secret = "jfksd888ASDF%%$$jslfsidfj";
		Client client = client().withGrantType(grantType).withClientSecret(secret).save();
		assertThat(client.getClientSecret()).startsWith("$2a$");
		assertThat(passwordEncodingService.matches(secret, client.getClientSecret())).isTrue();
	}

	@Test
	public void shouldNotBeAbleToCreateAnInvalidClient() {
		Client client = client().build();

		Result<Client> result = clientService.create(client);
		assertThat(result).rejected().withMessage("A client requires at least one grant type.");
	}

	@Test
	public void shouldBeAbleToDeleteAClient() {
		Client client = client().withGrantType(grantType).save();
		assertThat(clientService.findById(client.getId())).isNotNull();

		assertThat(clientService.delete(client)).accepted();
		assertThat(clientService.findById(client.getId())).rejected();
	}

	@Test
	public void shouldBeAbleToUpdateAClient() {
		Client client = client().withGrantType(grantType).save();

		Scope scope = scope().save();
		client.setScopes(Sets.newHashSet(scope));

		Result<Client> result = clientService.update(client);
		assertThat(result).accepted();

		Client updated = clientService.findById(client.getId()).getInstance();
		assertThat(updated.getScopes()).hasSize(1);
		assertThat(updated.getScopes()).containsExactly(scope);
	}

	@Test
	public void shouldNotBeAbleToUpdateAnInvalidClient() {
		Client client = client().withGrantType(grantType).save();
		client.setGrantTypes(new HashSet<>());

		Result<Client> result = clientService.update(client);
		assertThat(result).rejected().withError("CLI-0006", "A client requires at least one grant type.", UnprocessableEntityException.class);

		Client unmodified = clientService.findById(client.getId()).getInstance();
		assertThat(unmodified.getGrantTypes()).hasSize(1);
		assertThat(unmodified.getGrantTypes()).containsExactly(grantType);
	}

	@Test
	public void shouldBeAbleToFindAClientById() {
		Client client = client().withGrantType(grantType).save();
		assertThat(clientService.findById(client.getId()).getInstance()).isEqualTo(client);
	}

	@Test
	public void shouldNotBeAbleToFindAClientByIdIfTheClientIdIsNull() {
		assertThat(clientService.findById(null)).rejected().withError("CLI-0007", "A client id is required.", UnprocessableEntityException.class);
		assertThat(clientService.findById("")).rejected().withError("CLI-0007", "A client id is required.", UnprocessableEntityException.class);
		assertThat(clientService.findById("  ")).rejected().withError("CLI-0007", "A client id is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToFindAClientByIdIfTheClientIdDoesNotExist() {
		assertThat(clientService.findById("csrx")).rejected().withError("SYS-0001", "No such client id: csrx", EntityNotFoundException.class);
	}

	@Test
	public void shouldBeAbleToFindAClientByClientId() {
		Client client = client().withGrantType(grantType).save();
		assertThat(clientService.findByClientId(client.getClientId()).getInstance()).isEqualTo(client);
	}

	@Test
	public void shouldNotBeAbleToFindAClientByClientIdIfTheClientIdIsNullOrEmpty() {
		assertThat(clientService.findByClientId(null)).rejected().withError("CLI-0001", "A client id is required.", UnprocessableEntityException.class);
		assertThat(clientService.findByClientId("")).rejected().withError("CLI-0001", "A client id is required.", UnprocessableEntityException.class);
		assertThat(clientService.findByClientId("  ")).rejected().withError("CLI-0001", "A client id is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToFindAClientByClienIdIfTheClientIdDoesNotExist() {
		assertThat(clientService.findByClientId("csrx")).rejected().withError("CLI-0009", "No such client id: csrx", EntityNotFoundException.class);
	}

	@Test
	public void shouldBeAbleToListClients() {
		IntStream.range(0, 5).forEach(i -> client().withGrantType(grantType).save());

		Result<List<Client>> result = clientService.list(criteria());
		assertThat(result).accepted();

		List<Client> clients = result.getInstance();
		assertThat(clients.size()).isGreaterThanOrEqualTo(5);

		assertThat(clientService.list(criteria().limit(1)).getInstance().size()).isEqualTo(1);
		assertThat(clientService.list(criteria().limit(1).page(3)).getInstance().size()).isEqualTo(1);
	}

}
