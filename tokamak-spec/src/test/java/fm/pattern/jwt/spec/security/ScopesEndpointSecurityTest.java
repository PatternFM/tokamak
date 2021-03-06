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

package fm.pattern.jwt.spec.security;

import static fm.pattern.jwt.spec.PatternAssertions.assertThat;
import static fm.pattern.tokamak.sdk.dsl.AccessTokenDSL.token;
import static fm.pattern.tokamak.sdk.dsl.ClientDSL.client;
import static fm.pattern.tokamak.sdk.dsl.ScopeDSL.scope;

import org.junit.Before;
import org.junit.Test;

import fm.pattern.jwt.spec.AcceptanceTest;
import fm.pattern.tokamak.sdk.JwtClientProperties;
import fm.pattern.tokamak.sdk.ScopesClient;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.AccessTokenRepresentation;
import fm.pattern.tokamak.sdk.model.ClientRepresentation;
import fm.pattern.tokamak.sdk.model.ScopeRepresentation;
import fm.pattern.tokamak.sdk.model.ScopesRepresentation;

public class ScopesEndpointSecurityTest extends AcceptanceTest {

	private ScopesClient scopesClient = new ScopesClient(JwtClientProperties.getEndpoint());

	private String secret = "sdjgi45895jasSDF#$#23";
	
	private ClientRepresentation client;
	private AccessTokenRepresentation token;

	@Before
	public void before() {
		this.token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();
		this.client = client().withClientSecret(secret).withScopes("clients:read").withGrantTypes("client_credentials", "refresh_token").thatIs().persistent(token).build();
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToCreateAScope() {
		Result<ScopeRepresentation> response = scopesClient.create(scope().build(), null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToUpdateAScope() {
		Result<ScopeRepresentation> response = scopesClient.update(scope().withId("abc").build(), null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToDeleteAScope() {
		Result<ScopeRepresentation> response = scopesClient.delete("123456", null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToFindAScopeById() {
		Result<ScopeRepresentation> response = scopesClient.findById("12345", null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToFindAScopeByName() {
		Result<ScopeRepresentation> response = scopesClient.findByName("name", null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToListScopes() {
		Result<ScopesRepresentation> response = scopesClient.list(null);
		assertThat(response).rejected().withError(401, "AUT-0001", "Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToCreateAScope() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), secret).thatIs().persistent().build();

		Result<ScopeRepresentation> response = scopesClient.create(scope().build(), t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToUpdateAScope() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), secret).thatIs().persistent().build();

		Result<ScopeRepresentation> response = scopesClient.update(scope().withId("12345").build(), t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToDeleteAScope() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), secret).thatIs().persistent().build();

		Result<ScopeRepresentation> response = scopesClient.delete("12345", t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToFindAScopeById() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), secret).thatIs().persistent().build();

		Result<ScopeRepresentation> response = scopesClient.findById("12345", t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToFindAScopeByName() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), secret).thatIs().persistent().build();

		Result<ScopeRepresentation> response = scopesClient.findByName("username", t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToListScopes() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), secret).thatIs().persistent().build();

		Result<ScopesRepresentation> response = scopesClient.list(t.getAccessToken());
		assertThat(response).rejected().withError(403, "ATZ-0001", "Insufficient scope for this resource.");
	}

}
