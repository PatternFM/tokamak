package fm.pattern.tokamak.sdk.model;

import java.util.ArrayList;
import java.util.List;

import fm.pattern.tokamak.sdk.commons.Representation;

public class ScopesRepresentation extends Representation {

	private List<ScopeRepresentation> scopes = new ArrayList<ScopeRepresentation>();

	public ScopesRepresentation() {

	}

	public ScopesRepresentation(List<ScopeRepresentation> scopes) {
		this.scopes = scopes;
	}

	public List<ScopeRepresentation> getScopes() {
		return scopes;
	}

	public void setScopes(List<ScopeRepresentation> scopes) {
		this.scopes = scopes;
	}

}
