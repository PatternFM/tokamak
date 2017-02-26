package fm.pattern.jwt.sdk.dsl;

import fm.pattern.jwt.sdk.ClientCredentials;
import fm.pattern.jwt.sdk.OAuthClient;
import fm.pattern.jwt.sdk.UserCredentials;
import fm.pattern.jwt.sdk.model.AccessTokenRepresentation;

public class AccessTokenDSL extends AbstractDSL<AccessTokenDSL, AccessTokenRepresentation> {

	private OAuthClient client = new OAuthClient("http://localhost:9600");

	private ClientCredentials clientCredentials = null;
	private UserCredentials userCredentials = null;

	public static AccessTokenDSL token() {
		return new AccessTokenDSL();
	}

	public AccessTokenDSL at(String hostname) {
		this.client = new OAuthClient(hostname);
		return this;
	}

	public AccessTokenDSL withClient(String clientId, String clientSecret) {
		this.clientCredentials = new ClientCredentials(clientId, clientSecret);
		return this;
	}

	public AccessTokenDSL withClient(ClientCredentials clientCredentials) {
		this.clientCredentials = clientCredentials;
		return this;
	}

	public AccessTokenDSL withUser(String username, String password) {
		this.userCredentials = new UserCredentials(username, password);
		return this;
	}

	public AccessTokenDSL withUser(UserCredentials userCredentials) {
		this.userCredentials = userCredentials;
		return this;
	}

	public AccessTokenRepresentation build() {
		if (userCredentials != null && clientCredentials != null) {
			return client.getAccessToken(clientCredentials, userCredentials).getInstance();
		}
		return client.getAccessToken(clientCredentials).getInstance();
	}

}
