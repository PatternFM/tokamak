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
