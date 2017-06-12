package fm.pattern.tokamak.authorization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

public class OAuth2AuthorizationContextTest {

	@Mock
	private SecurityContext context;

	@Mock
	private OAuth2Authentication authentication;

	@Mock
	private TestingAuthenticationToken testAuthentication;

	@Mock
	private AnonymousAuthenticationToken anonymousAuthentication;

	@Mock
	private OAuth2Request request;

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

	@Test
	public void shouldReturnAnEmtpySetOfScopesIfTheAuthenticationIsNotAnOAuth2Authentication() {
		when(context.getAuthentication()).thenReturn(testAuthentication);
		SecurityContextHolder.setContext(context);

		OAuth2AuthorizationContext ctx = new OAuth2AuthorizationContext();
		assertThat(ctx.getScopes()).hasSize(0);
	}

	@Test
	public void shouldReturnAnEmptySetOfScopesIfTheAuthenticationHasNoScopes() {
		when(authentication.isAuthenticated()).thenReturn(true);
		when(authentication.getOAuth2Request()).thenReturn(request);

		OAuth2AuthorizationContext ctx = new OAuth2AuthorizationContext();
		assertThat(ctx.isAuthenticated()).isTrue();
		assertThat(ctx.getScopes()).hasSize(0);
	}

	@Test
	public void shouldReturnAnEmptySetOfAuthoritiesIfTheAuthenticationHasNoAuthorities() {
		when(authentication.isAuthenticated()).thenReturn(true);
		when(authentication.getOAuth2Request()).thenReturn(request);

		OAuth2AuthorizationContext ctx = new OAuth2AuthorizationContext();
		assertThat(ctx.isAuthenticated()).isTrue();
		assertThat(ctx.getAuthorities()).hasSize(0);
	}

	@Test
	public void shouldReturnAnEmtpySetOfAuthoritiesIfTheAuthenticationIsNotAnOAuth2Authentication() {
		when(context.getAuthentication()).thenReturn(testAuthentication);
		SecurityContextHolder.setContext(context);

		OAuth2AuthorizationContext ctx = new OAuth2AuthorizationContext();
		assertThat(ctx.getAuthorities()).hasSize(0);
	}

}
