package fm.pattern.tokamak.server.validation;

import static fm.pattern.tokamak.server.dsl.AudienceDSL.audience;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.Audience;
import fm.pattern.valex.ResourceConflictException;
import fm.pattern.valex.UnprocessableEntityException;

public class AudienceValidationTest extends IntegrationTest {

	@Test
	public void shouldBeAbleToCreateAValidAudience() {
		onCreate(audience().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToCreateAnAudienceWhenTheAudienceNameIsNotProvided() {
		onCreate(audience().withName(null).build()).rejected().withError("AUD-0001", "An audience name is required.", UnprocessableEntityException.class);
		onCreate(audience().withName("").build()).rejected().withError("AUD-0001", "An audience name is required.", UnprocessableEntityException.class);
		onCreate(audience().withName("  ").build()).rejected().withError("AUD-0001", "An audience name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAnAudienceWhenTheAudienceNameIsGreaterThan128Characters() {
		onCreate(audience().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withError("AUD-0002", "An audience name cannot be greater than 128 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAnAudienceWhenTheAudienceDescriptionIsGreaterThan255Characters() {
		onCreate(audience().withDescription(RandomStringUtils.randomAlphabetic(256)).build()).rejected().withError("AUD-0004", "An audience description cannot be greater than 255 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToCreateAnAudienceWhenTheAudienceNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);

		audience().withName(name).save();
		onCreate(audience().withName(name).build()).rejected().withError("AUD-0003", "This audience name is already in use.", ResourceConflictException.class);
	}

	@Test
	public void shouldBeAbleToUpdateAValidAudience() {
		onUpdate(audience().build()).accepted();
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAudienceWhenTheAudienceNameIsNotProvided() {
		onUpdate(audience().withName(null).build()).rejected().withError("AUD-0001", "An audience name is required.", UnprocessableEntityException.class);
		onUpdate(audience().withName("").build()).rejected().withError("AUD-0001", "An audience name is required.", UnprocessableEntityException.class);
		onUpdate(audience().withName("  ").build()).rejected().withError("AUD-0001", "An audience name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAudienceWhenTheAudienceNameIsGreaterThan128Characters() {
		onUpdate(audience().withName(RandomStringUtils.randomAlphabetic(129)).build()).rejected().withError("AUD-0002", "An audience name cannot be greater than 128 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAudienceWhenTheAudienceDescriptionIsGreaterThan255Characters() {
		onUpdate(audience().withDescription(RandomStringUtils.randomAlphabetic(256)).build()).rejected().withError("AUD-0004", "An audience description cannot be greater than 255 characters.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAudienceWhenTheAudienceNameAlredyExists() {
		String name = RandomStringUtils.randomAlphanumeric(15);
		audience().withName(name).save();

		Audience audience = audience().save();
		audience.setName(name);

		onUpdate(audience).rejected().withError("AUD-0003", "This audience name is already in use.", ResourceConflictException.class);
	}

}
