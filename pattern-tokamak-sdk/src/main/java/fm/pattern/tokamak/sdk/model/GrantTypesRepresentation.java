package fm.pattern.tokamak.sdk.model;

import java.util.ArrayList;
import java.util.List;

public class GrantTypesRepresentation {

	private List<GrantTypeRepresentation> grantTypes = new ArrayList<GrantTypeRepresentation>();

	public GrantTypesRepresentation() {

	}

	public GrantTypesRepresentation(List<GrantTypeRepresentation> grantTypes) {
		this.grantTypes = grantTypes;
	}

	public List<GrantTypeRepresentation> getGrantTypes() {
		return grantTypes;
	}

	public void setGrantTypes(List<GrantTypeRepresentation> grantTypes) {
		this.grantTypes = grantTypes;
	}

}
