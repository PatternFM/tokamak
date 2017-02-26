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

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fm.pattern.jwt.sdk.model.ClientRepresentation;
import fm.pattern.jwt.server.conversion.EgressConversionService;
import fm.pattern.jwt.server.conversion.IngressConversionService;
import fm.pattern.jwt.server.model.Client;
import fm.pattern.jwt.server.service.ClientService;

@RestController
public class ClientsEndpoint extends Endpoint {

	private final ClientService clientService;
	private final IngressConversionService ingress;
	private final EgressConversionService egress;

	@Autowired
	public ClientsEndpoint(ClientService clientService, IngressConversionService ingress, EgressConversionService egress) {
		this.clientService = clientService;
		this.ingress = ingress;
		this.egress = egress;
	}

	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(value = "/v1/clients", method = POST, consumes = "application/json", produces = "application/json")
	public ClientRepresentation create(@RequestBody ClientRepresentation representation) {
		Client client = ingress.convert(representation);
		Client created = validate(clientService.create(client));
		return egress.convert(validate(clientService.findById(created.getId())));
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/v1/clients/{id}", method = PUT, consumes = "application/json", produces = "application/json")
	public ClientRepresentation update(@PathVariable String id, @RequestBody ClientRepresentation representation) {
		Client client = ingress.update(representation, validate(clientService.findById(id)));
		return egress.convert(validate(clientService.update(client)));
	}

}
