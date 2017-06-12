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

package fm.pattern.tokamak.server.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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

import com.google.common.collect.Lists;

import fm.pattern.tokamak.server.security.AccountAuthenticationService;
import fm.pattern.tokamak.server.security.ClientAuthenticationService;
import fm.pattern.tokamak.server.security.CustomJwtTokenEnhancer;
import fm.pattern.tokamak.server.security.JWTTokenConverter;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfiguration {

	@Bean(name = "passwordEncoder")
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Configuration
	@Order(-5)
	protected static class LoginConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		private AuthenticationManager manager;

		@Autowired
		private AccountAuthenticationService accountAuthenticationService;

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.parentAuthenticationManager(manager);
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.formLogin().loginPage("/login").permitAll().and().requestMatchers().antMatchers("/login", "/oauth/authorize", "/oauth/confirm_access").and().authorizeRequests().anyRequest().authenticated().and().userDetailsService(accountAuthenticationService);
		}

	}

	@Configuration
	@EnableAuthorizationServer
	protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {

		@Value("${oauth2.audience}")
		private String audience;

		@Value("${oauth2.privateKey}")
		private String privateKey;

		@Value("${oauth2.publicKey}")
		private String publicKey;

		@Value("${oauth2.accessTokenValiditySeconds}")
		private Integer accessTokenValiditySeconds;

		@Value("${oauth2.refreshTokenValiditySeconds}")
		private Integer refreshTokenValiditySeconds;

		@Autowired
		private ClientAuthenticationService clientAuthenticationService;

		@Autowired
		private AccountAuthenticationService accountAuthenticationService;

		@Autowired
		private CustomJwtTokenEnhancer customTokenEnhancer;

		@Autowired
		private JWTTokenConverter tokenConverter;

		@Bean
		public AuthenticationProvider clientAuthenticationProvider() {
			DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
			provider.setPasswordEncoder(new BCryptPasswordEncoder());
			provider.setUserDetailsService(new ClientDetailsUserDetailsService(clientAuthenticationService));
			return provider;
		}

		@Bean
		public AuthenticationProvider userAuthenticationProvider() {
			DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
			provider.setPasswordEncoder(new BCryptPasswordEncoder());
			provider.setUserDetailsService(accountAuthenticationService);
			return provider;
		}

		@Bean
		public AuthenticationManager authenticationManager() {
			return new ProviderManager(Arrays.asList(clientAuthenticationProvider(), userAuthenticationProvider()));
		}

		@Bean
		public TokenStore tokenStore() {
			return new JwtTokenStore(accessTokenConverter());
		}

		@Bean
		public JwtAccessTokenConverter accessTokenConverter() {
			JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
			converter.setSigningKey(privateKey);
			converter.setVerifierKey(publicKey);
			converter.setAccessTokenConverter(tokenConverter);
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
			TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
			tokenEnhancerChain.setTokenEnhancers(Lists.newArrayList(customTokenEnhancer, accessTokenConverter()));
			return tokenEnhancerChain;
		}

		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.authenticationManager(authenticationManager()).tokenServices(tokenServices());
			endpoints.pathMapping("/oauth/token", "/v1/oauth/token");
			// endpoints.pathMapping("/oauth/authorize", "/v1/oauth/authorize");
		}

		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.withClientDetails(clientAuthenticationService);
		}

		public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
			oauthServer.passwordEncoder(new BCryptPasswordEncoder()).realm(audience);
		}

	}

}
