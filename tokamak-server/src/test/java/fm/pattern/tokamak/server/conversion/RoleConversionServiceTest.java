package fm.pattern.tokamak.server.conversion;

import static fm.pattern.tokamak.server.dsl.RoleDSL.role;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.tokamak.sdk.dsl.RoleDSL;
import fm.pattern.tokamak.sdk.model.RoleRepresentation;
import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.Role;

public class RoleConversionServiceTest extends IntegrationTest {

	@Autowired
	private RoleConversionService roleConversionService;

	@Test
	public void shouldBeAbleToConvertARoleIntoARoleRepresentation() {
		Role role = role().build();

		RoleRepresentation representation = roleConversionService.convert(role);
		assertThat(representation.getId()).isEqualTo(role.getId());
		assertThat(representation.getCreated()).isEqualTo(role.getCreated());
		assertThat(representation.getUpdated()).isEqualTo(role.getUpdated());
		assertThat(representation.getName()).isEqualTo(role.getName());
		assertThat(representation.getDescription()).isEqualTo(role.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertARoleRepresentationIntoANewRole() {
		RoleRepresentation representation = RoleDSL.role().build();

		Role role = roleConversionService.convert(representation);
		assertThat(role.getName()).isEqualTo(representation.getName());
		assertThat(role.getDescription()).isEqualTo(representation.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertARoleRepresentationIntoAnExistingRole() {
		RoleRepresentation representation = RoleDSL.role().build();
		Role role = role().build();

		Role updated = roleConversionService.convert(representation, role);
		assertThat(updated.getId()).isEqualTo(role.getId());
		assertThat(updated.getCreated()).isEqualTo(role.getCreated());
		assertThat(updated.getUpdated()).isEqualTo(role.getUpdated());
		assertThat(updated.getName()).isEqualTo(representation.getName());
		assertThat(updated.getDescription()).isEqualTo(representation.getDescription());
	}

}
