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

import fm.pattern.tokamak.server.model.Account;
import fm.pattern.valex.Result;
import fm.pattern.valex.annotations.Create;
import fm.pattern.valex.annotations.Delete;
import fm.pattern.valex.annotations.Update;

public interface AccountService {

	Result<Account> create(@Create Account account);

	Result<Account> update(@Update Account account);

	Result<Account> delete(@Delete Account account);

	Result<Account> findById(String id);

	Result<Account> findByUsername(String username);

	Result<Account> updatePassword(Account account, String currentPassword, String newPassword);

}
