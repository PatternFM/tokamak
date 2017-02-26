package fm.pattern.jwt.server.service;

import java.util.List;

import fm.pattern.jwt.server.model.Role;
import fm.pattern.microstructure.Result;

public interface RoleService {

	Result<Role> create(Role role);

	Result<Role> update(Role role);

	Result<Role> delete(Role role);

	Result<Role> findById(String id);

	Result<List<Role>> list();

}
