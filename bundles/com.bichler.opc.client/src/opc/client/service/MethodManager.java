package opc.client.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.common.ServiceResultException;

import opc.client.application.core.method.MethodElement;
import opc.sdk.core.enums.RequestType;

/**
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class MethodManager {
	public static final String ID = "opc.client.service.MethodManager";

	public void validate(ClientSession session, MethodElement[] elements2call) {
		int i = 0;
		for (MethodElement method : elements2call) {
			try {
				method.validate(session);
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						RequestType.Call.name() + " Could not validate the call for method: "
								+ method.getMethodId().toString() + " at " + i + ", " + e.getAdditionalTextField(),
						e);
			}
			i++;
		}
	}
}
