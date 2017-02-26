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

package fm.pattern.jwt.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import fm.pattern.jwt.server.model.Client;
import fm.pattern.jwt.server.service.ClientService;
import fm.pattern.microstructure.Result;

@Service
class ClientAuthenticationServiceImpl implements ClientAuthenticationService {

	private final ClientService clientService;

	@Autowired
	public ClientAuthenticationServiceImpl(ClientService clientService) {
		this.clientService = clientService;
	}

	public ClientDetails loadClientByClientId(String id) throws ClientRegistrationException {
		Result<Client> result = clientService.findByUsername(id);
		if (result.rejected()) {
			throw new UsernameNotFoundException("Could not find client with client id " + id);
		}
		return new AuthenticatedClient(result.getInstance());
	}

}
