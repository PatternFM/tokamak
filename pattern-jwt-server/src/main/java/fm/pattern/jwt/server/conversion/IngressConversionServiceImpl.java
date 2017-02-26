package fm.pattern.jwt.server.conversion;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fm.pattern.jwt.sdk.model.AccountRepresentation;
import fm.pattern.jwt.sdk.model.AuthorityRepresentation;
import fm.pattern.jwt.sdk.model.ClientRepresentation;
import fm.pattern.jwt.sdk.model.GrantTypeRepresentation;
import fm.pattern.jwt.sdk.model.RoleRepresentation;
import fm.pattern.jwt.sdk.model.ScopeRepresentation;
import fm.pattern.jwt.server.model.Account;
import fm.pattern.jwt.server.model.Authority;
import fm.pattern.jwt.server.model.Client;
import fm.pattern.jwt.server.model.GrantType;
import fm.pattern.jwt.server.model.Role;
import fm.pattern.jwt.server.model.Scope;
import fm.pattern.jwt.server.service.AuthorityService;
import fm.pattern.jwt.server.service.GrantTypeService;
import fm.pattern.jwt.server.service.RoleService;
import fm.pattern.jwt.server.service.ScopeService;
import fm.pattern.microstructure.Result;

@Service
class IngressConversionServiceImpl implements IngressConversionService {

	private final RoleService roleService;
	private final AuthorityService authorityService;
	private final ScopeService scopeService;
	private final GrantTypeService grantTypeService;

	@Autowired
	public IngressConversionServiceImpl(RoleService roleService, AuthorityService authorityService, ScopeService scopeService, GrantTypeService grantTypeService) {
		this.roleService = roleService;
		this.authorityService = authorityService;
		this.scopeService = scopeService;
		this.grantTypeService = grantTypeService;
	}

	public Account convert(AccountRepresentation representation) {
		Set<Role> roles = representation.getRoles() == null ? new HashSet<Role>() : representation.getRoles().stream().map(role -> lookup(role)).filter(role -> role != null).collect(Collectors.toSet());
		return new Account(representation.getUsername(), representation.getPassword(), roles);
	}

	public Client convert(ClientRepresentation representation) {
		Set<Scope> scopes = representation.getScopes() == null ? new HashSet<Scope>() : representation.getScopes().stream().map(scope -> lookup(scope)).filter(scope -> scope != null).collect(Collectors.toSet());
		Set<GrantType> grantTypes = representation.getGrantTypes() == null ? new HashSet<GrantType>() : representation.getGrantTypes().stream().map(grant -> lookup(grant)).filter(grant -> grant != null).collect(Collectors.toSet());
		Set<Authority> authorities = representation.getAuthorities() == null ? new HashSet<Authority>() : representation.getAuthorities().stream().map(auth -> lookup(auth)).filter(auth -> auth != null).collect(Collectors.toSet());
		return new Client(representation.getClientId(), representation.getClientSecret(), authorities, grantTypes, scopes);
	}

	public Role convert(RoleRepresentation representation) {
		return new Role(representation.getName());
	}

	public Authority convert(AuthorityRepresentation representation) {
		return new Authority(representation.getName());
	}

	public Scope convert(ScopeRepresentation representation) {
		return new Scope(representation.getName());
	}

	public GrantType convert(GrantTypeRepresentation representation) {
		return new GrantType(representation.getName());
	}

	public Account update(AccountRepresentation representation, Account account) {
		Set<Role> roles = representation.getRoles() == null ? new HashSet<Role>() : representation.getRoles().stream().map(role -> lookup(role)).filter(role -> role != null).collect(Collectors.toSet());
		account.setRoles(roles);
		return account;
	}

	public Client update(ClientRepresentation representation, Client client) {
		Set<Scope> scopes = representation.getScopes() == null ? new HashSet<Scope>() : representation.getScopes().stream().map(scope -> lookup(scope)).filter(scope -> scope != null).collect(Collectors.toSet());
		Set<GrantType> grantTypes = representation.getGrantTypes() == null ? new HashSet<GrantType>() : representation.getGrantTypes().stream().map(grant -> lookup(grant)).filter(grant -> grant != null).collect(Collectors.toSet());
		Set<Authority> authorities = representation.getAuthorities() == null ? new HashSet<Authority>() : representation.getAuthorities().stream().map(auth -> lookup(auth)).filter(auth -> auth != null).collect(Collectors.toSet());

		client.setScopes(scopes);
		client.setGrantTypes(grantTypes);
		client.setAuthorities(authorities);

		client.setAccessTokenValiditySeconds(representation.getAccessTokenValiditySeconds());
		client.setRefreshTokenValiditySeconds(representation.getRefreshTokenValiditySeconds());

		return null;
	}

	public Role update(RoleRepresentation representation, Role role) {
		role.setName(representation.getName());
		return role;
	}

	public Authority update(AuthorityRepresentation representation, Authority authority) {
		authority.setName(representation.getName());
		return authority;
	}

	public Scope update(ScopeRepresentation representation, Scope scope) {
		scope.setName(representation.getName());
		return scope;
	}

	public GrantType update(GrantTypeRepresentation representation, GrantType grantType) {
		grantType.setName(representation.getName());
		return grantType;
	}

	private Role lookup(RoleRepresentation representation) {
		Result<Role> result = roleService.findById(representation.getId());
		return result.accepted() ? result.getInstance() : null;
	}

	private Authority lookup(AuthorityRepresentation representation) {
		Result<Authority> result = authorityService.findById(representation.getId());
		return result.accepted() ? result.getInstance() : null;
	}

	private GrantType lookup(GrantTypeRepresentation representation) {
		Result<GrantType> result = grantTypeService.findById(representation.getId());
		return result.accepted() ? result.getInstance() : null;
	}

	private Scope lookup(ScopeRepresentation representation) {
		Result<Scope> result = scopeService.findById(representation.getId());
		return result.accepted() ? result.getInstance() : null;
	}

}
