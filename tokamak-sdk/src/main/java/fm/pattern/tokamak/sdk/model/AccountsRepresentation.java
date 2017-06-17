package fm.pattern.tokamak.sdk.model;

import java.util.ArrayList;
import java.util.List;

import fm.pattern.tokamak.sdk.commons.Representation;

public class AccountsRepresentation extends Representation {

	private List<AccountRepresentation> accounts = new ArrayList<>();

	public AccountsRepresentation() {

	}

	public AccountsRepresentation(List<AccountRepresentation> accounts) {
		this.accounts = accounts;
	}

	public List<AccountRepresentation> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<AccountRepresentation> accounts) {
		this.accounts = accounts;
	}

}
