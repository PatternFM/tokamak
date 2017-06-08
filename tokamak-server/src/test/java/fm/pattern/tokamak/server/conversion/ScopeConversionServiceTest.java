package fm.pattern.tokamak.server.conversion;

import static fm.pattern.tokamak.server.dsl.ScopeDSL.scope;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.tokamak.sdk.dsl.ScopeDSL;
import fm.pattern.tokamak.sdk.model.ScopeRepresentation;
import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.Scope;

public class ScopeConversionServiceTest extends IntegrationTest {

	@Autowired
	private ScopeConversionService scopeConversionService;

	@Test
	public void shouldBeAbleToConvertAScopeIntoAScopeRepresentation() {
		Scope scope = scope().build();

		ScopeRepresentation representation = scopeConversionService.convert(scope);
		assertThat(representation.getId()).isEqualTo(scope.getId());
		assertThat(representation.getCreated()).isEqualTo(scope.getCreated());
		assertThat(representation.getUpdated()).isEqualTo(scope.getUpdated());
		assertThat(representation.getName()).isEqualTo(scope.getName());
		assertThat(representation.getDescription()).isEqualTo(scope.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertAScopeRepresentationIntoANewScope() {
		ScopeRepresentation representation = ScopeDSL.scope().build();

		Scope scope = scopeConversionService.convert(representation);
		assertThat(scope.getName()).isEqualTo(representation.getName());
		assertThat(scope.getDescription()).isEqualTo(representation.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertAScopeRepresentationIntoAnExistingScope() {
		ScopeRepresentation representation = ScopeDSL.scope().build();
		Scope scope = scope().build();

		Scope updated = scopeConversionService.convert(representation, scope);
		assertThat(updated.getId()).isEqualTo(scope.getId());
		assertThat(updated.getCreated()).isEqualTo(scope.getCreated());
		assertThat(updated.getUpdated()).isEqualTo(scope.getUpdated());
		assertThat(updated.getName()).isEqualTo(representation.getName());
		assertThat(updated.getDescription()).isEqualTo(representation.getDescription());
	}

}
