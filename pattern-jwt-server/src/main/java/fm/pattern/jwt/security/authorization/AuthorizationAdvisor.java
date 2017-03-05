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

package fm.pattern.jwt.security.authorization;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Collections;
import java.util.Set;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fm.pattern.validation.AuthenticationException;
import fm.pattern.validation.AuthorizationException;
import fm.pattern.validation.Reportable;

@Aspect
@Component
public class AuthorizationAdvisor {

	private final AuthorizationContextProvider provider;

	@Autowired
	public AuthorizationAdvisor(AuthorizationContextProvider provider) {
		this.provider = provider;
	}

	@Before("execution(* *(..)) && @annotation(authorize)")
	public void handle(Authorize authorize) throws Throwable {
		if (!provider.isAuthenticated()) {
			throw new AuthenticationException(Reportable.report("auth.not.authenticated"));
		}

		if (isNotBlank(authorize.scopes())) {
			checkScopes(authorize.scopes());
		}

		if (isNotBlank(authorize.roles())) {
			checkRoles(authorize.roles());
		}

		if (isNotBlank(authorize.authorities())) {
			checkAuthorities(authorize.authorities());
		}
	}

	private void checkScopes(String input) {
		Set<String> scopes = StringTokenizer.tokenize(input);
		Set<String> grantedScopes = provider.getScopes();

		if (Collections.disjoint(scopes, grantedScopes)) {
			throw new AuthorizationException(Reportable.report("auth.invalid.scope"));
		}
	}

	private void checkRoles(String input) {
		Set<String> roles = StringTokenizer.tokenize(input);
		Set<String> grantedRoles = provider.getRoles();

		if (Collections.disjoint(roles, grantedRoles)) {
			throw new AuthorizationException(Reportable.report("auth.invalid.role"));
		}
	}

	private void checkAuthorities(String input) {
		Set<String> authorities = StringTokenizer.tokenize(input);
		Set<String> grantedAuthorities = provider.getAuthorities();

		if (Collections.disjoint(authorities, grantedAuthorities)) {
			throw new AuthorizationException(Reportable.report("auth.invalid.authority"));
		}
	}

}
