package fm.pattern.jwt.spec;

import org.junit.runner.RunWith;

import fm.pattern.jwt.sdk.ClientCredentials;

@RunWith(AcceptanceTestRunner.class)
public abstract class AcceptanceTest {

	public static final String TEST_CLIENT_ID = "test-client";
	public static final String TEST_CLIENT_SECRET = "bm46bksylwt2imkfbhgb";
	public static final ClientCredentials TEST_CLIENT_CREDENTIALS = new ClientCredentials(TEST_CLIENT_ID, TEST_CLIENT_SECRET);

	public static final String UNAUTHORIZED_GRANT_TYPE_CLIENT_CREDENTIALS = "Unauthorized grant type: client_credentials";
	public static final String UNAUTHORIZED_GRANT_TYPE_PASSWORD = "Unauthorized grant type: password";
	public static final String BAD_CREDENTIALS = "Bad credentials";
	public static final String ACCESS_DENIED_ERROR = "Access is denied";
	public static final String INVALID_ACCESS_TOKEN_ERROR = "Invalid access token: invalid";
	public static final String NOT_AUTHENTICATED_ERROR = "Full authentication is required to access this resource";
	public static final String INSUFFICIENT_SCOPE_ERROR = "Insufficient scope for this resource";
	public static final String UNAUTHORIZED_ERROR = "User is not authorized to perform the requested operation.";

	public static void pause(Integer milliseconds) {
		try {
			Thread.sleep(milliseconds);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
