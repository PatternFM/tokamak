package fm.pattern.tokamak.server.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import fm.pattern.tokamak.server.model.Authority;
import fm.pattern.tokamak.server.service.AuthorityService;
import fm.pattern.valex.Result;

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
		return create();
	}

	public Authority save() {
		Result<Authority> result = load(AuthorityService.class).create(create());
		if (result.accepted()) {
			return result.getInstance();
		}
		throw new IllegalStateException("Unable to create authority, errors:" + result.getErrors().toString());
	}

	private Authority create() {
		Authority authority = new Authority(name);
		authority.setDescription(description);
		return authority;
	}

}
