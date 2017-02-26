package fm.pattern.jwt.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fm.pattern.jwt.server.model.Account;
import fm.pattern.jwt.server.service.AccountService;

@Service("authenticationService")
class AccountAuthenticationServiceImpl implements AccountAuthenticationService {

	private final AccountService accountService;

	@Autowired
	AccountAuthenticationServiceImpl(AccountService accountService) {
		this.accountService = accountService;
	}

	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) {
		Account account = accountService.findByUsername(username).getInstance();
		if (account == null || account.isLocked()) {
			throw new UsernameNotFoundException("Could not find an active account with email address: " + username);
		}
		return new AuthenticatedAccount(account);
	}

}
