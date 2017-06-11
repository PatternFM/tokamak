package fm.pattern.tokamak.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import fm.pattern.tokamak.sdk.commons.Representation;

public class AccessTokenRepresentation extends Representation {

	private String accessToken;
	private String refreshToken;
	private String tokenType;
	private Integer expiresIn;
	private String scope;
	private String jti;
	private String issuer;
	private String subject;

	AccessTokenRepresentation() {

	}

	public AccessTokenRepresentation(String accessToken) {
		this.accessToken = accessToken;
	}

	public AccessTokenRepresentation(String accessToken, String refreshToken, String tokenType, Integer expiresIn, String scope) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.tokenType = tokenType;
		this.expiresIn = expiresIn;
		this.scope = scope;
	}

	@JsonProperty(value = "access_token")
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@JsonProperty(value = "refresh_token")
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@JsonProperty(value = "token_type")
	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	@JsonProperty(value = "expires_in")
	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getJti() {
		return jti;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@JsonProperty(value = "iss")
	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	@JsonProperty(value = "sub")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}
