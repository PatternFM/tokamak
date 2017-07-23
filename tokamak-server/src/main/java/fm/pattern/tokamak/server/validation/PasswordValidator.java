package fm.pattern.tokamak.server.validation;

import fm.pattern.tokamak.server.model.PasswordPolicy;
import fm.pattern.valex.Result;

public interface PasswordValidator {

	Result<String> validate(String password, PasswordPolicy policy);
	
}
