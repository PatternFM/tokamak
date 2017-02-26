package fm.pattern.jwt.server.conversion;

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

public interface IngressConversionService {

	Account convert(AccountRepresentation representation);

	Client convert(ClientRepresentation representation);

	Role convert(RoleRepresentation representation);

	Authority convert(AuthorityRepresentation representation);

	Scope convert(ScopeRepresentation representation);

	GrantType convert(GrantTypeRepresentation representation);

	Account update(AccountRepresentation representation, Account account);

	Client update(ClientRepresentation representation, Client client);

	Role update(RoleRepresentation representation, Role role);

	Authority update(AuthorityRepresentation representation, Authority authority);

	Scope update(ScopeRepresentation representation, Scope scope);

	GrantType update(GrantTypeRepresentation representation, GrantType grantType);

}
