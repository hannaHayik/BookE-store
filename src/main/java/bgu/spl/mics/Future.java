package bgu.spl.mics;

import java.util.concurrent.TimeUnit;

/**
 * A Future object represents a promised result - an object that will eventually
 * be resolved to hold a result of some operation. The class allows Retrieving
 * the result once it is available.
 * 
 * Only private methods may be added to this class. No public constructor is
 * allowed except for the empty constructor.
 */
public class Future<T> {

	private T result;
	private boolean keepWorking=true; 

	/**
	 * This should be the the only public constructor in this class.
	 */
	public Future() {
		// TODO: implement this
	}
	

	/**
	 * retrieves the result the Future object holds if it has been resolved. This is
	 * a blocking method! It waits for the computation in case it has not been
	 * completed.
	 * <p>
	 * 
	 * @return return the result of type T if it is available, if not wait until it
	 *         is available.
	 * 
	 */
	public T get() { // We saw it's better to use busy waiting in here, and the boolean variable indicates if someone did a resolve to this object.
		while ((this.result == null)&&(this.keepWorking)) {
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return this.result;
	}

	/**
	 * Resolves the result of this Future object.
	 */
	public void resolve(T result) {
		this.result = result;
		this.keepWorking=false;
	}

	/**
	 * @return true if this object has been resolved, false otherwise
	 */
	public boolean isDone() {
		return this.result != null;
	}

	/**
	 * retrieves the result the Future object holds if it has been resolved, This
	 * method is non-blocking, it has a limited amount of time determined by
	 * {@code timeout}
	 * <p>
	 * 
	 * @param timout the maximal amount of time units to wait for the result.
	 * @param unit   the {@link TimeUnit} time units to wait.
	 * @return return the result of type T if it is available, if not, wait for
	 *         {@code timeout} TimeUnits {@code unit}. If time has elapsed, return
	 *         null.
	 */
	public T get(long timeout, TimeUnit unit) {
		synchronized(this){
		long current_timeout = TimeUnit.MILLISECONDS.convert(timeout, unit);
		while (this.result == null && current_timeout > 0) {
			long current_time = System.currentTimeMillis();
			try {
				wait(current_timeout, 0);
			} catch (InterruptedException e) {
			}
			current_time -= System.currentTimeMillis();
			current_timeout += current_time;
		}
		return this.result;
		}
	}

}
