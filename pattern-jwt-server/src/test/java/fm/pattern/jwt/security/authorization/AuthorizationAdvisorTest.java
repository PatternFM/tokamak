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

package fm.pattern.jwt.security.authorization;

import static fm.pattern.jwt.server.dsl.AuthorizationDSL.authorize;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import fm.pattern.valex.AuthorizationException;
import fm.pattern.valex.Reportable;

public class AuthorizationAdvisorTest {

	private AuthorizationAdvisor advisor;
	private SimpleAuthorizationContextProvider provider;

	@Before
	public void before() {
		this.provider = new SimpleAuthorizationContextProvider().authenticated(true);
		this.advisor = new AuthorizationAdvisor(provider);
	}

	@Test
	public void shouldThrowAnAuthorizationErrorIfTheCurrentAuthorizationContextDoesNotHaveTheSpecifiedAnnoationRole() throws Throwable {
		try {
			advisor.handle(authorize().withRoles("role:user").build());
		}
		catch (AuthorizationException e) {
			List<Reportable> errors = e.getErrors();
			Assertions.assertThat(errors).hasSize(1);
			Assertions.assertThat(errors.get(0).getCode()).isEqualTo("ATZ-0003");
			Assertions.assertThat(errors.get(0).getMessage()).isEqualTo("Access is denied.");
		}
	}

	@Test
	public void shouldBeAuthorizedWhenTheCurrentAuthorizationContextHasTheSpecifiedAnnoationRole() throws Throwable {
		provider.withRoles("role:user");
		advisor.handle(authorize().withRoles("role:user").build());
	}

	@Test
	public void shouldThrowAnAuthorizationErrorIfTheCurrentAuthorizationContextDoesNotHaveTheSpecifiedAnnoationAuthority() throws Throwable {

		try {
			advisor.handle(authorize().withAuthorities("authority:internal").build());
		}
		catch (AuthorizationException e) {
			List<Reportable> errors = e.getErrors();
			Assertions.assertThat(errors).hasSize(1);
			Assertions.assertThat(errors.get(0).getCode()).isEqualTo("ATZ-0004");
			Assertions.assertThat(errors.get(0).getMessage()).isEqualTo("Access is denied.");
		}
	}

	@Test
	public void shouldBeAuthorizedWhenTheCurrentAuthorizationContextHasTheSpecifiedAnnoationAuthority() throws Throwable {
		provider.withAuthorities("authority:internal");
		advisor.handle(authorize().withAuthorities("authority:internal").build());
	}

	@Test
	public void shouldThrowAnAuthorizationErrorIfTheCurrentAuthorizationContextDoesNotHaveTheSpecifiedAnnoationScope() throws Throwable {

		try {
			advisor.handle(authorize().withScopes("accounts:read").build());
		}
		catch (AuthorizationException e) {
			List<Reportable> errors = e.getErrors();
			Assertions.assertThat(errors).hasSize(1);
			Assertions.assertThat(errors.get(0).getCode()).isEqualTo("ATZ-0001");
			Assertions.assertThat(errors.get(0).getMessage()).isEqualTo("Insufficient scope for this resource.");
		}
	}

	@Test
	public void shouldBeAuthorizedWhenTheCurrentAuthorizationContextHasTheSpecifiedAnnoationScope() throws Throwable {
		provider.withScopes("accounts:read");
		advisor.handle(authorize().withScopes("accounts:read").build());
	}

	@Test
	public void shouldBeAuthorizedWhenTheCurrentAuthorizationContextHasTheSpecifiedAnnoationScopeAuthorityAndRole() throws Throwable {
		provider.withScopes("accounts:read").withRoles("role:user").withAuthorities("authority:internal");
		advisor.handle(authorize().withScopes("accounts:read").withRoles("role:user").withAuthorities("authority:internal").build());
	}

	@Test
	public void shouldThrowAnAuthorizationErrorWhenTheCurrentAuthorizationContextIsMissingARequiredScopeRoleOrAuthority() throws Throwable {
		provider.withScopes("accounts:read").withAuthorities("authority:internal");
		
		try {
			advisor.handle(authorize().withScopes("accounts:read").withRoles("role:user").withAuthorities("authority:internal").build());
		}
		catch (AuthorizationException e) {
			List<Reportable> errors = e.getErrors();
			Assertions.assertThat(errors).hasSize(1);
			Assertions.assertThat(errors.get(0).getCode()).isEqualTo("ATZ-0003");
			Assertions.assertThat(errors.get(0).getMessage()).isEqualTo("Access is denied.");
		}
	}
	
}
