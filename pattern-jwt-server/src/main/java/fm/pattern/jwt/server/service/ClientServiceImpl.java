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

package fm.pattern.jwt.server.service;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.commons.util.ReflectionUtils;
import fm.pattern.jwt.server.model.Client;
import fm.pattern.jwt.server.repository.ClientRepository;
import fm.pattern.jwt.server.security.PasswordEncodingService;
import fm.pattern.valex.Result;
import fm.pattern.valex.ValidationService;
import fm.pattern.valex.sequences.Create;

@Service
class ClientServiceImpl extends DataServiceImpl<Client> implements ClientService {

	private PasswordEncodingService passwordEncodingService;
	private ValidationService validationService;
	private ClientRepository clientRepository;

	@Transactional(readOnly = false)
	public Result<Client> create(Client client) {
		Result<Client> result = validationService.validate(client, Create.class);
		if (result.rejected()) {
			return result;
		}
		ReflectionUtils.setValue(client, "clientSecret", passwordEncodingService.encode(client.getClientSecret()));
		return clientRepository.save(client);
	}

	@Transactional(readOnly = true)
	public Result<Client> findById(String id) {
		return super.findById(id, Client.class);
	}

	@Transactional(readOnly = true)
	public Result<Client> findByClientId(String clientId) {
		if (isBlank(clientId)) {
			return Result.reject("client.clientId.required");
		}

		Client client = clientRepository.findByClientId(clientId);
		return client != null ? Result.accept(client) : Result.reject("client.clientId.not_found", clientId);
	}

	@Autowired
	public void setPasswordEncodingService(PasswordEncodingService passwordEncodingService) {
		this.passwordEncodingService = passwordEncodingService;
	}

	@Autowired
	public void setValidationService(ValidationService validationService) {
		this.validationService = validationService;
	}

	@Autowired
	public void setClientRepository(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

}
