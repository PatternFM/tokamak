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

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.tokamak.server.model.Audience;
import fm.pattern.tokamak.server.repository.DataRepository;
import fm.pattern.valex.Result;
import fm.pattern.valex.annotations.Delete;

@Service
class AudienceServiceImpl extends DataServiceImpl<Audience> implements AudienceService {

    private final DataRepository repository;

    AudienceServiceImpl(@Qualifier("dataRepository") DataRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Result<Audience> delete(@Delete Audience audience) {
        Long count = repository.count(repository.sqlQuery("select count(_id) from ClientAudiences where audience_id = :id").setString("id", audience.getId()));
        if (count != 0) {
            return Result.reject("audience.delete.conflict", count, (count != 1 ? "clients are" : "client is"));
        }
        return repository.delete(audience);
    }

    @Transactional(readOnly = true)
    public Result<Audience> findById(String id) {
        return super.findById(id, Audience.class);
    }

    @Transactional(readOnly = true)
    public Result<Audience> findByName(String name) {
        if (isBlank(name)) {
            return Result.reject("audience.name.required");
        }

        Audience audience = (Audience) repository.query("from Audiences where name = :name").setString("name", name).uniqueResult();
        return audience == null ? Result.reject("audience.name.not_found", name) : Result.accept(audience);
    }

    @Transactional(readOnly = true)
    public Result<List<Audience>> list() {
        return super.list(Audience.class);
    }

}
