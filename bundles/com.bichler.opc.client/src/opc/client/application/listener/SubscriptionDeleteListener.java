package opc.client.application.listener;

import opc.client.service.ClientSession;
import opc.client.service.Subscription;

/**
 * 
 * @author Thomas Z&ouml;chauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public interface SubscriptionDeleteListener {
	void onDeleted(ClientSession session, Subscription subscription);
}
