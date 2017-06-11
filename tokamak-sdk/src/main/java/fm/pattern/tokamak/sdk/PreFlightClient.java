package fm.pattern.tokamak.sdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import fm.pattern.tokamak.sdk.commons.ErrorRepresentation;
import fm.pattern.tokamak.sdk.commons.RestClient;
import fm.pattern.tokamak.sdk.commons.Result;

public class PreFlightClient extends RestClient {

	public PreFlightClient(String endpoint) {
		super(endpoint);
	}

	public Result<String> check(String endpoint) {
		Response response = resource("endpoint").options();
		if (response.getStatus() == 200) {
			return Result.accept(response.getStatus(), "accepted");
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

}
