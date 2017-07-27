package fm.pattern.tokamak.server.service;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.tokamak.server.model.PasswordPolicy;
import fm.pattern.tokamak.server.repository.DataRepository;
import fm.pattern.valex.Result;

@Service
@SuppressWarnings("unchecked")
class PasswordPolicyServiceImpl extends DataServiceImpl<PasswordPolicy> implements PasswordPolicyService {

	private final DataRepository repository;

	@Autowired
	public PasswordPolicyServiceImpl(@Qualifier("dataRepository") DataRepository repository) {
		this.repository = repository;
	}

	@Transactional(readOnly = true)
	public Result<PasswordPolicy> findById(String id) {
		return super.findById(id, PasswordPolicy.class);
	}

	@Transactional(readOnly = true)
	public Result<PasswordPolicy> findByName(String name) {
		if (isBlank(name)) {
			return Result.reject("passwordPolicy.name.required");
		}

		Result<PasswordPolicy> result = super.findBy("name", name, PasswordPolicy.class);
		return result.accepted() ? result : Result.reject("passwordPolicy.name.not_found", name);
	}

	@Transactional(readOnly = true)
	public Result<List<PasswordPolicy>> list() {
		return Result.accept(repository.query("from PasswordPolicies order by name").getResultList());
	}

}
