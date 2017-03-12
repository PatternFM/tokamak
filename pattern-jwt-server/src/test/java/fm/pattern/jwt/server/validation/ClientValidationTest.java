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
import fm.pattern.valex.ResourceConflictException;
import fm.pattern.valex.UnprocessableEntityException;

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

		onCreate(client().withClientId(clientId).withGrantType(grantType).build()).rejected().withError("CLI-0003","This client id is already in use.",ResourceConflictException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheSetOfGrantTypesIsNull() {
		Client client = client().build();
		ReflectionUtils.setValue(client, "grantTypes", null);
		onCreate(client).rejected().withError("CLI-0006","A client requires at least one grant type.",UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheSetOfGrantTypesIsEmpty() {
		Client client = client().build();
		ReflectionUtils.setValue(client, "grantTypes", new HashSet<GrantType>());
		onCreate(client).rejected().withError("CLI-0006","A client requires at least one grant type.",UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientSecretIsNullOrEmpty() {
		onCreate(client().withGrantType(grantType).withClientSecret(null).build()).rejected().withError("CLI-0004","A client secret is required.",UnprocessableEntityException.class);
		onCreate(client().withGrantType(grantType).withClientSecret("").build()).rejected().withError("CLI-0004","A client secret is required.",UnprocessableEntityException.class);
		onCreate(client().withGrantType(grantType).withClientSecret("    ").build()).rejected().withError("CLI-0004","A client secret is required.",UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientSecretIsLessThan10Characters() {
		onCreate(client().withGrantType(grantType).withClientSecret("ABC").build()).rejected().withError("CLI-0005","A client secret must be between 10 and 255 characters.",UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientSecretIsGreaterThan255Characters() {
		onCreate(client().withGrantType(grantType).withClientSecret(randomAlphabetic(256)).build()).rejected().withError("CLI-0005","A client secret must be between 10 and 255 characters.",UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientIdIsNullOrEmpty() {
		onCreate(client().withGrantType(grantType).withClientId(null).build()).rejected().withError("CLI-0001","A client id is required.",UnprocessableEntityException.class);
		onCreate(client().withGrantType(grantType).withClientId("").build()).rejected().withError("CLI-0001","A client id is required.",UnprocessableEntityException.class);
		onCreate(client().withGrantType(grantType).withClientId("    ").build()).rejected().withError("CLI-0001","A client id is required.",UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientClientIdIsLessThan10Characters() {
		onCreate(client().withGrantType(grantType).withClientId("ABC").build()).rejected().withError("CLI-0002","A client id must be between 10 and 128 characters.",UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientClientIdIsGreaterThan128Characters() {
		onCreate(client().withGrantType(grantType).withClientId(randomAlphabetic(129)).build()).rejected().withError("CLI-0002","A client id must be between 10 and 128 characters.",UnprocessableEntityException.class);
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

		onUpdate(updated).rejected().withError("CLI-0003","This client id is already in use.",ResourceConflictException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheSetOfGrantTypesIsNull() {
		Client client = client().build();
		ReflectionUtils.setValue(client, "grantTypes", null);
		onUpdate(client).rejected().withError("CLI-0006","A client requires at least one grant type.",UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheSetOfGrantTypesIsEmpty() {
		Client client = client().build();
		ReflectionUtils.setValue(client, "grantTypes", new HashSet<GrantType>());
		onUpdate(client).rejected().withError("CLI-0006","A client requires at least one grant type.",UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientSecretIsNullOrEmpty() {
		onUpdate(client().withGrantType(grantType).withClientSecret(null).build()).rejected().withError("CLI-0004","A client secret is required.",UnprocessableEntityException.class);
		onUpdate(client().withGrantType(grantType).withClientSecret("").build()).rejected().withError("CLI-0004","A client secret is required.",UnprocessableEntityException.class);
		onUpdate(client().withGrantType(grantType).withClientSecret("    ").build()).rejected().withError("CLI-0004","A client secret is required.",UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientSecretIsLessThan10Characters() {
		onUpdate(client().withGrantType(grantType).withClientSecret("ABC").build()).rejected().withError("CLI-0005","A client secret must be between 10 and 255 characters.",UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientSecretIsGreaterThan255Characters() {
		onUpdate(client().withGrantType(grantType).withClientSecret(randomAlphabetic(256)).build()).rejected().withError("CLI-0005","A client secret must be between 10 and 255 characters.",UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientIdIsNullOrEmpty() {
		onUpdate(client().withGrantType(grantType).withClientId(null).build()).rejected().withError("CLI-0001","A client id is required.",UnprocessableEntityException.class);
		onUpdate(client().withGrantType(grantType).withClientId("").build()).rejected().withError("CLI-0001","A client id is required.",UnprocessableEntityException.class);
		onUpdate(client().withGrantType(grantType).withClientId("    ").build()).rejected().withError("CLI-0001","A client id is required.",UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientClientIdIsLessThan10Characters() {
		onUpdate(client().withGrantType(grantType).withClientId("ABC").build()).rejected().withError("CLI-0002","A client id must be between 10 and 128 characters.",UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientClientIdIsGreaterThan128Characters() {
		onUpdate(client().withGrantType(grantType).withClientId(randomAlphabetic(129)).build()).rejected().withError("CLI-0002","A client id must be between 10 and 128 characters.",UnprocessableEntityException.class);
	}

}
