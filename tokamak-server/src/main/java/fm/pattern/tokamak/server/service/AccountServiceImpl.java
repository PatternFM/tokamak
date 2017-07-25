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

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.tokamak.server.model.Account;
import fm.pattern.tokamak.server.model.PasswordPolicy;
import fm.pattern.tokamak.server.repository.Cache;
import fm.pattern.tokamak.server.repository.Criteria;
import fm.pattern.tokamak.server.repository.PaginatedList;
import fm.pattern.tokamak.server.validation.PasswordValidator;
import fm.pattern.valex.Reportable;
import fm.pattern.valex.Result;

@Service
@SuppressWarnings("unchecked")
class AccountServiceImpl extends DataServiceImpl<Account> implements AccountService {

	private static final String id_key = "accounts:id:%s";
	private static final String username_key = "accounts:username:%s";

	private final PasswordEncodingService passwordEncodingService;
	private final PasswordPolicyService passwordPolicyService;
	private final PasswordValidator passwordValidator;
	private final Cache cache;

	public AccountServiceImpl(PasswordEncodingService passwordEncodingService, PasswordPolicyService passwordPolicyService, PasswordValidator passwordValidator, @Qualifier("accountCache") Cache cache) {
		this.passwordEncodingService = passwordEncodingService;
		this.passwordPolicyService = passwordPolicyService;
		this.passwordValidator = passwordValidator;
		this.cache = cache;
	}

	@Transactional
	public Result<Account> create(Account account) {
		PasswordPolicy policy = passwordPolicyService.findByName("account-password-policy").orThrow();

		Result<String> password = passwordValidator.validate(account.getPassword(), policy);
		if (password.rejected()) {
			return Result.reject(password.getErrors().toArray(new Reportable[password.getErrors().size()]));
		}

		Result<Account> result = super.create(account.password(passwordEncodingService.encode(account.getPassword())));
		if (result.accepted()) {
			cache(result.getInstance());
		}

		return result;
	}

	@Transactional
	public Result<Account> update(Account account) {
		Result<Account> result = super.update(account);
		if (result.accepted()) {
			cache(result.getInstance());
		}
		return result;
	}

	@Transactional
	public Result<Account> updatePassword(Account account, String newPassword) {
		PasswordPolicy policy = passwordPolicyService.findByName("account-password-policy").orThrow();

		Result<String> result = passwordValidator.validate(newPassword, policy);
		if (result.rejected()) {
			return Result.reject(result.getErrors().toArray(new Reportable[result.getErrors().size()]));
		}

		return update(account.password(passwordEncodingService.encode(newPassword)));
	}

	@Transactional
	public Result<Account> updatePassword(Account account, String currentPassword, String newPassword) {
		if (isBlank(currentPassword)) {
			return Result.reject("current.password.required");
		}
		if (!passwordEncodingService.matches(currentPassword, account.getPassword())) {
			return Result.reject("current.password.mismatch");
		}
		return updatePassword(account, newPassword);
	}

	@Transactional
	public Result<Account> delete(Account account) {
		Result<Account> result = super.delete(account);
		if (result.accepted()) {
			cache.delete(String.format(id_key, account.getId()));
			cache.delete(String.format(username_key, account.getUsername()));
		}
		return result;
	}

	@Transactional(readOnly = true)
	public Result<Account> findById(String id) {
		Account account = cache.get(String.format(id_key, id), Account.class);
		if (account != null) {
			return Result.accept(account);
		}

		Result<Account> result = super.findById(id, Account.class);
		if (result.accepted()) {
			cache(result.getInstance());
		}

		return result;
	}

	@Transactional(readOnly = true)
	public Result<Account> findByUsername(String username) {
		if (isBlank(username)) {
			return Result.reject("account.username.required");
		}

		Result<Account> result = super.findBy("username", username, Account.class);
		if (result.accepted()) {
			cache(result.getInstance());
		}

		return result.accepted() ? result : Result.reject("account.username.not_found", username);
	}

	@Transactional(readOnly = true)
	public Result<List<Account>> list(Criteria criteria) {
		Long count = super.count(super.query("select count(account.id) from Accounts account"));
		List<Account> data = super.query("from Accounts order by username").setFirstResult(criteria.getFirstResult()).setMaxResults(criteria.getLimit()).getResultList();
		return Result.accept((List<Account>) new PaginatedList<Account>(data, count.intValue(), criteria));
	}

	private void cache(Account account) {
		cache.put(String.format(id_key, account.getId()), account);
		cache.put(String.format(username_key, account.getUsername()), account);
	}

}
