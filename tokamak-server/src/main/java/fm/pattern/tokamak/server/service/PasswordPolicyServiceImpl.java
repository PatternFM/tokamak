package fm.pattern.tokamak.server.service;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.tokamak.server.model.PasswordPolicy;
import fm.pattern.valex.Result;

@Service
class PasswordPolicyServiceImpl extends DataServiceImpl<PasswordPolicy> implements PasswordPolicyService {

	@Transactional(readOnly = true)
	public Result<PasswordPolicy> findByName(String name) {
		if (isBlank(name)) {
			return Result.reject("passwordPolicy.name.required");
		}

		Result<PasswordPolicy> result = super.findBy("name", name, PasswordPolicy.class);
		return result.accepted() ? result : Result.reject("passwordPolicy.name.not_found", name);
	}

}
