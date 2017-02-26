package fm.pattern.jwt.server.conversion;

import fm.pattern.commons.rest.ErrorsRepresentation;
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
import fm.pattern.microstructure.exceptions.ConsumableException;

public interface EgressConversionService {

	AccountRepresentation convert(Account account);

	ClientRepresentation convert(Client client);

	RoleRepresentation convert(Role role);

	AuthorityRepresentation convert(Authority authority);

	ScopeRepresentation convert(Scope scope);

	GrantTypeRepresentation convert(GrantType grantType);

	ErrorsRepresentation convert(ConsumableException exception);

}
