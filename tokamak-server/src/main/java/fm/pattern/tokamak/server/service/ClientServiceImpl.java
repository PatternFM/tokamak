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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.minimal.JSON;
import fm.pattern.tokamak.server.model.Client;
import fm.pattern.tokamak.server.model.SerializedClient;
import fm.pattern.tokamak.server.repository.SerializedClientRepository;
import fm.pattern.tokamak.server.security.PasswordEncodingService;
import fm.pattern.valex.Reportable;
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

	@Transactional
	public Result<Client> create(Client client) {
		client.setClientSecret(passwordEncodingService.encode(client.getClientSecret()));
		Result<Client> result = super.create(client);
		serializedClientRepository.save(result.getInstance().serialize());
		return result;
	}

	@Transactional
	public Result<Client> update(Client client) {
		Result<Client> result = super.update(client);
		serializedClientRepository.update(result.getInstance().serialize());
		return result;
	}

	@Transactional
	public Result<Client> delete(Client client) {
		Result<Client> result = super.delete(client);
		serializedClientRepository.delete(result.getInstance().serialize());
		return result;
	}

	@Transactional
	public Result<Client> findById(String id) {
		if (isBlank(id)) {
			return Result.reject("client.id.required");
		}

		Result<SerializedClient> result = serializedClientRepository.findById(id);
		if (result.accepted()) {
			return Result.accept(JSON.parse(result.getInstance().getPayload(), Client.class));
		}

		Result<Client> source = super.findById(id, Client.class);
		if (source.accepted()) {
			serializedClientRepository.save(source.getInstance().serialize());
			return source;
		}

		return Result.reject(result.getErrors().toArray(new Reportable[result.getErrors().size()]));
	}

	@Transactional
	public Result<Client> findByClientId(String clientId) {
		if (isBlank(clientId)) {
			return Result.reject("client.clientId.required");
		}

		Result<SerializedClient> result = serializedClientRepository.findByClientId(clientId);
		if (result.accepted()) {
			return Result.accept(JSON.parse(result.getInstance().getPayload(), Client.class));
		}

		Result<Client> source = super.findBy("clientId", clientId, Client.class);
		if (source.accepted()) {
			serializedClientRepository.save(source.getInstance().serialize());
			return source;
		}

		return Result.reject(result.getErrors().toArray(new Reportable[result.getErrors().size()]));
	}

}
