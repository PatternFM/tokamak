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
import fm.pattern.jwt.server.validation.UniqueValue;
import fm.pattern.validation.sequences.CreateLevel1;
import fm.pattern.validation.sequences.CreateLevel2;
import fm.pattern.validation.sequences.CreateLevel4;
import fm.pattern.validation.sequences.UpdateLevel1;
import fm.pattern.validation.sequences.UpdateLevel2;
import fm.pattern.validation.sequences.UpdateLevel4;

@Entity(name = "Accounts")
@UniqueValue(property = "username", message = "{account.username.conflict}", groups = { CreateLevel4.class, UpdateLevel4.class })
public class Account extends PersistentEntity {

	private static final long serialVersionUID = 2435019868978460407L;

	@Column(name = "username", nullable = false, unique = true)
	@NotBlank(message = "{account.username.required}", groups = { CreateLevel1.class, UpdateLevel1.class })
	@Size(min = 3, max = 128, message = "{account.username.size}", groups = { CreateLevel2.class, UpdateLevel2.class })
	private String username;

	@Column(name = "password", nullable = false)
	@NotBlank(message = "{account.password.required}", groups = { CreateLevel1.class, UpdateLevel1.class })
	@Size(min = 8, max = 255, message = "{account.password.size}", groups = { CreateLevel2.class, UpdateLevel2.class })
	private String password;

	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name = "AccountRoles", joinColumns = { @JoinColumn(name = "account_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") })
	private Set<Role> roles = new HashSet<Role>();

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public int hashCode() {
		return Objects.hashCode(getId());
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
