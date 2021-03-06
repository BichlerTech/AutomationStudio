package opc.client.application.listener;

import org.opcfoundation.ua.builtintypes.ServiceResult;

/**
 * A listener that is called when a client has lost its connection to a server.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 * 
 */
public interface ConnectionLostListener {
	public void onConnectionLost(ServiceResult result);
}
