package fm.pattern.tokamak.server.conversion;

import static fm.pattern.tokamak.server.dsl.AccountDSL.account;
import static fm.pattern.tokamak.server.dsl.RoleDSL.role;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.tokamak.sdk.dsl.AccountDSL;
import fm.pattern.tokamak.sdk.model.AccountRepresentation;
import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.Account;
import fm.pattern.tokamak.server.model.Role;

public class AccountConversionServiceTest extends IntegrationTest {

	@Autowired
	private AccountConversionService accountConversionService;

	@Autowired
	private RoleConversionService roleConversionService;

	@Test
	public void shouldBeAbleToConvertAnAccountIntoAnAccountRepresentation() {
		Account account = account().withRole(role().build()).build();

		AccountRepresentation representation = accountConversionService.convert(account);
		assertThat(representation.getId()).isEqualTo(account.getId());
		assertThat(representation.getCreated()).isEqualTo(account.getCreated());
		assertThat(representation.getUpdated()).isEqualTo(account.getUpdated());
		assertThat(representation.getUsername()).isEqualTo(account.getUsername());
		assertThat(representation.getPassword()).isNull();
		assertThat(representation.getRoles()).hasSize(1);
	}

	@Test
	public void shouldBeAbleToConvertAnAccountRepresentationIntoANewAccount() {
		Role role = role().save();

		AccountRepresentation representation = AccountDSL.account().withRoles(roleConversionService.convert(role)).build();

		Account account = accountConversionService.convert(representation);
		assertThat(account.getUsername()).isEqualTo(representation.getUsername());
		assertThat(account.getPassword()).isEqualTo(representation.getPassword());
		assertThat(account.getRoles()).hasSize(1);
		assertThat(account.getRoles()).contains(role);
	}

	@Test
	public void shouldBeAbleToConvertAnAccountRepresentationIntoAnExistingAccount() {
		Role role1 = role().save();
		Role role2 = role().save();
		Role role3 = role().save();

		AccountRepresentation representation = AccountDSL.account().withUsername("foo").withPassword("foo").withRoles(roleConversionService.convert(role3)).build();
		Account account = account().withRole(role1).withRole(role2).build();

		Account updated = accountConversionService.convert(representation, account);
		assertThat(updated.getId()).isEqualTo(account.getId());
		assertThat(updated.getCreated()).isEqualTo(account.getCreated());
		assertThat(updated.getUpdated()).isEqualTo(account.getUpdated());
		
		assertThat(updated.getRoles()).hasSize(1);
		assertThat(updated.getRoles()).contains(role3);

		assertThat(updated.getUsername()).isEqualTo(account.getUsername());
		assertThat(updated.getPassword()).isEqualTo(account.getPassword());
		assertThat(updated.isLocked()).isEqualTo(account.isLocked());
	}

}
