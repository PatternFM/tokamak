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

package fm.pattern.jwt.server.config;

import java.security.KeyPair;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import com.google.common.collect.Lists;

import fm.pattern.jwt.server.security.AccountAuthenticationService;
import fm.pattern.jwt.server.security.AccountTokenEnhancer;
import fm.pattern.jwt.server.security.ClientAuthenticationService;
import fm.pattern.jwt.server.security.SimpleAccessTokenConverter;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfiguration {

	private static final String RESOURCE_ID = "oauth-service";

	@Bean(name = "passwordEncoder")
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Configuration
	@EnableAuthorizationServer
	protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {

		/** TODO : Configure these attributes via yml file. */

		/** Access tokens are valid for 2 hours. **/
		private final Integer accessTokenValiditySeconds = 60 * 60 * 2;

		/** Refresh tokens are valid for 1 year. **/
		private final Integer refreshTokenValiditySeconds = 60 * 60 * 24 * 365 * 1;

		@Autowired
		private ClientAuthenticationService clientAuthenticationService;

		@Autowired
		private AccountAuthenticationService accountAuthenticationService;

		@Autowired
		private JwtEncryptionProperties jwtEncryptionProperties;

		@Autowired
		private AccountTokenEnhancer accountTokenEnhancer;

		@Bean
		public AuthenticationProvider clientAuthenticationProvider() {
			DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
			provider.setPasswordEncoder(passwordEncoder());
			provider.setUserDetailsService(new ClientDetailsUserDetailsService(clientAuthenticationService));
			return provider;
		}

		@Bean
		public AuthenticationProvider userAuthenticationProvider() {
			DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
			provider.setPasswordEncoder(passwordEncoder());
			provider.setUserDetailsService(accountAuthenticationService);
			return provider;
		}

		@Bean
		public AuthenticationManager authenticationManager() {
			return new ProviderManager(Arrays.asList(clientAuthenticationProvider(), userAuthenticationProvider()));
		}

		@Bean
		public TokenStore tokenStore() {
			return new JwtTokenStore(tokenEnhancer());
		}

		@Bean
		public JwtAccessTokenConverter tokenEnhancer() {
			KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource(jwtEncryptionProperties.getKeyStore()), jwtEncryptionProperties.getKeyStorePassword().toCharArray());
			KeyPair keyPair = factory.getKeyPair(jwtEncryptionProperties.getKeyPairAlias(), jwtEncryptionProperties.getKeyPairPassword().toCharArray());
			JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
			converter.setKeyPair(keyPair);
			converter.setAccessTokenConverter(new SimpleAccessTokenConverter());
			return converter;
		}

		@Bean(name = "defaultAuthorizationServerTokenServices")
		public AuthorizationServerTokenServices tokenServices() {
			DefaultTokenServices tokenServices = new DefaultTokenServices();
			tokenServices.setSupportRefreshToken(true);
			tokenServices.setTokenStore(tokenStore());
			tokenServices.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
			tokenServices.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
			tokenServices.setTokenEnhancer(tokenEnhancerChain());
			tokenServices.setClientDetailsService(clientAuthenticationService);
			return tokenServices;
		}

		@Bean
		public TokenEnhancerChain tokenEnhancerChain() {
			final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
			tokenEnhancerChain.setTokenEnhancers(Lists.newArrayList(accountTokenEnhancer, tokenEnhancer()));
			return tokenEnhancerChain;
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}

		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.authenticationManager(authenticationManager()).tokenServices(tokenServices());
			endpoints.pathMapping("/oauth/token", "/v1/oauth/token");
		}

		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.withClientDetails(clientAuthenticationService);
		}

		public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
			oauthServer.passwordEncoder(passwordEncoder()).realm(RESOURCE_ID);
		}

	}

}
