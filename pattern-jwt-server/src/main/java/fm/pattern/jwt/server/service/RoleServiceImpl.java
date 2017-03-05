/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fm.pattern.jwt.server.service;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.jwt.server.model.Role;
import fm.pattern.jwt.server.repository.DataRepository;
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
			return Result.reject("role.delete.conflict", count, (count != 1 ? "accounts are" : "account is"));
		}

		return repository.delete(role);
	}

	@Transactional(readOnly = true)
	public Result<Role> findById(String id) {
		return super.findById(id, Role.class);
	}

	@Transactional(readOnly = true)
	public Result<Role> findByName(String name) {
		if (isBlank(name)) {
			return Result.reject("role.name.required");
		}

		Role role = (Role) repository.query("from Roles where name = :name").setString("name", name).uniqueResult();
		return role == null ? Result.reject("role.name.not_found", name) : Result.accept(role);
	}

	@Transactional(readOnly = true)
	public Result<List<Role>> list() {
		return super.list(Role.class);
	}

}
