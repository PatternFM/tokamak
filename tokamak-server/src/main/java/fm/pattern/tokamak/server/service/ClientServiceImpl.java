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

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.tokamak.server.model.Client;
import fm.pattern.tokamak.server.repository.SerializedClientRepository;
import fm.pattern.tokamak.server.security.PasswordEncodingService;
import fm.pattern.valex.Result;

@Service
class ClientServiceImpl extends DataServiceImpl<Client> implements ClientService {

	private final PasswordEncodingService passwordEncodingService;
	private final SerializedClientRepository serializedClientRepository;

	@Autowired
	public ClientServiceImpl(PasswordEncodingService passwordEncodingService, SerializedClientRepository serializedClientRepository) {
		this.passwordEncodingService = passwordEncodingService;
		this.serializedClientRepository = serializedClientRepository;
	}

	@Transactional(readOnly = false)
	public Result<Client> create(Client client) {
		client.setClientSecret(passwordEncodingService.encode(client.getClientSecret()));
		return super.create(client);
	}

	@Transactional(readOnly = true)
	public Result<Client> findById(String id) {
		if (isBlank(id)) {
			return Result.reject("client.id.required");
		}

		try {
			Client client = (Client) super.query("from Clients client left join fetch client.authorities left join fetch client.audiences left join fetch client.scopes left join fetch client.grantTypes where client.id = :id").setParameter("id", id).getSingleResult();
			return Result.accept(client);
		}
		catch (EmptyResultDataAccessException | NoResultException e) {
			return Result.reject("system.not.found", "client", id);
		}
	}

	@Transactional(readOnly = true)
	public Result<Client> findByClientId(String clientId) {
		if (isBlank(clientId)) {
			return Result.reject("client.clientId.required");
		}

		try {
			Client client = (Client) super.query("from Clients client left join fetch client.authorities left join fetch client.audiences left join fetch client.scopes left join fetch client.grantTypes where client.clientId = :clientId").setParameter("clientId", clientId).getSingleResult();
			return Result.accept(client);
		}
		catch (EmptyResultDataAccessException | NoResultException e) {
			return Result.reject("client.clientId.not_found", clientId);
		}
	}

}
