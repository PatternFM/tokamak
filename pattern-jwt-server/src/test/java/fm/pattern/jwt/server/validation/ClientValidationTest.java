package fm.pattern.jwt.server.validation;

import static fm.pattern.jwt.server.dsl.ClientDSL.client;
import static fm.pattern.jwt.server.dsl.GrantTypeDSL.grantType;
import static fm.pattern.microstructure.ResultType.UNPROCESSABLE_ENTITY;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import fm.pattern.commons.util.ReflectionUtils;
import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.Client;
import fm.pattern.jwt.server.model.GrantType;

public class ClientValidationTest extends IntegrationTest {

	private GrantType grantType;

	@Before
	public void before() {
		this.grantType = grantType().thatIs().persistent().build();
	}

	@Test
	public void shouldBeAbleToCreateAClient() {
		onCreate(client().withGrantType(grantType).build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToCreateAClientIfTheClientIdAlreadyExists() {
		client().withClientId("firstclient").withGrantType(grantType).thatIs().persistent().build();
		onCreate(client().withClientId("firstclient").withGrantType(grantType).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.clientId.conflict").withDescription("This client id is already in use.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheSetOfGrantTypesIsNull() {
		Client client = client().build();
		ReflectionUtils.setValue(client, "grantTypes", null);
		onCreate(client).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.grantType.required").withDescription("A client requires at least one grant type.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheSetOfGrantTypesIsEmpty() {
		Client client = client().build();
		ReflectionUtils.setValue(client, "grantTypes", new HashSet<GrantType>());
		onCreate(client).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.grantType.required").withDescription("A client requires at least one grant type.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientSecretIsNullOrEmpty() {
		onCreate(client().withGrantType(grantType).withClientSecret(null).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.secret.required").withDescription("A client secret is required.");
		onCreate(client().withGrantType(grantType).withClientSecret("").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.secret.required").withDescription("A client secret is required.");
		onCreate(client().withGrantType(grantType).withClientSecret("    ").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.secret.required").withDescription("A client secret is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientSecretIsLessThan10Characters() {
		onCreate(client().withGrantType(grantType).withClientSecret("ABC").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.secret.size").withDescription("A client secret must be between 10 and 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientSecretIsGreaterThan255Characters() {
		onCreate(client().withGrantType(grantType).withClientSecret(randomAlphabetic(256)).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.secret.size").withDescription("A client secret must be between 10 and 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientIdIsNullOrEmpty() {
		onCreate(client().withGrantType(grantType).withClientId(null).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.clientId.required").withDescription("A client id is required.");
		onCreate(client().withGrantType(grantType).withClientId("").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.clientId.required").withDescription("A client id is required.");
		onCreate(client().withGrantType(grantType).withClientId("    ").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.clientId.required").withDescription("A client id is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientClientIdIsLessThan10Characters() {
		onCreate(client().withGrantType(grantType).withClientId("ABC").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.clientId.size").withDescription("A client id must be between 10 and 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientClientIdIsGreaterThan128Characters() {
		onCreate(client().withGrantType(grantType).withClientId(randomAlphabetic(129)).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.clientId.size").withDescription("A client id must be between 10 and 128 characters.");
	}

	@Test
	public void shouldBeAbleToUpdateAClient() {
		onUpdate(client().withGrantType(grantType).build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientIfTheClientIdAlreadyExists() {
		client().withClientId("firstclient").withGrantType(grantType).thatIs().persistent().build();

		Client updated = client().withClientId("secondclient").withGrantType(grantType).thatIs().persistent().build();
		updated.setClientId("firstclient");

		onUpdate(updated).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.clientId.conflict").withDescription("This client id is already in use.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheSetOfGrantTypesIsNull() {
		Client client = client().build();
		ReflectionUtils.setValue(client, "grantTypes", null);
		onUpdate(client).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.grantType.required").withDescription("A client requires at least one grant type.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheSetOfGrantTypesIsEmpty() {
		Client client = client().build();
		ReflectionUtils.setValue(client, "grantTypes", new HashSet<GrantType>());
		onUpdate(client).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.grantType.required").withDescription("A client requires at least one grant type.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientSecretIsNullOrEmpty() {
		onUpdate(client().withGrantType(grantType).withClientSecret(null).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.secret.required").withDescription("A client secret is required.");
		onUpdate(client().withGrantType(grantType).withClientSecret("").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.secret.required").withDescription("A client secret is required.");
		onUpdate(client().withGrantType(grantType).withClientSecret("    ").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.secret.required").withDescription("A client secret is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientSecretIsLessThan10Characters() {
		onUpdate(client().withGrantType(grantType).withClientSecret("ABC").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.secret.size").withDescription("A client secret must be between 10 and 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientSecretIsGreaterThan255Characters() {
		onUpdate(client().withGrantType(grantType).withClientSecret(randomAlphabetic(256)).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.secret.size").withDescription("A client secret must be between 10 and 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientIdIsNullOrEmpty() {
		onUpdate(client().withGrantType(grantType).withClientId(null).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.clientId.required").withDescription("A client id is required.");
		onUpdate(client().withGrantType(grantType).withClientId("").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.clientId.required").withDescription("A client id is required.");
		onUpdate(client().withGrantType(grantType).withClientId("    ").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.clientId.required").withDescription("A client id is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientClientIdIsLessThan10Characters() {
		onUpdate(client().withGrantType(grantType).withClientId("ABC").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.clientId.size").withDescription("A client id must be between 10 and 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientClientIdIsGreaterThan128Characters() {
		onUpdate(client().withGrantType(grantType).withClientId(randomAlphabetic(129)).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.clientId.size").withDescription("A client id must be between 10 and 128 characters.");
	}

}
