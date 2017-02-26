package fm.pattern.jwt.sdk;

public class ClientCredentials {

	private String clientId;
	private String secret;

	public ClientCredentials() {

	}

	public ClientCredentials(String clientId, String secret) {
		this.clientId = clientId;
		this.secret = secret;
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

	public void setSecret(String secret) {
		this.secret = secret;
	}

}
