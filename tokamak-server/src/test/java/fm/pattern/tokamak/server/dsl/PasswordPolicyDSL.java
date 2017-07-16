package fm.pattern.tokamak.server.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import fm.pattern.tokamak.server.model.PasswordPolicy;
import fm.pattern.tokamak.server.service.PasswordPolicyService;
import fm.pattern.valex.Result;

public class PasswordPolicyDSL extends AbstractDSL<PasswordPolicyDSL, PasswordPolicy> {

	private String name = randomAlphabetic(10);
	private String description = null;
	private Integer minLength = 1;
	private boolean requireUppercaseCharacter = false;
	private boolean requireLowercaseCharacter = false;
	private boolean requireNumericCharacter = false;
	private boolean requireSpecialCharacter = false;
	private boolean rejectCommonPasswords = false;

	public static PasswordPolicyDSL passwordPolicy() {
		return new PasswordPolicyDSL();
	}

	public PasswordPolicyDSL withName(String name) {
		this.name = name;
		return this;
	}

	public PasswordPolicyDSL withDescription(String description) {
		this.description = description;
		return this;
	}

	public PasswordPolicyDSL withMinLength(Integer minLength) {
		this.minLength = minLength;
		return this;
	}

	public PasswordPolicyDSL requireUppercaseCharacter(boolean require) {
		this.requireUppercaseCharacter = require;
		return this;
	}

	public PasswordPolicyDSL requireLowercaseCharacter(boolean require) {
		this.requireLowercaseCharacter = require;
		return this;
	}

	public PasswordPolicyDSL requireNumericCharacter(boolean require) {
		this.requireNumericCharacter = require;
		return this;
	}

	public PasswordPolicyDSL requireSpecialCharacter(boolean require) {
		this.requireSpecialCharacter = require;
		return this;
	}

	public PasswordPolicyDSL rejectCommonPasswords(boolean require) {
		this.rejectCommonPasswords = require;
		return this;
	}

	public PasswordPolicy build() {
		return create();
	}

	public PasswordPolicy save() {
		Result<PasswordPolicy> result = load(PasswordPolicyService.class).create(create());
		if (result.accepted()) {
			return result.getInstance();
		}
		throw new IllegalStateException("Unable to create password policy, errors:" + result.getErrors().toString());
	}

	private PasswordPolicy create() {
		PasswordPolicy policy = new PasswordPolicy();
		policy.setName(name);
		policy.setDescription(description);
		policy.setMinLength(minLength);
		policy.setRequireUppercaseCharacter(requireUppercaseCharacter);
		policy.setRequireLowercaseCharacter(requireLowercaseCharacter);
		policy.setRequireNumericCharacter(requireNumericCharacter);
		policy.setRequireSpecialCharacter(requireSpecialCharacter);
		policy.setRejectCommonPasswords(rejectCommonPasswords);
		return policy;
	}

}
