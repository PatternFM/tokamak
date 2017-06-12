package fm.pattern.tokamak.sdk.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import org.apache.commons.lang3.StringUtils;

import fm.pattern.tokamak.sdk.JwtClientProperties;
import fm.pattern.tokamak.sdk.ScopesClient;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.ScopeRepresentation;

public class ScopeDSL extends AbstractDSL<ScopeDSL, ScopeRepresentation> {

	private ScopesClient client = new ScopesClient(JwtClientProperties.getEndpoint());

	private String id = null;
	private String name = randomAlphanumeric(10);
	private String description = "description";

	public static ScopeDSL scope() {
		return new ScopeDSL();
	}

	public ScopeDSL withId(String id) {
		this.id = id;
		return this;
	}

	public ScopeDSL withName(String name) {
		this.name = name;
		return this;
	}

	public ScopeDSL withDescription(String description) {
		this.description = description;
		return this;
	}

	public ScopeRepresentation build() {
		ScopeRepresentation representation = create();
		if (!shouldPersist()) {
			return representation;
		}

		Result<ScopeRepresentation> response = client.create(representation, super.getToken().getAccessToken());
		if (response.rejected()) {
			throw new IllegalStateException("Unable to create scope, response from server: " + response.getErrors().toString());
		}

		return response.getInstance();
	}

	private ScopeRepresentation create() {
		ScopeRepresentation representation = new ScopeRepresentation();
		if (StringUtils.isNotBlank(id)) {
			representation.setId(id);
		}
		representation.setName(name);
		representation.setDescription(description);
		return representation;
	}

}
