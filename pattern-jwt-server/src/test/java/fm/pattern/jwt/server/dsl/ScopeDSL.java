package fm.pattern.jwt.server.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import fm.pattern.jwt.server.model.Scope;
import fm.pattern.jwt.server.service.ScopeService;
import fm.pattern.validation.Result;

public class ScopeDSL extends AbstractDSL<ScopeDSL, Scope> {

	private String name = randomAlphabetic(10);
	private String description = null;

	public static ScopeDSL scope() {
		return new ScopeDSL();
	}

	public ScopeDSL withName(String name) {
		this.name = name;
		return this;
	}

	public ScopeDSL withDescription(String description) {
		this.description = description;
		return this;
	}

	public Scope build() {
		Scope scope = new Scope(name);
		scope.setDescription(description);
		return shouldPersist() ? persist(scope) : scope;
	}

	private Scope persist(Scope scope) {
		Result<Scope> result = load(ScopeService.class).create(scope);
		if (result.accepted()) {
			return result.getInstance();
		}
		throw new IllegalStateException("Unable to create scope, errors:" + result.toString());
	}

}
