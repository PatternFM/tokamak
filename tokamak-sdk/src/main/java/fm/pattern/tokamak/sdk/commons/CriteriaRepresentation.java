package fm.pattern.tokamak.sdk.commons;

public class CriteriaRepresentation {

	private Long from;
	private Long to;

	private Integer page;
	private Integer limit;

	public static CriteriaRepresentation criteria() {
		return new CriteriaRepresentation();
	}

	public CriteriaRepresentation() {

	}

	public CriteriaRepresentation from(Long from) {
		this.setFrom(from);
		return this;
	}

	public CriteriaRepresentation to(Long to) {
		this.setTo(to);
		return this;
	}

	public CriteriaRepresentation page(Integer page) {
		this.page = page;
		return this;
	}

	public CriteriaRepresentation limit(Integer limit) {
		this.limit = limit;
		return this;
	}

	public Long getFrom() {
		return from;
	}

	public void setFrom(Long from) {
		this.from = from;
	}

	public Long getTo() {
		return to;
	}

	public void setTo(Long to) {
		this.to = to;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

}
