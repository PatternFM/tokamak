package fm.pattern.tokamak.server.service;

import static fm.pattern.tokamak.server.PatternAssertions.assertThat;
import static fm.pattern.tokamak.server.dsl.AccountDSL.account;
import static fm.pattern.tokamak.server.dsl.RoleDSL.role;
import static fm.pattern.tokamak.server.repository.Criteria.criteria;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.Account;
import fm.pattern.tokamak.server.model.Role;
import fm.pattern.valex.EntityNotFoundException;
import fm.pattern.valex.Result;
import fm.pattern.valex.UnprocessableEntityException;

public class AccountServiceIntegrationTest extends IntegrationTest {

	@Autowired
	private AccountService accountService;

	@Autowired
	private PasswordEncodingService passwordEncodingService;

	@Test
	public void shouldBeAbleToCreateAnAccount() {
		String password = "csli2i3R83lsjasi%%";
		Account account = account().withUsername("email@address.com").withPassword(password).build();

		Result<Account> result = accountService.create(account);
		assertThat(result).accepted();

		Account created = result.getInstance();
		assertThat(created.getId()).isNotNull();
		assertThat(created.getCreated()).isNotNull();
		assertThat(created.getUpdated()).isNotNull();
		assertThat(created.getCreated()).isEqualTo(created.getUpdated());
		assertThat(created.isLocked()).isFalse();
		assertThat(created.getUsername()).isEqualTo("email@address.com");
		assertThat(created.getPassword()).isNotNull();
		assertThat(passwordEncodingService.matches(password, created.getPassword())).isTrue();
	}

	@Test
	public void shouldNotBeAbleToCreateAnInvalidAccount() {
		Account account = account().withUsername(null).withPassword("password").build();
		assertThat(accountService.create(account)).rejected().withMessage("An account username is required.");
	}

	@Test
	public void shouldBeAbleToUpdateAnAccount() {
		Account account = account().save();
		account.setLocked(true);

		Result<Account> result = accountService.update(account);
		assertThat(result).accepted();
		assertThat(result.getInstance().isLocked()).isTrue();
	}

	@Test
	public void shouldBeAbleToUpdateAnAccountWithRoles() {
		Role role1 = role().save();
		Role role2 = role().save();
		Role role3 = role().save();

		Account account = account().withRole(role1).withRole(role2).withRole(role3).save();
		account.setRoles(new HashSet<>(Arrays.asList(role2)));

		Result<Account> result = accountService.update(account);
		assertThat(result).accepted();
		assertThat(result.getInstance().getRoles()).hasSize(1);
		assertThat(result.getInstance().getRoles()).contains(role2);
	}

	@Test
	public void shouldNotBeAbleToUpdateAnInvalidAccount() {
		Account account = account().save();
		account.setUsername(null);

		Result<Account> result = accountService.update(account);
		assertThat(result).rejected().withMessage("An account username is required.");
	}

	@Test
	public void shouldBeAbleToDeleteAnAccount() {
		Account account = account().save();
		assertThat(accountService.findById(account.getId())).accepted();

		assertThat(accountService.delete(account)).accepted();
		assertThat(accountService.findById(account.getId())).rejected().withMessage("No such account id: " + account.getId());
	}

	@Test
	public void shouldEncryptTheAccountPasswordBeforeSavingTheAccount() {
		String password = "pArls345axbd!!";
		
		Account account = account().withPassword(password).save();
		assertThat(account.getPassword()).startsWith("$2a$");
		assertThat(passwordEncodingService.matches(password, account.getPassword())).isTrue();
	}

	@Test
	public void shouldBeAbleToFindAnAccountById() {
		Account account = account().save();

		Result<Account> result = accountService.findById(account.getId());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(account);
	}

	@Test
	public void shouldNotBeAbleToFindAnAccountByIdIfTheAccountIdIsNullOrEmpty() {
		assertThat(accountService.findById(null)).rejected().withError("ACC-0006", "An account id is required.", UnprocessableEntityException.class);
		assertThat(accountService.findById("")).rejected().withError("ACC-0006", "An account id is required.", UnprocessableEntityException.class);
		assertThat(accountService.findById("  ")).rejected().withError("ACC-0006", "An account id is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToFindAnAccountByIdIfTheAccountIdDoesNotExist() {
		assertThat(accountService.findById("csrx")).rejected().withError("SYS-0001", "No such account id: csrx", EntityNotFoundException.class);
	}

	@Test
	public void shouldBeAbleToFindAnAccountByUsername() {
		Account account = account().save();

		Result<Account> result = accountService.findByUsername(account.getUsername());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(account);
	}

	@Test
	public void shouldNotBeAbleToFindAnAccountByUsernameIfTheUsernameIsNull() {
		assertThat(accountService.findByUsername(null)).rejected().withError("ACC-0001", "An account username is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToFindAnAccountByIdIfTheUsernameIsInvalid() {
		assertThat(accountService.findByUsername("csrx")).rejected().withError("ACC-0008", "No such username: csrx", EntityNotFoundException.class);
	}

	@Test
	public void shouldBeAbleToUpdateAPassword() {
		String currentPassword = "myOLDPassword2!";
		String newPassword = "myNEWPassword2!";

		Account account = account().withUsername("test@email.com").withPassword(currentPassword).save();
		assertThat(accountService.updatePassword(account, currentPassword, newPassword)).accepted();
		assertThat(accountService.updatePassword(account, newPassword)).accepted();
		
		assertAccountHasPassword("test@email.com", newPassword);
	}

	@Test
	public void shouldNotBeAbleToUpdateAPasswordWhenTheCurrentPasswordIsNotProvided() {
		Account account = account().withPassword("myOLDPassword12!").save();
		
		assertThat(accountService.updatePassword(account, null, "ABC")).rejected().withMessage("The current password is required.");
		assertThat(accountService.updatePassword(account, "", "ABC")).rejected().withMessage("The current password is required.");
		assertThat(accountService.updatePassword(account, "  ", "ABC")).rejected().withMessage("The current password is required.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAPasswordWhenTheCurrentPasswordDoesNotMatchTheProvidedPassword() {
		Account account = account().withPassword("myOLDPassword2!").save();
		assertThat(accountService.updatePassword(account, "myOLDPassword3!", "ABC")).rejected().withMessage("The password provided does not match your current password. Please try again.");
	}

	@Test
	public void shouldNotBeAbleToUpdateAPasswordWhenTheNewPasswordDoesNotMeetPolicyRequirements() {
		String currentPassword = "myOLDPassword2!";
		String newPassword = "pass!";

		Account account = account().withUsername("test@email.com").withPassword(currentPassword).save();
		assertThat(accountService.updatePassword(account, currentPassword, newPassword)).rejected();
		assertThat(accountService.updatePassword(account, newPassword)).rejected();
	}
	
	@Test
	public void shouldBeAbleToListAccounts() {
		IntStream.range(1, 5).forEach(i -> account().save());

		Result<List<Account>> result = accountService.list(criteria());
		assertThat(result).accepted();

		List<Account> accounts = result.getInstance();
		assertThat(accounts.size()).isGreaterThanOrEqualTo(5);
		
		assertThat(accountService.list(criteria().limit(1)).getInstance().size()).isEqualTo(1);
		assertThat(accountService.list(criteria().limit(1).page(3)).getInstance().size()).isEqualTo(1);
	}

	private void assertAccountHasPassword(String username, String expectedPassword) {
		String actualPassword = accountService.findByUsername(username).getInstance().getPassword();
		passwordEncodingService.matches(expectedPassword, actualPassword);
	}

}
