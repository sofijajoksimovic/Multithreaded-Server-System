package main;

import java.util.*;

import Requests.Request;
/**
 * A minimal custom ThreadPool with prioritization:
 *   • A submitter thread queues incoming Requests (PriorityQueue)
 *   • A pool of WorkerThreads consumes them based on priority
 *   • Uses wait()/notify() for coordination
 */
public class ThreadPool extends Thread {
	/**Singleton Holder ensures lazy loading; thread-safe per JVM classloader*/
	private static class Holder {
        static final ThreadPool INST = new ThreadPool();
    }
	private static int POOL_SIZE=10;
	private PriorityQueue<Request> waiting = new PriorityQueue<Request>();
	/**
     * Idle pool of threads waiting to perform work.
     * On request completion, WorkerThread calls completeNotify() to re-enter "idle"
     */
	private LinkedList<WorkerThread> idle = new LinkedList<WorkerThread>();
	/** All worker threads (used for shutdown interrupting) */
	private LinkedList<WorkerThread> allThreads = new LinkedList<WorkerThread>();
	private boolean stopRequest = false;

	/**
     * The main submitter thread:
     * picks highest-priority task from 'waiting' as soon as a WorkerThread is idle
     * and assigns it by calling WorkerThread.setRequest()
     */
	@Override
	public void run() {
		try {
			while (!this.isInterrupted()) {
				synchronized (this) {
					while (waiting.isEmpty() || idle.isEmpty()) {
						wait(500);
						if (stopRequest && waiting.isEmpty()) {
							stopAll();
							interrupt();
						}
					}
					
					idle.removeFirst().setRequest(waiting.poll());
				}
			}
		} catch (InterruptedException e) {}
	}
	/**
     * Private constructor to set up max POOL_SIZE WorkerThreads
     * Each is started and added to both "idle" and "allThreads"
     * Finally, the ThreadPool submitter thread itself is started
     */
	protected ThreadPool() {
		for (int i = 0; i < POOL_SIZE; ++i) {
			WorkerThread t = new WorkerThread(this);
			t.start();
			idle.add(t);
			allThreads.add(t);
		}
		start();
	}
	
	public static ThreadPool getThreadPool() {
		return Holder.INST;
	}
	/**
     * Called by WorkerThread after finishing its Request.
     * Marks the thread idle again and notifies submitter to assign next task.
     */
	public synchronized void completeNotify(WorkerThread wt) {
		idle.add(wt);
		notify();
	}
	/**
     * Submit a new Request from outside the pool.
     * Adds it to 'waiting' queue, then notifies the submitter loop.
     */
	public synchronized void assignTask(Request req) {
		waiting.add(req);
		notify();
	}
	 /**
     * Interrupt all worker threads underground.
     * WorkerThread.run() should pay attention to isInterrupted().
     */
	public synchronized void stopAll() {
		for (WorkerThread t : allThreads)
			t.interrupt();
	}
	/**
     * Gracefully ask the pool to stop once queued tasks drain out.
     * Sets flag then notifies submitter to re-check and stop.
     */
	public synchronized void requestStop() {
		stopRequest = true;
		notify();
	}
	 /**
     * After changing priority values inside Request objects,
     * this method reorders the queue by:
     *   - copying old contents to a temporary list
     *   - clearing and repopulating waiting queue
     */
	public synchronized void changeOfPriorities() {
		List<Request> temp = new ArrayList<>(waiting);
		waiting.clear();
		waiting.addAll(temp);
	}
}
