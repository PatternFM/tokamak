package fm.pattern.tokamak.server.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fm.pattern.tokamak.server.model.PasswordPolicy;
import fm.pattern.valex.Reportable;
import fm.pattern.valex.Result;

@Service
class PasswordValidatorImpl implements PasswordValidator {

	@Override
	public Result<String> validate(String password, PasswordPolicy policy) {
		List<Reportable> errors = new ArrayList<>();
		errors.addAll(validateUppercaseCharacter(password, policy).getErrors());
		errors.addAll(validateLowercaseCharacter(password, policy).getErrors());
		errors.addAll(validateSpecialCharacter(password, policy).getErrors());
		errors.addAll(validateMinimumLength(password, policy).getErrors());
		errors.addAll(validateCommonPasswords(password, policy).getErrors());
		return errors.isEmpty() ? Result.accept(password) : Result.reject(errors.toArray(new Reportable[errors.size()]));
	}

	private Result<String> validateUppercaseCharacter(String password, PasswordPolicy policy) {
		if (!policy.isRequireUppercaseCharacter()) {
			return Result.accept(password);
		}
		return !password.equals(password.toLowerCase()) ? Result.accept(password) : Result.reject("password.uppercase.required");
	}

	private Result<String> validateLowercaseCharacter(String password, PasswordPolicy policy) {
		if (!policy.isRequireLowercaseCharacter()) {
			return Result.accept(password);
		}
		return !password.equals(password.toUpperCase()) ? Result.accept(password) : Result.reject("password.lowercase.required");
	}

	private Result<String> validateSpecialCharacter(String password, PasswordPolicy policy) {
		if (!policy.isRequireSpecialCharacter()) {
			return Result.accept(password);
		}
		return !password.matches("[A-Za-z0-9 ]*") ? Result.accept(password) : Result.reject("password.specialcase.required");
	}

	private Result<String> validateMinimumLength(String password, PasswordPolicy policy) {
		if (password.length() < policy.getMinLength()) {
			return Result.reject("password.minlength", policy.getMinLength());
		}
		return Result.accept(password);
	}

	private Result<String> validateCommonPasswords(String password, PasswordPolicy policy) {
		if (policy.isRejectCommonPasswords() && CommonPasswordsDictionary.contains(password)) {
			return Result.reject("password.common");
		}
		return Result.accept(password);
	}

}
