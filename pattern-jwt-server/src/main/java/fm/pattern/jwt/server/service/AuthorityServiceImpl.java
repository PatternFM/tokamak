package fm.pattern.jwt.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.jwt.server.model.Authority;
import fm.pattern.jwt.server.repository.DataRepository;
import fm.pattern.microstructure.Consumable;
import fm.pattern.microstructure.Result;
import fm.pattern.microstructure.ValidationService;
import fm.pattern.microstructure.sequences.Delete;

@Service
class AuthorityServiceImpl extends DataServiceImpl<Authority> implements AuthorityService {

	private final DataRepository repository;
	private final ValidationService validationService;

	@Autowired
	AuthorityServiceImpl(@Qualifier("dataRepository") DataRepository repository, ValidationService validationService) {
		this.repository = repository;
		this.validationService = validationService;
	}

	@Transactional
	public Result<Authority> delete(Authority authority) {
		Result<Authority> result = validationService.validate(authority, Delete.class);
		if (result.rejected()) {
			return result;
		}

		Long count = repository.count(repository.sqlQuery("select count(_id) from ClientAuthorities where authority_id = :id").setString("id", authority.getId()));
		if (count != 0) {
			return Result.conflict(new Consumable("authority.delete.conflict", "This authority cannot be deleted as there are " + count + " clients currently linked to this authority."));
		}

		return repository.delete(authority);
	}

	@Transactional(readOnly = true)
	public Result<Authority> findById(String id) {
		return super.findById(id, Authority.class);
	}

	@Transactional(readOnly = true)
	public Result<List<Authority>> list() {
		return super.list(Authority.class);
	}

}
