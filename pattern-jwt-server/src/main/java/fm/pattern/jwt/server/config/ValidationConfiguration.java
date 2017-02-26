package fm.pattern.jwt.server.config;

import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import fm.pattern.microstructure.SimpleValidationService;
import fm.pattern.microstructure.ValidationService;

@Configuration
public class ValidationConfiguration {

	@Bean(name = "validator")
	public Validator validator() {
		return new LocalValidatorFactoryBean();
	}

	@Bean
	public ValidationService validationService() {
		return new SimpleValidationService(validator());
	}

}
