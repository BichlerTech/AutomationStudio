package com.bichler.astudio.opcua.components.ui;

import opc.client.application.UAClient;

/**
 * Singleton Instance of the Client!
 * 
 * @author Thomas Zöchbauer
 */
public class OPCUAClient {

	private static UAClient s_client = null;

	private OPCUAClient() {
	}

	public static UAClient getInstance() {

		if (OPCUAClient.s_client == null) {
			synchronized (OPCUAClient.class) {
				if (OPCUAClient.s_client == null) {
					UAClient instance = new UAClient();

					OPCUAClient.s_client = instance;
				}
			}
		}

		return OPCUAClient.s_client;
	}
}
