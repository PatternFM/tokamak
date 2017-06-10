package fm.pattern.tokamak.server.security;

public class CurrentAuthenticatedClientContext {

	private static final ThreadLocal<AuthenticatedClient> THREAD_LOCAL = new ThreadLocal<>();

	private CurrentAuthenticatedClientContext() {

	}

	public static AuthenticatedClient getAuthenticatedClient() {
		return THREAD_LOCAL.get();
	}

	public static AuthenticatedClient setAuthenticatedClient(AuthenticatedClient client) {
		if (client != null) {
			THREAD_LOCAL.set(client);
		}
		return client;
	}

	public static boolean hasAuthenticatedClient() {
		return THREAD_LOCAL.get() != null;
	}

	public static void clear() {
		THREAD_LOCAL.remove();
	}

}
