package fm.pattern.tokamak.sdk;

import static org.glassfish.jersey.client.authentication.HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD;
import static org.glassfish.jersey.client.authentication.HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
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

public class TokensClient extends RestClient {

	public TokensClient(String endpoint) {
		super(config(), endpoint);
	}

	public Result<AccessTokenRepresentation> getAccessToken(String clientId, String clientSecret) {
		return getAccessToken(new ClientCredentials(clientId, clientSecret));
	}

	public Result<AccessTokenRepresentation> getAccessToken(ClientCredentials clientCredentials) {
		Entity<String> entity = Entity.entity("grant_type=client_credentials", MediaType.APPLICATION_FORM_URLENCODED);

		Response response = resource("/oauth/token").property(HTTP_AUTHENTICATION_BASIC_USERNAME, clientCredentials.getClientId()).property(HTTP_AUTHENTICATION_BASIC_PASSWORD, clientCredentials.getSecret()).post(entity);
		if (response.getStatus() == 200) {
			return Result.accept(response.getStatus(), response.readEntity(AccessTokenRepresentation.class));
		}

		return Result.reject(response.getStatus(), null, resolve(response));
	}

	public Result<AccessTokenRepresentation> getAccessToken(String clientId, String clientSecret, String username, String password) {
		return getAccessToken(new ClientCredentials(clientId, clientSecret), new UserCredentials(username, password));
	}

	public Result<AccessTokenRepresentation> getAccessToken(ClientCredentials clientCredentials, String username, String password) {
		return getAccessToken(clientCredentials, new UserCredentials(username, password));
	}

	public Result<AccessTokenRepresentation> getAccessToken(ClientCredentials clientCredentials, UserCredentials userCredentials) {
		Entity<String> entity = Entity.entity("grant_type=password&username=" + userCredentials.getUsername() + "&password=" + userCredentials.getPassword(), MediaType.APPLICATION_FORM_URLENCODED);

		Response response = resource("/oauth/token").property(HTTP_AUTHENTICATION_BASIC_USERNAME, clientCredentials.getClientId()).property(HTTP_AUTHENTICATION_BASIC_PASSWORD, clientCredentials.getSecret()).post(entity);
		if (response.getStatus() == 200) {
			return Result.accept(response.getStatus(), response.readEntity(AccessTokenRepresentation.class));
		}

		return Result.reject(response.getStatus(), null, resolve(response));
	}

	public Result<AccessTokenRepresentation> exchange(String clientId, String clientSecret, String authorizationCode, String redirectUri) {
		return exchange(new ClientCredentials(clientId, clientSecret), authorizationCode, redirectUri);
	}

	public Result<AccessTokenRepresentation> exchange(ClientCredentials clientCredentials, String authorizationCode, String redirectUri) {
		Entity<String> entity = Entity.entity("grant_type=authorization_code&code=" + authorizationCode + "&redirect_uri=" + redirectUri, MediaType.APPLICATION_FORM_URLENCODED);

		Response response = resource("/oauth/token").property(HTTP_AUTHENTICATION_BASIC_USERNAME, clientCredentials.getClientId()).property(HTTP_AUTHENTICATION_BASIC_PASSWORD, clientCredentials.getSecret()).post(entity);
		if (response.getStatus() == 200) {
			return Result.accept(response.getStatus(), response.readEntity(AccessTokenRepresentation.class));
		}

		return Result.reject(response.getStatus(), null, resolve(response));
	}

	public Result<AccessTokenRepresentation> refreshAccessToken(String clientId, String clientSecret, AccessTokenRepresentation accessToken) {
		return refreshAccessToken(new ClientCredentials(clientId, clientSecret), accessToken);
	}

	public Result<AccessTokenRepresentation> refreshAccessToken(ClientCredentials clientCredentials, AccessTokenRepresentation accessToken) {
		Entity<String> entity = Entity.entity("grant_type=refresh_token&refresh_token=" + accessToken.getRefreshToken(), MediaType.APPLICATION_FORM_URLENCODED);

		Response response = resource("/oauth/token").property(HTTP_AUTHENTICATION_BASIC_USERNAME, clientCredentials.getClientId()).property(HTTP_AUTHENTICATION_BASIC_PASSWORD, clientCredentials.getSecret()).post(entity);
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
