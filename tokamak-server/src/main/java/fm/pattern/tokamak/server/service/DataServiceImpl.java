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
import static org.apache.commons.lang3.text.WordUtils.uncapitalize;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Entity;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.tokamak.server.repository.DataRepository;
import fm.pattern.valex.Result;

@Service
@SuppressWarnings({ "unchecked", "hiding" })
class DataServiceImpl<T> implements DataService<T> {

	@Resource(name = "dataRepository")
	private DataRepository repository;

	DataServiceImpl() {

	}

	@Transactional
	public Result<T> create(T entity) {
		return repository.save(entity);
	}

	@Transactional
	public Result<T> update(T entity) {
		return repository.update(entity);
	}

	@Transactional
	public Result<T> delete(T entity) {
		return repository.delete(entity);
	}

	@Transactional(readOnly = true)
	public Query query(String query) {
		return repository.query(query);
	}

	@Transactional(readOnly = true)
	public Result<T> findById(String id, Class<T> type) {
		if (isBlank(id)) {
			return Result.reject(uncapitalize(type.getSimpleName()) + ".id.required");
		}

		String name = type.getSimpleName().toLowerCase();
		T entity = repository.findById(id, type);
		return entity != null ? Result.accept(entity) : Result.reject("system.not.found", name, id);
	}

	@Transactional(readOnly = true)
	public Result<T> findBy(String key, String value, Class<T> type) {
		if (isBlank(value)) {
			return Result.reject(uncapitalize(type.getSimpleName()) + "." + key + ".required");
		}

		String name = type.getSimpleName().toLowerCase();
		T entity = repository.findBy(key, value, type);
		return entity != null ? Result.accept(entity) : Result.reject("system.not.found", name, value);
	}

	@Transactional(readOnly = true)
	public Result<List<T>> list(Class<T> type) {
		try {
			return Result.accept(repository.query("from " + entity(type) + " order by created").getResultList());
		}
		catch (Exception e) {
			return Result.reject("system.query.failed", e.getMessage());
		}
	}

	@Transactional(readOnly = true)
	public Long count(Query query) {
		return repository.count(query);
	}

	private <T> String entity(Class<T> entity) {
		if (entity.isAnnotationPresent(Entity.class)) {
			return entity.getAnnotation(Entity.class).name();
		}
		throw new IllegalStateException(entity.getClass().getSimpleName() + " must have an @Entity annotation configured with the entity name.");
	}

}
