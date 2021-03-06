package fm.pattern.tokamak.server.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import java.util.HashSet;
import java.util.Set;

import fm.pattern.tokamak.server.model.Account;
import fm.pattern.tokamak.server.model.Role;
import fm.pattern.tokamak.server.service.AccountService;
import fm.pattern.valex.Result;

public class AccountDSL extends AbstractDSL<AccountDSL, Account> {

	private String username = randomAlphabetic(20);
	private String password = randomAlphanumeric(8) + "aA9!";
	private Set<Role> roles = new HashSet<Role>();
	private boolean locked = false;

	public static AccountDSL account() {
		return new AccountDSL();
	}

	public AccountDSL withUsername(String username) {
		this.username = username;
		return this;
	}

	public AccountDSL withPassword(String password) {
		this.password = password;
		return this;
	}

	public AccountDSL withRole(Role role) {
		this.roles.add(role);
		return this;
	}

	public AccountDSL locked() {
		this.locked = true;
		return this;
	}

	public Account build() {
		return create();
	}

	public Account save() {
		Result<Account> result = load(AccountService.class).create(create());
		if (result.accepted()) {
			return result.getInstance();
		}
		throw new IllegalStateException("Unable to create account, errors:" + result.getErrors().toString());
	}

	private Account create() {
		Account account = new Account(username, password, roles);
		account.setLocked(locked);
		return account;
	}

}
