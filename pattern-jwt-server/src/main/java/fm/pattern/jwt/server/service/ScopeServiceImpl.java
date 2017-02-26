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
			return Result.conflict(new Consumable("scope.delete.conflict", "This scope cannot be deleted, " + count + (count != 1 ? " clients are" : " client is") + " linked to this scope."));
		}

		return repository.delete(scope);
	}

	@Transactional(readOnly = true)
	public Result<Scope> findById(String id) {
		return super.findById(id, Scope.class);
	}

	@Transactional(readOnly = true)
	public Result<Scope> findByName(String name) {
		if (isBlank(name)) {
			return Result.unprocessable_entity("{scope.get.name.required}");
		}

		Scope scope = (Scope) repository.query("from Scopes where name = :name").setString("name", name).uniqueResult();
		return scope == null ? Result.not_found("No such scope name: " + name) : Result.accept(scope);
	}

	@Transactional(readOnly = true)
	public Result<List<Scope>> list() {
		return super.list(Scope.class);
	}

}
