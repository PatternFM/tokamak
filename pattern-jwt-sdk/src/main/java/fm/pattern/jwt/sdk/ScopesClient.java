package fm.pattern.jwt.sdk;

import fm.pattern.commons.rest.RestClient;
import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.model.ScopeRepresentation;
import fm.pattern.jwt.sdk.model.ScopesRepresentation;

public class ScopesClient extends RestClient {

    public ScopesClient(String endpoint) {
        super(endpoint);
    }

    public Result<ScopeRepresentation> create(ScopeRepresentation representation, String token) {
        return post(resource("/v1/scopes"), representation, ScopeRepresentation.class, token);
    }

    public Result<ScopeRepresentation> update(ScopeRepresentation representation, String token) {
        return put(resource("/v1/scopes/" + representation.getId()), representation, ScopeRepresentation.class, token);
    }

    public Result<ScopeRepresentation> delete(String id, String token) {
        return delete(resource("/v1/scopes/" + id), token);
    }

    public Result<ScopeRepresentation> findById(String id, String token) {
        return get(resource("/v1/scopes/" + id), ScopeRepresentation.class, token);
    }

    public Result<ScopeRepresentation> findByName(String name, String token) {
        return get(resource("/v1/scopes/name/" + name), ScopeRepresentation.class, token);
    }
    
    public Result<ScopesRepresentation> list(String token) {
        return get(resource("/v1/scopes"), ScopesRepresentation.class, token);
    }

}
