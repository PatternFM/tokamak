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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.google.common.collect.Sets;

import fm.pattern.commons.util.IdGenerator;
import fm.pattern.commons.util.JSON;
import fm.pattern.jwt.server.validation.UniqueValue;
import fm.pattern.microstructure.sequences.CreateLevel1;
import fm.pattern.microstructure.sequences.CreateLevel2;
import fm.pattern.microstructure.sequences.CreateLevel4;
import fm.pattern.microstructure.sequences.UpdateLevel1;
import fm.pattern.microstructure.sequences.UpdateLevel2;
import fm.pattern.microstructure.sequences.UpdateLevel4;

@Entity(name = "Clients")
@UniqueValue(property = "username", message = "{client.username.conflict}", groups = { CreateLevel4.class, UpdateLevel4.class })
public class Client extends PersistentEntity {

	private static final long serialVersionUID = -229014499144213599L;

	@Column(name = "username", nullable = false, length = 120, updatable = false, unique = true)
	@NotBlank(message = "{client.username.required}", groups = { CreateLevel1.class, UpdateLevel1.class })
	@Size(min = 10, max = 128, message = "{client.username.size}", groups = { CreateLevel2.class, UpdateLevel2.class })
	private String username;

	@Column(name = "password", nullable = false, updatable = false, length = 255)
	@NotBlank(message = "{client.password.required}", groups = { CreateLevel1.class, UpdateLevel1.class })
	@Size(min = 10, max = 255, message = "{client.password.size}", groups = { CreateLevel2.class, UpdateLevel2.class })
	private String password;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "ClientAuthorities", joinColumns = { @JoinColumn(name = "client_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "authority_id", referencedColumnName = "id") })
	private Set<Authority> authorities = new HashSet<Authority>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "ClientScopes", joinColumns = { @JoinColumn(name = "client_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "scope_id", referencedColumnName = "id") })
	private Set<Scope> scopes = new HashSet<Scope>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "ClientGrantTypes", joinColumns = { @JoinColumn(name = "client_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "grant_type_id", referencedColumnName = "id") })
	@NotNull(message = "{client.grantType.required}", groups = { CreateLevel1.class, UpdateLevel1.class })
	@Size(min = 1, message = "{client.grantType.required}", groups = { CreateLevel1.class, UpdateLevel1.class })
	private Set<GrantType> grantTypes = new HashSet<GrantType>();

	@Column(name = "access_token_validity_seconds")
	private Integer accessTokenValiditySeconds;

	@Column(name = "refresh_token_validity_seconds")
	private Integer refreshTokenValiditySeconds;

	Client() {
		super(IdGenerator.generateId("cli", 30));
	}

	public Client(String username, String password, Set<Authority> authorities, Set<GrantType> grantTypes, Set<Scope> scope) {
		this();
		this.username = username;
		this.password = password;
		this.authorities.addAll(authorities);
		this.grantTypes.addAll(grantTypes);
		this.scopes.addAll(scope);
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

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public Set<Scope> getScopes() {
		return scopes;
	}

	public void setScopes(Set<Scope> scopes) {
		this.scopes = scopes;
	}

	public Set<GrantType> getGrantTypes() {
		return grantTypes;
	}

	public void setGrantTypes(Set<GrantType> grantTypes) {
		this.grantTypes = grantTypes;
	}

	public Set<String> getResourceIds() {
		return Sets.newHashSet("oauth-service");
	}

	public Integer getAccessTokenValiditySeconds() {
		return accessTokenValiditySeconds;
	}

	public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}

	public Integer getRefreshTokenValiditySeconds() {
		return refreshTokenValiditySeconds;
	}

	public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}

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

		final Client client = (Client) obj;
		return getId().equalsIgnoreCase(client.getId());
	}

	public String toString() {
		return JSON.stringify(this);
	}

}
