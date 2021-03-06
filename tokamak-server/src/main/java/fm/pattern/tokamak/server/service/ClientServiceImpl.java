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

package fm.pattern.tokamak.server.service;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.tokamak.server.model.Client;
import fm.pattern.tokamak.server.model.PasswordPolicy;
import fm.pattern.tokamak.server.repository.Cache;
import fm.pattern.tokamak.server.repository.Criteria;
import fm.pattern.tokamak.server.repository.PaginatedList;
import fm.pattern.tokamak.server.validation.PasswordValidator;
import fm.pattern.valex.Reportable;
import fm.pattern.valex.Result;

@Service
@SuppressWarnings("unchecked")
class ClientServiceImpl extends DataServiceImpl<Client> implements ClientService {

	private static final String id_key = "clients:id:%s";
	private static final String client_id_key = "clients:client_id:%s";

	private final PasswordEncodingService passwordEncodingService;
	private final PasswordPolicyService passwordPolicyService;
	private final PasswordValidator passwordValidator;
	private final Cache cache;

	@Autowired
	public ClientServiceImpl(PasswordEncodingService passwordEncodingService, PasswordPolicyService passwordPolicyService, PasswordValidator passwordValidator, @Qualifier("clientCache") Cache cache) {
		this.passwordEncodingService = passwordEncodingService;
		this.passwordPolicyService = passwordPolicyService;
		this.passwordValidator = passwordValidator;
		this.cache = cache;
	}

	@Transactional
	public Result<Client> create(Client client) {
		PasswordPolicy policy = passwordPolicyService.findByName("client-password-policy").orThrow();

		Result<String> password = passwordValidator.validate(client.getClientSecret(), policy);
		if (password.rejected()) {
			return Result.reject(password.getErrors().toArray(new Reportable[password.getErrors().size()]));
		}

		Result<Client> result = super.create(client.clientSecret(passwordEncodingService.encode(client.getClientSecret())));
		if (result.accepted()) {
			cache(result.getInstance());
		}

		return result;
	}

	@Transactional
	public Result<Client> update(Client client) {
		Result<Client> result = super.update(client);
		if (result.accepted()) {
			cache(result.getInstance());
		}
		return result;
	}

	@Transactional
	public Result<Client> updateClientSecret(Client client, String newSecret) {
		PasswordPolicy policy = passwordPolicyService.findByName("client-password-policy").orThrow();

		Result<String> result = passwordValidator.validate(newSecret, policy);
		if (result.rejected()) {
			return Result.reject(result.getErrors().toArray(new Reportable[result.getErrors().size()]));
		}

		return update(client.clientSecret(passwordEncodingService.encode(newSecret)));
	}

	@Transactional
	public Result<Client> updateClientSecret(Client client, String currentSecret, String newSecret) {
		if (isBlank(currentSecret)) {
			return Result.reject("current.secret.required");
		}
		if (!passwordEncodingService.matches(currentSecret, client.getClientSecret())) {
			return Result.reject("current.secret.mismatch");
		}
		return updateClientSecret(client, newSecret);
	}

	@Transactional
	public Result<Client> delete(Client client) {
		Result<Client> result = super.delete(client);
		if (result.accepted()) {
			cache.delete(String.format(id_key, client.getId()));
			cache.delete(String.format(client_id_key, client.getClientId()));
		}
		return result;
	}

	@Transactional(readOnly = true)
	public Result<Client> findById(String id) {
		Client client = cache.get(String.format(id_key, id), Client.class);
		if (client != null) {
			return Result.accept(client);
		}

		Result<Client> result = super.findById(id, Client.class);
		if (result.accepted()) {
			cache(result.getInstance());
		}

		return result;
	}

	@Transactional(readOnly = true)
	public Result<Client> findByClientId(String clientId) {
		if (isBlank(clientId)) {
			return Result.reject("client.clientId.required");
		}

		Client client = cache.get(String.format(client_id_key, clientId), Client.class);
		if (client != null) {
			return Result.accept(client);
		}

		Result<Client> result = super.findBy("clientId", clientId, Client.class);
		if (result.accepted()) {
			cache(result.getInstance());
		}

		return result.accepted() ? result : Result.reject("client.clientId.not_found", clientId);
	}

	@Transactional(readOnly = true)
	public Result<List<Client>> list(Criteria criteria) {
		Long count = super.count(super.query("select count(client.id) from Clients client"));
		List<Client> clients = super.query("from Clients order by created desc").setFirstResult(criteria.getFirstResult()).setMaxResults(criteria.getLimit()).getResultList();
		return Result.accept((List<Client>) new PaginatedList<Client>(clients, count.intValue(), criteria));
	}

	private void cache(Client client) {
		cache.put(String.format(id_key, client.getId()), client);
		cache.put(String.format(client_id_key, client.getClientId()), client);
	}

}
