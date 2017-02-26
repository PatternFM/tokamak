package fm.pattern.jwt.server.service;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.jwt.server.model.Account;
import fm.pattern.jwt.server.repository.AccountRepository;
import fm.pattern.jwt.server.security.PasswordEncodingService;
import fm.pattern.microstructure.Result;
import fm.pattern.microstructure.ValidationService;
import fm.pattern.microstructure.sequences.Create;

@Service
class AccountServiceImpl extends DataServiceImpl<Account> implements AccountService {

	private final AccountRepository accountRepository;
	private final ValidationService validationService;
	private final PasswordEncodingService passwordEncodingService;

	@Autowired
	public AccountServiceImpl(AccountRepository accountRepository, ValidationService validationService, PasswordEncodingService passwordEncodingService) {
		this.accountRepository = accountRepository;
		this.validationService = validationService;
		this.passwordEncodingService = passwordEncodingService;
	}

	@Transactional
	public Result<Account> create(Account account) {
		Result<Account> result = validationService.validate(account, Create.class);
		if (result.rejected()) {
			return result;
		}
		
		account.setPassword(passwordEncodingService.encode(account.getPassword()));
		return accountRepository.save(account);
	}

	@Transactional(readOnly = true)
	public Result<Account> findById(String id) {
		return super.findById(id, Account.class);
	}

	@Transactional(readOnly = true)
	public Result<Account> findByUsername(String username) {
		if (isBlank(username)) {
			return Result.reject("{account.get.username.required}");
		}

		Account account = accountRepository.findByUsername(username);
		return account != null ? Result.accept(account) : Result.not_found("No such username: " + username);
	}

	@Transactional
	public Result<Account> updatePassword(Account account, String currentPassword, String newPassword) {
		if (isBlank(currentPassword)) {
			return Result.reject("Your current password must be provided.");
		}
		if (isBlank(newPassword)) {
			return Result.reject("Your new password must be provided.");
		}
		if (!passwordEncodingService.matches(currentPassword, account.getPassword())) {
			return Result.reject("The password you provided does not match your current password. Please try again.");
		}
		if (newPassword.length() < 8 || newPassword.length() > 50) {
			return Result.reject("Your new password must be between 8 and 50 characters.");
		}

		account.setPassword(passwordEncodingService.encode(newPassword));
		return update(account);
	}

}
