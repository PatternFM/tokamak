package fm.pattern.jwt.server.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import fm.pattern.jwt.server.model.Account;

public class AuthenticatedAccount implements UserDetails {

	private static final long serialVersionUID = 2452117613952715622L;
	private final Account account;

	public AuthenticatedAccount(Account account) {
		this.account = account;
	}

	public String getUsername() {
		return account.getUsername();
	}

	public String getPassword() {
		return account.getPassword();
	}

	public String getIdentfifier() {
		return account.getId();
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return account.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return !account.isLocked();
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}

}
