package opc.sdk.core.classes.ua.base;

import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.Variant;

public abstract class BaseVariableTypeGen extends BaseTypeGen {
	public BaseVariableTypeGen() {
		super();
	}

	public void onReadValue(DataValue value, ServiceResult result) {
	}

	public void onWriteValue(DataValue value, ServiceResult result) {
	}

	protected Variant getVariant() {
		if (getAddressSpaceNode() instanceof VariableNode) {
			return ((VariableNode) getAddressSpaceNode()).getValue();
		} else if (getAddressSpaceNode() instanceof VariableTypeNode) {
			return ((VariableTypeNode) getAddressSpaceNode()).getValue();
		}
		return Variant.NULL;
	}

	public abstract Object getValue();
}
