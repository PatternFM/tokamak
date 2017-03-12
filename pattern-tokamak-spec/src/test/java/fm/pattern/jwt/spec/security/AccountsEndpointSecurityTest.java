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
import static fm.pattern.tokamak.sdk.dsl.AccountDSL.account;
import static fm.pattern.tokamak.sdk.dsl.ClientDSL.client;

import org.junit.Before;
import org.junit.Test;

import fm.pattern.jwt.spec.AcceptanceTest;
import fm.pattern.tokamak.sdk.AccountsClient;
import fm.pattern.tokamak.sdk.JwtClientProperties;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.AccessTokenRepresentation;
import fm.pattern.tokamak.sdk.model.AccountRepresentation;
import fm.pattern.tokamak.sdk.model.ClientRepresentation;

public class AccountsEndpointSecurityTest extends AcceptanceTest {

	private AccountsClient accountsClient = new AccountsClient(JwtClientProperties.getEndpoint());

	private ClientRepresentation client;
	private AccessTokenRepresentation token;

	@Before
	public void before() {
		this.token = token().withClient(TEST_CLIENT_CREDENTIALS).thatIs().persistent().build();
		this.client = client().withClientSecret("client_secret").withScopes("clients:read").withGrantTypes("client_credentials", "refresh_token").thatIs().persistent(token).build();
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToCreateAnAccount() {
		Result<AccountRepresentation> response = accountsClient.create(account().build(), null);
		assertThat(response).rejected().withResponseCode(401).withCode("AUT-0001").withMessage("Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToCreateAnAccount() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), "client_secret").thatIs().persistent().build();

		Result<AccountRepresentation> response = accountsClient.create(account().build(), t.getAccessToken());
		assertThat(response).rejected().withResponseCode(403).withCode("ATZ-0001").withMessage("Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToUpdateAnAccount() {
		Result<AccountRepresentation> response = accountsClient.update(account().withId("12345").build(), null);
		assertThat(response).rejected().withResponseCode(401).withCode("AUT-0001").withMessage("Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToUpdateAnAccount() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), "client_secret").thatIs().persistent().build();

		Result<AccountRepresentation> response = accountsClient.update(account().withId("12345").build(), t.getAccessToken());
		assertThat(response).rejected().withResponseCode(403).withCode("ATZ-0001").withMessage("Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToDeleteAnAccount() {
		Result<AccountRepresentation> response = accountsClient.delete("123456", null);
		assertThat(response).rejected().withResponseCode(401).withCode("AUT-0001").withMessage("Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToDeleteAnAccount() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), "client_secret").thatIs().persistent().build();

		Result<AccountRepresentation> response = accountsClient.delete("12345", t.getAccessToken());
		assertThat(response).rejected().withResponseCode(403).withCode("ATZ-0001").withMessage("Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToFindAnAccountById() {
		Result<AccountRepresentation> response = accountsClient.findById("12345", null);
		assertThat(response).rejected().withResponseCode(401).withCode("AUT-0001").withMessage("Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToFindAnAccountById() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), "client_secret").thatIs().persistent().build();

		Result<AccountRepresentation> response = accountsClient.findById("12345", t.getAccessToken());
		assertThat(response).rejected().withResponseCode(403).withCode("ATZ-0001").withMessage("Insufficient scope for this resource.");
	}

	@Test
	public void clientsThatDoNotPresentAnAccessTokenShouldNotBeAbleToFindAnAccountByUsername() {
		Result<AccountRepresentation> response = accountsClient.findByUsername("username", null);
		assertThat(response).rejected().withResponseCode(401).withCode("AUT-0001").withMessage("Full authentication is required to access this resource.");
	}

	@Test
	public void clientsThatDoNotHaveTheAppropriateScopeShouldNotBeAbleToFindAnAccountByUsername() {
		AccessTokenRepresentation t = token().withClient(client.getClientId(), "client_secret").thatIs().persistent().build();

		Result<AccountRepresentation> response = accountsClient.findByUsername("username", t.getAccessToken());
		assertThat(response).rejected().withResponseCode(403).withCode("ATZ-0001").withMessage("Insufficient scope for this resource.");
	}

}
