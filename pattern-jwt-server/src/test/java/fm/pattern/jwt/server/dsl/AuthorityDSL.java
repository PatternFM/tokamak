package fm.pattern.jwt.server.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import fm.pattern.jwt.server.model.Authority;
import fm.pattern.jwt.server.service.AuthorityService;
import fm.pattern.validation.Result;

public class AuthorityDSL extends AbstractDSL<AuthorityDSL, Authority> {

	private String name = randomAlphabetic(10);
	private String description = null;

	public static AuthorityDSL authority() {
		return new AuthorityDSL();
	}

	public AuthorityDSL withName(String name) {
		this.name = name;
		return this;
	}

	public AuthorityDSL withDescription(String description) {
		this.description = description;
		return this;
	}

	public Authority build() {
		Authority authority = new Authority(name);
		authority.setDescription(description);
		return shouldPersist() ? persist(authority) : authority;
	}

	private Authority persist(Authority authority) {
		Result<Authority> result = load(AuthorityService.class).create(authority);
		if (result.accepted()) {
			return result.getInstance();
		}
		throw new IllegalStateException("Unable to create authority, errors:" + result.toString());
	}

}
