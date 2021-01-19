package opc.client.application.listener;

import opc.client.service.ClientSession;

/**
 * Listener when a client tries to reconnect.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public interface ReconnectListener {
	void onReconnectStarted(ClientSession session);

	void onReconnectFinished(ClientSession session, boolean successfull);

	void onConnectionLost(ClientSession session);

	void onReconnectStopped(ClientSession session);
}
