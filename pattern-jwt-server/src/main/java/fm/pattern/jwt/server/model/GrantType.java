package fm.pattern.jwt.server.model;

import static com.google.common.base.Objects.equal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.google.common.base.Objects;

import fm.pattern.commons.util.IdGenerator;
import fm.pattern.commons.util.JSON;
import fm.pattern.jwt.server.validation.UniqueValue;
import fm.pattern.microstructure.sequences.CreateLevel1;
import fm.pattern.microstructure.sequences.CreateLevel2;
import fm.pattern.microstructure.sequences.CreateLevel3;
import fm.pattern.microstructure.sequences.UpdateLevel1;
import fm.pattern.microstructure.sequences.UpdateLevel2;
import fm.pattern.microstructure.sequences.UpdateLevel3;

@Entity(name = "GrantTypes")
@UniqueValue(property = "name", message = "{grantType.name.conflict}", groups = { CreateLevel3.class, UpdateLevel3.class })
public class GrantType extends PersistentEntity {

	private static final long serialVersionUID = -8385482795202177569L;

	@Column(name = "name", nullable = false, unique = true)
	@NotBlank(message = "{grantType.name.required}", groups = { CreateLevel1.class, UpdateLevel1.class })
	@Size(max = 128, message = "{grantType.name.size}", groups = { CreateLevel2.class, UpdateLevel2.class })
	private String name;

	GrantType() {
		super(IdGenerator.generateId("gnt", 30));
	}

	public GrantType(String name) {
		this();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int hashCode() {
		return Objects.hashCode(getId());
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Role)) {
			return false;
		}

		final GrantType grantType = (GrantType) obj;
		return equal(this.getId(), grantType.getId());
	}

	public String toString() {
		return JSON.stringify(this);
	}

}
