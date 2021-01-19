package opc.sdk.ua.classes;

import opc.sdk.ua.IOPCContext;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.AttributeWriteMask;
import org.opcfoundation.ua.core.Identifiers;

public class BaseDataVariableType<T> extends BaseVariableType<T> {
	public BaseDataVariableType(BaseNode parent) {
		super(parent);
		if (parent != null)
			setReferenceTypeId(Identifiers.HasComponent);
	}

	@Override
	protected void initialize(IOPCContext context) {
		setSymbolicName("BaseDataVariableType_Instance1");
		setNodeId(null);
		setBrowseName(new QualifiedName(1, getSymbolicName()));
		setDisplayName(new LocalizedText(getSymbolicName(), ""));
		setDescription(null);
		setWriteMask(AttributeWriteMask.None);
		setUserWriteMask(AttributeWriteMask.None);
		setReferenceTypeId(Identifiers.HasComponent);
		setTypeDefinitionId(getDefaultTypeDefinitionId());
		setValue(null);
		setDataType(getDefaultDataTypeId());
		setValueRank(getDefaultValueRank());
		setArrayDimensions(null);
		setHistorizing(false);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.BaseDataVariableType;
	}
}
