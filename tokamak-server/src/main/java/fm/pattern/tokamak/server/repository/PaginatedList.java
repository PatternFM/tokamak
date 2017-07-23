package fm.pattern.tokamak.server.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@SuppressWarnings({ "hiding" })
public class PaginatedList<T> implements List<T> {

	private final Criteria criteria;
	private final List<T> data = new ArrayList<>();
	private final Integer total;

	public PaginatedList(List<T> data, Integer total, Criteria criteria) {
		this.data.addAll(data);
		this.total = total <= 0 ? 0 : total;
		this.criteria = criteria;
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public Range getRange() {
		return criteria.getRange();
	}

	public Integer getLimit() {
		return criteria.getLimit();
	}

	public Integer getPage() {
		return criteria.getPage();
	}

	public Integer getTotal() {
		return total;
	}

	public Integer getMaxResultsOnNextPage() {
		if (!hasNextPage()) {
			return 0;
		}
		return (getRemainingResults() > getLimit() ? getLimit() : getRemainingResults());
	}

	public Integer getRemainingResults() {
		Integer remaining = getTotal() - (getPage() * getLimit());
		return remaining <= 0 ? 0 : remaining;
	}

	public Integer getTotalPages() {
		if (total == 0) {
			return 0;
		}
		return (int) Math.ceil((double) total / (double) getLimit());
	}

	public boolean hasNextPage() {
		return getTotalPages() > 1 && (getPage() < getTotalPages());
	}

	public boolean hasPreviousPage() {
		return getTotalPages() > 1 && (getPage() != 1);
	}

	@Override
	public boolean add(T o) {
		return data.add(o);
	}

	@Override
	public void add(int index, T element) {
		data.add(index, element);
	}

	@Override
	public void clear() {
		data.clear();
	}

	@Override
	public boolean contains(Object o) {
		return data.contains(o);
	}

	@Override
	public T get(int index) {
		return data.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return data.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return data.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return data.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return data.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return data.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return data.remove(o);
	}

	@Override
	public T remove(int index) {
		return data.remove(index);
	}

	@Override
	public T set(int index, T element) {
		return data.set(index, element);
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return data.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return data.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return data.toArray(a);
	}

	@Override
	public boolean containsAll(java.util.Collection<?> c) {
		return data.containsAll(c);
	}

	@Override
	public boolean addAll(java.util.Collection<? extends T> c) {
		return data.addAll(c);
	}

	@Override
	public boolean addAll(int index, java.util.Collection<? extends T> c) {
		return data.addAll(index, c);
	}

	@Override
	public boolean removeAll(java.util.Collection<?> c) {
		return data.removeAll(c);
	}

	@Override
	public boolean retainAll(java.util.Collection<?> c) {
		return data.retainAll(c);
	}

}
