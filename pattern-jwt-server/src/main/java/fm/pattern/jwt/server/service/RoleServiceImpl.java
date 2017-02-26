package fm.pattern.jwt.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.jwt.server.model.Role;
import fm.pattern.jwt.server.repository.DataRepository;
import fm.pattern.microstructure.Consumable;
import fm.pattern.microstructure.Result;
import fm.pattern.microstructure.ValidationService;
import fm.pattern.microstructure.sequences.Delete;

@Service
class RoleServiceImpl extends DataServiceImpl<Role> implements RoleService {

	private final DataRepository repository;
	private final ValidationService validationService;

	@Autowired
	RoleServiceImpl(@Qualifier("dataRepository") DataRepository repository, ValidationService validationService) {
		this.repository = repository;
		this.validationService = validationService;
	}

	@Transactional
	public Result<Role> delete(Role role) {
		Result<Role> result = validationService.validate(role, Delete.class);
		if (result.rejected()) {
			return result;
		}

		Long count = repository.count(repository.sqlQuery("select count(_id) from AccountRoles where role_id = :id").setString("id", role.getId()));
		if (count != 0) {
			return Result.conflict(new Consumable("role.delete.conflict", "This role cannot be deleted as there are " + count + " accounts currently linked to this role."));
		}

		return repository.delete(role);
	}

	@Transactional(readOnly = true)
	public Result<Role> findById(String id) {
		return super.findById(id, Role.class);
	}

	@Transactional(readOnly = true)
	public Result<List<Role>> list() {
		return super.list(Role.class);
	}

}
