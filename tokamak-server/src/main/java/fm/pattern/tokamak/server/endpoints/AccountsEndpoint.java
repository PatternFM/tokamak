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

package fm.pattern.tokamak.server.endpoints;

import static fm.pattern.tokamak.server.pagination.Criteria.criteria;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fm.pattern.tokamak.authorization.Authorize;
import fm.pattern.tokamak.authorization.OAuth2AuthorizationContext;
import fm.pattern.tokamak.sdk.commons.PaginatedListRepresentation;
import fm.pattern.tokamak.sdk.model.AccountRepresentation;
import fm.pattern.tokamak.sdk.model.SecretsRepresentation;
import fm.pattern.tokamak.server.conversion.AccountConversionService;
import fm.pattern.tokamak.server.conversion.PaginatedListConversionService;
import fm.pattern.tokamak.server.model.Account;
import fm.pattern.tokamak.server.pagination.PaginatedList;
import fm.pattern.tokamak.server.service.AccountService;

@RestController
public class AccountsEndpoint extends Endpoint {

	private final AccountService accountService;
	private final AccountConversionService accountConversionService;
	private final PaginatedListConversionService paginatedListConversionService;

	@Autowired
	public AccountsEndpoint(AccountService accountService, AccountConversionService accountConversionService, PaginatedListConversionService paginatedListConversionService) {
		this.accountService = accountService;
		this.accountConversionService = accountConversionService;
		this.paginatedListConversionService = paginatedListConversionService;
	}

	@Authorize(scopes = "accounts:create")
	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(value = "/v1/accounts", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public AccountRepresentation create(@RequestBody AccountRepresentation representation) {
		Account account = accountConversionService.convert(representation);
		Account created = accountService.create(account).orThrow();
		return accountConversionService.convert(accountService.findById(created.getId()).orThrow());
	}

	@Authorize(scopes = "accounts:update")
	@RequestMapping(value = "/v1/accounts/{id}", method = PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public AccountRepresentation update(@PathVariable String id, @RequestBody AccountRepresentation representation) {
		Account account = accountConversionService.convert(representation, accountService.findById(id).orThrow());
		Account updated = accountService.update(account).orThrow();
		return accountConversionService.convert(accountService.findById(updated.getId()).orThrow());
	}

	@Authorize(scopes = "accounts:update", roles = "tokamak:admin,tokamak:user")
	@RequestMapping(value = "/v1/accounts/{id}/password", method = PUT, consumes = "application/json", produces = "application/json")
	public AccountRepresentation updatePassword(@PathVariable String id, @RequestBody SecretsRepresentation representation) {
		Set<String> roles = new OAuth2AuthorizationContext().getRoles();

		if (roles.contains("tokamak:admin")) {
			Account account = accountService.findById(id).orThrow();
			Account updated = accountService.updatePassword(account, representation.getNewSecret()).orThrow();
			return accountConversionService.convert(accountService.findById(updated.getId()).orThrow());
		}

		Account account = accountService.findByUsername(new OAuth2AuthorizationContext().getUsername()).orThrow();
		Account updated = accountService.updatePassword(account, representation.getCurrentSecret(), representation.getNewSecret()).orThrow();
		return accountConversionService.convert(accountService.findById(updated.getId()).orThrow());
	}

	@Authorize(scopes = "accounts:delete")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/v1/accounts/{id}", method = DELETE)
	public void delete(@PathVariable String id) {
		Account account = accountService.findById(id).orThrow();
		accountService.delete(account).orThrow();
	}

	@Authorize(scopes = "accounts:read")
	@RequestMapping(value = "/v1/accounts/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
	public AccountRepresentation findById(@PathVariable String id) {
		return accountConversionService.convert(accountService.findById(id).orThrow());
	}

	@Authorize(scopes = "accounts:read")
	@RequestMapping(value = "/v1/accounts/username/{username}", method = GET, produces = APPLICATION_JSON_VALUE)
	public AccountRepresentation findByUsername(@PathVariable String username) {
		return accountConversionService.convert(accountService.findByUsername(username).orThrow());
	}

	@Authorize(scopes = "accounts:read")
	@RequestMapping(value = "/v1/accounts", method = GET, produces = APPLICATION_JSON_VALUE)
	public PaginatedListRepresentation<AccountRepresentation> list(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) {
		PaginatedList<Account> accounts = (PaginatedList<Account>) accountService.list(criteria().page(page).limit(limit)).orThrow();
		PaginatedListRepresentation<AccountRepresentation> representation = paginatedListConversionService.convert(accounts, AccountRepresentation.class);
		return representation.withPayload(accounts.stream().map(a -> accountConversionService.convert(a)).collect(Collectors.toList()));
	}

}
