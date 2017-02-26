package fm.pattern.jwt.server.service;

import java.util.List;

import fm.pattern.jwt.server.model.Authority;
import fm.pattern.microstructure.Result;

public interface AuthorityService {

	Result<Authority> create(Authority authority);

	Result<Authority> update(Authority authority);

	Result<Authority> delete(Authority authority);

	Result<Authority> findById(String id);

	Result<List<Authority>> list();

}
