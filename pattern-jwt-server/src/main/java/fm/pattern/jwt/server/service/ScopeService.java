package fm.pattern.jwt.server.service;

import java.util.List;

import fm.pattern.jwt.server.model.Scope;
import fm.pattern.microstructure.Result;

public interface ScopeService {

	Result<Scope> create(Scope scope);

	Result<Scope> update(Scope scope);

	Result<Scope> delete(Scope scope);

	Result<Scope> findById(String id);

	Result<List<Scope>> list();

}
