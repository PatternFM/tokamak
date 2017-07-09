package fm.pattern.tokamak.sdk.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import org.apache.commons.lang3.StringUtils;

import fm.pattern.tokamak.sdk.AuthoritiesClient;
import fm.pattern.tokamak.sdk.JwtClientProperties;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.AuthorityRepresentation;

public class AuthorityDSL extends AbstractDSL<AuthorityDSL, AuthorityRepresentation> {

	private AuthoritiesClient client = new AuthoritiesClient(JwtClientProperties.getEndpoint());

	private String id = null;
	private String name = randomAlphanumeric(10);
	private String description  = "Auto-generated authority created by acceptance tests.";

	public static AuthorityDSL authority() {
		return new AuthorityDSL();
	}

	public AuthorityDSL withId(String id) {
		this.id = id;
		return this;
	}

	public AuthorityDSL withName(String name) {
		this.name = name;
		return this;
	}

	public AuthorityDSL withDescription(String description) {
		this.description = description;
		return this;
	}

	public AuthorityRepresentation build() {
		AuthorityRepresentation representation = create();
		if (!shouldPersist()) {
			return representation;
		}

		Result<AuthorityRepresentation> response = client.create(representation, super.getToken().getAccessToken());
		if (response.rejected()) {
			throw new IllegalStateException("Unable to create authority, response from server: " + response.getErrors().toString());
		}

		return response.getInstance();
	}

	private AuthorityRepresentation create() {
		AuthorityRepresentation representation = new AuthorityRepresentation();
		if (StringUtils.isNotBlank(id)) {
			representation.setId(id);
		}
		representation.setName(name);
		representation.setDescription(description);
		return representation;
	}

}
