package fm.pattern.tokamak.server.service;

import static fm.pattern.tokamak.server.PatternAssertions.assertThat;
import static fm.pattern.tokamak.server.dsl.PasswordPolicyDSL.passwordPolicy;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.PasswordPolicy;
import fm.pattern.valex.EntityNotFoundException;
import fm.pattern.valex.Result;
import fm.pattern.valex.UnprocessableEntityException;

public class PasswordPolicyServiceIntegrationTest extends IntegrationTest {

	@Autowired
	private PasswordPolicyService passwordPolicyService;

	@Test
	public void shouldBeAbleToFindAPasswordPolicyById() {
		PasswordPolicy policy = passwordPolicy().save();

		Result<PasswordPolicy> result = passwordPolicyService.findById(policy.getId());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(policy);
	}

	@Test
	public void shouldNotBeAbleToFindAPasswordPolicyByIdIfThePasswordPolicyIdIsNullOrEmpty() {
		assertThat(passwordPolicyService.findById(null)).rejected().withError("POL-1000", "A password policy id is required.", UnprocessableEntityException.class);
		assertThat(passwordPolicyService.findById("")).rejected().withError("POL-1000", "A password policy id is required.", UnprocessableEntityException.class);
		assertThat(passwordPolicyService.findById("  ")).rejected().withError("POL-1000", "A password policy id is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToFindAPasswordPolicyByIdIfThePasswordPolicyIdDoesNotExist() {
		assertThat(passwordPolicyService.findById("csrx")).rejected().withError("SYS-0001", "No such passwordpolicy id: csrx", EntityNotFoundException.class);
	}

	@Test
	public void shouldBeAbleToFindAPasswordPolicyByName() {
		PasswordPolicy passwordPolicy = passwordPolicy().save();

		Result<PasswordPolicy> result = passwordPolicyService.findByName(passwordPolicy.getName());
		assertThat(result).accepted();
		assertThat(result.getInstance()).isEqualToComparingFieldByField(passwordPolicy);
	}

	@Test
	public void shouldNotBeAbleToFindAPasswordPolicyByNameIfTheNameIsNull() {
		assertThat(passwordPolicyService.findByName(null)).rejected().withError("POL-0002", "A policy name is required.", UnprocessableEntityException.class);
	}

	@Test
	public void shouldNotBeAbleToFindAPasswordPolicyByIdIfTheNameIsInvalid() {
		assertThat(passwordPolicyService.findByName("csrx")).rejected().withError("POL-0008", "No such policy name: csrx", EntityNotFoundException.class);
	}

	@Test
	public void shouldBeAbleToListAllPasswordPolicies() {
		assertThat(passwordPolicyService.list()).accepted();
	}

}
