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

package fm.pattern.jwt.server.validation;

import static org.apache.commons.lang3.StringUtils.isBlank;

import javax.persistence.Entity;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fm.pattern.commons.util.ReflectionUtils;
import fm.pattern.jwt.server.model.PersistentEntity;
import fm.pattern.jwt.server.repository.DataRepository;
import fm.pattern.microstructure.ValidatorSupport;

@Component
public class UniqueValueValidator extends ValidatorSupport implements ConstraintValidator<UniqueValue, PersistentEntity> {

	private String property;
	private final DataRepository repository;

	@Autowired
	public UniqueValueValidator(@Qualifier("transactionalDataRepository") DataRepository repository) {
		this.repository = repository;
	}

	public void initialize(UniqueValue annotation) {
		this.property = annotation.property();
	}

	public boolean isValid(PersistentEntity entity, ConstraintValidatorContext constraint) {
		final String value = ReflectionUtils.getString(entity, property);
		if (isBlank(value)) {
			return true;
		}

		String query = "select count(id) from " + getTableName(entity) + " where " + property + " = :value and id != :id";
		return repository.count(resolve(query, value, entity)) == 0;
	}

	private Query resolve(String base, String value, PersistentEntity entity) {
		return repository.getCurrentSession().createSQLQuery(base).setString("value", value).setString("id", entity.getId());
	}

	private String getTableName(PersistentEntity entity) {
		if (entity.getClass().isAnnotationPresent(Entity.class)) {
			Entity annotation = entity.getClass().getAnnotation(Entity.class);
			return annotation.name();
		}
		throw new IllegalStateException(entity.getClass().getSimpleName() + " must have an @Entity annotation configured with the entity name.");
	}

}
