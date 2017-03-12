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

import static fm.pattern.jwt.sdk.dsl.AccountDSL.account;
import static fm.pattern.jwt.sdk.dsl.AudienceDSL.audience;
import static fm.pattern.jwt.sdk.dsl.AuthorityDSL.authority;
import static fm.pattern.jwt.sdk.dsl.GrantTypeDSL.grantType;
import static fm.pattern.jwt.sdk.dsl.RoleDSL.role;
import static fm.pattern.jwt.sdk.dsl.ScopeDSL.scope;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.jwt.sdk.model.AccountRepresentation;
import fm.pattern.jwt.sdk.model.AudienceRepresentation;
import fm.pattern.jwt.sdk.model.AuthorityRepresentation;
import fm.pattern.jwt.sdk.model.GrantTypeRepresentation;
import fm.pattern.jwt.sdk.model.RoleRepresentation;
import fm.pattern.jwt.sdk.model.ScopeRepresentation;
import fm.pattern.jwt.server.IntegrationTest;
import fm.pattern.jwt.server.model.Account;
import fm.pattern.jwt.server.model.Audience;
import fm.pattern.jwt.server.model.Authority;
import fm.pattern.jwt.server.model.GrantType;
import fm.pattern.jwt.server.model.Role;
import fm.pattern.jwt.server.model.Scope;

public class IngressConversionServiceTest extends IntegrationTest {

	@Autowired
	private IngressConversionService ingress;

	@Test
	public void shouldBeAbleToConvertAnAccountRepresentationIntoAnAccount() {
		AccountRepresentation representation = account().build();
		Account account = ingress.convert(representation);

		assertThat(representation.getUsername()).isEqualTo(account.getUsername());
		for (RoleRepresentation role : representation.getRoles()) {
			assertThat(account.getRoles()).extracting("id").contains(role.getId());
		}
	}

	@Test
	public void shouldBeAbleToConvertARoleIntoARoleRepresentation() {
		RoleRepresentation representation = role().build();
		Role role = ingress.convert(representation);

		assertThat(role.getName()).isEqualTo(representation.getName());
		assertThat(role.getDescription()).isEqualTo(representation.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertAnAuthorityIntoAnAuthorityRepresentation() {
		AuthorityRepresentation representation = authority().build();
		Authority authority = ingress.convert(representation);

		assertThat(authority.getName()).isEqualTo(representation.getName());
		assertThat(authority.getDescription()).isEqualTo(representation.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertAGrantTypeIntoAGrantTypeRepresentation() {
		GrantTypeRepresentation representation = grantType().build();
		GrantType grantType = ingress.convert(representation);

		assertThat(grantType.getName()).isEqualTo(representation.getName());
		assertThat(grantType.getDescription()).isEqualTo(representation.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertAScopeIntoAScopeRepresentation() {
		ScopeRepresentation representation = scope().build();
		Scope scope = ingress.convert(representation);

		assertThat(scope.getName()).isEqualTo(representation.getName());
		assertThat(scope.getDescription()).isEqualTo(representation.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertAnAudienceIntoAnAudienceRepresentation() {
		AudienceRepresentation representation = audience().build();
		Audience audience = ingress.convert(representation);

		assertThat(audience.getName()).isEqualTo(representation.getName());
		assertThat(audience.getDescription()).isEqualTo(representation.getDescription());
	}

}
