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

import fm.pattern.tokamak.sdk.model.AuthoritiesRepresentation;
import fm.pattern.tokamak.sdk.model.AuthorityRepresentation;
import fm.pattern.tokamak.server.conversion.EgressConversionService;
import fm.pattern.tokamak.server.conversion.IngressConversionService;
import fm.pattern.tokamak.server.model.Authority;
import fm.pattern.tokamak.server.service.AuthorityService;

@RestController
public class AuthoritiesEndpoint extends Endpoint {

	private final AuthorityService authorityService;
	private final IngressConversionService ingress;
	private final EgressConversionService egress;

	@Autowired
	public AuthoritiesEndpoint(AuthorityService authorityService, IngressConversionService ingress, EgressConversionService egress) {
		this.authorityService = authorityService;
		this.ingress = ingress;
		this.egress = egress;
	}

	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(value = "/v1/authorities", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public AuthorityRepresentation create(@RequestBody AuthorityRepresentation representation) {
		Authority authority = ingress.convert(representation);
		Authority created = authorityService.create(authority).orThrow();
		return egress.convert(authorityService.findById(created.getId()).orThrow());
	}

	@RequestMapping(value = "/v1/authorities/{id}", method = PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public AuthorityRepresentation update(@PathVariable String id, @RequestBody AuthorityRepresentation representation) {
		Authority authority = ingress.update(representation, authorityService.findById(id).orThrow());
		return egress.convert(authorityService.update(authority).orThrow());
	}

	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/v1/authorities/{id}", method = DELETE)
	public void delete(@PathVariable String id) {
		Authority authority = authorityService.findById(id).orThrow();
		authorityService.delete(authority).orThrow();
	}

	@RequestMapping(value = "/v1/authorities/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
	public AuthorityRepresentation findById(@PathVariable String id) {
		Authority authority = authorityService.findById(id).orThrow();
		return egress.convert(authority);
	}

	@RequestMapping(value = "/v1/authorities/name/{name}", method = GET, produces = APPLICATION_JSON_VALUE)
	public AuthorityRepresentation findByName(@PathVariable String name) {
		Authority authority = authorityService.findByName(name).orThrow();
		return egress.convert(authority);
	}

	@RequestMapping(value = "/v1/authorities", method = GET, produces = APPLICATION_JSON_VALUE)
	public AuthoritiesRepresentation list() {
		List<Authority> authorities = authorityService.list().orThrow();
		return new AuthoritiesRepresentation(authorities.stream().map(authority -> egress.convert(authority)).collect(Collectors.toList()));
	}

}
