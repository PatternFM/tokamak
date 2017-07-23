package fm.pattern.tokamak.server.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fm.pattern.tokamak.sdk.commons.PaginatedListRepresentation;
import fm.pattern.tokamak.server.repository.PaginatedList;

@Service
public class PaginatedListConversionService {

	private final CriteriaConversionService criteriaConversionService;

	@Autowired
	public PaginatedListConversionService(CriteriaConversionService criteriaConversionService) {
		this.criteriaConversionService = criteriaConversionService;
	}

	public <C> PaginatedListRepresentation<C> convert(PaginatedList<?> list, Class<C> type) {
		PaginatedListRepresentation<C> representation = new PaginatedListRepresentation<>();
		representation.setCriteria(criteriaConversionService.convert(list.getCriteria()));
		representation.setTotal(list.getTotal());
		representation.setRemaining(list.getRemainingResults());
		representation.setPages(list.getTotalPages());
		return representation;
	}

}
