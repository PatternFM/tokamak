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

import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import fm.pattern.jwt.server.model.Client;

public class AuthenticatedClient extends BaseClientDetails implements ClientDetails {

	private static final long serialVersionUID = 782533447342660L;

	private Client client;

	public AuthenticatedClient(Client client) {
		super.setClientId(client.getClientId());
		super.setClientSecret(client.getClientSecret());

		super.setScope(client.getScopes().stream().map(scope -> scope.getName()).collect(Collectors.toCollection(HashSet::new)));
		super.setAuthorities(client.getAuthorities().stream().map(authority -> new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toCollection(HashSet::new)));
		super.setAuthorizedGrantTypes(client.getGrantTypes().stream().map(grantType -> grantType.getName().toLowerCase()).collect(Collectors.toCollection(HashSet::new)));
		super.setResourceIds(client.getAudiences().stream().map(audience -> audience.getName().toLowerCase()).collect(Collectors.toCollection(HashSet::new)));
		super.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
		super.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());

		this.client = client;
	}

	public Client getClient() {
		return client;
	}

}