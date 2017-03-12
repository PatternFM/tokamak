package fm.pattern.jwt.server.service;

import static fm.pattern.jwt.server.PatternAssertions.assertThat;
import static fm.pattern.jwt.server.dsl.AudienceDSL.audience;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.Audience;
import fm.pattern.valex.EntityNotFoundException;
import fm.pattern.valex.Result;
import fm.pattern.valex.UnprocessableEntityException;

public class AudienceServiceIntegrationTest extends IntegrationTest {

	@Autowired
	private AudienceService audienceService;

	@Test
	public void shouldBeAbleToCreateAnAudience() {
		Audience audience = new Audience("user");

		Result<Audience> result = audienceService.create(audience);
		assertThat(result).accepted();

		Audience created = result.getInstance();
		assertThat(created.getName()).isEqualTo(audience.getName());
		assertThat(created.getId()).isNotNull();
		assertThat(created.getCreated()).isNotNull();
		assertThat(created.getUpdated()).isNotNull();
		assertThat(created.getCreated()).isEqualTo(created.getUpdated());
	}

	@Test
	public void shouldNotBeAbleToCreateAnAudienceIfTheAudienceIsInvalid() {
		Audience audience = audience().withName(null).build();
		assertThat(audienceService.create(audience)).rejected().withMessage("An audience name is required.");
	}

	@Test
	public void shouldBeAbleToUpdateAnAudience() {
		Audience audience = audience().thatIs().persistent().build();
		audience.setName("first");

		assertThat(audienceService.update(audience)).accepted();

		Audience found = audienceService.findById(audience.getId()).getInstance();
		assertThat(found.getName()).isEqualTo("first");
	}

	@Test
	public void shouldNotBeAbleToUpdateAnAudienceIfTheAudienceIsInvalid() {
		Audience audience = audience().thatIs().persistent().build();
		audience.setName(null);

		assertThat(audienceService.update(audience)).rejected().withMessage("An audience name is required.");
	}

	@Test
	public void shouldBeAbleToDeleteAnAudience() {
		Audience audience = audience().thatIs().persistent().build();
		assertThat(audienceService.findById(audience.getId())).accepted();

		assertThat(audienceService.delete(audience)).accepted();
		assertThat(audienceService.findById(audience.getId())).rejected();
	}

	@Test
	public void shouldBeAbleToFindAnAudienceById() {
		Audience audience = audience().thatIs().persistent().build();

		Result<Audience> result = audienceService.findById(audience.getId());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(audience);
	}

	@Test
	public void shouldNotBeAbleToFindAnAudienceByIdIfTheAudienceIdIsNullOrEmpty() {
		assertThat(audienceService.findById(null)).rejected().withError("AUD-0006", "An audience id is required.", UnprocessableEntityException.class);
		assertThat(audienceService.findById("")).rejected().withError("AUD-0006", "An audience id is required.", UnprocessableEntityException.class);
		assertThat(audienceService.findById("  ")).rejected().withError("AUD-0006", "An audience id is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToFindAnAudienceByIdIfTheAudienceIdDoesNotExist() {
		assertThat(audienceService.findById("csrx")).rejected().withError("SYS-0001", "No such audience id: csrx", EntityNotFoundException.class);
	}

	@Test
	public void shouldBeAbleToFindAnAudienceByName() {
		Audience audience = audience().thatIs().persistent().build();

		Result<Audience> result = audienceService.findByName(audience.getName());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(audience);
	}

	@Test
	public void shouldNotBeAbleToFindAnAudienceByNameIfTheAudienceNameIsNullOrEmpty() {
		assertThat(audienceService.findByName(null)).rejected().withError("AUD-0001", "An audience name is required.", UnprocessableEntityException.class);
		assertThat(audienceService.findByName("")).rejected().withError("AUD-0001", "An audience name is required.", UnprocessableEntityException.class);
		assertThat(audienceService.findByName("  ")).rejected().withError("AUD-0001", "An audience name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToFindAnAudienceByNameIfTheAudienceNameDoesNotExist() {
		assertThat(audienceService.findByName("csrx")).rejected().withError("AUD-0008", "No such audience name: csrx", EntityNotFoundException.class);
	}

	@Test
	public void shouldBeAbleToListAllAuthorities() {
		range(0, 5).forEach(i -> audience().thatIs().persistent().build());

		Result<List<Audience>> result = audienceService.list();
		assertThat(result).accepted();
		assertThat(result.getInstance().size()).isGreaterThanOrEqualTo(5);
	}

}
