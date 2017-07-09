package fm.pattern.tokamak.server.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component("clientCache")
@SuppressWarnings("unchecked")
class ClientCache implements Cache {

	private final RedisTemplate<String, Object> template;

	@Autowired
	public ClientCache(@Qualifier("clientRedisTemplate") RedisTemplate<String, Object> template) {
		this.template = template;
		this.template.setKeySerializer(new StringRedisSerializer());
	}

	@Override
	public <T> T put(String key, T value) {
		try {
			template.opsForValue().set(key, value);
		}
		catch (Exception e) {

		}
		return value;
	}

	@Override
	public void delete(String key) {
		try {
			template.delete(key);
		}
		catch (Exception e) {

		}
	}

	@Override
	public boolean contains(String key) {
		try {
			return template.hasKey(key);
		}
		catch (Exception e) {
			return false;
		}
	}

	@Override
	public <T> T get(String key, Class<T> type) {
		try {
			return (T) template.opsForValue().get(key);
		}
		catch (Exception e) {
			return null;
		}
	}

}
