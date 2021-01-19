package com.bichler.opc.driver.base.handler;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.CallMethodResult;
import org.opcfoundation.ua.core.StatusCodes;

import com.bichler.opc.comdrv.IMethodListener;
import com.bichler.opc.driver.base.context.DriverContext;

import opc.sdk.ua.IOPCContext;
import opc.sdk.ua.IOPCOperation;
import opc.sdk.ua.classes.BaseMethod;
import opc.sdk.ua.classes.BaseNode;

public class BaseMethodHandler extends AbstractBaseHandler implements IMethodListener {
	public BaseMethodHandler() {
		super();
	}

	@Override
	public CallMethodResult callMethod(IOPCOperation context, NodeId objId, NodeId methodId, Variant[] inputargs) {
		BaseNode method = getManager().getNode(methodId);
		// skip not valid methods
		if (!(method instanceof BaseMethod)) {
			CallMethodResult result = new CallMethodResult();
			result.setStatusCode(new StatusCode(StatusCodes.Bad_MethodInvalid));
			return result;
		}
		// output parameters
		List<Variant> outputArguments = new ArrayList<>();
		List<StatusCode> inputArgumentResults = new ArrayList<>();
		// call method
		IOPCContext drivercontext = new DriverContext(getDriverId(), context.getSession());
		ServiceResult status = ((BaseMethod) method).call(drivercontext, inputargs, inputArgumentResults,
				outputArguments);
		// CallMethod result
		CallMethodResult result = new CallMethodResult();
		result.setStatusCode(status.getCode());
		result.setOutputArguments(outputArguments.toArray(new Variant[0]));
		result.setInputArgumentResults(inputArgumentResults.toArray(new StatusCode[0]));
		return result;
	}
	// file methods (ByteString = byte[])
	// # Open
	// [in] Byte mode [out] UInt32 fileHandle
	// # Close
	// [in] UInt32 fileHandle
	// # Read
	// [in] Uint32 fileHandle [in] Int32 length [out] ByteString data
	// # Write
	// [in] UInt32 fileHandle [in] ByteString data
	// # GetPosition
	// [in] UInt32 fileHandle [out] UInt64 position
	// # SetPosition
	// [in] UInt32 fileHandle [in] UInt64 position
}
