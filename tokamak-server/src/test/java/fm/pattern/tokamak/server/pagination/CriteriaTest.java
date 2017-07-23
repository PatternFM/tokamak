package fm.pattern.tokamak.server.pagination;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;

import fm.pattern.tokamak.server.repository.Criteria;

public class CriteriaTest {

	@Test
	public void shouldHaveAFromDate() {
		assertThat(Criteria.criteria().from(new Date()).hasFrom()).isTrue();
	}

	@Test
	public void shouldHaveAToDate() {
		assertThat(Criteria.criteria().to(new Date()).hasTo()).isTrue();
	}

	@Test
	public void shouldHaveADefaultLimitOf50() {
		assertThat(Criteria.criteria().getLimit()).isEqualTo(50);
	}

	@Test
	public void shouldProduceADefaultLimitIfTheLimitIsInvalid() {
		assertThat(Criteria.criteria().limit(-3).getLimit()).isEqualTo(50);
		assertThat(Criteria.criteria().limit(null).getLimit()).isEqualTo(50);
	}

	@Test
	public void shouldBeAbleToSetALimit() {
		assertThat(Criteria.criteria().limit(15).getLimit()).isEqualTo(15);
	}

}
