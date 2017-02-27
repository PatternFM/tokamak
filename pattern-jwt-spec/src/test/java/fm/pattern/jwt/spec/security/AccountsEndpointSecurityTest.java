package fm.pattern.jwt.spec.security;

import fm.pattern.commons.rest.JwtClientProperties;
import fm.pattern.jwt.sdk.AccountsClient;
import fm.pattern.jwt.spec.AcceptanceTest;

public class AccountsEndpointSecurityTest extends AcceptanceTest {

	private AccountsClient accountsClient = new AccountsClient(JwtClientProperties.getEndpoint());

}
