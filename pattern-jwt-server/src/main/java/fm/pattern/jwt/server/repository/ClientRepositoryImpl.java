package fm.pattern.jwt.server.repository;

import org.springframework.stereotype.Repository;

import fm.pattern.jwt.server.model.Client;

@Repository
class ClientRepositoryImpl extends DataRepositoryImpl implements ClientRepository {

	public Client findByUsername(String username) {
		return (Client) query("from Clients where username = :username").setString("username", username).uniqueResult();
	}

}
