package opc.sdk.core.classes.ua.core;

public class NonTransparentRedundancyType extends ServerRedundancyType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.NonTransparentRedundancyType;
	private PropertyType serverUriArray;

	public NonTransparentRedundancyType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getServerUriArray() {
		return serverUriArray;
	}

	public void setServerUriArray(PropertyType value) {
		serverUriArray = value;
	}

	@Override
	public String toString() {
		return "NonTransparentRedundancyType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
