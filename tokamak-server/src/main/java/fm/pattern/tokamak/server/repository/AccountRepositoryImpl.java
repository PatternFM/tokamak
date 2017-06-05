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

package fm.pattern.tokamak.server.repository;

import javax.persistence.NoResultException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import fm.pattern.tokamak.server.model.Account;

@Repository
class AccountRepositoryImpl extends DataRepositoryImpl implements AccountRepository {

	public Account findByUsername(String username) {
		try {
			return (Account) query("from Accounts where username = :username").setParameter("username", username).getSingleResult();
		}
		catch (EmptyResultDataAccessException | NoResultException e) {
			return null;
		}
	}

}
