package fm.pattern.tokamak.server.repository;

public interface Cache {

	<T> T put(String key, T value);

	void delete(String key);

	<T> T get(String key, Class<T> type);

	boolean contains(String key);

}
