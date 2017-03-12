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

package fm.pattern.tokamak.server.conversion;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fm.pattern.tokamak.sdk.commons.ErrorRepresentation;
import fm.pattern.tokamak.sdk.commons.ErrorsRepresentation;
import fm.pattern.tokamak.sdk.model.AccountRepresentation;
import fm.pattern.tokamak.sdk.model.AudienceRepresentation;
import fm.pattern.tokamak.sdk.model.AuthorityRepresentation;
import fm.pattern.tokamak.sdk.model.ClientRepresentation;
import fm.pattern.tokamak.sdk.model.GrantTypeRepresentation;
import fm.pattern.tokamak.sdk.model.RoleRepresentation;
import fm.pattern.tokamak.sdk.model.ScopeRepresentation;
import fm.pattern.tokamak.server.model.Account;
import fm.pattern.tokamak.server.model.Audience;
import fm.pattern.tokamak.server.model.Authority;
import fm.pattern.tokamak.server.model.Client;
import fm.pattern.tokamak.server.model.GrantType;
import fm.pattern.tokamak.server.model.Role;
import fm.pattern.tokamak.server.model.Scope;
import fm.pattern.valex.ReportableException;

@Service
class EgressConversionServiceImpl implements EgressConversionService {

	EgressConversionServiceImpl() {

	}

	public AccountRepresentation convert(Account account) {
		AccountRepresentation representation = new AccountRepresentation();
		representation.setId(account.getId());
		representation.setCreated(account.getCreated());
		representation.setUpdated(account.getUpdated());
		representation.setUsername(account.getUsername());
		representation.setLocked(account.isLocked());
		representation.setRoles(account.getRoles().stream().map(role -> convert(role)).collect(Collectors.toSet()));
		return representation;
	}

	public ClientRepresentation convert(Client client) {
		ClientRepresentation representation = new ClientRepresentation();
		representation.setId(client.getId());
		representation.setCreated(client.getCreated());
		representation.setUpdated(client.getUpdated());
		representation.setClientId(client.getClientId());
		representation.setAudiences(client.getAudiences().stream().map(audience -> convert(audience)).collect(Collectors.toCollection(HashSet::new)));
		representation.setAuthorities(client.getAuthorities().stream().map(authority -> convert(authority)).collect(Collectors.toCollection(HashSet::new)));
		representation.setGrantTypes(client.getGrantTypes().stream().map(grantType -> convert(grantType)).collect(Collectors.toCollection(HashSet::new)));
		representation.setScopes(client.getScopes().stream().map(scope -> convert(scope)).collect(Collectors.toCollection(HashSet::new)));
		representation.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
		representation.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
		return representation;
	}

	public ErrorsRepresentation convert(ReportableException exception) {
		List<ErrorRepresentation> errors = exception.getErrors().stream().map(e -> new ErrorRepresentation(e.getCode(), e.getMessage())).collect(Collectors.toList());
		return new ErrorsRepresentation(errors);
	}

	public RoleRepresentation convert(Role role) {
		RoleRepresentation representation = new RoleRepresentation(role.getId());
		representation.setCreated(role.getCreated());
		representation.setUpdated(role.getUpdated());
		representation.setName(role.getName());
		representation.setDescription(role.getDescription());
		return representation;
	}

	public AuthorityRepresentation convert(Authority authority) {
		AuthorityRepresentation representation = new AuthorityRepresentation(authority.getId());
		representation.setCreated(authority.getCreated());
		representation.setUpdated(authority.getUpdated());
		representation.setName(authority.getName());
		representation.setDescription(authority.getDescription());
		return representation;
	}

	public AudienceRepresentation convert(Audience audience) {
		AudienceRepresentation representation = new AudienceRepresentation(audience.getId());
		representation.setCreated(audience.getCreated());
		representation.setUpdated(audience.getUpdated());
		representation.setName(audience.getName());
		representation.setDescription(audience.getDescription());
		return representation;
	}

	public GrantTypeRepresentation convert(GrantType grantType) {
		GrantTypeRepresentation representation = new GrantTypeRepresentation(grantType.getId());
		representation.setCreated(grantType.getCreated());
		representation.setUpdated(grantType.getUpdated());
		representation.setName(grantType.getName());
		representation.setDescription(grantType.getDescription());
		return representation;
	}

	public ScopeRepresentation convert(Scope scope) {
		ScopeRepresentation representation = new ScopeRepresentation(scope.getId());
		representation.setCreated(scope.getCreated());
		representation.setUpdated(scope.getUpdated());
		representation.setName(scope.getName());
		representation.setDescription(scope.getDescription());
		return representation;
	}

}
