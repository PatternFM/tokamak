package fm.pattern.tokamak.sdk;

import static org.glassfish.jersey.client.authentication.HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD;
import static org.glassfish.jersey.client.authentication.HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.fasterxml.jackson.databind.ObjectMapper;

import fm.pattern.tokamak.sdk.commons.ErrorRepresentation;
import fm.pattern.tokamak.sdk.commons.RestClient;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.AccessTokenRepresentation;

public class AuthorizationClient extends RestClient {

	public AuthorizationClient(String endpoint) {
		super(config(), endpoint);
	}

	public Result<AccessTokenRepresentation> authorize(ClientCredentials clientCredentials) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("client_id", clientCredentials.getClientId());
		params.put("response_type", "code");
		params.put("redirect_uri", "https://www.google.com");

		Response response = resource("/v1/oauth/authorize", params).property(HTTP_AUTHENTICATION_BASIC_USERNAME, clientCredentials.getClientId()).property(HTTP_AUTHENTICATION_BASIC_PASSWORD, clientCredentials.getSecret()).get();
		if (response.getStatus() == 200) {
			return Result.accept(response.getStatus(), response.readEntity(AccessTokenRepresentation.class));
		}

		return Result.reject(response.getStatus(), null, resolve(response));
	}

	private List<ErrorRepresentation> resolve(Response response) {
		ObjectMapper mapper = new ObjectMapper();

		String body = getResponseBody(response);
		try {
			if (body.contains("error_description")) {
				OAuthError error = mapper.readValue(body, OAuthError.class);
				return Arrays.asList(new ErrorRepresentation(error.getError(), error.getDescription()));
			}
			if (body.contains("timestamp")) {
				OAuthBadCredentialsError error = mapper.readValue(body, OAuthBadCredentialsError.class);
				return Arrays.asList(new ErrorRepresentation(error.getError(), error.getMessage()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ErrorRepresentation>();
	}

	private static ClientConfig config() {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
		clientConfig.register(HttpAuthenticationFeature.basicBuilder().build());
		clientConfig.register(JacksonFeature.class);
		clientConfig.register(new LoggingFilter(Logger.getAnonymousLogger(), true));
		return clientConfig;
	}

}
