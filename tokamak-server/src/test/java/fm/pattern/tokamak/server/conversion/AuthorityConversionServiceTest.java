package fm.pattern.tokamak.server.conversion;

import static fm.pattern.tokamak.server.dsl.AuthorityDSL.authority;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.tokamak.sdk.dsl.AuthorityDSL;
import fm.pattern.tokamak.sdk.model.AuthorityRepresentation;
import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.Authority;

public class AuthorityConversionServiceTest extends IntegrationTest {

	@Autowired
	private AuthorityConversionService authorityConversionService;

	@Test
	public void shouldBeAbleToConvertAAuthorityIntoAAuthorityRepresentation() {
		Authority authority = authority().build();

		AuthorityRepresentation representation = authorityConversionService.convert(authority);
		assertThat(representation.getId()).isEqualTo(authority.getId());
		assertThat(representation.getCreated()).isEqualTo(authority.getCreated());
		assertThat(representation.getUpdated()).isEqualTo(authority.getUpdated());
		assertThat(representation.getName()).isEqualTo(authority.getName());
		assertThat(representation.getDescription()).isEqualTo(authority.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertAAuthorityRepresentationIntoANewAuthority() {
		AuthorityRepresentation representation = AuthorityDSL.authority().build();

		Authority authority = authorityConversionService.convert(representation);
		assertThat(authority.getName()).isEqualTo(representation.getName());
		assertThat(authority.getDescription()).isEqualTo(representation.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertAAuthorityRepresentationIntoAnExistingAuthority() {
		AuthorityRepresentation representation = AuthorityDSL.authority().build();
		Authority authority = authority().build();

		Authority updated = authorityConversionService.convert(representation, authority);
		assertThat(updated.getId()).isEqualTo(authority.getId());
		assertThat(updated.getCreated()).isEqualTo(authority.getCreated());
		assertThat(updated.getUpdated()).isEqualTo(authority.getUpdated());
		assertThat(updated.getName()).isEqualTo(representation.getName());
		assertThat(updated.getDescription()).isEqualTo(representation.getDescription());
	}

}
