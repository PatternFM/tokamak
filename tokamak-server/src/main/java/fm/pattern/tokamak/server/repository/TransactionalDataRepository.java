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

package fm.pattern.tokamak.server.repository;

import org.flywaydb.core.Flyway;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.valex.Result;

@Repository("transactionalDataRepository")
class TransactionalDataRepository implements DataRepository {

	private final DataRepository repository;

	@Autowired
	public TransactionalDataRepository(@Qualifier("dataRepository") DataRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public <T> Result<T> save(T instance) {
		return repository.save(instance);
	}

	@Transactional
	public <T> Result<T> update(T instance) {
		return repository.update(instance);
	}

	@Transactional
	public <T> Result<T> delete(T instance) {
		return repository.delete(instance);
	}

	@Transactional(readOnly = true)
	public <T> T findById(String id, Class<T> type) {
		return repository.findById(id, type);
	}

	@Transactional
	public Query query(String query) {
		return repository.query(query);
	}

	@Transactional
	public SQLQuery sqlQuery(String query) {
		return repository.sqlQuery(query);
	}

	@Transactional(readOnly = true)
	public Long count(Query query) {
		return repository.count(query);
	}

	@Transactional
	public Session getCurrentSession() {
		return repository.getCurrentSession();
	}

	@Transactional
	public Flyway getFlyway() {
		return repository.getFlyway();
	}

}
