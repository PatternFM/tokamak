package fm.pattern.tokamak.sdk.model;

import java.util.ArrayList;
import java.util.List;

import fm.pattern.tokamak.sdk.commons.Representation;

public class PasswordPoliciesRepresentation extends Representation {

	private List<PasswordPolicyRepresentation> policies = new ArrayList<>();

	public PasswordPoliciesRepresentation() {

	}

	public PasswordPoliciesRepresentation(List<PasswordPolicyRepresentation> policies) {
		this.policies = policies;
	}

	public List<PasswordPolicyRepresentation> getPolicies() {
		return policies;
	}

	public void setPolicies(List<PasswordPolicyRepresentation> policies) {
		this.policies = policies;
	}

}
