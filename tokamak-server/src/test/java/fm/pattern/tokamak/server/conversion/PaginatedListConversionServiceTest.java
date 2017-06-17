package fm.pattern.tokamak.server.conversion;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.tokamak.sdk.commons.PaginatedListRepresentation;
import fm.pattern.tokamak.sdk.model.AccountRepresentation;
import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.pagination.Criteria;
import fm.pattern.tokamak.server.pagination.PaginatedList;

public class PaginatedListConversionServiceTest extends IntegrationTest {

	@Autowired
	private PaginatedListConversionService paginatedListConversionService;

	@Autowired
	private CriteriaConversionService criteriaConversionService;

	@Test
	public void shouldBeAbleToConverAPaginatedListIntoAPaginatedListRepresentation() {
		Criteria criteria = Criteria.criteria().from(new Date()).to(new Date()).limit(10).page(1);
		PaginatedList<?> list = new PaginatedList<>(new ArrayList<>(), 100, criteria);

		PaginatedListRepresentation<AccountRepresentation> representation = paginatedListConversionService.convert(list, AccountRepresentation.class);
		assertThat(representation.getPayload()).isEmpty();
		assertThat(representation.getCriteria()).isEqualToComparingFieldByField(criteriaConversionService.convert(criteria));
		assertThat(representation.getPages()).isEqualTo(10);
		assertThat(representation.getTotal()).isEqualTo(100);
		assertThat(representation.getRemaining()).isEqualTo(90);
	}

}
