package fm.pattern.tokamak.sdk;

import fm.pattern.tokamak.sdk.commons.RestClient;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.ClientRepresentation;

public class ClientsClient extends RestClient {

    public ClientsClient(String endpoint) {
        super(endpoint);
    }

    public Result<ClientRepresentation> create(ClientRepresentation representation, String token) {
        return post(resource("/v1/clients"), representation, ClientRepresentation.class, token);
    }

    public Result<ClientRepresentation> update(ClientRepresentation representation, String token) {
        return put(resource("/v1/clients/" + representation.getId()), representation, ClientRepresentation.class, token);
    }

    public Result<ClientRepresentation> delete(String id, String token) {
        return delete(resource("/v1/clients/" + id), token);
    }

    public Result<ClientRepresentation> findById(String id, String token) {
        return get(resource("/v1/clients/" + id), ClientRepresentation.class, token);
    }

}
