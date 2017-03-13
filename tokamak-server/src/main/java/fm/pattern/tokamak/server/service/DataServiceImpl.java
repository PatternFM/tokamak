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

import static fm.pattern.valex.Reportable.report;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.text.WordUtils.uncapitalize;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Entity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.tokamak.server.repository.DataRepository;
import fm.pattern.valex.Result;
import fm.pattern.valex.annotations.Create;
import fm.pattern.valex.annotations.Delete;
import fm.pattern.valex.annotations.Update;

@Service
@SuppressWarnings({ "unchecked", "hiding" })
class DataServiceImpl<T> implements DataService<T> {

    @Resource(name = "dataRepository")
    private DataRepository repository;

    DataServiceImpl() {

    }

    @Transactional
    public Result<T> create(@Create T entity) {
        return repository.save(entity);
    }

    @Transactional
    public Result<T> update(@Update T entity) {
        return repository.update(entity);
    }

    @Transactional
    public Result<T> delete(@Delete T entity) {
        return repository.delete(entity);
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
