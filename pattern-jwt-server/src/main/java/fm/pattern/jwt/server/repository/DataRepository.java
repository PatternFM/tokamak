package fm.pattern.jwt.server.repository;

import org.flywaydb.core.Flyway;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import fm.pattern.microstructure.Result;

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
