package fm.pattern.jwt.server.dsl;

import java.util.HashSet;
import java.util.Set;

import fm.pattern.commons.util.IdGenerator;
import fm.pattern.jwt.server.model.Authority;
import fm.pattern.jwt.server.model.Client;
import fm.pattern.jwt.server.model.GrantType;
import fm.pattern.jwt.server.model.Scope;
import fm.pattern.jwt.server.service.ClientService;
import fm.pattern.microstructure.Result;

public class ClientDSL extends AbstractDSL<ClientDSL, Client> {

	private String username = IdGenerator.generateId(15);
	private String password = IdGenerator.generateId(15);
	private Set<Authority> authorities = new HashSet<Authority>();
	private Set<GrantType> grantTypes = new HashSet<GrantType>();
	private Set<Scope> scopes = new HashSet<>();

	public static ClientDSL client() {
		ClientDSL clientDSL = new ClientDSL();
		return clientDSL;
	}

	public ClientDSL withUsername(String username) {
		this.username = username;
		return this;
	}

	public ClientDSL withPassword(String password) {
		this.password = password;
		return this;
	}

	public ClientDSL withScope(Scope scope) {
		this.scopes.add(scope);
		return this;
	}

	public ClientDSL withAuthority(Authority authority) {
		this.authorities.add(authority);
		return this;
	}

	public ClientDSL withGrantType(GrantType grantType) {
		this.grantTypes.add(grantType);
		return this;
	}

	public Client build() {
		Client client = new Client(username, password, authorities, grantTypes, scopes);
		return shouldPersist() ? persist(client) : client;
	}

	private Client persist(Client client) {
		Result<Client> result = load(ClientService.class).create(client);
		if (result.accepted()) {
			return result.getInstance();
		}
		throw new IllegalStateException("Unable to create client: " + result.getErrors().toString());
	}

}
