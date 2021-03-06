package opc.sdk.core.classes.ua.core;

public class ServerRedundancyType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ServerRedundancyType;
	private PropertyType redundancySupport;

	public ServerRedundancyType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getRedundancySupport() {
		return redundancySupport;
	}

	public void setRedundancySupport(PropertyType value) {
		redundancySupport = value;
	}

	@Override
	public String toString() {
		return "ServerRedundancyType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
