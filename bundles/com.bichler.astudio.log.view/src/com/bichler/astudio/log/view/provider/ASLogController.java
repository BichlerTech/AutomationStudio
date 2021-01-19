package com.bichler.astudio.log.view.provider;

import java.io.IOException;

import org.eclipse.swt.widgets.Display;

import com.bichler.astudio.log.view.Activator;
import com.bichler.astudio.log.view.preferences.ASLogPreferenceManager;
import com.bichler.astudio.log.view.viewer.ASLogView;
import com.bichler.astudio.log.server.ASLogServer;
import com.bichler.astudio.log.server.core.ASLog;
import com.bichler.astudio.log.server.core.ASLogCollection;
import com.bichler.astudio.log.server.core.ASLogNotifier;
import com.bichler.astudio.log.server.listener.ASLogEventListener;

public class ASLogController {

	/**
	 * Preferences store
	 */
	private static ASLogPreferenceManager preferences;

	/**
	 * Collection containing received Log4j messages.
	 */
	private final ASLogCollection logCollection;
	/**
	 * Listener to store into log collection.
	 */
	private ASLogEventListener logListener;
	/**
	 * Thread to update view.
	 */
	private RefreshTask refreshTimer;
	/**
	 * ServerSocket to receive Log4j messages.
	 */
	private ASLogServer server;

	/**
	 * Controller for incomming log messages into CometLogView.
	 * 
	 * @param View
	 *            CometLog4jView
	 */
	public ASLogController(ASLogView view) {
		preferences = Activator.getDefault().getPreferenceManager();

		this.logCollection = new ASLogCollection(preferences.getLogBufferSize());
		this.logListener = new ASLogEventListener() {

			@Override
			public void handleLogEvent(ASLog logEvent) {
				logCollection.add(logEvent);
			}
		};

		ASLogNotifier.addListener(this.logListener);

		this.refreshTimer = new RefreshTask(view);
		this.refreshTimer.start();
	}

	public void clear() {
		this.logCollection.clear();
	}

	/**
	 * Disposes the controller.
	 */
	public void dispose() {
		ASLogNotifier.removeListener(this.logListener);
		this.refreshTimer.stopRun();
		stopServer();
	}

	/**
	 * Starts CometLog4jServer.
	 */
	public void startServer() {
		if (this.server != null) {
			// server already started
			return;
		}

		try {
			this.server = new ASLogServer(preferences.getLogServerPort());
			this.server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stops CometLog4jServer.
	 */
	public void stopServer() {
		if (this.server != null) {
			this.server.shutdown();
			this.server = null;
		}
	}

	/**
	 * Gets CometLog collection containing all Log4j events.
	 * 
	 * @return CometLogCollection
	 */
	public ASLogCollection getLogCollection() {
		return this.logCollection;
	}

	private static class RefreshTask extends Thread {

		private ASLogView view;
		private volatile boolean running = true;

		/**
		 * Thread to update CometLog4jView.
		 * 
		 * @param View
		 *            CometLog4jView
		 */
		public RefreshTask(ASLogView view) {
			this.view = view;
			setDaemon(true);
		}

		@Override
		public void run() {
			while (this.running) {
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						view.refreshViewer();
					}
				});

				try {
					sleep(preferences.getRefreshTime());
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					e.printStackTrace();
				}
			}
		}

		/**
		 * Finish executing thread.
		 */
		public void stopRun() {
			this.running = false;
		}
	}
}
