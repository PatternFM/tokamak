package fm.pattern.jwt.server.repository;

import org.springframework.stereotype.Repository;

import fm.pattern.jwt.server.model.Account;

@Repository
class AccountRepositoryImpl extends DataRepositoryImpl implements AccountRepository {

	public Account findByUsername(String username) {
		return (Account) query("from Accounts where username = :username").setString("username", username).uniqueResult();
	}

}
