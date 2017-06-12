package fm.pattern.tokamak.sdk;

public class ClientCredentials {

	private String clientId;
	private String secret;

	public ClientCredentials() {

	}

	public ClientCredentials(String clientId, String clientSecret) {
		this.clientId = clientId;
		this.secret = clientSecret;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String clientSecret) {
		this.secret = clientSecret;
	}

}
