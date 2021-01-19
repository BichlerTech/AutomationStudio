package opc.client.service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.StatusCodes;

/**
 * 
 * Attempts to reconnect to a server automatically after a connection has lost.
 * 
 * 
 * 
 * @author Thomas Z&ouml;chbauer
 * 
 * @since 23.05.2012, HB-Softsolution e.U.
 * 
 */
public class ReconnectSessionHandler {
	private ClientSession session = null;
	private long reconnectPeriod = 0;
	private Timer reconnectTimer = null;
	private Object lock = new Object();
	private volatile boolean userCanceled = false;
	private boolean overflowReconnected = false;
	// the reconnect should stop due to an internal stop signal
	private boolean stopped = false;

	/**
	 * 
	 * Beginns the reconnect process.
	 * 
	 * 
	 * 
	 * @param period
	 * 
	 * 
	 * 
	 * @param ClientSessionKeepAlive
	 * 
	 * @param ReconnectedPerioad
	 * 
	 * @throws ServiceResultException
	 * 
	 */
	public void beginReconnect(final ClientSession session, long delay, long period) {
		this.session = session;
		synchronized (this.lock) {
			// added HB 20170117
			if (!this.userCanceled) {
				this.setReconnectPeriod(period);
				session.onReconnectStart();
				this.reconnectTimer = new Timer("UA Session Reconnect Timer - " + session.getSessionName(), false);
				this.reconnectTimer.schedule(new ReconnectTimerTask(), delay, period);
			}
		}
	}

	public void beginReconnect(ClientSession session) {
		this.session = session;
		synchronized (this.lock) {
			// added HB 20170117
			if (!this.userCanceled) {
				this.setReconnectPeriod(-1);
				session.onReconnectStart();
				this.reconnectTimer = new Timer("UA Session Reconnect Timer - " + session.getSessionName(), false);
				this.reconnectTimer.schedule(new ReconnectTimerTask(), 0);
			}
		}
	}

	public long getReconnectPeriod() {
		return reconnectPeriod;
	}

	public void setReconnectPeriod(long reconnectPeriod) {
		this.reconnectPeriod = reconnectPeriod;
	}

	// HB 2017.01.17
	public void cancelReconnect() {
		userCanceled = true;
	}

	public void stopReconnect() {
		// cancel the reconnect timer
		if (reconnectTimer != null) {
			reconnectTimer.cancel();
			reconnectTimer = null;
		}
	}

	class ReconnectTimerTask extends TimerTask {
		@Override
		public void run() {
			onReconnect();
		}

		private void onReconnect() {
			// if reconnect is canceled -> stop reconnect timer
			if (userCanceled) {
				synchronized (lock) {
					cancel();
					if (reconnectTimer != null) {
						reconnectTimer.cancel();
						reconnectTimer.purge();
						reconnectTimer = null;
					}
				}
			} else {
				synchronized (lock) {
					if (reconnectTimer == null) {
						return;
					}
					if (doReconnect()) {
						// callback
						cancelReconnect();
						session.onReconnectFinish(true);
					} else {
						if (session.getReconnectCount() > -1 && session.decrementCounter() <= 0) {
							// do we reach reconnect count, so stop timer
							stopReconnect();
							session.onReconnectStopped();
							cancel();
						} else {
							if (reconnectPeriod <= 0) {
								cancelReconnect();
								session.onReconnectFinish(false);
							}
						}
					}
				}
			}
		}

		private boolean doReconnect() {
			synchronized (lock) {
				if (overflowReconnected) {
					return true;
				}
				try {
					session.reconnect(false);
					return true;
				} catch (NullPointerException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.FINE,
							"Reconnect failed, due to following error: " + e.getMessage());
					boolean recreateNow = false;
					/**
					 * 
					 * Session is not valid!
					 * 
					 */
					if (StatusCodes.Bad_SessionClosed.equals(e.getStatusCode().getValue())
							|| StatusCodes.Bad_SessionIdInvalid.equals(e.getStatusCode().getValue())) {
						recreateNow = true;
					} else {
						DateTime lastKeepAliveTimer = session.getLastKeepAliveTime();
						double timeout = session.getSessionTimeout();
						if (lastKeepAliveTimer != null && timeout >= 0) {
							long timedOut = (long) (session.getLastKeepAliveTime().getTimeInMillis()
									+ session.getSessionTimeout());
							// check if reconnection is still an option
							if (timedOut > DateTime.currentTime().getTimeInMillis()) {
								return false;
							}
						}
					}
					// must re-create the subscription when the server comes back
					if (!recreateNow) {
						return false;
					}
				}
				try {
					session.recreate();
					overflowReconnected = true;
					return true;
				} catch (Exception e) {
					Logger.getLogger(getClass().getName()).log(Level.FINE,
							"doReconnect failed, due to following error: " + e.getMessage());
					return false;
				}
			}
		}
	}
}
