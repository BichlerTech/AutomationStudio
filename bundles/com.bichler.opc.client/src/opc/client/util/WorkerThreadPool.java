package opc.client.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A thread pool for the UAClient application to execute listeners called from a
 * session.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class WorkerThreadPool {
	private static ExecutorService threadPool = null;

	/**
	 * private constructor to hide the public one
	 */
	private WorkerThreadPool() {
	}

	/**
	 * Returns a Threadpool to execute user threads
	 * 
	 * @return Threadpool
	 */
	public static ExecutorService getThreadPool() {
		if (threadPool == null) {
			threadPool = Executors.newCachedThreadPool();
		}
		return threadPool;
	}
}
