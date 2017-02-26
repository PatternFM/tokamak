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
		client().withUsername("firstclient").withGrantType(grantType).thatIs().persistent().build();
		onCreate(client().withUsername("firstclient").withGrantType(grantType).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.username.conflict").withDescription("This client id is already in use.");
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
		onCreate(client().withGrantType(grantType).withPassword(null).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.password.required").withDescription("A client secret is required.");
		onCreate(client().withGrantType(grantType).withPassword("").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.password.required").withDescription("A client secret is required.");
		onCreate(client().withGrantType(grantType).withPassword("    ").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.password.required").withDescription("A client secret is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientSecretIsLessThan10Characters() {
		onCreate(client().withGrantType(grantType).withPassword("ABC").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.password.size").withDescription("A client secret must be between 10 and 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientSecretIsGreaterThan255Characters() {
		onCreate(client().withGrantType(grantType).withPassword(randomAlphabetic(256)).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.password.size").withDescription("A client secret must be between 10 and 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheUsernameIsNullOrEmpty() {
		onCreate(client().withGrantType(grantType).withUsername(null).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.username.required").withDescription("A client id is required.");
		onCreate(client().withGrantType(grantType).withUsername("").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.username.required").withDescription("A client id is required.");
		onCreate(client().withGrantType(grantType).withUsername("    ").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.username.required").withDescription("A client id is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientUsernameIsLessThan10Characters() {
		onCreate(client().withGrantType(grantType).withUsername("ABC").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.username.size").withDescription("A client id must be between 10 and 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientUsernameIsGreaterThan128Characters() {
		onCreate(client().withGrantType(grantType).withUsername(randomAlphabetic(129)).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.username.size").withDescription("A client id must be between 10 and 128 characters.");
	}

	@Test
	public void shouldBeAbleToUpdateAClient() {
		onUpdate(client().withGrantType(grantType).build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientIfTheClientIdAlreadyExists() {
		client().withUsername("firstclient").withGrantType(grantType).thatIs().persistent().build();

		Client updated = client().withUsername("secondclient").withGrantType(grantType).thatIs().persistent().build();
		updated.setUsername("firstclient");

		onUpdate(updated).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.username.conflict").withDescription("This client id is already in use.");
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
		onUpdate(client().withGrantType(grantType).withPassword(null).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.password.required").withDescription("A client secret is required.");
		onUpdate(client().withGrantType(grantType).withPassword("").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.password.required").withDescription("A client secret is required.");
		onUpdate(client().withGrantType(grantType).withPassword("    ").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.password.required").withDescription("A client secret is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientSecretIsLessThan10Characters() {
		onUpdate(client().withGrantType(grantType).withPassword("ABC").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.password.size").withDescription("A client secret must be between 10 and 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientSecretIsGreaterThan255Characters() {
		onUpdate(client().withGrantType(grantType).withPassword(randomAlphabetic(256)).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.password.size").withDescription("A client secret must be between 10 and 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheUsernameIsNullOrEmpty() {
		onUpdate(client().withGrantType(grantType).withUsername(null).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.username.required").withDescription("A client id is required.");
		onUpdate(client().withGrantType(grantType).withUsername("").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.username.required").withDescription("A client id is required.");
		onUpdate(client().withGrantType(grantType).withUsername("    ").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.username.required").withDescription("A client id is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientUsernameIsLessThan10Characters() {
		onUpdate(client().withGrantType(grantType).withUsername("ABC").build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.username.size").withDescription("A client id must be between 10 and 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientUsernameIsGreaterThan128Characters() {
		onUpdate(client().withGrantType(grantType).withUsername(randomAlphabetic(129)).build()).rejected().withType(UNPROCESSABLE_ENTITY).withCode("client.username.size").withDescription("A client id must be between 10 and 128 characters.");
	}

}
