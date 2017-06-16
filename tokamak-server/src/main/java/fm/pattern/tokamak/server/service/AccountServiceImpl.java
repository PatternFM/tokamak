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

package fm.pattern.tokamak.server.service;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.tokamak.server.model.Account;
import fm.pattern.tokamak.server.pagination.Collection;
import fm.pattern.tokamak.server.pagination.Criteria;
import fm.pattern.tokamak.server.security.PasswordEncodingService;
import fm.pattern.valex.Result;

@Service
@SuppressWarnings("unchecked")
class AccountServiceImpl extends DataServiceImpl<Account> implements AccountService {

	private final PasswordEncodingService passwordEncodingService;

	public AccountServiceImpl(PasswordEncodingService passwordEncodingService) {
		this.passwordEncodingService = passwordEncodingService;
	}

	@Transactional
	public Result<Account> create(Account account) {
		account.setPassword(passwordEncodingService.encode(account.getPassword()));
		return super.create(account);
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

		Result<Account> result = super.findBy("username", username, Account.class);
		return result.accepted() ? result : Result.reject("account.username.not_found", username);
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

	@Transactional(readOnly = true)
	public Result<List<Account>> list(Criteria criteria) {
		Long count = super.count(super.query("select count(account.id) from Account account"));
		List<Account> data = super.query("from Accounts order by created desc").setFirstResult(criteria.getFirstResult()).setMaxResults(criteria.getLimit()).getResultList();
		return Result.accept((List<Account>) new Collection<Account>(data, count.intValue(), criteria));
	}

}
