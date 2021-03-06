package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.IOPCContext;

public class AddressSpaceFileType extends FileType {
	protected BaseMethod exportNamespaceMethod;

	public AddressSpaceFileType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
		super.initializeChildren(context);
		this.exportNamespaceMethod = new BaseMethod(this);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.ServerStatusType;
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.exportNamespaceMethod != null) {
			children.add(this.exportNamespaceMethod);
		}
		children.addAll(super.getChildren());
		return children;
	}
}
