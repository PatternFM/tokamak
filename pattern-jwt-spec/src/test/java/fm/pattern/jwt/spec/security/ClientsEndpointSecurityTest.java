package fm.pattern.jwt.spec.security;

import fm.pattern.commons.rest.JwtClientProperties;
import fm.pattern.jwt.sdk.ClientsClient;
import fm.pattern.jwt.spec.AcceptanceTest;

public class ClientsEndpointSecurityTest extends AcceptanceTest {

	private ClientsClient clientsClient = new ClientsClient(JwtClientProperties.getEndpoint());

}
