package fm.pattern.tokamak.server.service;

import java.util.List;

import fm.pattern.tokamak.server.model.PasswordPolicy;
import fm.pattern.valex.Result;
import fm.pattern.valex.annotations.Create;
import fm.pattern.valex.annotations.Delete;
import fm.pattern.valex.annotations.Update;

public interface PasswordPolicyService {

	Result<PasswordPolicy> create(@Create PasswordPolicy policy);

	Result<PasswordPolicy> update(@Update PasswordPolicy policy);

	Result<PasswordPolicy> delete(@Delete PasswordPolicy policy);

	Result<PasswordPolicy> findById(String id);
	
	Result<PasswordPolicy> findByName(String name);

	Result<List<PasswordPolicy>> list();
	
}
