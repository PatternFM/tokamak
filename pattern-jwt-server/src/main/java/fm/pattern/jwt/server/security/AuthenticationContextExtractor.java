package fm.pattern.jwt.server.security;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

final class AuthenticationContextExtractor {

	private AuthenticationContextExtractor() {

	}

	static String getAccountId() {
		Authentication authentication = getContext().getAuthentication();
		if (!(authentication instanceof OAuth2Authentication)) {
			return null;
		}

		OAuth2Authentication oauth = (OAuth2Authentication) authentication;

		Map<String, Serializable> extensions = oauth.getOAuth2Request().getExtensions();
		if (extensions == null || extensions.isEmpty()) {
			return null;
		}

		if (!extensions.containsKey("account_id")) {
			return null;
		}

		return (String) extensions.get("account_id");
	}

	static String getClientId() {
		Authentication authentication = getContext().getAuthentication();
		if (!(authentication instanceof OAuth2Authentication)) {
			return null;
		}

		OAuth2Authentication oauth = (OAuth2Authentication) authentication;
		return oauth.getOAuth2Request().getClientId();
	}

	static Set<String> getClientRoles() {
		Authentication authentication = getContext().getAuthentication();
		if (!(authentication instanceof OAuth2Authentication)) {
			return new HashSet<String>();
		}

		OAuth2Authentication oauth = (OAuth2Authentication) authentication;
		if (oauth.getAuthorities() == null || oauth.getAuthorities().isEmpty()) {
			return new HashSet<String>();
		}

		return oauth.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.toSet());
	}

	static Set<String> getUserRoles() {
		Authentication authentication = getContext().getAuthentication();
		if (!(authentication instanceof OAuth2Authentication)) {
			return new HashSet<String>();
		}

		OAuth2Authentication oauth = (OAuth2Authentication) authentication;
		if (oauth.isClientOnly()) {
			return new HashSet<String>();
		}

		Authentication userAuthentication = oauth.getUserAuthentication();
		if (userAuthentication.getAuthorities() == null || userAuthentication.getAuthorities().isEmpty()) {
			return new HashSet<String>();
		}

		return userAuthentication.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.toSet());
	}

}
