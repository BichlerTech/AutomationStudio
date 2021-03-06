package opc.sdk.ua.classes;

import java.util.Locale;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.AttributeWriteMask;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.IOPCContext;
import opc.sdk.ua.constants.BrowseNames;

public class PropertyVariableType<T> extends BaseVariableType<T> {
	public PropertyVariableType(BaseNode parent) {
		super(parent);
	}

	/**
	 * Initializes the instance with the default values.
	 */
	@Override
	protected void initialize(IOPCContext context) {
		setSymbolicName(BrowseNames.PROPERTYTYPE + "_Instance");
		setBrowseName(new QualifiedName(getSymbolicName()));
		setDisplayName(new LocalizedText(getSymbolicName(), Locale.getDefault()));
		setDescription(LocalizedText.EMPTY);
		setWriteMask(AttributeWriteMask.None);
		setUserWriteMask(AttributeWriteMask.None);
		setReferenceTypeId(Identifiers.HasProperty);
		setTypeDefinitionId(getDefaultTypeDefinitionId());
		setValue(null);
		setDataType(getDefaultDataTypeId());
		setValueRank(getDefaultValueRank());
		setArrayDimensions(null);
		setAccessLevels((byte) AccessLevel.CurrentRead.getValue());
		setUserAccessLevels((byte) AccessLevel.CurrentRead.getValue());
		setMinimumSamplingInterval(0);
		setHistorizing(false);
	}

	public void update(IOPCContext context) {
		context.syncValueFromDriver(getNodeId(), new Variant(getValue()), context.getDriverId());
	}

	/**
	 * Get the id of the default type definition node for the instance.
	 */
	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.PropertyType;
	}
}
