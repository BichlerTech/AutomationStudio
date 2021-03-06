package opc.sdk.core.classes.ua.core;

public class TransparentRedundancyType extends ServerRedundancyType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.TransparentRedundancyType;
	private PropertyType currentServerId;
	private PropertyType redundantServerArray;

	public TransparentRedundancyType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getCurrentServerId() {
		return currentServerId;
	}

	public void setCurrentServerId(PropertyType value) {
		currentServerId = value;
	}

	public PropertyType getRedundantServerArray() {
		return redundantServerArray;
	}

	public void setRedundantServerArray(PropertyType value) {
		redundantServerArray = value;
	}

	@Override
	public String toString() {
		return "TransparentRedundancyType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
