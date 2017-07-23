package fm.pattern.tokamak.sdk.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import fm.pattern.tokamak.sdk.JwtClientProperties;
import fm.pattern.tokamak.sdk.PasswordPoliciesClient;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.PasswordPolicyRepresentation;

public class PasswordPolicyDSL extends AbstractDSL<PasswordPolicyDSL, PasswordPolicyRepresentation> {

	private PasswordPoliciesClient client = new PasswordPoliciesClient(JwtClientProperties.getEndpoint());

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

	public PasswordPolicyRepresentation build() {
		return create();
	}

	public PasswordPolicyRepresentation save() {
		Result<PasswordPolicyRepresentation> response = client.create(create(), super.getToken().getAccessToken());
		if (response.rejected()) {
			throw new IllegalStateException("Unable to create password policy, response from server: " + response.getErrors().toString());
		}
		return response.getInstance();
	}

	private PasswordPolicyRepresentation create() {
		PasswordPolicyRepresentation policy = new PasswordPolicyRepresentation();
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
