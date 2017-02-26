package fm.pattern.jwt.server.dsl;

import fm.pattern.jwt.server.StaticApplicationContextHolder;

@SuppressWarnings("unchecked")
abstract class AbstractDSL<T, C> {

	private boolean persistent;

	public T thatIs() {
		return (T) this;
	}

	public T thatAre() {
		return (T) this;
	}

	public T and() {
		return (T) this;
	}

	public T persistent() {
		this.persistent = true;
		return (T) this;
	}

	public boolean shouldPersist() {
		return persistent;
	}

	public <S> S load(Class<S> serviceClass) {
		return StaticApplicationContextHolder.get(serviceClass);
	}

	public static void pause(Integer milliseconds) {
		try {
			Thread.sleep(milliseconds);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public abstract C build();

}
