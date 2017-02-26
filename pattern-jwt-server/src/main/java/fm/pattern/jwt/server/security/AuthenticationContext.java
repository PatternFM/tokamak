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
