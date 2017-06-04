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

package fm.pattern.tokamak.server.model;

import static com.google.common.base.Objects.equal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.google.common.base.Objects;

import fm.pattern.commons.util.IdGenerator;
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

@Entity(name = "Roles")
@UniqueValue(property = "name", message = "{role.name.conflict}", groups = { CreateLevel3.class, UpdateLevel3.class })
public class Role extends PersistentEntity {

	private static final long serialVersionUID = -5647907379923624257L;

	@Getter
	@Setter
	@Column(name = "name", nullable = false, unique = true)
	@NotBlank(message = "{role.name.required}", groups = { CreateLevel1.class, UpdateLevel1.class })
	@Size(max = 128, message = "{role.name.size}", groups = { CreateLevel2.class, UpdateLevel2.class })
	private String name;

	@Getter
	@Setter
	@Column(name = "description")
	@Size(max = 255, message = "{role.description.size}", groups = { CreateLevel2.class, UpdateLevel2.class })
	private String description;

	Role() {
		super(IdGenerator.generateId("rol", 30));
	}

	public Role(String name) {
		this();
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

		final Role role = (Role) obj;
		return equal(this.getId(), role.getId());
	}

	public String toString() {
		return JSON.stringify(this);
	}

}
