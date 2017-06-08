package fm.pattern.tokamak.server.conversion;

import static fm.pattern.tokamak.server.dsl.AudienceDSL.audience;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.tokamak.sdk.dsl.AudienceDSL;
import fm.pattern.tokamak.sdk.model.AudienceRepresentation;
import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.Audience;

public class AudienceConversionServiceTest extends IntegrationTest {

	@Autowired
	private AudienceConversionService audienceConversionService;

	@Test
	public void shouldBeAbleToConvertAAudienceIntoAAudienceRepresentation() {
		Audience audience = audience().build();

		AudienceRepresentation representation = audienceConversionService.convert(audience);
		assertThat(representation.getId()).isEqualTo(audience.getId());
		assertThat(representation.getCreated()).isEqualTo(audience.getCreated());
		assertThat(representation.getUpdated()).isEqualTo(audience.getUpdated());
		assertThat(representation.getName()).isEqualTo(audience.getName());
		assertThat(representation.getDescription()).isEqualTo(audience.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertAAudienceRepresentationIntoANewAudience() {
		AudienceRepresentation representation = AudienceDSL.audience().build();

		Audience audience = audienceConversionService.convert(representation);
		assertThat(audience.getName()).isEqualTo(representation.getName());
		assertThat(audience.getDescription()).isEqualTo(representation.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertAAudienceRepresentationIntoAnExistingAudience() {
		AudienceRepresentation representation = AudienceDSL.audience().build();
		Audience audience = audience().build();

		Audience updated = audienceConversionService.convert(representation, audience);
		assertThat(updated.getId()).isEqualTo(audience.getId());
		assertThat(updated.getCreated()).isEqualTo(audience.getCreated());
		assertThat(updated.getUpdated()).isEqualTo(audience.getUpdated());
		assertThat(updated.getName()).isEqualTo(representation.getName());
		assertThat(updated.getDescription()).isEqualTo(representation.getDescription());
	}

}
