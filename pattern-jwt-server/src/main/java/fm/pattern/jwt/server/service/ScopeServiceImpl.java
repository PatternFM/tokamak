package fm.pattern.jwt.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.jwt.server.model.Scope;
import fm.pattern.jwt.server.repository.DataRepository;
import fm.pattern.microstructure.Consumable;
import fm.pattern.microstructure.Result;
import fm.pattern.microstructure.ValidationService;
import fm.pattern.microstructure.sequences.Delete;

@Service
class ScopeServiceImpl extends DataServiceImpl<Scope> implements ScopeService {

	private final DataRepository repository;
	private final ValidationService validationService;

	@Autowired
	ScopeServiceImpl(@Qualifier("dataRepository") DataRepository repository, ValidationService validationService) {
		this.repository = repository;
		this.validationService = validationService;
	}

	@Transactional
	public Result<Scope> delete(Scope scope) {
		Result<Scope> result = validationService.validate(scope, Delete.class);
		if (result.rejected()) {
			return result;
		}

		Long count = repository.count(repository.sqlQuery("select count(_id) from ClientScopes where scope_id = :id").setString("id", scope.getId()));
		if (count != 0) {
			return Result.conflict(new Consumable("scope.delete.conflict", "This scope cannot be deleted as there are " + count + " clients currently linked to this scope."));
		}

		return repository.delete(scope);
	}

	@Transactional(readOnly = true)
	public Result<Scope> findById(String id) {
		return super.findById(id, Scope.class);
	}

	@Transactional(readOnly = true)
	public Result<List<Scope>> list() {
		return super.list(Scope.class);
	}

}
