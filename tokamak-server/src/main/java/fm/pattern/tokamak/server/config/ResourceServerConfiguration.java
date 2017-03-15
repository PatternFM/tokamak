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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import fm.pattern.tokamak.authorization.AuthorizationContextProvider;
import fm.pattern.tokamak.authorization.OAuth2AuthorizationContext;
import fm.pattern.tokamak.server.endpoints.RestExceptionRenderer;

@Configuration
@EnableWebSecurity
public class ResourceServerConfiguration {

    @Bean
    public AuthorizationContextProvider authorizationContextProvider() {
        return new OAuth2AuthorizationContext();
    }

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfig extends ResourceServerConfigurerAdapter {

        @Value("${oauth2.audience}")
        private String audience;

        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().permitAll();
        }

        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.accessDeniedHandler(accessDeniedHandler()).authenticationEntryPoint(authenticationEntryPoint()).resourceId(audience);
        }

        @Bean
        public AccessDeniedHandler accessDeniedHandler() {
            OAuth2AccessDeniedHandler accessDeniedHandler = new OAuth2AccessDeniedHandler();
            accessDeniedHandler.setExceptionRenderer(new RestExceptionRenderer());
            return accessDeniedHandler;
        }

        @Bean
        public AuthenticationEntryPoint authenticationEntryPoint() {
            OAuth2AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
            authenticationEntryPoint.setExceptionRenderer(new RestExceptionRenderer());
            return authenticationEntryPoint;
        }

    }

}
