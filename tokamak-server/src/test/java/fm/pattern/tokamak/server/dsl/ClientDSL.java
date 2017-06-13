package fm.pattern.tokamak.server.dsl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import fm.pattern.commons.util.IdGenerator;
import fm.pattern.tokamak.server.model.Audience;
import fm.pattern.tokamak.server.model.Authority;
import fm.pattern.tokamak.server.model.Client;
import fm.pattern.tokamak.server.model.GrantType;
import fm.pattern.tokamak.server.model.Scope;
import fm.pattern.tokamak.server.service.ClientService;
import fm.pattern.valex.Result;

public class ClientDSL extends AbstractDSL<ClientDSL, Client> {

	private String clientId = IdGenerator.generateId(15);
	private String clientSecret = IdGenerator.generateId(15);
	private Set<Authority> authorities = new HashSet<Authority>();
	private Set<Audience> audiences = new HashSet<Audience>();
	private Set<GrantType> grantTypes = new HashSet<GrantType>();
	private Set<Scope> scopes = new HashSet<>();
	private String name;

	private Integer accessTokenValiditySeconds = 600;
	private Integer refreshTokenValiditySeconds = 3600;

	public static ClientDSL client() {
		ClientDSL clientDSL = new ClientDSL();
		return clientDSL;
	}

	public ClientDSL withClientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	public ClientDSL withClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
		return this;
	}

	public ClientDSL withName(String name) {
		this.name = name;
		return this;
	}

	public ClientDSL withScope(Scope... scopes) {
		this.scopes.addAll(Arrays.asList(scopes));
		return this;
	}

	public ClientDSL withAuthority(Authority... authorities) {
		this.authorities.addAll(Arrays.asList(authorities));
		return this;
	}

	public ClientDSL withAudience(Audience... audiences) {
		this.audiences.addAll(Arrays.asList(audiences));
		return this;
	}

	public ClientDSL withGrantType(GrantType... grantTypes) {
		this.grantTypes.addAll(Arrays.asList(grantTypes));
		return this;
	}

	public ClientDSL withAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
		return this;
	}

	public ClientDSL withRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
		return this;
	}

	public Client build() {
		Client client = new Client(clientId, clientSecret, authorities, audiences, grantTypes, scopes);
		client.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
		client.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
		client.setName(name);
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
