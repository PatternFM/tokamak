package fm.pattern.tokamak.server.conversion;

import static fm.pattern.tokamak.server.dsl.GrantTypeDSL.grantType;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.tokamak.sdk.dsl.GrantTypeDSL;
import fm.pattern.tokamak.sdk.model.GrantTypeRepresentation;
import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.GrantType;

public class GrantTypeConversionServiceTest extends IntegrationTest {

	@Autowired
	private GrantTypeConversionService grantTypeConversionService;

	@Test
	public void shouldBeAbleToConvertAGrantTypeIntoAGrantTypeRepresentation() {
		GrantType grantType = grantType().build();

		GrantTypeRepresentation representation = grantTypeConversionService.convert(grantType);
		assertThat(representation.getId()).isEqualTo(grantType.getId());
		assertThat(representation.getCreated()).isEqualTo(grantType.getCreated());
		assertThat(representation.getUpdated()).isEqualTo(grantType.getUpdated());
		assertThat(representation.getName()).isEqualTo(grantType.getName());
		assertThat(representation.getDescription()).isEqualTo(grantType.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertAGrantTypeRepresentationIntoANewGrantType() {
		GrantTypeRepresentation representation = GrantTypeDSL.grantType().build();

		GrantType grantType = grantTypeConversionService.convert(representation);
		assertThat(grantType.getName()).isEqualTo(representation.getName());
		assertThat(grantType.getDescription()).isEqualTo(representation.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertAGrantTypeRepresentationIntoAnExistingGrantType() {
		GrantTypeRepresentation representation = GrantTypeDSL.grantType().build();
		GrantType grantType = grantType().build();

		GrantType updated = grantTypeConversionService.convert(representation, grantType);
		assertThat(updated.getId()).isEqualTo(grantType.getId());
		assertThat(updated.getCreated()).isEqualTo(grantType.getCreated());
		assertThat(updated.getUpdated()).isEqualTo(grantType.getUpdated());
		assertThat(updated.getName()).isEqualTo(representation.getName());
		assertThat(updated.getDescription()).isEqualTo(representation.getDescription());
	}

}
