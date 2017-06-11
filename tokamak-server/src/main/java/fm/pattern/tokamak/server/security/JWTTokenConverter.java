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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

@SuppressWarnings("unchecked")
public class JWTTokenConverter extends DefaultAccessTokenConverter {

	private static final String CLIENT_AUTHORITIES = "client_authorities";

	/**
	 * Values placed into the map will be included in the JWT token only, not the OAuth 2 response itself.
	 */
	@Override
	public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
		Map<String, Object> map = (Map<String, Object>) super.convertAccessToken(token, authentication);

		OAuth2Request request = authentication.getOAuth2Request();
		Set<String> authorities = request.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet());

		map.put(CLIENT_AUTHORITIES, authorities);

		return map;
	}

	@Override
	public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
		List<String> authorities = (List<String>) map.get(CLIENT_AUTHORITIES);
		Collection<GrantedAuthority> grantedAuthorities = authorities.stream().map(a -> new SimpleGrantedAuthority(a)).collect(Collectors.toList());

		OAuth2Authentication authentication = super.extractAuthentication(map);
		OAuth2Request request = authentication.getOAuth2Request();
		OAuth2Request enhancedRequest = new OAuth2Request(request.getRequestParameters(), request.getClientId(), grantedAuthorities, request.isApproved(), request.getScope(), request.getResourceIds(), request.getRedirectUri(), request.getResponseTypes(), request.getExtensions());

		return new OAuth2Authentication(enhancedRequest, authentication.getUserAuthentication());
	}

}
