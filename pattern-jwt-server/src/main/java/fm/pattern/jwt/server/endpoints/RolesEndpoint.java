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

import fm.pattern.jwt.sdk.model.RoleRepresentation;
import fm.pattern.jwt.sdk.model.RolesRepresentation;
import fm.pattern.jwt.server.conversion.EgressConversionService;
import fm.pattern.jwt.server.conversion.IngressConversionService;
import fm.pattern.jwt.server.model.Role;
import fm.pattern.jwt.server.service.RoleService;

@RestController
public class RolesEndpoint extends Endpoint {

	private final RoleService roleService;
	private final IngressConversionService ingress;
	private final EgressConversionService egress;

	@Autowired
	public RolesEndpoint(RoleService roleService, IngressConversionService ingress, EgressConversionService egress) {
		this.roleService = roleService;
		this.ingress = ingress;
		this.egress = egress;
	}

	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(value = "/v1/roles", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public RoleRepresentation create(@RequestBody RoleRepresentation representation) {
		Role role = ingress.convert(representation);
		Role created = validate(roleService.create(role));
		return egress.convert(validate(roleService.findById(created.getId())));
	}

	@RequestMapping(value = "/v1/roles/{id}", method = PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public RoleRepresentation update(@PathVariable String id, @RequestBody RoleRepresentation representation) {
		Role role = ingress.update(representation, validate(roleService.findById(id)));
		return egress.convert(validate(roleService.update(role)));
	}

	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/v1/roles/{id}", method = DELETE)
	public void delete(@PathVariable String id) {
		Role role = validate(roleService.findById(id));
		validate(roleService.delete(role));
	}

	@RequestMapping(value = "/v1/roles/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
	public RoleRepresentation findById(@PathVariable String id) {
		Role role = validate(roleService.findById(id));
		return egress.convert(role);
	}

	@RequestMapping(value = "/v1/roles/name/{name}", method = GET, produces = APPLICATION_JSON_VALUE)
	public RoleRepresentation findByName(@PathVariable String name) {
		Role role = validate(roleService.findByName(name));
		return egress.convert(role);
	}

	@RequestMapping(value = "/v1/roles", method = GET, produces = APPLICATION_JSON_VALUE)
	public RolesRepresentation list() {
		List<Role> roles = validate(roleService.list());
		return new RolesRepresentation(roles.stream().map(role -> egress.convert(role)).collect(Collectors.toList()));
	}

}
