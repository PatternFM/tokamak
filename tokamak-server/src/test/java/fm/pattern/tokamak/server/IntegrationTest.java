package fm.pattern.tokamak.server;

import static fm.pattern.tokamak.server.PatternAssertions.assertThat;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.tokamak.server.config.ApplicationConfiguration;
import fm.pattern.tokamak.server.config.RedisConfiguration;
import fm.pattern.valex.Result;
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
		Result<T> result = validationService.validate(instance, Create.class);
		return assertThat(result);
	}

	public <T> ResultAssertions onUpdate(T instance) {
		Result<T> result = validationService.validate(instance, Update.class);
		return assertThat(result);
	}

	public <T> ResultAssertions onDelete(T instance) {
		Result<T> result = validationService.validate(instance, Delete.class);
		return assertThat(result);
	}

}
