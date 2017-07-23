package fm.pattern.tokamak.server.repository;

public class Range {

	private static final Integer DEFAULT_PAGE = 1;
	private static final Integer DEFAULT_LIMIT = 50;

	private Integer limit;
	private Integer page;

	public static Range all() {
		return range().page(DEFAULT_PAGE).limit(Integer.MAX_VALUE);
	}

	public static Range range() {
		return new Range();
	}

	private Range() {

	}

	public Range page(Integer page) {
		this.page = (page == null || page < 1) ? DEFAULT_PAGE : page;
		return this;
	}

	public Range limit(Integer limit) {
		this.limit = (limit == null || limit < 1) ? DEFAULT_LIMIT : limit;
		return this;
	}

	public Integer getLimit() {
		return limit == null ? DEFAULT_LIMIT : limit;
	}

	public Integer getPage() {
		return page == null ? 1 : page;
	}

	public Integer getFirstResult() {
		return getPage() == 1 ? 0 : (getPage() - 1) * getLimit();
	}

}
