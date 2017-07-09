package fm.pattern.tokamak.server.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {

	private static final Integer CLIENT_DB_INDEX = 4;
	private static final Integer ACCOUNT_DB_INDEX = 5;

	@Primary
	@Bean("clientConnectionFactory")
	RedisConnectionFactory clientConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setDatabase(CLIENT_DB_INDEX);
		return factory;
	}

	@Bean("accountConnectionFactory")
	RedisConnectionFactory accountConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setDatabase(ACCOUNT_DB_INDEX);
		return factory;
	}

	@Bean("clientRedisTemplate")
	RedisTemplate<?, ?> clientRedisTemplate(@Qualifier("clientConnectionFactory") RedisConnectionFactory clientConnectionFactory) {
		RedisTemplate<byte[], byte[]> template = new RedisTemplate<byte[], byte[]>();
		template.setConnectionFactory(clientConnectionFactory);
		return template;
	}

	@Bean("accountRedisTemplate")
	RedisTemplate<?, ?> accountRedisTemplate(@Qualifier("accountConnectionFactory") RedisConnectionFactory accountConnectionFactory) {
		RedisTemplate<byte[], byte[]> template = new RedisTemplate<byte[], byte[]>();
		template.setConnectionFactory(accountConnectionFactory);
		return template;
	}

}
