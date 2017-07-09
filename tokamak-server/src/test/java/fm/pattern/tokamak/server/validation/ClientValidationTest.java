package fm.pattern.tokamak.server.validation;

import static fm.pattern.tokamak.server.dsl.ClientDSL.client;
import static fm.pattern.tokamak.server.dsl.GrantTypeDSL.grantType;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.Client;
import fm.pattern.tokamak.server.model.GrantType;
import fm.pattern.valex.ResourceConflictException;
import fm.pattern.valex.UnprocessableEntityException;

public class ClientValidationTest extends IntegrationTest {

	private GrantType grantType;

	@Before
	public void before() {
		this.grantType = grantType().save();
	}

	@Test
	public void shouldBeAbleToCreateAClient() {
		onCreate(client().withGrantType(grantType).build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToCreateAClientIfTheClientIdAlreadyExists() {
		String clientId = RandomStringUtils.randomAlphanumeric(15);
		client().withClientId(clientId).withGrantType(grantType).save();

		onCreate(client().withClientId(clientId).withGrantType(grantType).build()).rejected().withError("CLI-0003", "This client id is already in use.", ResourceConflictException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheSetOfGrantTypesIsEmpty() {
		Client client = client().build();
		onCreate(client).rejected().withError("CLI-0006", "A client requires at least one grant type.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientSecretIsNullOrEmpty() {
		onCreate(client().withGrantType(grantType).withClientSecret(null).build()).rejected().withError("CLI-0004", "A client secret is required.", UnprocessableEntityException.class);
		onCreate(client().withGrantType(grantType).withClientSecret("").build()).rejected().withError("CLI-0004", "A client secret is required.", UnprocessableEntityException.class);
		onCreate(client().withGrantType(grantType).withClientSecret("    ").build()).rejected().withError("CLI-0004", "A client secret is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientSecretIsLessThan10Characters() {
		onCreate(client().withGrantType(grantType).withClientSecret("ABC").build()).rejected().withError("CLI-0005", "A client secret must be between 10 and 255 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientSecretIsGreaterThan255Characters() {
		onCreate(client().withGrantType(grantType).withClientSecret(randomAlphabetic(256)).build()).rejected().withError("CLI-0005", "A client secret must be between 10 and 255 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientIdIsNullOrEmpty() {
		onCreate(client().withGrantType(grantType).withClientId(null).build()).rejected().withError("CLI-0001", "A client id is required.", UnprocessableEntityException.class);
		onCreate(client().withGrantType(grantType).withClientId("").build()).rejected().withError("CLI-0001", "A client id is required.", UnprocessableEntityException.class);
		onCreate(client().withGrantType(grantType).withClientId("    ").build()).rejected().withError("CLI-0001", "A client id is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientIdIsLessThan10Characters() {
		onCreate(client().withGrantType(grantType).withClientId("ABC").build()).rejected().withError("CLI-0002", "A client id must be between 10 and 128 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientIdIsGreaterThan128Characters() {
		onCreate(client().withGrantType(grantType).withClientId(randomAlphabetic(129)).build()).rejected().withError("CLI-0002", "A client id must be between 10 and 128 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientNameIsNullOrEmpty() {
		onCreate(client().withGrantType(grantType).withName(null).build()).rejected().withError("CLI-0011", "A client name is required.", UnprocessableEntityException.class);
		onCreate(client().withGrantType(grantType).withName("").build()).rejected().withError("CLI-0011", "A client name is required.", UnprocessableEntityException.class);
		onCreate(client().withGrantType(grantType).withName("    ").build()).rejected().withError("CLI-0011", "A client name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientNameIsGreaterThan50Characters() {
		onCreate(client().withGrantType(grantType).withName(randomAlphabetic(51)).build()).rejected().withError("CLI-0010", "A client name cannot be greater than 50 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheClientDescriptionIsGreaterThan255Characters() {
		onCreate(client().withGrantType(grantType).withDescription(randomAlphabetic(256)).build()).rejected().withError("CLI-0013", "A client description cannot be greater than 255 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAClientWhenTheRedirectUriIsGreaterThan255Characters() {
		onCreate(client().withGrantType(grantType).withRedirectUri(randomAlphabetic(256)).build()).rejected().withError("CLI-0012", "A client redirect URI must be less than 255 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldBeAbleToUpdateAClient() {
		onUpdate(client().withGrantType(grantType).build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientIfTheClientIdAlreadyExists() {
		client().withClientId("firstclient").withGrantType(grantType).save();

		Client updated = client().withClientId("secondclient").withGrantType(grantType).save();
		updated.setClientId("firstclient");

		onUpdate(updated).rejected().withError("CLI-0003", "This client id is already in use.", ResourceConflictException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheSetOfGrantTypesIsEmpty() {
		Client client = client().build();
		onUpdate(client).rejected().withError("CLI-0006", "A client requires at least one grant type.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientSecretIsNullOrEmpty() {
		onUpdate(client().withGrantType(grantType).withClientSecret(null).build()).rejected().withError("CLI-0004", "A client secret is required.", UnprocessableEntityException.class);
		onUpdate(client().withGrantType(grantType).withClientSecret("").build()).rejected().withError("CLI-0004", "A client secret is required.", UnprocessableEntityException.class);
		onUpdate(client().withGrantType(grantType).withClientSecret("    ").build()).rejected().withError("CLI-0004", "A client secret is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientSecretIsLessThan10Characters() {
		onUpdate(client().withGrantType(grantType).withClientSecret("ABC").build()).rejected().withError("CLI-0005", "A client secret must be between 10 and 255 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientSecretIsGreaterThan255Characters() {
		onUpdate(client().withGrantType(grantType).withClientSecret(randomAlphabetic(256)).build()).rejected().withError("CLI-0005", "A client secret must be between 10 and 255 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientIdIsNullOrEmpty() {
		onUpdate(client().withGrantType(grantType).withClientId(null).build()).rejected().withError("CLI-0001", "A client id is required.", UnprocessableEntityException.class);
		onUpdate(client().withGrantType(grantType).withClientId("").build()).rejected().withError("CLI-0001", "A client id is required.", UnprocessableEntityException.class);
		onUpdate(client().withGrantType(grantType).withClientId("    ").build()).rejected().withError("CLI-0001", "A client id is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientClientIdIsLessThan10Characters() {
		onUpdate(client().withGrantType(grantType).withClientId("ABC").build()).rejected().withError("CLI-0002", "A client id must be between 10 and 128 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientClientIdIsGreaterThan128Characters() {
		onUpdate(client().withGrantType(grantType).withClientId(randomAlphabetic(129)).build()).rejected().withError("CLI-0002", "A client id must be between 10 and 128 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientNameIsNullOrEmpty() {
		onUpdate(client().withGrantType(grantType).withName(null).build()).rejected().withError("CLI-0011", "A client name is required.", UnprocessableEntityException.class);
		onUpdate(client().withGrantType(grantType).withName("").build()).rejected().withError("CLI-0011", "A client name is required.", UnprocessableEntityException.class);
		onUpdate(client().withGrantType(grantType).withName("    ").build()).rejected().withError("CLI-0011", "A client name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientNameIsGreaterThan50Characters() {
		onUpdate(client().withGrantType(grantType).withName(randomAlphabetic(51)).build()).rejected().withError("CLI-0010", "A client name cannot be greater than 50 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheClientDescriptionIsGreaterThan255Characters() {
		onUpdate(client().withGrantType(grantType).withDescription(randomAlphabetic(256)).build()).rejected().withError("CLI-0013", "A client description cannot be greater than 255 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAClientWhenTheRedirectUriIsGreaterThan255Characters() {
		onUpdate(client().withGrantType(grantType).withRedirectUri(randomAlphabetic(256)).build()).rejected().withError("CLI-0012", "A client redirect URI must be less than 255 characters.", UnprocessableEntityException.class);
	}

}
