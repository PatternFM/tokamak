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
import java.util.stream.Collectors;

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

import fm.pattern.commons.util.IdGenerator;
import fm.pattern.commons.util.JSON;
import fm.pattern.jwt.server.validation.UniqueValue;
import fm.pattern.valex.sequences.CreateLevel1;
import fm.pattern.valex.sequences.CreateLevel2;
import fm.pattern.valex.sequences.CreateLevel4;
import fm.pattern.valex.sequences.UpdateLevel1;
import fm.pattern.valex.sequences.UpdateLevel2;
import fm.pattern.valex.sequences.UpdateLevel4;

@Entity(name = "Clients")
@UniqueValue(property = "clientId", message = "{client.clientId.conflict}", groups = { CreateLevel4.class, UpdateLevel4.class })
public class Client extends PersistentEntity {

	private static final long serialVersionUID = -229014499144213599L;

	@Column(name = "client_id", nullable = false, updatable = false, unique = true)
	@NotBlank(message = "{client.clientId.required}", groups = { CreateLevel1.class, UpdateLevel1.class })
	@Size(min = 10, max = 128, message = "{client.clientId.size}", groups = { CreateLevel2.class, UpdateLevel2.class })
	private String clientId;

	@Column(name = "client_secret", nullable = false, updatable = false)
	@NotBlank(message = "{client.secret.required}", groups = { CreateLevel1.class, UpdateLevel1.class })
	@Size(min = 10, max = 255, message = "{client.secret.size}", groups = { CreateLevel2.class, UpdateLevel2.class })
	private String clientSecret;

	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name = "ClientAudiences", joinColumns = { @JoinColumn(name = "client_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "audience_id", referencedColumnName = "id") })
	private Set<Audience> audiences = new HashSet<Audience>();

	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name = "ClientAuthorities", joinColumns = { @JoinColumn(name = "client_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "authority_id", referencedColumnName = "id") })
	private Set<Authority> authorities = new HashSet<Authority>();

	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name = "ClientScopes", joinColumns = { @JoinColumn(name = "client_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "scope_id", referencedColumnName = "id") })
	private Set<Scope> scopes = new HashSet<Scope>();

	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
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

	public Client(String clientId, String clientSecret, Set<Authority> authorities, Set<Audience> audiences, Set<GrantType> grantTypes, Set<Scope> scope) {
		this();

		this.clientId = clientId;
		this.clientSecret = clientSecret;

		this.authorities.addAll(authorities.stream().filter(a -> a != null).collect(Collectors.toList()));
		this.audiences.addAll(audiences.stream().filter(a -> a != null).collect(Collectors.toList()));
		this.grantTypes.addAll(grantTypes.stream().filter(a -> a != null).collect(Collectors.toList()));
		this.scopes.addAll(scope.stream().filter(a -> a != null).collect(Collectors.toList()));
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public Set<Audience> getAudiences() {
		return audiences;
	}

	public void setAudiences(Set<Audience> audiences) {
		this.audiences = audiences;
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
