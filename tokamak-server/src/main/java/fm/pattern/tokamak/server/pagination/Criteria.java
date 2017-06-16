package fm.pattern.tokamak.server.pagination;

import java.util.Date;

public class Criteria {

	private Range range = Range.range();
	private Date from = null;
	private Date to = null;

	public static Criteria criteria() {
		return new Criteria();
	}

	public Criteria from(Date date) {
		return from(date.getTime());
	}

	public Criteria from(Long timestamp) {
		this.from = timestamp == null ? null : new Date(timestamp);
		return this;
	}

	public Date getFrom() {
		return from;
	}

	public boolean hasFrom() {
		return from != null;
	}

	public Criteria to(Date date) {
		return to(date.getTime());
	}

	public Criteria to(Long timestamp) {
		this.to = timestamp == null ? null : new Date(timestamp);
		return this;
	}

	public Date getTo() {
		return to;
	}

	public boolean hasTo() {
		return to != null;
	}

	public Criteria limit(Integer limit) {
		range.limit(limit);
		return this;
	}

	public Integer getLimit() {
		return range.getLimit();
	}

	public Criteria page(Integer page) {
		range.page(page);
		return this;
	}

	public Integer getPage() {
		return range.getPage();
	}

	public Integer getFirstResult() {
		return range.getFirstResult();
	}

	public Range getRange() {
		return range;
	}

}
