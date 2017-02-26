package fm.pattern.jwt.sdk.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.AuthoritiesClient;
import fm.pattern.jwt.sdk.ClientsClient;
import fm.pattern.jwt.sdk.GrantTypesClient;
import fm.pattern.jwt.sdk.ScopesClient;
import fm.pattern.jwt.sdk.model.AuthorityRepresentation;
import fm.pattern.jwt.sdk.model.ClientRepresentation;
import fm.pattern.jwt.sdk.model.GrantTypeRepresentation;
import fm.pattern.jwt.sdk.model.ScopeRepresentation;

public class ClientDSL extends AbstractDSL<ClientDSL, ClientRepresentation> {

    // TODO: Replace hard-coded values with configuration.
    private ClientsClient client = new ClientsClient("http://localhost:9600");
    private ScopesClient scopesClient = new ScopesClient("http://localhost:9600");
    private GrantTypesClient grantTypesClient = new GrantTypesClient("http://localhost:9600");
    private AuthoritiesClient authoritiesClient = new AuthoritiesClient("http://localhost:9600");

    private String clientId = "cli_" + randomAlphanumeric(15);
    private String clientSecret = "pwd_" + randomAlphanumeric(15);

    private Integer accessTokenValiditySeconds = 600;
    private Integer refreshTokenValiditySeconds = 6000;

    private Set<ScopeRepresentation> scopes = new HashSet<ScopeRepresentation>();
    private Set<GrantTypeRepresentation> grantTypes = new HashSet<GrantTypeRepresentation>();
    private Set<AuthorityRepresentation> authorities = new HashSet<AuthorityRepresentation>();

    private Set<String> scopesLookup = new HashSet<String>();
    private Set<String> grantTypesLookup = new HashSet<String>();
    private Set<String> authoritiesLookup = new HashSet<String>();

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

    public ClientDSL withAuthorities(String... authorities) {
        this.authoritiesLookup.clear();
        this.authoritiesLookup.addAll(Arrays.asList(authorities));
        return this;
    }

    public ClientDSL withScopes(ScopeRepresentation... scopes) {
        this.scopes.clear();
        this.scopes.addAll(Arrays.asList(scopes));
        return this;
    }

    public ClientDSL withScopes(String... scopes) {
        this.scopesLookup.clear();
        this.scopesLookup.addAll(Arrays.asList(scopes));
        return this;
    }

    public ClientDSL withGrantTypes(GrantTypeRepresentation... grantTypes) {
        this.grantTypes.clear();
        this.grantTypes.addAll(Arrays.asList(grantTypes));
        return this;
    }

    public ClientDSL withGrantTypes(String... grantTypes) {
        this.grantTypesLookup.clear();
        this.grantTypesLookup.addAll(Arrays.asList(grantTypes));
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
        resolve();

        ClientRepresentation client = new ClientRepresentation();
        client.setClientId(clientId);
        client.setClientSecret(clientSecret);
        client.setScopes(scopes);
        client.setGrantTypes(grantTypes);
        client.setAuthorities(authorities);
        client.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
        client.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
        return client;
    }

    private void resolve() {
        if (super.getToken() == null) {
            return;
        }

        for (String name : scopesLookup) {
            Result<ScopeRepresentation> result = scopesClient.findByName(name, super.getToken().getAccessToken());
            if (result.accepted()) {
                scopes.add(result.getInstance());
            }
        }

        for (String name : grantTypesLookup) {
            Result<GrantTypeRepresentation> result = grantTypesClient.findByName(name, super.getToken().getAccessToken());
            if (result.accepted()) {
                grantTypes.add(result.getInstance());
            }
        }

        for (String name : authoritiesLookup) {
            Result<AuthorityRepresentation> result = authoritiesClient.findByName(name, super.getToken().getAccessToken());
            if (result.accepted()) {
                authorities.add(result.getInstance());
            }
        }
    }

}
