package fm.pattern.jwt.server.service;

import fm.pattern.jwt.server.model.Account;
import fm.pattern.microstructure.Result;

public interface AccountService {

	Result<Account> create(Account account);

	Result<Account> update(Account account);

	Result<Account> delete(Account account);

	Result<Account> findById(String id);

	Result<Account> findByUsername(String username);

	// TODO: Create UpdatePasswordRequest object, with JSR validations
	Result<Account> updatePassword(Account account, String currentPassword, String newPassword);

}
