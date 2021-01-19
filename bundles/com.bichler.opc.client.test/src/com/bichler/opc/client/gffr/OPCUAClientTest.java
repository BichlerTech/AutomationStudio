package com.bichler.opc.client.gffr;

import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.EndpointDescription;

public class OPCUAClientTest {

	public static void main(String[] args) {
		
		OPCUAClient_GetFileForRead test = new OPCUAClient_GetFileForRead();
		try {
			// Local server
			//EndpointDescription endpoint; = test.discover("opc.tcp://127.0.0.1:4840");
			EndpointDescription endpoint = test.discover("opc.tcp://192.168.110.1:4840");
			test.testFileRead(endpoint);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
	}

}
