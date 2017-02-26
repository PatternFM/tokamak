package fm.pattern.jwt.server.security;

import java.util.Set;

public class AuthenticationContextHolder {

	private static final ThreadLocal<AuthenticationContext> THREAD_LOCAL = new ThreadLocal<>();

	private AuthenticationContextHolder() {

	}

	public static AuthenticationContext getContext() {
		return THREAD_LOCAL.get();
	}

	public static AuthenticationContext setContext(AuthenticationContext context) {
		if (context != null) {
			THREAD_LOCAL.set(context);
		}
		return context;
	}

	public static boolean hasContext() {
		return THREAD_LOCAL.get() != null;
	}

	public static String getAccountId() {
		return hasContext() ? getContext().getAccountId() : null;
	}

	public static Set<String> getUserRoles() {
		return hasContext() ? getContext().getUserRoles() : null;
	}

	public static String getClientId() {
		return hasContext() ? getContext().getClientId() : null;
	}

	public static Set<String> getClientRoles() {
		return hasContext() ? getContext().getClientRoles() : null;
	}

	public static void clear() {
		THREAD_LOCAL.remove();
	}

}
