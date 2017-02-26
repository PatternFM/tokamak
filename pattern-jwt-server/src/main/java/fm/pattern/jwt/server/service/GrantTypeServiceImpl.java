package fm.pattern.jwt.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.jwt.server.model.GrantType;
import fm.pattern.jwt.server.repository.DataRepository;
import fm.pattern.microstructure.Consumable;
import fm.pattern.microstructure.Result;
import fm.pattern.microstructure.ValidationService;
import fm.pattern.microstructure.sequences.Delete;

@Service
class GrantTypeServiceImpl extends DataServiceImpl<GrantType> implements GrantTypeService {

	private final DataRepository repository;
	private final ValidationService validationService;

	@Autowired
	GrantTypeServiceImpl(@Qualifier("dataRepository") DataRepository repository, ValidationService validationService) {
		this.repository = repository;
		this.validationService = validationService;
	}

	@Transactional
	public Result<GrantType> delete(GrantType grantType) {
		Result<GrantType> result = validationService.validate(grantType, Delete.class);
		if (result.rejected()) {
			return result;
		}
		
		Long count = repository.count(repository.sqlQuery("select count(_id) from ClientGrantTypes where grant_type_id = :id").setString("id", grantType.getId()));
		if (count != 0) {
			return Result.conflict(new Consumable("grantType.delete.conflict", "This grant type cannot be deleted as there are " + count + " clients currently linked to this grant type."));
		}
		
		return repository.delete(grantType);
	}

	@Transactional(readOnly = true)
	public Result<GrantType> findById(String id) {
		return super.findById(id, GrantType.class);
	}

	@Transactional(readOnly = true)
	public Result<List<GrantType>> list() {
		return super.list(GrantType.class);
	}

}
