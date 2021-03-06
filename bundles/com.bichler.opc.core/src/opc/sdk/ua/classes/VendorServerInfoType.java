package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

public class VendorServerInfoType extends BaseObjectType {
	public VendorServerInfoType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.VendorServerInfoType;
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		children.addAll(super.getChildren());
		return children;
	}
}
