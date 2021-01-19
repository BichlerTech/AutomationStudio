package opc.sdk.ua.classes;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.AttributeWriteMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;

import opc.sdk.ua.IOPCContext;
import opc.sdk.ua.constants.BrowseNames;

public class OPCObjectType extends BaseType {
	public OPCObjectType(BaseNode parent) {
		super(NodeClass.ObjectType, parent);
	}

	@Override
	protected void initialize(IOPCContext context) {
		setSuperTypeId(Identifiers.BaseObjectType);
		setNodeId(Identifiers.BaseObjectType);
		setBrowseName(new QualifiedName(BrowseNames.BASEOBJECTTYPE));
		setDisplayName(new LocalizedText(BrowseNames.BASEOBJECTTYPE, ""));
		setDescription(null);
		setWriteMask(AttributeWriteMask.None);
		setUserWriteMask(AttributeWriteMask.None);
		setIsAbstract(false);
	}
}
