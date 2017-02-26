package fm.pattern.jwt.server.repository;

import fm.pattern.jwt.server.model.Client;

public interface ClientRepository extends DataRepository {

	Client findByUsername(String username);

}
