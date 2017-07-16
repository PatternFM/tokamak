package fm.pattern.tokamak.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import fm.pattern.commons.util.JSON;
import fm.pattern.tokamak.server.validation.UniqueValue;
import fm.pattern.valex.sequences.CreateLevel1;
import fm.pattern.valex.sequences.CreateLevel2;
import fm.pattern.valex.sequences.CreateLevel3;
import fm.pattern.valex.sequences.UpdateLevel1;
import fm.pattern.valex.sequences.UpdateLevel2;
import fm.pattern.valex.sequences.UpdateLevel3;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "PasswordPolicies")
@UniqueValue(property = "name", message = "{passwordPolicy.name.conflict}", groups = { CreateLevel3.class, UpdateLevel3.class })
public class PasswordPolicy extends PersistentEntity {

	private static final long serialVersionUID = 5899777992531451401L;

	@Getter
	@Setter
	@Column(nullable = false, unique = true, updatable = false)
	@NotBlank(message = "{passwordPolicy.name.required}", groups = { CreateLevel1.class, UpdateLevel1.class })
	@Size(max = 30, message = "{passwordPolicy.name.size}", groups = { CreateLevel2.class, UpdateLevel2.class })
	private String name;

	@Getter
	@Setter
	@Column(nullable = true)
	@Size(max = 255, message = "{passwordPolicy.description.size}", groups = { CreateLevel1.class, UpdateLevel1.class })
	private String description;

	@Getter
	@Setter
	@Column(nullable = false)
	@NotNull(message = "{passwordPolicy.minLength.required}", groups = { CreateLevel1.class, UpdateLevel1.class })
	@Min(value = 1, message = "{passwordPolicy.minLength.tooSmall}", groups = { CreateLevel2.class, UpdateLevel2.class })
	@Max(value = 255, message = "{passwordPolicy.minLength.tooLarge}", groups = { CreateLevel2.class, UpdateLevel2.class })
	private Integer minLength;

	@Getter
	@Setter
	@Column(nullable = false)
	private boolean requireUppercaseCharacter;

	@Getter
	@Setter
	@Column(nullable = false)
	private boolean requireLowercaseCharacter;

	@Getter
	@Setter
	@Column(nullable = false)
	private boolean requireNumericCharacter;

	@Getter
	@Setter
	@Column(nullable = false)
	private boolean requireSpecialCharacter;

	@Getter
	@Setter
	@Column(nullable = false)
	private boolean rejectCommonPasswords;

	public int hashCode() {
		return getId().hashCode();
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Client)) {
			return false;
		}

		final PasswordPolicy policy = (PasswordPolicy) obj;
		return getId().equalsIgnoreCase(policy.getId());
	}

	public String toString() {
		return JSON.stringify(this);
	}

}
