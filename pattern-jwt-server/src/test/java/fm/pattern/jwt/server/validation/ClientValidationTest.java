package fm.pattern.jwt.server.validation;

import static fm.pattern.jwt.server.dsl.ClientDSL.client;
import static fm.pattern.jwt.server.dsl.GrantTypeDSL.grantType;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.util.HashSet;

import org.apache.commons.lang3.RandomStringUtils;
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
		String clientId = RandomStringUtils.randomAlphanumeric(15);
		client().withClientId(clientId).withGrantType(grantType).thatIs().persistent().build();

		onCreate(client().withClientId(clientId).withGrantType(grantType).build()).rejected().withCode("CLI-0003").withMessage("This client id is already in use.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheSetOfGrantTypesIsNull() {
		Client client = client().build();
		ReflectionUtils.setValue(client, "grantTypes", null);
		onCreate(client).rejected().withCode("CLI-0006").withMessage("A client requires at least one grant type.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheSetOfGrantTypesIsEmpty() {
		Client client = client().build();
		ReflectionUtils.setValue(client, "grantTypes", new HashSet<GrantType>());
		onCreate(client).rejected().withCode("CLI-0006").withMessage("A client requires at least one grant type.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientSecretIsNullOrEmpty() {
		onCreate(client().withGrantType(grantType).withClientSecret(null).build()).rejected().withCode("CLI-0004").withMessage("A client secret is required.");
		onCreate(client().withGrantType(grantType).withClientSecret("").build()).rejected().withCode("CLI-0004").withMessage("A client secret is required.");
		onCreate(client().withGrantType(grantType).withClientSecret("    ").build()).rejected().withCode("CLI-0004").withMessage("A client secret is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientSecretIsLessThan10Characters() {
		onCreate(client().withGrantType(grantType).withClientSecret("ABC").build()).rejected().withCode("CLI-0005").withMessage("A client secret must be between 10 and 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientSecretIsGreaterThan255Characters() {
		onCreate(client().withGrantType(grantType).withClientSecret(randomAlphabetic(256)).build()).rejected().withCode("CLI-0005").withMessage("A client secret must be between 10 and 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientIdIsNullOrEmpty() {
		onCreate(client().withGrantType(grantType).withClientId(null).build()).rejected().withCode("CLI-0001").withMessage("A client id is required.");
		onCreate(client().withGrantType(grantType).withClientId("").build()).rejected().withCode("CLI-0001").withMessage("A client id is required.");
		onCreate(client().withGrantType(grantType).withClientId("    ").build()).rejected().withCode("CLI-0001").withMessage("A client id is required.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientClientIdIsLessThan10Characters() {
		onCreate(client().withGrantType(grantType).withClientId("ABC").build()).rejected().withCode("CLI-0002").withMessage("A client id must be between 10 and 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientClientIdIsGreaterThan128Characters() {
		onCreate(client().withGrantType(grantType).withClientId(randomAlphabetic(129)).build()).rejected().withCode("CLI-0002").withMessage("A client id must be between 10 and 128 characters.");
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

		onUpdate(updated).rejected().withCode("CLI-0003").withMessage("This client id is already in use.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheSetOfGrantTypesIsNull() {
		Client client = client().build();
		ReflectionUtils.setValue(client, "grantTypes", null);
		onUpdate(client).rejected().withCode("CLI-0006").withMessage("A client requires at least one grant type.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheSetOfGrantTypesIsEmpty() {
		Client client = client().build();
		ReflectionUtils.setValue(client, "grantTypes", new HashSet<GrantType>());
		onUpdate(client).rejected().withCode("CLI-0006").withMessage("A client requires at least one grant type.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientSecretIsNullOrEmpty() {
		onUpdate(client().withGrantType(grantType).withClientSecret(null).build()).rejected().withCode("CLI-0004").withMessage("A client secret is required.");
		onUpdate(client().withGrantType(grantType).withClientSecret("").build()).rejected().withCode("CLI-0004").withMessage("A client secret is required.");
		onUpdate(client().withGrantType(grantType).withClientSecret("    ").build()).rejected().withCode("CLI-0004").withMessage("A client secret is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientSecretIsLessThan10Characters() {
		onUpdate(client().withGrantType(grantType).withClientSecret("ABC").build()).rejected().withCode("CLI-0005").withMessage("A client secret must be between 10 and 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientSecretIsGreaterThan255Characters() {
		onUpdate(client().withGrantType(grantType).withClientSecret(randomAlphabetic(256)).build()).rejected().withCode("CLI-0005").withMessage("A client secret must be between 10 and 255 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientIdIsNullOrEmpty() {
		onUpdate(client().withGrantType(grantType).withClientId(null).build()).rejected().withCode("CLI-0001").withMessage("A client id is required.");
		onUpdate(client().withGrantType(grantType).withClientId("").build()).rejected().withCode("CLI-0001").withMessage("A client id is required.");
		onUpdate(client().withGrantType(grantType).withClientId("    ").build()).rejected().withCode("CLI-0001").withMessage("A client id is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientClientIdIsLessThan10Characters() {
		onUpdate(client().withGrantType(grantType).withClientId("ABC").build()).rejected().withCode("CLI-0002").withMessage("A client id must be between 10 and 128 characters.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientClientIdIsGreaterThan128Characters() {
		onUpdate(client().withGrantType(grantType).withClientId(randomAlphabetic(129)).build()).rejected().withCode("CLI-0002").withMessage("A client id must be between 10 and 128 characters.");
	}

}
