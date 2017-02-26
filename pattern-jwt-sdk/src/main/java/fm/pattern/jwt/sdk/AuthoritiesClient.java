package fm.pattern.jwt.sdk;

import fm.pattern.commons.rest.RestClient;
import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.model.AuthoritiesRepresentation;
import fm.pattern.jwt.sdk.model.AuthorityRepresentation;

public class AuthoritiesClient extends RestClient {

    public AuthoritiesClient(String endpoint) {
        super(endpoint);
    }

    public Result<AuthorityRepresentation> create(AuthorityRepresentation representation, String token) {
        return post(resource("/v1/authorities"), representation, AuthorityRepresentation.class, token);
    }

    public Result<AuthorityRepresentation> update(AuthorityRepresentation representation, String token) {
        return put(resource("/v1/authorities/" + representation.getId()), representation, AuthorityRepresentation.class, token);
    }

    public Result<AuthorityRepresentation> delete(String id, String token) {
        return delete(resource("/v1/authorities/" + id), token);
    }

    public Result<AuthorityRepresentation> findById(String id, String token) {
        return get(resource("/v1/authorities/" + id), AuthorityRepresentation.class, token);
    }

    public Result<AuthoritiesRepresentation> list(String token) {
        return get(resource("/v1/authorities"), AuthoritiesRepresentation.class, token);
    }

}
