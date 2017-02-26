package fm.pattern.jwt.server.repository;

public interface DatabaseHealthCheckRepository extends DataRepository {

	boolean isHealthy();
	
}
