package fm.pattern.tokamak.server.repository;

import static fm.pattern.tokamak.server.dsl.AccountDSL.account;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.model.Account;

public class CacheIntegrationTest extends IntegrationTest {

	@Autowired
	@Qualifier("accountCache")
	private Cache cache;

	@Test
	public void shouldBeAbleToStoreAndRetrieveAndDeleteAnObjectFromCache() {
		Account account = account().thatIs().persistent().build();
		cache.put("account", account);

		Account result = cache.get("account", Account.class);
		assertThat(result).isEqualTo(account);

		cache.delete("account");
		assertThat(cache.get("account", Account.class)).isNull();
	}

	@Test
	public void shouldBeAbleToDetectWhetherAKeyIsInTheCache() {
		cache.put("key", "value");

		assertThat(cache.contains("key")).isTrue();
		assertThat(cache.contains("bar")).isFalse();

		cache.delete("key");
	}

	@Test
	public void shouldReturnANullValueWhenAValueCannotBeFoundForAKey() {
		assertThat(cache.get("lasidjfalsdifjsl", String.class)).isNull();
	}

}
