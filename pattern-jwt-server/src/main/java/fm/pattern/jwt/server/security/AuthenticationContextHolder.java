/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
