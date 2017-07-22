package fm.pattern.tokamak.sdk.model;

public class SecretsRepresentation {

	private String newSecret;
	private String currentSecret;

	public SecretsRepresentation() {

	}

	public SecretsRepresentation(String newSecret) {
		this.newSecret = newSecret;
	}

	public SecretsRepresentation(String newSecret, String currentSecret) {
		this.newSecret = newSecret;
		this.currentSecret = currentSecret;
	}
	
	public String getCurrentSecret() {
		return currentSecret;
	}

	public void setCurrentSecret(String currentSecret) {
		this.currentSecret = currentSecret;
	}

	public String getNewSecret() {
		return newSecret;
	}

	public void setNewSecret(String newSecret) {
		this.newSecret = newSecret;
	}

}
