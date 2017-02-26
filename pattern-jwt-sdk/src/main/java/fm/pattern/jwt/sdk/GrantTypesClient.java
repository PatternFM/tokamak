package fm.pattern.jwt.sdk;

import fm.pattern.commons.rest.RestClient;
import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.model.GrantTypeRepresentation;
import fm.pattern.jwt.sdk.model.GrantTypesRepresentation;

public class GrantTypesClient extends RestClient {

    public GrantTypesClient(String endpoint) {
        super(endpoint);
    }

    public Result<GrantTypeRepresentation> findById(String id, String token) {
        return get(resource("/v1/grant_types/" + id), GrantTypeRepresentation.class, token);
    }

    public Result<GrantTypeRepresentation> findByName(String name, String token) {
        return get(resource("/v1/grant_types/name/" + name), GrantTypeRepresentation.class, token);
    }
    
    public Result<GrantTypesRepresentation> list(String token) {
        return get(resource("/v1/grant_types"), GrantTypesRepresentation.class, token);
    }

}
