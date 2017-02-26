package fm.pattern.jwt.server.service;

import java.util.List;

import fm.pattern.jwt.server.model.GrantType;
import fm.pattern.microstructure.Result;

public interface GrantTypeService {

	Result<GrantType> create(GrantType grantType);

	Result<GrantType> update(GrantType grantType);

	Result<GrantType> delete(GrantType grantType);

	Result<GrantType> findById(String id);

	Result<List<GrantType>> list();

}
