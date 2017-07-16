package fm.pattern.tokamak.server.validation;

import static fm.pattern.tokamak.server.dsl.PasswordPolicyDSL.passwordPolicy;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.valex.ResourceConflictException;
import fm.pattern.valex.UnprocessableEntityException;

public class PasswordPolicyValidationTest extends IntegrationTest {

	@Test
	public void shouldBeAbleToCreateAValidPasswordPolicy() {
		onCreate(passwordPolicy().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToCreateAPasswordPolicyWhenThePolicyNameIsNotProvided() {
		onCreate(passwordPolicy().withName(null).build()).rejected().withError("POL-0002", "A policy name is required.", UnprocessableEntityException.class);
		onCreate(passwordPolicy().withName("").build()).rejected().withError("POL-0002", "A policy name is required.", UnprocessableEntityException.class);
		onCreate(passwordPolicy().withName("  ").build()).rejected().withError("POL-0002", "A policy name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAPasswordPolicyWhenThePolicyNameIsGreaterThan30Characters() {
		onCreate(passwordPolicy().withName(RandomStringUtils.randomAlphabetic(31)).build()).rejected().withError("POL-0003", "A policy name cannot be greater than 30 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAPasswordPolicyWhenThePasswordPolicyDescriptionIsGreaterThan255Characters() {
		onCreate(passwordPolicy().withDescription(RandomStringUtils.randomAlphabetic(256)).build()).rejected().withError("POL-0004", "A policy description cannot be greater than 255 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAPasswordPolicyWhenThePasswordPolicyNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);

		passwordPolicy().withName(name).save();
		onCreate(passwordPolicy().withName(name).build()).rejected().withError("POL-0001", "This policy name is already in use.", ResourceConflictException.class);
	}

	@Test
	public void shouldNotBeABleToCreateAPasswordPolicyWhenTheMinimumPasswordLengthIsNotProvided() {
		onCreate(passwordPolicy().withMinLength(null).build()).rejected().withError("POL-0005", "A password minimum length is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeABleToCreateAPasswordPolicyWhenTheMinimumPasswordLengthIsTooSmall() {
		onCreate(passwordPolicy().withMinLength(0).build()).rejected().withError("POL-0006", "A password minimum length must be greater than 0.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeABleToCreateAPasswordPolicyWhenTheMinimumPasswordLengthIsTooLarge() {
		onCreate(passwordPolicy().withMinLength(256).build()).rejected().withError("POL-0007", "A password minimum length must be less than 255.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldBeAbleToUpdateAValidPasswordPolicy() {
		onUpdate(passwordPolicy().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateAPasswordPolicyWhenThePolicyNameIsNotProvided() {
		onUpdate(passwordPolicy().withName(null).build()).rejected().withError("POL-0002", "A policy name is required.", UnprocessableEntityException.class);
		onUpdate(passwordPolicy().withName("").build()).rejected().withError("POL-0002", "A policy name is required.", UnprocessableEntityException.class);
		onUpdate(passwordPolicy().withName("  ").build()).rejected().withError("POL-0002", "A policy name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAPasswordPolicyWhenThePolicyNameIsGreaterThan30Characters() {
		onUpdate(passwordPolicy().withName(RandomStringUtils.randomAlphabetic(31)).build()).rejected().withError("POL-0003", "A policy name cannot be greater than 30 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAPasswordPolicyWhenThePasswordPolicyDescriptionIsGreaterThan255Characters() {
		onUpdate(passwordPolicy().withDescription(RandomStringUtils.randomAlphabetic(256)).build()).rejected().withError("POL-0004", "A policy description cannot be greater than 255 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAPasswordPolicyWhenThePasswordPolicyNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);

		passwordPolicy().withName(name).save();
		onUpdate(passwordPolicy().withName(name).build()).rejected().withError("POL-0001", "This policy name is already in use.", ResourceConflictException.class);
	}

	@Test
	public void shouldNotBeABleToUpdateAPasswordPolicyWhenTheMinimumPasswordLengthIsNotProvided() {
		onUpdate(passwordPolicy().withMinLength(null).build()).rejected().withError("POL-0005", "A password minimum length is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeABleToUpdateAPasswordPolicyWhenTheMinimumPasswordLengthIsTooSmall() {
		onUpdate(passwordPolicy().withMinLength(0).build()).rejected().withError("POL-0006", "A password minimum length must be greater than 0.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeABleToUpdateAPasswordPolicyWhenTheMinimumPasswordLengthIsTooLarge() {
		onUpdate(passwordPolicy().withMinLength(256).build()).rejected().withError("POL-0007", "A password minimum length must be less than 255.", UnprocessableEntityException.class);
	}

}
