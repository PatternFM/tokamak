package fm.pattern.jwt.server.repository;

import org.springframework.stereotype.Repository;

@Repository
class DatabaseHealthCheckRepositoryImpl extends DataRepositoryImpl implements DatabaseHealthCheckRepository {

	public boolean isHealthy() {
		try {
			sqlQuery("SELECT 1 FROM HeartBeat").uniqueResult();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

}
