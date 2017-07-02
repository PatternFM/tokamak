package fm.pattern.tokamak.server;

import static fm.pattern.tokamak.server.PatternAssertions.assertThat;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.tokamak.server.config.ApplicationConfiguration;
import fm.pattern.tokamak.server.config.RedisConfiguration;
import fm.pattern.valex.ValidationService;
import fm.pattern.valex.sequences.Create;
import fm.pattern.valex.sequences.Delete;
import fm.pattern.valex.sequences.Update;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ApplicationConfiguration.class, TestConfiguration.class, RedisConfiguration.class })
@Transactional
public abstract class IntegrationTest {

	@Autowired
	private ValidationService validationService;

	public <T> ResultAssertions onCreate(T instance) {
		return assertThat(validationService.validate(instance, Create.class));
	}

	public <T> ResultAssertions onUpdate(T instance) {
		return assertThat(validationService.validate(instance, Update.class));
	}

	public <T> ResultAssertions onDelete(T instance) {
		return assertThat(validationService.validate(instance, Delete.class));
	}

}
