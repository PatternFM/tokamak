package fm.pattern.jwt.server.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import fm.pattern.jwt.server.model.Audience;
import fm.pattern.jwt.server.service.AudienceService;
import fm.pattern.microstructure.Result;

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
		Audience audience = new Audience(name);
		audience.setDescription(description);
		return shouldPersist() ? persist(audience) : audience;
	}

	private Audience persist(Audience audience) {
		Result<Audience> result = load(AudienceService.class).create(audience);
		if (result.accepted()) {
			return result.getInstance();
		}
		throw new IllegalStateException("Unable to create audience, errors:" + result.toString());
	}

}
