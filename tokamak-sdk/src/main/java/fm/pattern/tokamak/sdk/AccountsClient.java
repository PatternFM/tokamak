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
import fm.pattern.tokamak.sdk.commons.TokenHolder;
import fm.pattern.tokamak.sdk.model.AccountRepresentation;

public class AccountsClient extends RestClient {

	public AccountsClient(String endpoint) {
		super(endpoint);
	}

	public Result<AccountRepresentation> create(AccountRepresentation representation) {
		return create(representation, TokenHolder.token());
	}

	public Result<AccountRepresentation> create(AccountRepresentation representation, String token) {
		return post(resource("/v1/accounts"), representation, AccountRepresentation.class, token);
	}

	public Result<AccountRepresentation> update(AccountRepresentation representation) {
		return update(representation, TokenHolder.token());
	}

	public Result<AccountRepresentation> update(AccountRepresentation representation, String token) {
		return put(resource("/v1/accounts/" + representation.getId()), representation, AccountRepresentation.class, token);
	}

	public Result<AccountRepresentation> delete(String id) {
		return delete(id, TokenHolder.token());
	}

	public Result<AccountRepresentation> delete(String id, String token) {
		return delete(resource("/v1/accounts/" + id), token);
	}

	public Result<AccountRepresentation> findById(String id) {
		return findById(id, TokenHolder.token());
	}

	public Result<AccountRepresentation> findById(String id, String token) {
		return get(resource("/v1/accounts/" + id), AccountRepresentation.class, token);
	}

	public Result<AccountRepresentation> findByUsername(String username) {
		return findByUsername(username, TokenHolder.token());
	}

	public Result<AccountRepresentation> findByUsername(String username, String token) {
		return get(resource("/v1/accounts/username/" + username), AccountRepresentation.class, token);
	}

	public Result<PaginatedListRepresentation<AccountRepresentation>> list(CriteriaRepresentation criteria) {
		return list(criteria, TokenHolder.token());
	}

	public Result<PaginatedListRepresentation<AccountRepresentation>> list(CriteriaRepresentation criteria, String token) {
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

		Response response = resource("/v1/accounts", params).header("Authorization", "Bearer " + token).get();
		if (response.getStatus() == 200) {
			return Result.accept(response.getStatus(), response.readEntity(new GenericType<PaginatedListRepresentation<AccountRepresentation>>() {
			}));
		}

		return Result.reject(response.getStatus(), null, response.readEntity(ErrorsRepresentation.class).getErrors());
	}

}
