package opc.client.application.listener;

import opc.client.service.ClientSession;

/**
 * Listener for a client connection. Notifies when a connection is established
 * or a connection is terminated.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 * 
 */
public interface ConnectionListener {
	void onServerConnected(ClientSession session);

	void onServerClose(ClientSession session);
}
