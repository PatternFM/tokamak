package fm.pattern.tokamak.server.validation;

import static fm.pattern.tokamak.server.ResultAssertions.assertThat;
import static fm.pattern.tokamak.server.dsl.PasswordPolicyDSL.passwordPolicy;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.PasswordPolicy;
import fm.pattern.tokamak.server.security.PasswordValidator;
import fm.pattern.valex.UnprocessableEntityException;

public class PasswordValidatorTest extends IntegrationTest {

	@Autowired
	private PasswordValidator validator;

	@Test
	public void shouldBeAbleToValidateAPassword() {
		PasswordPolicy policy = passwordPolicy().build();
		assertThat(validator.validate("password", policy)).accepted();
	}

	@Test
	public void shouldFailValidationWhenAnUppercaseCharacterIsRequired() {
		PasswordPolicy policy = passwordPolicy().requireUppercaseCharacter(true).build();

		assertThat(validator.validate("lowercase", policy)).rejected().withError("PWD-0001", "At least one uppercase character is required.", UnprocessableEntityException.class);
		assertThat(validator.validate("oneUpper", policy)).accepted();
		assertThat(validator.validate("Upper", policy)).accepted();
		assertThat(validator.validate("uppeR", policy)).accepted();
	}

	@Test
	public void shouldFailValidationWhenALowercaseCharacterIsRequired() {
		PasswordPolicy policy = passwordPolicy().requireLowercaseCharacter(true).build();

		assertThat(validator.validate("UPPERCASE", policy)).rejected().withError("PWD-0002", "At least one lowercase character is required.", UnprocessableEntityException.class);
		assertThat(validator.validate("ONElOWER", policy)).accepted();
		assertThat(validator.validate("lOWER", policy)).accepted();
		assertThat(validator.validate("LOWEr", policy)).accepted();
	}

	@Test
	public void shouldFailValidationWhenASpecialCharacterIsRequired() {
		PasswordPolicy policy = passwordPolicy().requireSpecialCharacter(true).build();

		assertThat(validator.validate("nospecial", policy)).rejected().withError("PWD-0003", "At least one special character is required.", UnprocessableEntityException.class);
		assertThat(validator.validate("one!special", policy)).accepted();
		assertThat(validator.validate("one@special", policy)).accepted();
		assertThat(validator.validate("one#special", policy)).accepted();
		assertThat(validator.validate("one$special", policy)).accepted();
		assertThat(validator.validate("one%special", policy)).accepted();
		assertThat(validator.validate("one^special", policy)).accepted();
		assertThat(validator.validate("*onespecial", policy)).accepted();
		assertThat(validator.validate("onespecial&", policy)).accepted();
	}
	
	@Test
	public void shouldFailValidationWhenANumericCharacterIsRequired() {
		PasswordPolicy policy = passwordPolicy().requireNumericCharacter(true).build();

		assertThat(validator.validate("nonumbers", policy)).rejected().withError("PWD-0006", "At least one digit is required.", UnprocessableEntityException.class);
		assertThat(validator.validate("1numbers", policy)).accepted();
		assertThat(validator.validate("numbers2", policy)).accepted();
		assertThat(validator.validate("numb3ers", policy)).accepted();
	}

	@Test
	public void shouldFailValidationWhenThePasswordDoesNotMeetMinimumLengthRequirements() {
		PasswordPolicy policy = passwordPolicy().withMinLength(4).build();
		assertThat(validator.validate("aaa", policy)).rejected().withError("PWD-0004", "The password must be at least 4 characters.", UnprocessableEntityException.class);
		assertThat(validator.validate("aaaaa&", policy)).accepted();
	}

	@Test
	public void shouldFailValidationWhenThePasswordIsInTheCommonPasswordsDictionary() {
		PasswordPolicy policy = passwordPolicy().rejectCommonPasswords(true).build();
		assertThat(validator.validate("password", policy)).rejected().withError("PWD-0005", "The password entered is too common, please choose another password.", UnprocessableEntityException.class);
		assertThat(validator.validate("123456", policy)).rejected().withError("PWD-0005", "The password entered is too common, please choose another password.", UnprocessableEntityException.class);
		assertThat(validator.validate("adf4l29s##5l&", policy)).accepted();
	}

}
