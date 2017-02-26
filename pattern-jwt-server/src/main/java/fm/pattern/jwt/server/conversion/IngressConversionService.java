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

public interface IngressConversionService {

	Account convert(AccountRepresentation representation);

	Client convert(ClientRepresentation representation);

	Role convert(RoleRepresentation representation);

	Authority convert(AuthorityRepresentation representation);

	Scope convert(ScopeRepresentation representation);

	GrantType convert(GrantTypeRepresentation representation);

	Account update(AccountRepresentation representation, Account account);

	Client update(ClientRepresentation representation, Client client);

	Role update(RoleRepresentation representation, Role role);

	Authority update(AuthorityRepresentation representation, Authority authority);

	Scope update(ScopeRepresentation representation, Scope scope);

	GrantType update(GrantTypeRepresentation representation, GrantType grantType);

}
