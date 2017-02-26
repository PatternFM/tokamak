package fm.pattern.jwt.server.repository;

import java.math.BigInteger;

import javax.persistence.Entity;

import org.flywaydb.core.Flyway;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fm.pattern.microstructure.Result;

@Repository("dataRepository")
@SuppressWarnings("unchecked")
class DataRepositoryImpl implements DataRepository {

	private SessionFactory sessionFactory;
	private Flyway flyway;

	DataRepositoryImpl() {

	}

	public <T> T findById(String id, Class<T> type) {
		return (T) query("from " + entityName(type) + " where id = :id").setString("id", id).uniqueResult();
	}

	public <T> Result<T> save(T instance) {
		try {
			sessionFactory.getCurrentSession().save(instance);
			sessionFactory.getCurrentSession().flush();
			return Result.accept(instance);
		}
		catch (Exception e) {
			return Result.internal_error(e.getMessage());
		}
	}

	public <T> Result<T> update(T instance) {
		try {
			sessionFactory.getCurrentSession().update(instance);
			sessionFactory.getCurrentSession().flush();
			return Result.accept(instance);
		}
		catch (Exception e) {
			return Result.internal_error(e.getMessage());
		}
	}

	public <T> Result<T> delete(T instance) {
		try {
			sessionFactory.getCurrentSession().delete(instance);
			sessionFactory.getCurrentSession().flush();
			return Result.accept(instance);
		}
		catch (Exception e) {
			return Result.internal_error(e.getMessage());
		}
	}

	public Query query(String query) {
		return sessionFactory.getCurrentSession().createQuery(query);
	}

	public Query namedQuery(String namedQuery) {
		return sessionFactory.getCurrentSession().getNamedQuery(namedQuery);
	}

	public SQLQuery sqlQuery(String sqlQuery) {
		return sessionFactory.getCurrentSession().createSQLQuery(sqlQuery).addSynchronizedQuerySpace("");
	}

	public SQLQuery namedSqlQuery(String namedQuery) {
		SQLQuery query = (SQLQuery) sessionFactory.getCurrentSession().getNamedQuery(namedQuery);
		return query.addSynchronizedQuerySpace("");
	}

	public Long count(Query query) {
		Object count = query.uniqueResult();
		return (count instanceof Long) ? (Long) count : ((BigInteger) query.uniqueResult()).longValue();
	}

	public Flyway getFlyway() {
		return flyway;
	}

	public Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
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
