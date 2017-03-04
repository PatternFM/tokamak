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

import static fm.pattern.microstructure.Reportable.report;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.jwt.server.model.GrantType;
import fm.pattern.jwt.server.repository.DataRepository;
import fm.pattern.microstructure.Reportable;
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
			return Result.conflict(new Reportable("grantType.delete.conflict", "This grant type cannot be deleted, " + count + (count != 1 ? " clients are" : " client is") + " linked to this grant type."));
		}

		return repository.delete(grantType);
	}

	@Transactional(readOnly = true)
	public Result<GrantType> findById(String id) {
		return super.findById(id, GrantType.class);
	}

	@Transactional(readOnly = true)
	public Result<GrantType> findByName(String name) {
		if (isBlank(name)) {
			return Result.invalid(report("grantType.get.name.required"));
		}

		GrantType grantType = (GrantType) repository.query("from GrantTypes where name = :name").setString("name", name).uniqueResult();
		return grantType == null ? Result.not_found(report("grantType.get.name.nf", name)) : Result.accept(grantType);
	}

	@Transactional(readOnly = true)
	public Result<List<GrantType>> list() {
		return super.list(GrantType.class);
	}

}
