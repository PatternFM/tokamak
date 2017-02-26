package fm.pattern.jwt.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import fm.pattern.jwt.server.security.AuthenticationContextInterceptor;

@Configuration
@ComponentScan("fm.pattern.jwt.server.endpoints")
public class WebConfiguration extends WebMvcConfigurerAdapter {

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AuthenticationContextInterceptor());
	}

}
