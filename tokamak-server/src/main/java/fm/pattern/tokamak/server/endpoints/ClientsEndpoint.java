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

import static fm.pattern.tokamak.server.repository.Criteria.criteria;
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
import fm.pattern.tokamak.sdk.model.ClientRepresentation;
import fm.pattern.tokamak.sdk.model.SecretsRepresentation;
import fm.pattern.tokamak.server.conversion.ClientConversionService;
import fm.pattern.tokamak.server.conversion.PaginatedListConversionService;
import fm.pattern.tokamak.server.model.Client;
import fm.pattern.tokamak.server.repository.PaginatedList;
import fm.pattern.tokamak.server.service.ClientService;

@RestController
public class ClientsEndpoint extends Endpoint {

	private final ClientService clientService;
	private final ClientConversionService clientConversionService;
	private final PaginatedListConversionService paginatedListConversionService;

	@Autowired
	public ClientsEndpoint(ClientService clientService, ClientConversionService clientConversionService, PaginatedListConversionService paginatedListConversionService) {
		this.clientService = clientService;
		this.clientConversionService = clientConversionService;
		this.paginatedListConversionService = paginatedListConversionService;
	}

	@Authorize(scopes = "clients:create")
	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(value = "/v1/clients", method = POST, consumes = "application/json", produces = "application/json")
	public ClientRepresentation create(@RequestBody ClientRepresentation representation) {
		Client client = clientConversionService.convert(representation);
		Client created = clientService.create(client).orThrow();
		return clientConversionService.convert(clientService.findById(created.getId()).orThrow());
	}

	@Authorize(scopes = "clients:update")
	@RequestMapping(value = "/v1/clients/{id}", method = PUT, consumes = "application/json", produces = "application/json")
	public ClientRepresentation update(@PathVariable String id, @RequestBody ClientRepresentation representation) {
		Client client = clientConversionService.convert(representation, clientService.findById(id).orThrow());
		Client updated = clientService.update(client).orThrow();
		return clientConversionService.convert(clientService.findById(updated.getId()).orThrow());
	}

	@Authorize(scopes = "clients:update", roles = "tokamak:admin,tokamak:user")
	@RequestMapping(value = "/v1/clients/{id}/secrets", method = PUT, consumes = "application/json", produces = "application/json")
	public ClientRepresentation updateSecret(@PathVariable String id, @RequestBody SecretsRepresentation representation) {
		Client client = clientService.findById(id).orThrow();
		Set<String> roles = new OAuth2AuthorizationContext().getRoles();
		
		if (roles.contains("tokamak:admin")) {
			Client updated = clientService.updateClientSecret(client, representation.getNewSecret()).orThrow();
			return clientConversionService.convert(clientService.findById(updated.getId()).orThrow());
		}

		Client updated = clientService.updateClientSecret(client, representation.getCurrentSecret(), representation.getNewSecret()).orThrow();
		return clientConversionService.convert(clientService.findById(updated.getId()).orThrow());
	}

	@Authorize(scopes = "clients:delete")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/v1/clients/{id}", method = DELETE)
	public void delete(@PathVariable String id) {
		Client client = clientService.findById(id).orThrow();
		clientService.delete(client).orThrow();
	}

	@Authorize(scopes = "clients:read")
	@RequestMapping(value = "/v1/clients/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
	public ClientRepresentation findById(@PathVariable String id) {
		return clientConversionService.convert(clientService.findById(id).orThrow());
	}

	@Authorize(scopes = "clients:read")
	@RequestMapping(value = "/v1/clients", method = GET, produces = APPLICATION_JSON_VALUE)
	public PaginatedListRepresentation<ClientRepresentation> list(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) {
		PaginatedList<Client> clients = (PaginatedList<Client>) clientService.list(criteria().page(page).limit(limit)).orThrow();
		PaginatedListRepresentation<ClientRepresentation> representation = paginatedListConversionService.convert(clients, ClientRepresentation.class);
		return representation.withPayload(clients.stream().map(a -> clientConversionService.convert(a)).collect(Collectors.toList()));
	}

}
