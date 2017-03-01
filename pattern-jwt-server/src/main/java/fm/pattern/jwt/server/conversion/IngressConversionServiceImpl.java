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

package fm.pattern.jwt.server.conversion;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fm.pattern.jwt.sdk.model.AccountRepresentation;
import fm.pattern.jwt.sdk.model.AuthorityRepresentation;
import fm.pattern.jwt.sdk.model.ClientRepresentation;
import fm.pattern.jwt.sdk.model.GrantTypeRepresentation;
import fm.pattern.jwt.sdk.model.RoleRepresentation;
import fm.pattern.jwt.sdk.model.ScopeRepresentation;
import fm.pattern.jwt.server.model.Account;
import fm.pattern.jwt.server.model.Authority;
import fm.pattern.jwt.server.model.Client;
import fm.pattern.jwt.server.model.GrantType;
import fm.pattern.jwt.server.model.Role;
import fm.pattern.jwt.server.model.Scope;
import fm.pattern.jwt.server.service.AuthorityService;
import fm.pattern.jwt.server.service.GrantTypeService;
import fm.pattern.jwt.server.service.RoleService;
import fm.pattern.jwt.server.service.ScopeService;
import fm.pattern.microstructure.Result;

@Service
class IngressConversionServiceImpl implements IngressConversionService {

	private final RoleService roleService;
	private final AuthorityService authorityService;
	private final ScopeService scopeService;
	private final GrantTypeService grantTypeService;

	@Autowired
	public IngressConversionServiceImpl(RoleService roleService, AuthorityService authorityService, ScopeService scopeService, GrantTypeService grantTypeService) {
		this.roleService = roleService;
		this.authorityService = authorityService;
		this.scopeService = scopeService;
		this.grantTypeService = grantTypeService;
	}

	public Account convert(AccountRepresentation representation) {
		Set<Role> roles = representation.getRoles() == null ? new HashSet<Role>() : representation.getRoles().stream().map(role -> lookup(role)).filter(role -> role != null).collect(Collectors.toSet());
		return new Account(representation.getUsername(), representation.getPassword(), roles);
	}

	public Client convert(ClientRepresentation representation) {
		Set<Scope> scopes = representation.getScopes() == null ? new HashSet<Scope>() : representation.getScopes().stream().map(scope -> lookup(scope)).filter(scope -> scope != null).collect(Collectors.toSet());
		Set<GrantType> grantTypes = representation.getGrantTypes() == null ? new HashSet<GrantType>() : representation.getGrantTypes().stream().map(grant -> lookup(grant)).filter(grant -> grant != null).collect(Collectors.toSet());
		Set<Authority> authorities = representation.getAuthorities() == null ? new HashSet<Authority>() : representation.getAuthorities().stream().map(auth -> lookup(auth)).filter(auth -> auth != null).collect(Collectors.toSet());

		Client client = new Client(representation.getClientId(), representation.getClientSecret(), authorities, grantTypes, scopes);

		if (representation.getAccessTokenValiditySeconds() != null) {
			client.setAccessTokenValiditySeconds(representation.getAccessTokenValiditySeconds());
		}
		if (representation.getRefreshTokenValiditySeconds() != null) {
			client.setRefreshTokenValiditySeconds(representation.getRefreshTokenValiditySeconds());
		}

		return client;

	}

	public Role convert(RoleRepresentation representation) {
		Role role = new Role(representation.getName());
		role.setDescription(representation.getDescription());
		return role;
	}

	public Authority convert(AuthorityRepresentation representation) {
		Authority authority = new Authority(representation.getName());
		authority.setDescription(representation.getDescription());
		return authority;
	}

	public Scope convert(ScopeRepresentation representation) {
		Scope scope = new Scope(representation.getName());
		scope.setDescription(representation.getDescription());
		return scope;
	}

	public GrantType convert(GrantTypeRepresentation representation) {
		GrantType grantType = new GrantType(representation.getName());
		grantType.setDescription(representation.getDescription());
		return grantType;
	}

	public Account convert(AccountRepresentation representation, Account account) {
		Set<Role> roles = representation.getRoles() == null ? new HashSet<Role>() : representation.getRoles().stream().map(role -> lookup(role)).filter(role -> role != null).collect(Collectors.toSet());
		account.setRoles(roles);
		account.setLocked(representation.isLocked());
		return account;
	}

	public Client update(ClientRepresentation representation, Client client) {
		Set<Scope> scopes = representation.getScopes() == null ? new HashSet<Scope>() : representation.getScopes().stream().map(scope -> lookup(scope)).filter(scope -> scope != null).collect(Collectors.toSet());
		Set<GrantType> grantTypes = representation.getGrantTypes() == null ? new HashSet<GrantType>() : representation.getGrantTypes().stream().map(grant -> lookup(grant)).filter(grant -> grant != null).collect(Collectors.toSet());
		Set<Authority> authorities = representation.getAuthorities() == null ? new HashSet<Authority>() : representation.getAuthorities().stream().map(auth -> lookup(auth)).filter(auth -> auth != null).collect(Collectors.toSet());

		client.setScopes(scopes);
		client.setGrantTypes(grantTypes);
		client.setAuthorities(authorities);

		client.setAccessTokenValiditySeconds(representation.getAccessTokenValiditySeconds());
		client.setRefreshTokenValiditySeconds(representation.getRefreshTokenValiditySeconds());

		return client;
	}

	public Role update(RoleRepresentation representation, Role role) {
		role.setName(representation.getName());
		role.setDescription(representation.getDescription());
		return role;
	}

	public Authority update(AuthorityRepresentation representation, Authority authority) {
		authority.setName(representation.getName());
		authority.setDescription(representation.getDescription());
		return authority;
	}

	public Scope update(ScopeRepresentation representation, Scope scope) {
		scope.setName(representation.getName());
		scope.setDescription(representation.getDescription());
		return scope;
	}

	public GrantType update(GrantTypeRepresentation representation, GrantType grantType) {
		grantType.setName(representation.getName());
		grantType.setDescription(representation.getDescription());
		return grantType;
	}

	private Role lookup(RoleRepresentation representation) {
		Result<Role> result = roleService.findById(representation.getId());
		return result.accepted() ? result.getInstance() : null;
	}

	private Authority lookup(AuthorityRepresentation representation) {
		Result<Authority> result = authorityService.findById(representation.getId());
		return result.accepted() ? result.getInstance() : null;
	}

	private GrantType lookup(GrantTypeRepresentation representation) {
		Result<GrantType> result = grantTypeService.findById(representation.getId());
		return result.accepted() ? result.getInstance() : null;
	}

	private Scope lookup(ScopeRepresentation representation) {
		Result<Scope> result = scopeService.findById(representation.getId());
		return result.accepted() ? result.getInstance() : null;
	}

}
