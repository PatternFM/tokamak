package fm.pattern.jwt.sdk;

import fm.pattern.commons.rest.RestClient;
import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.model.AccountRepresentation;

public class AccountsClient extends RestClient {

    public AccountsClient(String endpoint) {
        super(endpoint);
    }

    public Result<AccountRepresentation> create(AccountRepresentation representation, String token) {
        return post(resource("/v1/accounts"), representation, AccountRepresentation.class, token);
    }

    public Result<AccountRepresentation> update(AccountRepresentation representation, String token) {
        return put(resource("/v1/accounts/" + representation.getId()), representation, AccountRepresentation.class, token);
    }

    public Result<AccountRepresentation> delete(String id, String token) {
        return delete(resource("/v1/accounts/" + id), token);
    }

    public Result<AccountRepresentation> findById(String id, String token) {
        return get(resource("/v1/accounts/" + id), AccountRepresentation.class, token);
    }

    public Result<AccountRepresentation> findByUsername(String username, String token) {
        return get(resource("/v1/accounts/username/" + username), AccountRepresentation.class, token);
    }

}
