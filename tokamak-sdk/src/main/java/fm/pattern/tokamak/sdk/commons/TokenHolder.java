package fm.pattern.tokamak.sdk.commons;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class TokenHolder {

	private static String token = null;
	private static final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

	private TokenHolder() {

	}

	public static String token() {
		rwl.readLock().lock();
		try {
			return token;
		}
		finally {
			rwl.readLock().unlock();
		}

	}

	public static void token(String tok) {
		rwl.writeLock().lock();
		try {
			token = tok;
		}
		finally {
			rwl.writeLock().unlock();
		}
	}

}
