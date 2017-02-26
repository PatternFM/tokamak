package fm.pattern.jwt.sdk.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import fm.pattern.commons.rest.Representation;

public class ClientRepresentation extends Representation {

	private String id;
	private Date created;
	private Date updated;

	private String clientId;
	private String clientSecret;

	private Set<AuthorityRepresentation> authorities = new HashSet<AuthorityRepresentation>();
	private Set<GrantTypeRepresentation> grantTypes = new HashSet<GrantTypeRepresentation>();
	private Set<ScopeRepresentation> scopes = new HashSet<ScopeRepresentation>();

	private Integer accessTokenValiditySeconds;
	private Integer refreshTokenValiditySeconds;

	public ClientRepresentation() {

	}

	public ClientRepresentation(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
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

	public void setClientSecret(String secret) {
		this.clientSecret = secret;
	}

	public Set<AuthorityRepresentation> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<AuthorityRepresentation> authorities) {
		this.authorities = authorities;
	}

	public Set<GrantTypeRepresentation> getGrantTypes() {
		return grantTypes;
	}

	public void setGrantTypes(Set<GrantTypeRepresentation> grantTypes) {
		this.grantTypes = grantTypes;
	}

	public Set<ScopeRepresentation> getScopes() {
		return scopes;
	}

	public void setScopes(Set<ScopeRepresentation> scopes) {
		this.scopes = scopes;
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

}
