package fm.pattern.jwt.server.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.jwt.server.IntegrationTest;

public class DatabaseHealthCheckServiceTest extends IntegrationTest {

	@Autowired
	private DatabaseHealthCheckService databaseHealthCheckService;

	@Test
	public void shouldBeAbleToPingTheHealthOfTheDatabase() {
		assertThat(databaseHealthCheckService.isHealthy()).isTrue();
	}

}
