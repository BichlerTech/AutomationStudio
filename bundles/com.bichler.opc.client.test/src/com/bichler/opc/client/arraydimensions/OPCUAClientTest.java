package com.bichler.opc.client.arraydimensions;

import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.EndpointDescription;

public class OPCUAClientTest {

	public static void main(String[] args) {
		
		OPCUAClient_ArrayDimensions test = new OPCUAClient_ArrayDimensions();
		try {
			// Local server
			//EndpointDescription endpoint; = test.discover("opc.tcp://127.0.0.1:4840");
			EndpointDescription endpoint = test.discover("opc.tcp://192.168.110.1:4840");
			test.testArrayDimensions(endpoint);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
	}

}
