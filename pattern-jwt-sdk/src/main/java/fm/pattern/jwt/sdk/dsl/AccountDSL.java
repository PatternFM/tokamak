package fm.pattern.jwt.sdk.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.AccountsClient;
import fm.pattern.jwt.sdk.model.AccountRepresentation;
import fm.pattern.jwt.sdk.model.RoleRepresentation;

public class AccountDSL extends AbstractDSL<AccountDSL, AccountRepresentation> {

    // TODO: Replace hard-coded values with configuration.
    private AccountsClient client = new AccountsClient("http://localhost:9600");

    private String username = "usr_" + randomAlphanumeric(15);
    private String password = "pwd_" + randomAlphanumeric(15);

    private Set<RoleRepresentation> roles = new HashSet<RoleRepresentation>();

    public static AccountDSL account() {
        return new AccountDSL();
    }

    public AccountDSL at(String hostname) {
        this.client = new AccountsClient(hostname);
        return this;
    }

    public AccountDSL withUsername(String username) {
        this.username = username;
        return this;
    }

    public AccountDSL withPassword(String password) {
        this.password = password;
        return this;
    }

    public AccountDSL withRoles(RoleRepresentation... roles) {
        this.roles.addAll(Arrays.asList(roles));
        return this;
    }

    public AccountRepresentation build() {
        AccountRepresentation representation = create();
        if (!shouldPersist()) {
            return representation;
        }

        Result<AccountRepresentation> response = client.create(representation, super.getToken().getAccessToken());
        if (response.rejected()) {
            throw new IllegalStateException("Unable to create account, response from server: " + response.getErrors().toString());
        }

        return response.getInstance();
    }

    private AccountRepresentation create() {
        AccountRepresentation representation = new AccountRepresentation();
        representation.setUsername(username);
        representation.setPassword(password);
        representation.setRoles(roles);
        return representation;
    }

}
