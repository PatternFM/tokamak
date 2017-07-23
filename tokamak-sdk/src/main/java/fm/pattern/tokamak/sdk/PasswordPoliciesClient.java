package fm.pattern.tokamak.sdk;

import fm.pattern.tokamak.sdk.commons.RestClient;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.commons.TokenHolder;
import fm.pattern.tokamak.sdk.model.PasswordPolicyRepresentation;

public class PasswordPoliciesClient extends RestClient {

	public PasswordPoliciesClient(String endpoint) {
		super(endpoint);
	}

	public Result<PasswordPolicyRepresentation> create(PasswordPolicyRepresentation representation) {
		return create(representation, TokenHolder.token());
	}

	public Result<PasswordPolicyRepresentation> create(PasswordPolicyRepresentation representation, String token) {
		return post(resource("/v1/policies"), representation, PasswordPolicyRepresentation.class, token);
	}

	public Result<PasswordPolicyRepresentation> update(PasswordPolicyRepresentation representation) {
		return update(representation, TokenHolder.token());
	}

	public Result<PasswordPolicyRepresentation> update(PasswordPolicyRepresentation representation, String token) {
		return put(resource("/v1/policies/" + representation.getId()), representation, PasswordPolicyRepresentation.class, token);
	}

	public Result<PasswordPolicyRepresentation> findById(String id) {
		return findById(id, TokenHolder.token());
	}

	public Result<PasswordPolicyRepresentation> findById(String id, String token) {
		return get(resource("/v1/policies/" + id), PasswordPolicyRepresentation.class, token);
	}

	public Result<PasswordPolicyRepresentation> findByName(String username) {
		return findByName(username, TokenHolder.token());
	}

	public Result<PasswordPolicyRepresentation> findByName(String username, String token) {
		return get(resource("/v1/policies/name/" + username), PasswordPolicyRepresentation.class, token);
	}

}
