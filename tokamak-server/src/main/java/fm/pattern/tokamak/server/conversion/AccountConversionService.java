package fm.pattern.tokamak.server.conversion;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fm.pattern.tokamak.sdk.model.AccountRepresentation;
import fm.pattern.tokamak.sdk.model.RoleRepresentation;
import fm.pattern.tokamak.server.model.Account;
import fm.pattern.tokamak.server.model.Role;
import fm.pattern.tokamak.server.service.RoleService;
import fm.pattern.valex.Result;

@Service
public class AccountConversionService {

	private final RoleService roleService;
	private final RoleConversionService roleConversionService;

	@Autowired
	public AccountConversionService(RoleService roleService, RoleConversionService roleConversionService) {
		this.roleService = roleService;
		this.roleConversionService = roleConversionService;
	}

	public AccountRepresentation convert(Account account) {
		AccountRepresentation representation = new AccountRepresentation();
		representation.setId(account.getId());
		representation.setCreated(account.getCreated());
		representation.setUpdated(account.getUpdated());
		representation.setUsername(account.getUsername());
		representation.setLocked(account.isLocked());
		representation.setRoles(account.getRoles().stream().map(role -> roleConversionService.convert(role)).collect(Collectors.toSet()));
		return representation;
	}

	public Account convert(AccountRepresentation representation) {
		Set<Role> roles = representation.getRoles() == null ? new HashSet<Role>() : representation.getRoles().stream().map(role -> lookup(role)).filter(role -> role != null).collect(Collectors.toSet());
		return new Account(representation.getUsername(), representation.getPassword(), roles);
	}

	public Account convert(AccountRepresentation representation, Account account) {
		Set<Role> roles = representation.getRoles() == null ? new HashSet<Role>() : representation.getRoles().stream().map(role -> lookup(role)).filter(role -> role != null).collect(Collectors.toSet());
		account.setRoles(roles);
		account.setLocked(representation.isLocked());
		return account;
	}

	private Role lookup(RoleRepresentation representation) {
		Result<Role> result = roleService.findById(representation.getId());
		return result.accepted() ? result.getInstance() : null;
	}

}
