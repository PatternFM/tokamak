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

@Entity(name = "Scopes")
@UniqueValue(property = "name", message = "{scope.name.conflict}", groups = { CreateLevel3.class, UpdateLevel3.class })
public class Scope extends PersistentEntity {

	private static final long serialVersionUID = 5066562591827034738L;

	@Column(name = "name", nullable = false, unique = true)
	@NotBlank(message = "{scope.name.required}", groups = { CreateLevel1.class, UpdateLevel1.class })
	@Size(max = 128, message = "{scope.name.size}", groups = { CreateLevel2.class, UpdateLevel2.class })
	private String name;

	Scope() {
		super(IdGenerator.generateId("scp", 30));
	}

	public Scope(String name) {
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
		if (!(obj instanceof Scope)) {
			return false;
		}

		final Scope scope = (Scope) obj;
		return equal(this.getId(), scope.getId());
	}

	public String toString() {
		return JSON.stringify(this);
	}

}
