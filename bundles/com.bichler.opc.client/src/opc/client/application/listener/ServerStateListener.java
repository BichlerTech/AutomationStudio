package opc.client.application.listener;

import opc.client.service.ClientSession;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.core.ServerState;

/**
 * Listener to notify serverstate changes.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public interface ServerStateListener {
	void onServerStateChange(ClientSession session, ServerState oldState, ServerState newState);

	void onServerShutdown(ClientSession session, long secondsTillShutdown, LocalizedText shutdownReason);

	void onStatusChange(ClientSession session, ServerState status);
}
