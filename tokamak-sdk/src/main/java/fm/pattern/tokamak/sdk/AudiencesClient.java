package fm.pattern.tokamak.sdk;

import fm.pattern.tokamak.sdk.commons.RestClient;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.commons.TokenHolder;
import fm.pattern.tokamak.sdk.model.AudienceRepresentation;
import fm.pattern.tokamak.sdk.model.AudiencesRepresentation;

public class AudiencesClient extends RestClient {

	public AudiencesClient(String endpoint) {
		super(endpoint);
	}

	public Result<AudienceRepresentation> create(AudienceRepresentation representation) {
		return create(representation, TokenHolder.token());
	}

	public Result<AudienceRepresentation> create(AudienceRepresentation representation, String token) {
		return post(resource("/v1/audiences"), representation, AudienceRepresentation.class, token);
	}

	public Result<AudienceRepresentation> update(AudienceRepresentation representation) {
		return update(representation, TokenHolder.token());
	}

	public Result<AudienceRepresentation> update(AudienceRepresentation representation, String token) {
		return put(resource("/v1/audiences/" + representation.getId()), representation, AudienceRepresentation.class, token);
	}

	public Result<AudienceRepresentation> delete(String id) {
		return delete(id, TokenHolder.token());
	}

	public Result<AudienceRepresentation> delete(String id, String token) {
		return delete(resource("/v1/audiences/" + id), token);
	}

	public Result<AudienceRepresentation> findById(String id) {
		return findById(id, TokenHolder.token());
	}

	public Result<AudienceRepresentation> findById(String id, String token) {
		return get(resource("/v1/audiences/" + id), AudienceRepresentation.class, token);
	}

	public Result<AudienceRepresentation> findByName(String name) {
		return findByName(name, TokenHolder.token());
	}

	public Result<AudienceRepresentation> findByName(String name, String token) {
		return get(resource("/v1/audiences/name/" + name), AudienceRepresentation.class, token);
	}

	public Result<AudiencesRepresentation> list() {
		return list(TokenHolder.token());
	}

	public Result<AudiencesRepresentation> list(String token) {
		return get(resource("/v1/audiences"), AudiencesRepresentation.class, token);
	}

}
