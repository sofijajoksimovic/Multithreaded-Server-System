package main;

import Requests.Request;
/**
 * WorkerThread pulls a Request assigned to it by the ThreadPool,
 * executes it, and returns itself to the idle pool.
 */
public class WorkerThread extends Thread {
	
	private Request request = null;
	private ThreadPool pool;
	
	public WorkerThread(ThreadPool pool) {
		this.pool = pool;
	}
	
	@Override
	public void run() {
		try {
			while (!interrupted()) {
				synchronized(this) {
					while (request == null) {
						wait();
					}
				}
				
				request.handle();
				
				request = null;
				pool.completeNotify(this);
			}
		} catch (InterruptedException e) {}
	}
	 /**
     * Assigns a Request to this WorkerThread and wakes it, in a thread-safe way.
     * @param r non-null Request to process
     */
	public synchronized void setRequest(Request r) {
		this.request = r;
		notify();
	}
}
