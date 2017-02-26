package fm.pattern.jwt.server.service;

import fm.pattern.jwt.server.model.Client;
import fm.pattern.microstructure.Result;

public interface ClientService {

	Result<Client> create(Client client);

	Result<Client> update(Client client);
	
	Result<Client> delete(Client client);

	Result<Client> findById(String id);

	Result<Client> findByUsername(String username);

}
