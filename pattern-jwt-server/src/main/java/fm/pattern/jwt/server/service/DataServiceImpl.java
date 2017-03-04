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
import static org.apache.commons.lang3.text.WordUtils.uncapitalize;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.jwt.server.repository.DataRepository;
import fm.pattern.microstructure.Result;
import fm.pattern.microstructure.ValidationService;
import fm.pattern.microstructure.sequences.Create;
import fm.pattern.microstructure.sequences.Delete;
import fm.pattern.microstructure.sequences.Update;

@Service
@SuppressWarnings({ "unchecked", "hiding" })
class DataServiceImpl<T> implements DataService<T> {

	@Resource(name = "dataRepository")
	private DataRepository repository;

	@Autowired
	private ValidationService validationService;

	DataServiceImpl() {

	}

	@Transactional
	public Result<T> create(T entity) {
		Result<T> result = validationService.validate(entity, Create.class);
		return result.rejected() ? result : repository.save(entity);
	}

	@Transactional
	public Result<T> update(T entity) {
		Result<T> result = validationService.validate(entity, Update.class);
		return result.rejected() ? result : repository.update(entity);
	}

	@Transactional
	public Result<T> delete(T entity) {
		Result<T> result = validationService.validate(entity, Delete.class);
		return result.rejected() ? result : repository.delete(entity);
	}

	@Transactional(readOnly = true)
	public Result<T> findById(String id, Class<T> type) {
		String name = type.getSimpleName().toLowerCase();
		if (isBlank(id)) {
			return Result.reject(report(uncapitalize(type.getSimpleName()) + ".id.required"));
		}

		T entity = repository.findById(id, type);
		return entity != null ? Result.accept(entity) : Result.reject(report("system.not.found", name, id));
	}

	@Transactional(readOnly = true)
	public Result<List<T>> list(Class<T> type) {
		try {
			return Result.accept(repository.query("from " + entity(type) + " order by created").list());
		}
		catch (Exception e) {
			return Result.reject(report("system.query.failed", e.getMessage()));
		}
	}

	private <T> String entity(Class<T> entity) {
		if (entity.isAnnotationPresent(Entity.class)) {
			return entity.getAnnotation(Entity.class).name();
		}
		throw new IllegalStateException(entity.getClass().getSimpleName() + " must have an @Entity annotation configured with the entity name.");
	}

}
