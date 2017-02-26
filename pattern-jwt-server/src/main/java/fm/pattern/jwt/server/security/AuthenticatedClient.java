package fm.pattern.jwt.server.security;

import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import fm.pattern.jwt.server.model.Client;

public class AuthenticatedClient extends BaseClientDetails implements ClientDetails {

	private static final long serialVersionUID = 782533447342660L;

	private Client client;

	public AuthenticatedClient(Client client) {
		super.setClientId(client.getUsername());
		super.setClientSecret(client.getPassword());

		super.setScope(client.getScopes().stream().map(scope -> scope.getName()).collect(Collectors.toCollection(HashSet::new)));
		super.setAuthorities(client.getAuthorities().stream().map(authority -> new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toCollection(HashSet::new)));
		super.setAuthorizedGrantTypes(client.getGrantTypes().stream().map(grantType -> grantType.getName().toLowerCase()).collect(Collectors.toCollection(HashSet::new)));
		super.setResourceIds(client.getResourceIds());
		super.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
		super.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());

		this.client = client;
	}

	public Client getClient() {
		return client;
	}

}