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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.google.common.base.Objects;

import fm.pattern.commons.util.IdGenerator;
import fm.pattern.commons.util.JSON;
import fm.pattern.tokamak.server.validation.UniqueValue;
import fm.pattern.valex.sequences.CreateLevel1;
import fm.pattern.valex.sequences.CreateLevel2;
import fm.pattern.valex.sequences.CreateLevel4;
import fm.pattern.valex.sequences.UpdateLevel1;
import fm.pattern.valex.sequences.UpdateLevel2;
import fm.pattern.valex.sequences.UpdateLevel4;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "Accounts")
@UniqueValue(property = "username", message = "{account.username.conflict}", groups = { CreateLevel4.class, UpdateLevel4.class })
public class Account extends PersistentEntity {

	private static final long serialVersionUID = 2435019868978460407L;

	@Getter
	@Setter
	@Column(name = "username", nullable = false, unique = true)
	@NotBlank(message = "{account.username.required}", groups = { CreateLevel1.class, UpdateLevel1.class })
	@Size(min = 3, max = 128, message = "{account.username.size}", groups = { CreateLevel2.class, UpdateLevel2.class })
	private String username;

	@Getter
	@Setter
	@Column(name = "password", nullable = false)
	@NotBlank(message = "{account.password.required}", groups = { CreateLevel1.class, UpdateLevel1.class })
	@Size(min = 8, max = 255, message = "{account.password.size}", groups = { CreateLevel2.class, UpdateLevel2.class })
	private String password;

	@Getter
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "AccountRoles", joinColumns = { @JoinColumn(name = "account_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") })
	private Set<Role> roles = new HashSet<Role>();

	@Getter
	@Setter
	@Column(name = "locked", nullable = false)
	private boolean locked;

	Account() {

	}

	public Account(String username, String password, Set<Role> roles) {
		super(IdGenerator.generateId("acc", 30));

		this.username = username;
		this.password = password;
		this.roles.addAll(roles);
		this.locked = false;
	}

	public Account addRole(Role role) {
		if (role != null) {
			this.roles.add(role);
		}
		return this;
	}

	public boolean hasRole(Role role) {
		return roles.stream().filter(r -> r.equals(role)).count() != 0;
	}

	public int hashCode() {
		return Objects.hashCode(getId());
	}

	public void setRoles(Set<Role> roles) {
		this.roles.clear();
		if (roles != null) {
			this.roles.addAll(roles);
		}
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Account)) {
			return false;
		}

		final Account account = (Account) obj;
		return equal(this.getId(), account.getId());
	}

	public String toString() {
		return JSON.stringify(this);
	}

}
