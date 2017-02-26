package fm.pattern.jwt.server.security;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import fm.pattern.jwt.server.model.Account;
import fm.pattern.jwt.server.service.AccountService;
import fm.pattern.microstructure.Result;

@Component
public class AccountTokenEnhancer extends JwtAccessTokenConverter {

	private final AccountService accountService;

	@Autowired
	public AccountTokenEnhancer(AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);

		Map<String, Object> additionalInformation = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());
		additionalInformation.put("client_id", authentication.getOAuth2Request().getClientId());

		Authentication userAuthentication = authentication.getUserAuthentication();
		if (userAuthentication == null) {
			customAccessToken.setAdditionalInformation(additionalInformation);
			return super.enhance(customAccessToken, authentication);
		}

		AuthenticatedAccount user = getUser(userAuthentication);
		if (user == null) {
			customAccessToken.setAdditionalInformation(additionalInformation);
			return super.enhance(customAccessToken, authentication);
		}

		additionalInformation.put("account_id", user.getIdentfifier());

		customAccessToken.setAdditionalInformation(additionalInformation);
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
