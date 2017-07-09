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

package fm.pattern.tokamak.server.service;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.tokamak.server.model.Authority;
import fm.pattern.tokamak.server.repository.Cache;
import fm.pattern.tokamak.server.repository.DataRepository;
import fm.pattern.valex.Result;

@Service
@SuppressWarnings("unchecked")
class AuthorityServiceImpl extends DataServiceImpl<Authority> implements AuthorityService {

	private final DataRepository repository;
	private final Cache cache;

	@Autowired
	AuthorityServiceImpl(@Qualifier("dataRepository") DataRepository repository, @Qualifier("clientCache") Cache cache) {
		this.repository = repository;
		this.cache = cache;
	}

	@Transactional
	public Result<Authority> update(Authority authority) {
		Result<Authority> result = super.update(authority);
		if (result.accepted()) {
			cache.flush();
		}
		return result;
	}

	@Transactional
	public Result<Authority> delete(Authority authority) {
		Long count = repository.count(repository.sqlQuery("select count(_id) from ClientAuthorities where authority_id = :id").setParameter("id", authority.getId()));
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
	public Result<List<Authority>> findExistingById(List<String> ids) {
		if (ids == null || ids.isEmpty()) {
			return Result.accept(new ArrayList<>());
		}

		List<String> parameters = ids.stream().filter(id -> isNotBlank(id)).collect(Collectors.toList());
		if (parameters == null || parameters.isEmpty()) {
			return Result.accept(new ArrayList<>());
		}

		return Result.accept(query("from Authorities where id in (:ids)").setParameter("ids", parameters).getResultList());
	}

	@Transactional(readOnly = true)
	public Result<Authority> findByName(String name) {
		if (isBlank(name)) {
			return Result.reject("authority.name.required");
		}

		Result<Authority> result = super.findBy("name", name, Authority.class);
		return result.accepted() ? result : Result.reject("authority.name.not_found", name);
	}

	@Transactional(readOnly = true)
	public Result<List<Authority>> list() {
		return super.list(Authority.class);
	}

}
