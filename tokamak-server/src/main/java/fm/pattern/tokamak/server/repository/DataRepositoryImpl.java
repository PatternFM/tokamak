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

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import fm.pattern.minimal.Reflection;
import fm.pattern.valex.Result;

@Repository("dataRepository")
@SuppressWarnings("unchecked")
class DataRepositoryImpl implements DataRepository {

	private Flyway flyway;

	@PersistenceContext
	private EntityManager em;

	DataRepositoryImpl() {

	}

	public <T> T findBy(String key, String value, Class<T> type) {
		try {
			return (T) query("from " + entityName(type) + " where " + key + " = :value").setParameter("value", value).getSingleResult();
		}
		catch (EmptyResultDataAccessException | NoResultException e) {
			return null;
		}
	}

	public <T> T findById(String id, Class<T> type) {
		try {
			return (T) query("from " + entityName(type) + " where id = :id").setParameter("id", id).getSingleResult();
		}
		catch (EmptyResultDataAccessException | NoResultException e) {
			return null;
		}
	}

	public <T> Result<T> save(T instance) {
		try {
			em.persist(instance);
			em.flush();
			em.clear();
			return Result.accept(instance);
		}
		catch (Exception e) {
			return Result.reject("system.create.failed", e.getMessage());
		}
	}

	public <T> Result<T> update(T instance) {
		try {
			Reflection.set(instance, "updated", new Date());
			em.merge(instance);
			em.flush();
			em.clear();
			return Result.accept(instance);
		}
		catch (Exception e) {
			return Result.reject("system.update.failed", e.getMessage());
		}
	}

	public <T> Result<T> delete(T instance) {
		try {
			em.remove(em.contains(instance) ? instance : em.merge(instance));
			em.flush();
			return Result.accept(instance);
		}
		catch (Exception e) {
			return Result.reject("system.delete.failed", e.getMessage());
		}
	}

	public Query query(String query) {
		return em.createQuery(query);
	}

	public Query namedQuery(String namedQuery) {
		return em.createNamedQuery(namedQuery);
	}

	public Query sqlQuery(String sqlQuery) {
		return em.createNativeQuery(sqlQuery);
	}

	public Long count(Query query) {
		try {
			Object count = query.getSingleResult();
			return (count instanceof Long) ? (Long) count : ((BigInteger) count).longValue();
		}
		catch (EmptyResultDataAccessException | NoResultException e) {
			return 0L;
		}
		catch (Exception e) {
			return 0L;
		}
	}

	public Flyway getFlyway() {
		return flyway;
	}

	@Autowired
	public void setFlyway(Flyway flyway) {
		this.flyway = flyway;
	}

	private <T> String entityName(Class<T> entity) {
		if (entity.isAnnotationPresent(Entity.class)) {
			return entity.getAnnotation(Entity.class).name();
		}
		throw new IllegalStateException(entity.getClass().getSimpleName() + " must have an @Entity annotation configured with the entity name.");
	}

}
