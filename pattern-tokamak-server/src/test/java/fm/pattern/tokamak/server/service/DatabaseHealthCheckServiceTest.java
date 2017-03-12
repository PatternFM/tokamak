package fm.pattern.tokamak.server.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.service.DatabaseHealthCheckService;

public class DatabaseHealthCheckServiceTest extends IntegrationTest {

	@Autowired
	private DatabaseHealthCheckService databaseHealthCheckService;

	@Test
	public void shouldBeAbleToPingTheHealthOfTheDatabase() {
		assertThat(databaseHealthCheckService.isHealthy()).isTrue();
	}

}
