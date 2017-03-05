package fm.pattern.jwt.server;

import static fm.pattern.jwt.server.PatternAssertions.assertThat;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.jwt.server.config.PersistenceConfiguration;
import fm.pattern.jwt.server.config.ValidationConfiguration;
import fm.pattern.validation.Result;
import fm.pattern.validation.ValidationService;
import fm.pattern.validation.sequences.Create;
import fm.pattern.validation.sequences.Delete;
import fm.pattern.validation.sequences.Update;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { PersistenceConfiguration.class, TestConfiguration.class, ValidationConfiguration.class })
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
