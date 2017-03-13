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

package fm.pattern.tokamak.server.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

@SuppressWarnings("unchecked")
public class SimpleAccessTokenConverter extends DefaultAccessTokenConverter {

	@Override
	public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
		Map<String, Object> response = (Map<String, Object>) super.convertAccessToken(token, authentication);
		OAuth2Request clientToken = authentication.getOAuth2Request();
		// TODO: Is this the correct attribute for client authorities?
		response.put("clientAuthorities", clientToken.getAuthorities());
		return response;
	}

	@Override
	public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
		Collection<HashMap<String, String>> clientAuthorities = (Collection<HashMap<String, String>>) map.get("clientAuthorities");

		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		for (HashMap<String, String> grantedAuthority : clientAuthorities) {
			for (String authority : grantedAuthority.values()) {
				grantedAuthorities.add(new SimpleGrantedAuthority(authority));
			}
		}

		OAuth2Authentication authentication = super.extractAuthentication(map);
		OAuth2Request request = authentication.getOAuth2Request();
		OAuth2Request enhancedRequest = new OAuth2Request(request.getRequestParameters(), request.getClientId(), grantedAuthorities, request.isApproved(), request.getScope(), request.getResourceIds(), request.getRedirectUri(), request.getResponseTypes(), request.getExtensions());

		// TOOD: Add custome token attributes here.
//		if (map.containsKey("account_id")) {
//			request.getExtensions().put("account_id", (String) map.get("account_id"));
//		}

		return new OAuth2Authentication(enhancedRequest, authentication.getUserAuthentication());
	}

}
