package fm.pattern.tokamak.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "PasswordPolicies")
public class PasswordPolicy extends PersistentEntity {

	private static final long serialVersionUID = 5899777992531451401L;

	@Getter
	@Setter
	@Column(nullable = false)
	private Integer minLength;

	@Getter
	@Setter
	@Column(nullable = false)
	private Integer maxLength;

	@Getter
	@Setter
	@Column(nullable = false)
	private boolean requireUppercaseCharacter = true;

	@Getter
	@Setter
	@Column(nullable = false)
	private boolean requireLowercaseCharacter = true;

	@Getter
	@Setter
	@Column(nullable = false)
	private boolean requireNumericCharacter = true;

	@Getter
	@Setter
	@Column(nullable = false)
	private boolean requireSpecialCharacter = false;

}
