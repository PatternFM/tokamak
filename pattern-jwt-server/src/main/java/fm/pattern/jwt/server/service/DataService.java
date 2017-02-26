package fm.pattern.jwt.server.service;

import java.util.List;

import fm.pattern.microstructure.Result;

public interface DataService<T> {

	Result<T> create(T role);

	Result<T> update(T role);

	Result<T> delete(T role);

	Result<T> findById(String id, Class<T> type);

	Result<List<T>> list(Class<T> type);

}
