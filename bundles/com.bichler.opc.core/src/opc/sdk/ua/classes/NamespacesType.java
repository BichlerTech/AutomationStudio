package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.IOPCContext;

public class NamespacesType extends BaseObjectType {
	protected AddressSpaceFileType addressSpaceFile;

	public NamespacesType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
		super.initializeChildren(context);
		this.addressSpaceFile = new AddressSpaceFileType(this);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.NamespacesType;
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.addressSpaceFile != null) {
			children.add(this.addressSpaceFile);
		}
		children.addAll(super.getChildren());
		return children;
	}
}
