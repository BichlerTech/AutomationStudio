package opc.sdk.core.classes.ua.core;

public class FolderType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.FolderType;

	public FolderType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "FolderType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
