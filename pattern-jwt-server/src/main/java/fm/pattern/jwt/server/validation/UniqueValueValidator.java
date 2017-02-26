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
