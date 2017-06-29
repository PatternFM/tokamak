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

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import fm.pattern.tokamak.server.model.Account;
import fm.pattern.tokamak.server.service.AccountService;
import fm.pattern.valex.Result;

@Component
public class CustomJwtTokenEnhancer extends JwtAccessTokenConverter {

	@Value("${oauth2.issuer}")
	private String issuer;

	private final AccountService accountService;

	@Autowired
	public CustomJwtTokenEnhancer(AccountService accountService) {
		this.accountService = accountService;
	}

	/**
	 * Values placed into the map will be included in the JWT token and the OAuth 2 response itself.
	 */
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);

		Map<String, Object> map = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());
		map.put("sub", authentication.getOAuth2Request().getClientId());
		map.put("iss", issuer);
		map.put("iat", new Date().getTime() / 1000);

		Authentication userAuthentication = authentication.getUserAuthentication();
		if (userAuthentication == null) {
			customAccessToken.setAdditionalInformation(map);
			return super.enhance(customAccessToken, authentication);
		}

		AuthenticatedAccount account = getUser(userAuthentication);
		if (account == null) {
			customAccessToken.setAdditionalInformation(map);
			return super.enhance(customAccessToken, authentication);
		}

		map.put("sub", account.getIdentfifier());

		customAccessToken.setAdditionalInformation(map);
		return super.enhance(customAccessToken, authentication);
	}

	private AuthenticatedAccount getUser(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		if (principal instanceof String) {
			String username = (String) authentication.getPrincipal();
			Result<Account> result = accountService.findByUsername(username);
			return result.accepted() ? new AuthenticatedAccount(result.getInstance()) : null;
		}
		return (AuthenticatedAccount) authentication.getPrincipal();
	}

}
