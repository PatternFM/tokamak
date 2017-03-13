package fm.pattern.tokamak.sdk;

import fm.pattern.tokamak.sdk.commons.RestClient;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.GrantTypeRepresentation;
import fm.pattern.tokamak.sdk.model.GrantTypesRepresentation;

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
