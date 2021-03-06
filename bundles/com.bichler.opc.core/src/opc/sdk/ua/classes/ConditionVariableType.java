package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.ua.constants.BrowseNames;
import opc.sdk.ua.constants.NodeStateChangeMasks;

public class ConditionVariableType<T> extends BaseDataVariableType<T> {
	private PropertyVariableType<DateTime> sourceTimestamp = null;

	public ConditionVariableType(BaseNode parent) {
		super(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opc.sdk.ua.classes.BaseDataVariableState#getDefaultTypeDefinitionId(org
	 * .opcfoundation.ua.common.NamespaceTable)
	 */
	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.ConditionVariableType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opc.sdk.ua.classes.BaseVariableState#getDefaultDataTypeId(org.opcfoundation
	 * .ua.common.NamespaceTable)
	 */
	@Override
	protected NodeId getDefaultDataTypeId() {
		return Identifiers.BaseDataType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opc.sdk.ua.classes.BaseVariableState#getDefaultValueRank()
	 */
	@Override
	protected int getDefaultValueRank() {
		return ValueRanks.Scalar.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opc.sdk.ua.classes.BaseDataVariableState#findChild(opc.sdk.core.context
	 * .ISystemContext, org.opcfoundation.ua.builtintypes.QualifiedName, boolean,
	 * opc.sdk.ua.classes.BaseInstanceState)
	 */
	@Override
	protected BaseInstance findChild(QualifiedName browseName, boolean createOrReplace, BaseInstance replacement) {
		if (QualifiedName.isNull(browseName)) {
			return null;
		}
		BaseInstance instance = null;
		// BrowseNames name = BrowseNames.valueOf(browseName.getName());
		if (browseName.getName().equalsIgnoreCase(BrowseNames.SOURCETIMESTAMP)) {
			if (createOrReplace && this.sourceTimestamp == null) {
				if (replacement == null) {
					setSourceTimestamp(new PropertyVariableType<DateTime>(this));
				} else {
					setSourceTimestamp((PropertyVariableType<DateTime>) replacement);
				}
			}
			instance = this.sourceTimestamp;
		}
		if (instance != null) {
			return instance;
		}
		return super.findChild(browseName, createOrReplace, replacement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opc.sdk.ua.classes.BaseDataVariableState#getChildren()
	 */
	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.sourceTimestamp != null) {
			children.add(this.sourceTimestamp);
		}
		children.addAll(super.getChildren());
		return children;
	}

	/**
	 * @return the sourceTimestamp
	 */
	public PropertyVariableType<DateTime> getSourceTimestamp() {
		return sourceTimestamp;
	}

	/**
	 * @param sourceTimestamp the sourceTimestamp to set
	 */
	public void setSourceTimestamp(PropertyVariableType<DateTime> sourceTimestamp) {
		if (this.sourceTimestamp != sourceTimestamp) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.sourceTimestamp = sourceTimestamp;
	}
}
