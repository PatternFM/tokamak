package fm.pattern.jwt.server.repository;

import org.flywaydb.core.Flyway;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.microstructure.Result;

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
