package opc.sdk.ua.classes;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.AttributeWriteMask;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.ua.IOPCContext;
import opc.sdk.ua.constants.BrowseNames;

public class OPCBaseDataVariableType extends OPCVariableType {
	/**
	 * Initializes the type with its defalt attribute values.
	 */
	public OPCBaseDataVariableType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected void initialize(IOPCContext context) {
		setSuperTypeId(Identifiers.BaseVariableType);
		setNodeId(Identifiers.BaseDataVariableType);
		setBrowseName(new QualifiedName(BrowseNames.BASEDATAVARIABLETYPE));
		setDescription(new LocalizedText(BrowseNames.BASEDATAVARIABLETYPE, ""));
		setDescription(null);
		setWriteMask(AttributeWriteMask.None);
		setUserWriteMask(AttributeWriteMask.UserWriteMask);
		setIsAbstract(false);
		setValue(null);
		setDataType(Identifiers.BaseDataType);
		setValueRank(ValueRanks.Any.getValue());
		setArrayDimensions(null);
	}
}
