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

import fm.pattern.tokamak.server.model.Client;
import fm.pattern.tokamak.server.security.PasswordEncodingService;
import fm.pattern.valex.Result;

@Service
class ClientServiceImpl extends DataServiceImpl<Client> implements ClientService {

	private final PasswordEncodingService passwordEncodingService;

	@Autowired
	public ClientServiceImpl(PasswordEncodingService passwordEncodingService) {
		this.passwordEncodingService = passwordEncodingService;
	}

	@Transactional(readOnly = false)
	public Result<Client> create(Client client) {
		client.setClientSecret(passwordEncodingService.encode(client.getClientSecret()));
		return super.create(client);
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

		Result<Client> result = super.findBy("clientId", clientId, Client.class);
		return result.accepted() ? result : Result.reject("client.clientId.not_found", clientId);
	}

}
