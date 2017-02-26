package fm.pattern.jwt.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.jwt.server.repository.DatabaseHealthCheckRepository;

@Service
class DatabaseHealthCheckServiceImpl implements DatabaseHealthCheckService {

	private final DatabaseHealthCheckRepository databaseHealthCheckRepository;

	@Autowired
	public DatabaseHealthCheckServiceImpl(DatabaseHealthCheckRepository databaseHealthCheckRepository) {
		this.databaseHealthCheckRepository = databaseHealthCheckRepository;
	}

	@Transactional(readOnly = true)
	public boolean isHealthy() {
		return databaseHealthCheckRepository.isHealthy();
	}

}
