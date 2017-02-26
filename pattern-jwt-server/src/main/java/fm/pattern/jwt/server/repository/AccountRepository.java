package fm.pattern.jwt.server.repository;

import fm.pattern.jwt.server.model.Account;

public interface AccountRepository extends DataRepository {

	Account findByUsername(String username);

}
