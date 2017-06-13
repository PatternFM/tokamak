package fm.pattern.tokamak.sdk.model;

import java.util.HashSet;
import java.util.Set;

public class ClientRepresentation extends EntityRepresentation {

	private String clientId;
	private String clientSecret;
	private String name;

	private Set<AuthorityRepresentation> authorities = new HashSet<AuthorityRepresentation>();
	private Set<AudienceRepresentation> audiences = new HashSet<AudienceRepresentation>();
	private Set<GrantTypeRepresentation> grantTypes = new HashSet<GrantTypeRepresentation>();
	private Set<ScopeRepresentation> scopes = new HashSet<ScopeRepresentation>();

	private Integer accessTokenValiditySeconds;
	private Integer refreshTokenValiditySeconds;

	public ClientRepresentation() {

	}

	public ClientRepresentation(String id) {
		super(id);
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<AuthorityRepresentation> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<AuthorityRepresentation> authorities) {
		this.authorities = authorities;
	}

	public Set<AudienceRepresentation> getAudiences() {
		return audiences;
	}

	public void setAudiences(Set<AudienceRepresentation> audiences) {
		this.audiences = audiences;
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
