package opc.sdk.ua;

import java.util.List;

import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;

public interface ICallHandler {
	ServiceResult call(IOPCContext context, Variant[] inputArguments, List<StatusCode> inputArgumentResults,
			List<Variant> outputArguments);
}
