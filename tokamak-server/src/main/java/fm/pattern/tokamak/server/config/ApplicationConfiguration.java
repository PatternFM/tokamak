/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fm.pattern.tokamak.server.config;

import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import fm.pattern.valex.SimpleValidationService;
import fm.pattern.valex.ValidationService;
import fm.pattern.valex.annotations.ValidationAdvisor;

@Configuration
@EnableTransactionManagement
public class ApplicationConfiguration {

	@Bean(name = "validator")
	public Validator validator() {
		return new LocalValidatorFactoryBean();
	}

	@Bean
	public ValidationService validationService() {
		return new SimpleValidationService(validator());
	}

	@Bean
	public ValidationAdvisor validationAdvisor() {
		return new ValidationAdvisor(validationService());
	}

}
