package fm.pattern.tokamak.server.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import fm.pattern.tokamak.server.model.Scope;
import fm.pattern.tokamak.server.service.ScopeService;
import fm.pattern.valex.Result;

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
		return create();
	}

	public Scope save() {
		Result<Scope> result = load(ScopeService.class).create(create());
		if (result.accepted()) {
			return result.getInstance();
		}
		throw new IllegalStateException("Unable to create scope, errors:" + result.getErrors().toString());
	}

	private Scope create() {
		Scope scope = new Scope(name);
		scope.setDescription(description);
		return scope;
	}

}
