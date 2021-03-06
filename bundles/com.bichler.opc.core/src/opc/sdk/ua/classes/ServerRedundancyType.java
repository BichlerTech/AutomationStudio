package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.RedundancySupport;

import opc.sdk.ua.IOPCContext;

public class ServerRedundancyType extends BaseObjectType {
	protected PropertyVariableType<RedundancySupport> redundancySupport;

	public ServerRedundancyType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
		super.initializeChildren(context);
		this.redundancySupport = new PropertyVariableType<>(this);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.ServerRedundancyType;
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.redundancySupport != null) {
			children.add(this.redundancySupport);
		}
		children.addAll(super.getChildren());
		return children;
	}
}
