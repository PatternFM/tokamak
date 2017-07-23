package fm.pattern.tokamak.sdk;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import fm.pattern.tokamak.sdk.commons.CriteriaRepresentation;
import fm.pattern.tokamak.sdk.commons.ErrorsRepresentation;
import fm.pattern.tokamak.sdk.commons.PaginatedListRepresentation;
import fm.pattern.tokamak.sdk.commons.RestClient;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.ClientRepresentation;
import fm.pattern.tokamak.sdk.model.SecretsRepresentation;

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

	public Result<ClientRepresentation> updateSecret(ClientRepresentation representation, SecretsRepresentation secrets, String token) {
		return put(resource("/v1/clients/" + representation.getId() + "/secret"), secrets, ClientRepresentation.class, token);
	}
	
	public Result<ClientRepresentation> delete(String id, String token) {
		return delete(resource("/v1/clients/" + id), token);
	}

	public Result<ClientRepresentation> findById(String id, String token) {
		return get(resource("/v1/clients/" + id), ClientRepresentation.class, token);
	}

	public Result<PaginatedListRepresentation<ClientRepresentation>> list(CriteriaRepresentation criteria, String token) {
		Map<String, Object> params = new HashMap<String, Object>();

		if (criteria.getFrom() != null) {
			params.put("from", criteria.getFrom());
		}
		if (criteria.getTo() != null) {
			params.put("to", criteria.getTo());
		}
		if (criteria.getPage() != null) {
			params.put("page", criteria.getPage());
		}
		if (criteria.getLimit() != null) {
			params.put("limit", criteria.getLimit());
		}

		Response response = resource("/v1/clients", params).header("Authorization", "Bearer " + token).get();
		if (response.getStatus() == 200) {
			return Result.accept(response.getStatus(), response.readEntity(new GenericType<PaginatedListRepresentation<ClientRepresentation>>() {
			}));
		}

		return Result.reject(response.getStatus(), null, response.readEntity(ErrorsRepresentation.class).getErrors());
	}

}
