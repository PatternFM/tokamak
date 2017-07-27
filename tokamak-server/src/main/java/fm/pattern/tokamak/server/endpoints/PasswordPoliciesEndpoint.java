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
import fm.pattern.tokamak.sdk.model.PasswordPoliciesRepresentation;
import fm.pattern.tokamak.sdk.model.PasswordPolicyRepresentation;
import fm.pattern.tokamak.server.conversion.PasswordPolicyConversionService;
import fm.pattern.tokamak.server.model.PasswordPolicy;
import fm.pattern.tokamak.server.service.PasswordPolicyService;

@RestController
public class PasswordPoliciesEndpoint extends Endpoint {

	private final PasswordPolicyService policyService;
	private final PasswordPolicyConversionService converter;

	@Autowired
	public PasswordPoliciesEndpoint(PasswordPolicyService policyService, PasswordPolicyConversionService policyConversionService) {
		this.policyService = policyService;
		this.converter = policyConversionService;
	}

	@Authorize(scopes = "policies:create")
	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(value = "/v1/policies", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public PasswordPolicyRepresentation create(@RequestBody PasswordPolicyRepresentation representation) {
		PasswordPolicy policy = converter.convert(representation);
		PasswordPolicy created = policyService.create(policy).orThrow();
		return converter.convert(policyService.findById(created.getId()).orThrow());
	}

	@Authorize(scopes = "policies:update")
	@RequestMapping(value = "/v1/policies/{id}", method = PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public PasswordPolicyRepresentation update(@PathVariable String id, @RequestBody PasswordPolicyRepresentation representation) {
		PasswordPolicy policy = converter.convert(representation, policyService.findById(id).orThrow());
		return converter.convert(policyService.update(policy).orThrow());
	}

	@Authorize(scopes = "policies:read")
	@RequestMapping(value = "/v1/policies/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
	public PasswordPolicyRepresentation findById(@PathVariable String id) {
		PasswordPolicy policy = policyService.findById(id).orThrow();
		return converter.convert(policy);
	}

	@Authorize(scopes = "policies:read")
	@RequestMapping(value = "/v1/policies/name/{name}", method = GET, produces = APPLICATION_JSON_VALUE)
	public PasswordPolicyRepresentation findByName(@PathVariable String name) {
		PasswordPolicy policy = policyService.findByName(name).orThrow();
		return converter.convert(policy);
	}

	@Authorize(scopes = "policies:read")
	@RequestMapping(value = "/v1/policies", method = GET, produces = APPLICATION_JSON_VALUE)
	public PasswordPoliciesRepresentation list() {
		List<PasswordPolicy> policies = policyService.list().orThrow();
		return new PasswordPoliciesRepresentation(policies.stream().map(policy -> converter.convert(policy)).collect(Collectors.toList()));
	}

}
