package fm.pattern.tokamak.server.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import fm.pattern.tokamak.server.model.Audience;
import fm.pattern.tokamak.server.service.AudienceService;
import fm.pattern.valex.Result;

public class AudienceDSL extends AbstractDSL<AudienceDSL, Audience> {

	private String name = randomAlphabetic(10);
	private String description = null;

	public static AudienceDSL audience() {
		return new AudienceDSL();
	}

	public AudienceDSL withName(String name) {
		this.name = name;
		return this;
	}

	public AudienceDSL withDescription(String description) {
		this.description = description;
		return this;
	}

	public Audience build() {
		return create();
	}

	public Audience save() {
		Result<Audience> result = load(AudienceService.class).create(create());
		if (result.accepted()) {
			return result.getInstance();
		}
		throw new IllegalStateException("Unable to create audience, errors:" + result.getErrors().toString());
	}

	private Audience create() {
		Audience audience = new Audience(name);
		audience.setDescription(description);
		return audience;
	}

}
