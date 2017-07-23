package fm.pattern.tokamak.server.security;

public class CurrentAuthenticatedAccountContext {

	private static final ThreadLocal<AuthenticatedAccount> THREAD_LOCAL = new ThreadLocal<>();

	private CurrentAuthenticatedAccountContext() {

	}

	public static AuthenticatedAccount getAuthenticatedAccount() {
		return THREAD_LOCAL.get();
	}

	public static AuthenticatedAccount setAuthenticatedAccount(AuthenticatedAccount account) {
		if (account != null) {
			THREAD_LOCAL.set(account);
		}
		return account;
	}

	public static boolean hasAuthenticatedAccount() {
		return THREAD_LOCAL.get() != null;
	}

	public static void clear() {
		THREAD_LOCAL.remove();
	}

}
