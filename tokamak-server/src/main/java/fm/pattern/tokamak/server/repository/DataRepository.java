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

import fm.pattern.valex.Result;

public interface DataRepository {

	<T> Result<T> save(T instance);

	<T> Result<T> update(T instance);

	<T> Result<T> delete(T instance);

	<T> T findById(String id, Class<T> type);

	Query query(String query);

	SQLQuery sqlQuery(String query);

	Long count(Query query);

	Session getCurrentSession();

	Flyway getFlyway();

}
