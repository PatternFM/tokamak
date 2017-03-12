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

import fm.pattern.jwt.server.model.Authority;
import fm.pattern.jwt.server.repository.DataRepository;
import fm.pattern.valex.Result;
import fm.pattern.valex.ValidationService;
import fm.pattern.valex.sequences.Delete;

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
			return Result.reject("authority.delete.conflict", count, (count != 1 ? "clients are" : "client is"));
		}

		return repository.delete(authority);
	}

	@Transactional(readOnly = true)
	public Result<Authority> findById(String id) {
		return super.findById(id, Authority.class);
	}

	@Transactional(readOnly = true)
	public Result<Authority> findByName(String name) {
		if (isBlank(name)) {
			return Result.reject("authority.name.required");
		}

		Authority authority = (Authority) repository.query("from Authorities where name = :name").setString("name", name).uniqueResult();
		return authority == null ? Result.reject("authority.name.not_found", name) : Result.accept(authority);
	}

	@Transactional(readOnly = true)
	public Result<List<Authority>> list() {
		return super.list(Authority.class);
	}

}
