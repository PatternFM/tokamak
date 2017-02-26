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

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fm.pattern.jwt.sdk.model.GrantTypeRepresentation;
import fm.pattern.jwt.sdk.model.GrantTypesRepresentation;
import fm.pattern.jwt.server.conversion.EgressConversionService;
import fm.pattern.jwt.server.model.GrantType;
import fm.pattern.jwt.server.service.GrantTypeService;

@RestController
public class GrantTypesEndpoint extends Endpoint {

	private final GrantTypeService grantService;
	private final EgressConversionService egress;

	@Autowired
	public GrantTypesEndpoint(GrantTypeService grantService, EgressConversionService egress) {
		this.grantService = grantService;
		this.egress = egress;
	}

	@RequestMapping(value = "/v1/grant_types/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
	public GrantTypeRepresentation findById(@PathVariable String id) {
		GrantType grant = validate(grantService.findById(id));
		return egress.convert(grant);
	}

	@RequestMapping(value = "/v1/grant_types/name/{name}", method = GET, produces = APPLICATION_JSON_VALUE)
	public GrantTypeRepresentation findByName(@PathVariable String name) {
		GrantType grant = validate(grantService.findByName(name));
		return egress.convert(grant);
	}

	@RequestMapping(value = "/v1/grant_types", method = GET, produces = APPLICATION_JSON_VALUE)
	public GrantTypesRepresentation list() {
		List<GrantType> grants = validate(grantService.list());
		return new GrantTypesRepresentation(grants.stream().map(grant -> egress.convert(grant)).collect(Collectors.toList()));
	}

}
