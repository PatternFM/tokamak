package fm.pattern.tokamak.sdk.commons;

import java.util.List;

public class PaginatedListRepresentation<T> extends Representation {

	private List<T> payload;
	private CriteriaRepresentation criteria;

	private Integer total;
	private Integer remaining;
	private Integer pages;
	
	public PaginatedListRepresentation() {

	}

	public PaginatedListRepresentation(List<T> payload) {
		this.payload = payload;
	}

	public List<T> getPayload() {
		return payload;
	}

	public void setPayload(List<T> payload) {
		this.payload = payload;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getRemaining() {
		return remaining;
	}

	public void setRemaining(Integer remaining) {
		this.remaining = remaining;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public CriteriaRepresentation getCriteria() {
		return criteria;
	}

	public void setCriteria(CriteriaRepresentation criteria) {
		this.criteria = criteria;
	}

}
