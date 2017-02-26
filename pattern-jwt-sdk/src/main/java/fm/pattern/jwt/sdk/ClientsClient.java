package fm.pattern.jwt.sdk;

import fm.pattern.commons.rest.RestClient;
import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.model.ClientRepresentation;

public class ClientsClient extends RestClient {

	public ClientsClient(String endpoint) {
		super(endpoint);
	}

	public Result<ClientRepresentation> create(ClientRepresentation representation, String token) {
		return post(resource("/v1/clients"), representation, ClientRepresentation.class, token);
	}

}
