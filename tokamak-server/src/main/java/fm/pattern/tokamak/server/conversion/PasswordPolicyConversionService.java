package fm.pattern.tokamak.server.conversion;

import org.springframework.stereotype.Service;

import fm.pattern.tokamak.sdk.model.PasswordPolicyRepresentation;
import fm.pattern.tokamak.server.model.PasswordPolicy;

@Service
public class PasswordPolicyConversionService {

	public PasswordPolicyRepresentation convert(PasswordPolicy passwordPolicy) {
		PasswordPolicyRepresentation representation = new PasswordPolicyRepresentation(passwordPolicy.getId());
		representation.setCreated(passwordPolicy.getCreated());
		representation.setUpdated(passwordPolicy.getUpdated());
		representation.setName(passwordPolicy.getName());
		representation.setDescription(passwordPolicy.getDescription());

		representation.setRejectCommonPasswords(passwordPolicy.isRejectCommonPasswords());
		representation.setRequireLowercaseCharacter(passwordPolicy.isRequireLowercaseCharacter());
		representation.setRequireNumericCharacter(passwordPolicy.isRequireNumericCharacter());
		representation.setRequireSpecialCharacter(passwordPolicy.isRequireSpecialCharacter());
		representation.setRequireUppercaseCharacter(passwordPolicy.isRequireUppercaseCharacter());
		return representation;
	}

	public PasswordPolicy convert(PasswordPolicyRepresentation representation) {
		PasswordPolicy passwordPolicy = new PasswordPolicy();
		passwordPolicy.setName(representation.getName());
		passwordPolicy.setDescription(representation.getDescription());
		passwordPolicy.setRejectCommonPasswords(representation.isRejectCommonPasswords());
		passwordPolicy.setRequireLowercaseCharacter(representation.isRequireLowercaseCharacter());
		passwordPolicy.setRequireNumericCharacter(representation.isRequireNumericCharacter());
		passwordPolicy.setRequireSpecialCharacter(representation.isRequireSpecialCharacter());
		passwordPolicy.setRequireUppercaseCharacter(representation.isRequireUppercaseCharacter());
		return passwordPolicy;
	}

	public PasswordPolicy convert(PasswordPolicyRepresentation representation, PasswordPolicy passwordPolicy) {
		passwordPolicy.setName(representation.getName());
		passwordPolicy.setDescription(representation.getDescription());
		passwordPolicy.setRejectCommonPasswords(representation.isRejectCommonPasswords());
		passwordPolicy.setRequireLowercaseCharacter(representation.isRequireLowercaseCharacter());
		passwordPolicy.setRequireNumericCharacter(representation.isRequireNumericCharacter());
		passwordPolicy.setRequireSpecialCharacter(representation.isRequireSpecialCharacter());
		passwordPolicy.setRequireUppercaseCharacter(representation.isRequireUppercaseCharacter());
		return passwordPolicy;
	}

}
