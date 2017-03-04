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
			return Result.reject("account.username.required");
		}

		Account account = accountRepository.findByUsername(username);
		return account != null ? Result.accept(account) : Result.not_found("account.username.not_found", username);
	}

	// TODO: Refactor into a PasswordPolicy model.
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
