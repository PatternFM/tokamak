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

import static fm.pattern.tokamak.server.dsl.AccountDSL.account;
import static fm.pattern.tokamak.server.dsl.AudienceDSL.audience;
import static fm.pattern.tokamak.server.dsl.AuthorityDSL.authority;
import static fm.pattern.tokamak.server.dsl.ClientDSL.client;
import static fm.pattern.tokamak.server.dsl.GrantTypeDSL.grantType;
import static fm.pattern.tokamak.server.dsl.RoleDSL.role;
import static fm.pattern.tokamak.server.dsl.ScopeDSL.scope;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.tokamak.sdk.commons.ErrorsRepresentation;
import fm.pattern.tokamak.sdk.model.AccountRepresentation;
import fm.pattern.tokamak.sdk.model.AudienceRepresentation;
import fm.pattern.tokamak.sdk.model.AuthorityRepresentation;
import fm.pattern.tokamak.sdk.model.ClientRepresentation;
import fm.pattern.tokamak.sdk.model.GrantTypeRepresentation;
import fm.pattern.tokamak.sdk.model.RoleRepresentation;
import fm.pattern.tokamak.sdk.model.ScopeRepresentation;
import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.conversion.EgressConversionService;
import fm.pattern.tokamak.server.model.Account;
import fm.pattern.tokamak.server.model.Audience;
import fm.pattern.tokamak.server.model.Authority;
import fm.pattern.tokamak.server.model.Client;
import fm.pattern.tokamak.server.model.GrantType;
import fm.pattern.tokamak.server.model.Role;
import fm.pattern.tokamak.server.model.Scope;
import fm.pattern.valex.Reportable;
import fm.pattern.valex.ReportableException;
import fm.pattern.valex.UnprocessableEntityException;

public class EgressConversionServiceTest extends IntegrationTest {

	@Autowired
	private EgressConversionService egress;

	@Test
	public void shouldBeAbleToConvertAnAccountIntoAnAccountRepresentation() {
		Account account = account().withRole(role().thatIs().persistent().build()).thatIs().persistent().build();
		AccountRepresentation representation = egress.convert(account);

		assertThat(representation.getId()).isEqualTo(account.getId());
		assertThat(representation.getCreated()).isEqualTo(account.getCreated());
		assertThat(representation.getUpdated()).isEqualTo(account.getUpdated());
		assertThat(representation.getUsername()).isEqualTo(account.getUsername());
		assertThat(representation.isLocked()).isEqualTo(account.isLocked());

		for (RoleRepresentation role : representation.getRoles()) {
			assertThat(account.getRoles()).extracting("id").contains(role.getId());
		}
	}

	@Test
	public void shouldBeAbleToConvertAClientIntoAClientRepresentation() {
		Client client = client().withGrantType(grantType().thatIs().persistent().build()).withAudience(audience().thatIs().persistent().build()).withAuthority(authority().thatIs().persistent().build()).withScope(scope().thatIs().persistent().build()).build();
		ClientRepresentation representation = egress.convert(client);

		assertThat(representation.getId()).isEqualTo(client.getId());
		assertThat(representation.getCreated()).isEqualTo(client.getCreated());
		assertThat(representation.getUpdated()).isEqualTo(client.getUpdated());

		for (ScopeRepresentation scope : representation.getScopes()) {
			assertThat(client.getScopes()).extracting("id").contains(scope.getId());
		}

		for (AuthorityRepresentation authority : representation.getAuthorities()) {
			assertThat(client.getAuthorities()).extracting("id").contains(authority.getId());
		}

		for (AudienceRepresentation audience : representation.getAudiences()) {
			assertThat(client.getAudiences()).extracting("id").contains(audience.getId());
		}

		for (GrantTypeRepresentation grantType : representation.getGrantTypes()) {
			assertThat(client.getGrantTypes()).extracting("id").contains(grantType.getId());
		}
	}

	@Test
	public void shouldBeAbleToConvertARoleIntoARoleRepresentation() {
		Role role = role().thatIs().persistent().build();
		RoleRepresentation representation = egress.convert(role);

		assertThat(representation.getId()).isEqualTo(role.getId());
		assertThat(representation.getCreated()).isEqualTo(role.getCreated());
		assertThat(representation.getUpdated()).isEqualTo(role.getUpdated());
		assertThat(representation.getName()).isEqualTo(role.getName());
		assertThat(representation.getDescription()).isEqualTo(role.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertAnAuthorityIntoAnAuthorityRepresentation() {
		Authority authority = authority().thatIs().persistent().build();
		AuthorityRepresentation representation = egress.convert(authority);

		assertThat(representation.getId()).isEqualTo(authority.getId());
		assertThat(representation.getCreated()).isEqualTo(authority.getCreated());
		assertThat(representation.getUpdated()).isEqualTo(authority.getUpdated());
		assertThat(representation.getName()).isEqualTo(authority.getName());
		assertThat(representation.getDescription()).isEqualTo(authority.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertAGrantTypeIntoAGrantTypeRepresentation() {
		GrantType grantType = grantType().thatIs().persistent().build();
		GrantTypeRepresentation representation = egress.convert(grantType);

		assertThat(representation.getId()).isEqualTo(grantType.getId());
		assertThat(representation.getCreated()).isEqualTo(grantType.getCreated());
		assertThat(representation.getUpdated()).isEqualTo(grantType.getUpdated());
		assertThat(representation.getName()).isEqualTo(grantType.getName());
		assertThat(representation.getDescription()).isEqualTo(grantType.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertAScopeIntoAScopeRepresentation() {
		Scope scope = scope().thatIs().persistent().build();
		ScopeRepresentation representation = egress.convert(scope);

		assertThat(representation.getId()).isEqualTo(scope.getId());
		assertThat(representation.getCreated()).isEqualTo(scope.getCreated());
		assertThat(representation.getUpdated()).isEqualTo(scope.getUpdated());
		assertThat(representation.getName()).isEqualTo(scope.getName());
		assertThat(representation.getDescription()).isEqualTo(scope.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertAnAudienceIntoAAudienceRepresentation() {
		Audience audience = audience().thatIs().persistent().build();
		AudienceRepresentation representation = egress.convert(audience);

		assertThat(representation.getId()).isEqualTo(audience.getId());
		assertThat(representation.getCreated()).isEqualTo(audience.getCreated());
		assertThat(representation.getUpdated()).isEqualTo(audience.getUpdated());
		assertThat(representation.getName()).isEqualTo(audience.getName());
		assertThat(representation.getDescription()).isEqualTo(audience.getDescription());
	}

	@Test
	public void shouldBeAbleToConvertAReportableExceptionIntoAnErrorsRepresentation() {
		ReportableException exception = new UnprocessableEntityException(Reportable.report("client.id.required"));
		ErrorsRepresentation errors = egress.convert(exception);

		assertThat(errors.getErrors()).hasSize(1);
	}

}