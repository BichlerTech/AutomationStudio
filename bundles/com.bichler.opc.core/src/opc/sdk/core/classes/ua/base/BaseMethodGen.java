package opc.sdk.core.classes.ua.base;

import java.util.List;

import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.Variant;

public abstract class BaseMethodGen extends NodeGen {
	public BaseMethodGen() {
		super();
	}

	public abstract ServiceResult onCall(List<Variant> inputArguments, List<Variant> outputArguments);
}
