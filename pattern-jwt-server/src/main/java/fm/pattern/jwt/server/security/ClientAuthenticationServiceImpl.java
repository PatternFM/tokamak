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
