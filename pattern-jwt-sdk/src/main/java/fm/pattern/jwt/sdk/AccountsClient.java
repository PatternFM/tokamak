package fm.pattern.jwt.sdk;

import java.util.HashMap;
import java.util.Map;

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

	public Result<AccountRepresentation> findById(String id, String token) {
		return get(resource("/v1/accounts/" + id), AccountRepresentation.class, token);
	}

	public Result<AccountRepresentation> findByEmail(String username, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		return get(resource("/v1/accounts/username", params), AccountRepresentation.class, token);
	}

}
