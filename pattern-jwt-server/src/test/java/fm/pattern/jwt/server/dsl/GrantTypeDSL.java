package fm.pattern.jwt.server.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import fm.pattern.jwt.server.model.GrantType;
import fm.pattern.jwt.server.service.GrantTypeService;
import fm.pattern.microstructure.Result;

public class GrantTypeDSL extends AbstractDSL<GrantTypeDSL, GrantType> {

	private String name = randomAlphabetic(10);

	public static GrantTypeDSL grantType() {
		return new GrantTypeDSL();
	}

	public GrantTypeDSL withName(String name) {
		this.name = name;
		return this;
	}

	public GrantType build() {
		GrantType grantType = new GrantType(name);
		return shouldPersist() ? persist(grantType) : grantType;
	}

	private GrantType persist(GrantType grantType) {
		Result<GrantType> result = load(GrantTypeService.class).create(grantType);
		if (result.accepted()) {
			return result.getInstance();
		}
		throw new IllegalStateException("Unable to create grant type, errors:" + result.toString());
	}

}
