package fm.pattern.jwt.server.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import fm.pattern.commons.util.IdGenerator;
import fm.pattern.microstructure.sequences.CreateLevel1;
import fm.pattern.microstructure.sequences.DeleteLevel1;
import fm.pattern.microstructure.sequences.UpdateLevel1;

@MappedSuperclass
public class PersistentEntity implements Serializable {

	private static final long serialVersionUID = -2873931442549637189L;

	@Id
	@Column(name = "_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long _id;

	@Column(name = "id", nullable = false, unique = true, updatable = false)
	@NotBlank(message = "{entity.id.required}", groups = { CreateLevel1.class, UpdateLevel1.class, DeleteLevel1.class })
	private String id;

	@Column(name = "created", nullable = false, updatable = false)
	@NotNull(message = "{entity.created.required}", groups = { CreateLevel1.class, UpdateLevel1.class })
	private Date created;

	@Column(name = "updated", nullable = false)
	@NotNull(message = "{entity.updated.required}", groups = { CreateLevel1.class, UpdateLevel1.class })
	private Date updated;

	public PersistentEntity() {
		this(IdGenerator.generateId());
	}

	public PersistentEntity(String id) {
		this.id = id;
		this.created = new Date();
		this.updated = created;
	}

	public String getId() {
		return id;
	}

	public Date getCreated() {
		return new Date(created.getTime());
	}

	public Date getUpdated() {
		return new Date(updated.getTime());
	}

	public String toString() {
		return "\"type\":\"" + this.getClass().getSimpleName() + "\", \"id\":" + getId() + "\", \"created\":" + getCreated().getTime() + ", \"updated\":" + getUpdated().getTime();
	}

}
