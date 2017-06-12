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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fm.pattern.tokamak.authorization.Authorize;
import fm.pattern.tokamak.sdk.model.ScopeRepresentation;
import fm.pattern.tokamak.sdk.model.ScopesRepresentation;
import fm.pattern.tokamak.server.conversion.ScopeConversionService;
import fm.pattern.tokamak.server.model.Scope;
import fm.pattern.tokamak.server.service.ScopeService;

@RestController
public class ScopesEndpoint extends Endpoint {

	private final ScopeService scopeService;
	private final ScopeConversionService scopeConversionService;

	@Autowired
	public ScopesEndpoint(ScopeService scopeService, ScopeConversionService scopeConversionService) {
		this.scopeService = scopeService;
		this.scopeConversionService = scopeConversionService;
	}

	@Authorize(scopes = "scopes:create")
	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(value = "/v1/scopes", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ScopeRepresentation create(@RequestBody ScopeRepresentation representation) {
		Scope scope = scopeConversionService.convert(representation);
		Scope created = scopeService.create(scope).orThrow();
		return scopeConversionService.convert(scopeService.findById(created.getId()).orThrow());
	}

	@Authorize(scopes = "scopes:update")
	@RequestMapping(value = "/v1/scopes/{id}", method = PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ScopeRepresentation update(@PathVariable String id, @RequestBody ScopeRepresentation representation) {
		Scope scope = scopeConversionService.convert(representation, scopeService.findById(id).orThrow());
		return scopeConversionService.convert(scopeService.update(scope).orThrow());
	}

	@Authorize(scopes = "scopes:delete")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/v1/scopes/{id}", method = DELETE)
	public void delete(@PathVariable String id) {
		Scope scope = scopeService.findById(id).orThrow();
		scopeService.delete(scope).orThrow();
	}

	@Authorize(scopes = "scopes:read")
	@RequestMapping(value = "/v1/scopes/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
	public ScopeRepresentation findById(@PathVariable String id) {
		Scope scope = scopeService.findById(id).orThrow();
		return scopeConversionService.convert(scope);
	}

	@Authorize(scopes = "scopes:read")
	@RequestMapping(value = "/v1/scopes/name/{name}", method = GET, produces = APPLICATION_JSON_VALUE)
	public ScopeRepresentation findByName(@PathVariable String name) {
		Scope scope = scopeService.findByName(name).orThrow();
		return scopeConversionService.convert(scope);
	}

	@Authorize(scopes = "scopes:read")
	@RequestMapping(value = "/v1/scopes", method = GET, produces = APPLICATION_JSON_VALUE)
	public ScopesRepresentation list() {
		List<Scope> scopes = scopeService.list().orThrow();
		return new ScopesRepresentation(scopes.stream().map(scope -> scopeConversionService.convert(scope)).collect(Collectors.toList()));
	}

}
