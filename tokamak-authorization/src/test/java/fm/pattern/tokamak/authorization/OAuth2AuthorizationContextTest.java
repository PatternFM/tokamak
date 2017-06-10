package fm.pattern.tokamak.authorization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class OAuth2AuthorizationContextTest {

	@Mock
	private SecurityContext context;

	@Mock
	private OAuth2Authentication authentication;

	@Mock
	private AnonymousAuthenticationToken anonymousAuthentication;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);

		when(context.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(context);
	}

	@Test
	public void shouldBeAuthenticatedWhenTheUnderlyingAuthenticationIsAuthenticated() {
		when(authentication.isAuthenticated()).thenReturn(true);

		OAuth2AuthorizationContext ctx = new OAuth2AuthorizationContext();
		Assertions.assertThat(ctx.isAuthenticated()).isTrue();
	}

	@Test
	public void shouldNotBeAuthenticatedWhenTheUnderlyingAuthenticationIsNotAuthenticated() {
		when(authentication.isAuthenticated()).thenReturn(false);

		OAuth2AuthorizationContext ctx = new OAuth2AuthorizationContext();
		assertThat(ctx.isAuthenticated()).isFalse();
	}

	@Test
	public void shouldNotBeAuthenticatedWhenTheUnderlyingAuthenticationIsNull() {
		when(context.getAuthentication()).thenReturn(null);
		SecurityContextHolder.setContext(context);

		OAuth2AuthorizationContext ctx = new OAuth2AuthorizationContext();
		assertThat(ctx.isAuthenticated()).isFalse();
	}

	@Test
	public void shouldNotBeAuthenticatedWhenTheUnderlyingAuthenticationIsNotAnOAuth2Authentication() {
		when(context.getAuthentication()).thenReturn(anonymousAuthentication);
		SecurityContextHolder.setContext(context);

		OAuth2AuthorizationContext ctx = new OAuth2AuthorizationContext();
		assertThat(ctx.isAuthenticated()).isFalse();
	}

}
