package fm.pattern.jwt.server.security;

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

		if (map.containsKey("account_id")) {
			request.getExtensions().put("account_id", (String) map.get("account_id"));
		}

		return new OAuth2Authentication(enhancedRequest, authentication.getUserAuthentication());
	}

}
