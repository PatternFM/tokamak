package fm.pattern.jwt.server.config;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtEncryptionProperties {

	@NotNull
	private String keyPairAlias;

	@NotNull
	private String keyPairPassword;

	@NotNull
	private String keyStore;

	@NotNull
	private String keyStorePassword;

	public String getKeyPairAlias() {
		return keyPairAlias;
	}

	public void setKeyPairAlias(String keyPairAlias) {
		this.keyPairAlias = keyPairAlias;
	}

	public String getKeyPairPassword() {
		return keyPairPassword;
	}

	public void setKeyPairPassword(String keyPairPassword) {
		this.keyPairPassword = keyPairPassword;
	}

	public String getKeyStore() {
		return keyStore;
	}

	public void setKeyStore(String keyStore) {
		this.keyStore = keyStore;
	}

	public String getKeyStorePassword() {
		return keyStorePassword;
	}

	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}

}
