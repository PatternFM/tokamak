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

	@Column(name = "description")
	@Size(max = 255, message = "{scope.description.size}", groups = { CreateLevel2.class, UpdateLevel2.class })
	private String description;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
