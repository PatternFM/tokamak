package fm.pattern.jwt.sdk;

import fm.pattern.commons.rest.RestClient;
import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.model.RoleRepresentation;
import fm.pattern.jwt.sdk.model.RolesRepresentation;

public class RolesClient extends RestClient {

	public RolesClient(String endpoint) {
		super(endpoint);
	}

	public Result<RoleRepresentation> create(RoleRepresentation representation, String token) {
		return post(resource("/v1/roles"), representation, RoleRepresentation.class, token);
	}

	public Result<RoleRepresentation> update(RoleRepresentation representation, String token) {
		return put(resource("/v1/roles/" + representation.getId()), representation, RoleRepresentation.class, token);
	}

	public Result<RoleRepresentation> delete(String id, String token) {
		return delete(resource("/v1/roles/" + id), token);
	}

	public Result<RoleRepresentation> findById(String id, String token) {
		return get(resource("/v1/roles/" + id), RoleRepresentation.class, token);
	}

	   public Result<RoleRepresentation> findByName(String name, String token) {
	        return get(resource("/v1/roles/name/" + name), RoleRepresentation.class, token);
	    }
	
	public Result<RolesRepresentation> list(String token) {
		return get(resource("/v1/roles"), RolesRepresentation.class, token);
	}

}
