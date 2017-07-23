package fm.pattern.tokamak.server.conversion;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fm.pattern.tokamak.sdk.commons.CriteriaRepresentation;
import fm.pattern.tokamak.server.IntegrationTest;
import fm.pattern.tokamak.server.repository.Criteria;

public class CriteriaConversionServiceTest extends IntegrationTest {

	@Autowired
	private CriteriaConversionService criteriaConversionService;

	@Test
	public void shouldBeAbleToConvertCriteriaIntoACriteriaRepresentation() {
		Criteria criteria = Criteria.criteria().from(new Date()).to(new Date()).limit(10).page(1);

		CriteriaRepresentation representation = criteriaConversionService.convert(criteria);
		assertThat(representation.getPage()).isEqualTo(criteria.getPage());
		assertThat(representation.getLimit()).isEqualTo(criteria.getLimit());
		assertThat(representation.getFrom()).isNotNull();
		assertThat(representation.getTo()).isNotNull();
	}

}
