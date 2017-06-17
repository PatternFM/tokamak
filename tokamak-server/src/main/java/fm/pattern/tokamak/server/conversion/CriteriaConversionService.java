package fm.pattern.tokamak.server.conversion;

import org.springframework.stereotype.Service;

import fm.pattern.tokamak.sdk.commons.CriteriaRepresentation;
import fm.pattern.tokamak.server.pagination.Criteria;

@Service
public class CriteriaConversionService {

	public CriteriaRepresentation convert(Criteria criteria) {
		CriteriaRepresentation representation = new CriteriaRepresentation();
		representation.setFrom(criteria.hasFrom() ? criteria.getFrom().getTime() : null);
		representation.setTo(criteria.hasTo() ? criteria.getTo().getTime() : null);
		representation.setPage(criteria.getPage());
		representation.setLimit(criteria.getLimit());
		return representation;
	}

}
