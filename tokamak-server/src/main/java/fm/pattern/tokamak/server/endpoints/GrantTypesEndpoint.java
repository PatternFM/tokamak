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
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fm.pattern.tokamak.authorization.Authorize;
import fm.pattern.tokamak.sdk.model.GrantTypeRepresentation;
import fm.pattern.tokamak.sdk.model.GrantTypesRepresentation;
import fm.pattern.tokamak.server.conversion.GrantTypeConversionService;
import fm.pattern.tokamak.server.model.GrantType;
import fm.pattern.tokamak.server.service.GrantTypeService;

@RestController
public class GrantTypesEndpoint extends Endpoint {

	private final GrantTypeService grantTypeService;
	private final GrantTypeConversionService grantTypeConversionService;

	@Autowired
	public GrantTypesEndpoint(GrantTypeService grantTypeService, GrantTypeConversionService grantTypeConversionService) {
		this.grantTypeService = grantTypeService;
		this.grantTypeConversionService = grantTypeConversionService;
	}

	@Authorize(scopes = "grant_types:read")
	@RequestMapping(value = "/v1/grant_types/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
	public GrantTypeRepresentation findById(@PathVariable String id) {
		GrantType grantType = grantTypeService.findById(id).orThrow();
		return grantTypeConversionService.convert(grantType);
	}

	@Authorize(scopes = "grant_types:read")
	@RequestMapping(value = "/v1/grant_types/name/{name}", method = GET, produces = APPLICATION_JSON_VALUE)
	public GrantTypeRepresentation findByName(@PathVariable String name) {
		GrantType grantType = grantTypeService.findByName(name).orThrow();
		return grantTypeConversionService.convert(grantType);
	}

	@Authorize(scopes = "grant_types:read")
	@RequestMapping(value = "/v1/grant_types", method = GET, produces = APPLICATION_JSON_VALUE)
	public GrantTypesRepresentation list() {
		List<GrantType> grantTypes = grantTypeService.list().orThrow();
		return new GrantTypesRepresentation(grantTypes.stream().map(grant -> grantTypeConversionService.convert(grant)).collect(Collectors.toList()));
	}

}
