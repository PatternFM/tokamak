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

package fm.pattern.tokamak.authorization;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import fm.pattern.tokamak.authorization.AuthorizationContextProvider;

public class SimpleAuthorizationContextProvider implements AuthorizationContextProvider {

	private Set<String> scopes = new HashSet<String>();
	private Set<String> authorities = new HashSet<String>();
	private Set<String> roles = new HashSet<String>();

	private boolean authenticated = false;

	public SimpleAuthorizationContextProvider() {

	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public SimpleAuthorizationContextProvider authenticated(boolean authenticated) {
		this.authenticated = authenticated;
		return this;
	}

	public SimpleAuthorizationContextProvider withScopes(String... scopes) {
		this.scopes.addAll(Arrays.asList(scopes));
		return this;
	}

	public SimpleAuthorizationContextProvider withRoles(String... roles) {
		this.roles.addAll(Arrays.asList(roles));
		return this;
	}

	public SimpleAuthorizationContextProvider withAuthorities(String... authorities) {
		this.authorities.addAll(Arrays.asList(authorities));
		return this;
	}

	public Set<String> getScopes() {
		return scopes;
	}

	public Set<String> getAuthorities() {
		return authorities;
	}

	public Set<String> getRoles() {
		return roles;
	}

}
