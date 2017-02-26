package fm.pattern.jwt.sdk.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.ClientsClient;
import fm.pattern.jwt.sdk.model.AuthorityRepresentation;
import fm.pattern.jwt.sdk.model.ClientRepresentation;
import fm.pattern.jwt.sdk.model.GrantTypeRepresentation;
import fm.pattern.jwt.sdk.model.ScopeRepresentation;

public class ClientDSL extends AbstractDSL<ClientDSL, ClientRepresentation> {

    // TODO: Replace hard-coded values with configuration.
    private ClientsClient client = new ClientsClient("http://localhost:9600");

    private String clientId = "cli_" + randomAlphanumeric(15);
    private String clientSecret = "pwd_" + randomAlphanumeric(15);

    private Set<ScopeRepresentation> scopes = new HashSet<ScopeRepresentation>();
    private Set<GrantTypeRepresentation> grantTypes = new HashSet<GrantTypeRepresentation>();
    private Set<AuthorityRepresentation> authorities = new HashSet<AuthorityRepresentation>();

    public static ClientDSL client() {
        return new ClientDSL();
    }

    private ClientDSL() {

    }

    public ClientDSL withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public ClientDSL withClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public ClientDSL withAuthorities(AuthorityRepresentation... authorities) {
        this.authorities.clear();
        this.authorities.addAll(Arrays.asList(authorities));
        return this;
    }

    public ClientDSL withScopes(ScopeRepresentation... scopes) {
        this.scopes.clear();
        this.scopes.addAll(Arrays.asList(scopes));
        return this;
    }

    public ClientDSL withGrantTypes(GrantTypeRepresentation... grantTypes) {
        this.grantTypes.clear();
        this.grantTypes.addAll(Arrays.asList(grantTypes));
        return this;
    }

    public ClientRepresentation build() {
        ClientRepresentation representation = create();
        if (!shouldPersist()) {
            return representation;
        }

        Result<ClientRepresentation> response = client.create(representation, super.getToken().getAccessToken());
        if (response.getResponseCode() != 201) {
            throw new IllegalStateException("Unable to create client, response from server: " + response.getErrors().toString());
        }

        return response.getInstance();
    }

    private ClientRepresentation create() {
        ClientRepresentation client = new ClientRepresentation();
        client.setClientId(clientId);
        client.setClientSecret(clientSecret);
        client.setScopes(scopes);
        client.setGrantTypes(grantTypes);
        client.setAuthorities(authorities);
        return client;
    }

}
