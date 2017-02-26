package fm.pattern.commons.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

public class RestClient {

	private final Client client;
	private final String endpoint;

	public RestClient(String endpoint) {
		this(config(), endpoint);
	}

	public RestClient(ClientConfig config, String endpoint) {
		this.client = ClientBuilder.newClient(config);
		this.endpoint = endpoint;
	}

	public Client getClient() {
		return client;
	}

	public String getResponseBody(Response response) {
		String body = response.readEntity(String.class);
		return body != null ? body.trim() : body;
	}

	public Invocation.Builder resource(String resource, Map<String, Object> queryParameters) {
		if (queryParameters == null || queryParameters.isEmpty()) {
			return resource(resource);
		}

		WebTarget target = getClient().target(endpoint).path(resource);
		for (Map.Entry<String, Object> entry : queryParameters.entrySet()) {
			if (isNotBlank(entry.getKey())) {
				target = target.queryParam(entry.getKey(), entry.getValue());
			}
		}

		return target.request(APPLICATION_JSON);
	}

	public Invocation.Builder resource(String resource) {
		return getClient().target(endpoint).path(resource).request(APPLICATION_JSON);
	}

	protected final <T, S> Result<T> put(Invocation.Builder resource, S representation, Class<T> clazz, String token) {
		if (isNotBlank(token)) {
			resource.header("Authorization", "Bearer " + token);
		}

		Response response = resource.put(Entity.entity(representation, APPLICATION_JSON));
		if (response.getStatus() == 200) {
			return Result.accept(response.getStatus(), response.readEntity(clazz));
		}

		return Result.reject(response.getStatus(), null, response.readEntity(ErrorsRepresentation.class).getErrors());
	}

	protected final <T, S> Result<T> post(Invocation.Builder resource, S representation, Class<T> clazz, String token) {
		if (isNotBlank(token)) {
			resource.header("Authorization", "Bearer " + token);
		}

		Response response = resource.post(Entity.entity(representation, APPLICATION_JSON));
		if (response.getStatus() == 201) {
			return Result.accept(response.getStatus(), response.readEntity(clazz));
		}

		return Result.reject(response.getStatus(), null, response.readEntity(ErrorsRepresentation.class).getErrors());
	}

	protected final <T> Result<T> delete(Invocation.Builder resource, String token) {
		if (isNotBlank(token)) {
			resource.header("Authorization", "Bearer " + token);
		}

		Response response = resource.delete();
		if (response.getStatus() == 204) {
			return Result.accept(response.getStatus(), null);
		}

		return Result.reject(response.getStatus(), null, response.readEntity(ErrorsRepresentation.class).getErrors());
	}

	protected final <T> Result<T> get(Invocation.Builder resource, Class<T> clazz, String token) {
		if (isNotBlank(token)) {
			resource.header("Authorization", "Bearer " + token);
		}

		Response response = resource.get();
		if (response.getStatus() == 200) {
			return Result.accept(response.getStatus(), response.readEntity(clazz));
		}

		return Result.reject(response.getStatus(), null, response.readEntity(ErrorsRepresentation.class).getErrors());
	}

	private static ClientConfig config() {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
		clientConfig.register(new LoggingFilter(Logger.getAnonymousLogger(), true));
		clientConfig.register(JacksonFeature.class);
		clientConfig.register(MultiPartFeature.class);
		return clientConfig;
	}

}
