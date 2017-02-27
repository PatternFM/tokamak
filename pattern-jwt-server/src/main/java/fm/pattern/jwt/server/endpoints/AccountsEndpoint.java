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

package fm.pattern.jwt.server.endpoints;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fm.pattern.jwt.sdk.model.AccountRepresentation;
import fm.pattern.jwt.security.authorization.Authorize;
import fm.pattern.jwt.server.conversion.EgressConversionService;
import fm.pattern.jwt.server.conversion.IngressConversionService;
import fm.pattern.jwt.server.model.Account;
import fm.pattern.jwt.server.service.AccountService;

@RestController
public class AccountsEndpoint extends Endpoint {

	private final AccountService accountService;
	private final IngressConversionService ingress;
	private final EgressConversionService egress;

	@Autowired
	public AccountsEndpoint(AccountService accountService, IngressConversionService ingress, EgressConversionService egress) {
		this.accountService = accountService;
		this.ingress = ingress;
		this.egress = egress;
	}

	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(value = "/v1/accounts", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public AccountRepresentation create(@RequestBody AccountRepresentation representation) {
		Account account = ingress.convert(representation);
		Account created = validate(accountService.create(account));
		return egress.convert(validate(accountService.findById(created.getId())));
	}

	@Authorize(scopes = "accounts:read")
	@RequestMapping(value = "/v1/accounts/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
	public AccountRepresentation findById(@PathVariable String id) {
		return egress.convert(validate(accountService.findById(id)));
	}

	@Authorize(scopes = "accounts:read")
	@RequestMapping(value = "/v1/accounts/username/{username}", method = GET, produces = APPLICATION_JSON_VALUE)
	public AccountRepresentation findByUsername(@PathVariable String username) {
		return egress.convert(validate(accountService.findByUsername(username)));
	}

}
