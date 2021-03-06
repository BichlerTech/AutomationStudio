package com.bichler.opc.comdrv;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.CallMethodResult;

import opc.sdk.ua.IOPCOperation;

public interface IMethodListener {
	/**
	 * Calls the required method and returns the result
	 * 
	 * @param context   Operation context of method call.
	 * @param nodeId    NodeId of object to which the method belongs to.
	 * @param methodId  NodeId of the method to call.
	 * @param inputargs List with all required input arguments.
	 */
	CallMethodResult callMethod(IOPCOperation context, NodeId objId, NodeId methodId, Variant[] inputargs);
}
