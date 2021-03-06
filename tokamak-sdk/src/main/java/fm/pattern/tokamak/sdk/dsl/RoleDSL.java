package fm.pattern.tokamak.sdk.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import org.apache.commons.lang3.StringUtils;

import fm.pattern.tokamak.sdk.JwtClientProperties;
import fm.pattern.tokamak.sdk.RolesClient;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.RoleRepresentation;

public class RoleDSL extends AbstractDSL<RoleDSL, RoleRepresentation> {

	private RolesClient client = new RolesClient(JwtClientProperties.getEndpoint());

	private String id = null;
	private String name = randomAlphanumeric(10);
	private String description  = "Auto-generated role created by acceptance tests.";

	public static RoleDSL role() {
		return new RoleDSL();
	}

	public RoleDSL withId(String id) {
		this.id = id;
		return this;
	}

	public RoleDSL withName(String name) {
		this.name = name;
		return this;
	}

	public RoleDSL withDescription(String description) {
		this.description = description;
		return this;
	}

	public RoleRepresentation build() {
		RoleRepresentation representation = create();
		if (!shouldPersist()) {
			return representation;
		}

		Result<RoleRepresentation> response = client.create(representation, super.getToken().getAccessToken());
		if (response.rejected()) {
			throw new IllegalStateException("Unable to create role, response from server: " + response.getErrors().toString());
		}

		return response.getInstance();
	}

	private RoleRepresentation create() {
		RoleRepresentation representation = new RoleRepresentation();
		if (StringUtils.isNotEmpty(id)) {
			representation.setId(id);
		}
		representation.setName(name);
		representation.setDescription(description);
		return representation;
	}

}
