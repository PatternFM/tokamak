package fm.pattern.jwt.server.security;

import java.util.Set;

public class AuthenticationContext {

	private String accountId;
	private Set<String> userRoles;

	private String clientId;
	private Set<String> clientRoles;

	public AuthenticationContext() {
		this.accountId = AuthenticationContextExtractor.getAccountId();
		this.userRoles = AuthenticationContextExtractor.getUserRoles();
		this.clientId = AuthenticationContextExtractor.getClientId();
		this.clientRoles = AuthenticationContextExtractor.getClientRoles();
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Set<String> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<String> userRoles) {
		this.userRoles = userRoles;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Set<String> getClientRoles() {
		return clientRoles;
	}

	public void setClientRoles(Set<String> clientRoles) {
		this.clientRoles = clientRoles;
	}

}
