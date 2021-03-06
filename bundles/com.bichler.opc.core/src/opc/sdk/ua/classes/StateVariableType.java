package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.ua.constants.BrowseNames;
import opc.sdk.ua.constants.NodeStateChangeMasks;

/**
 * MISSING InitializeoptionalChildren
 * 
 * @author thomas
 * 
 */
public class StateVariableType extends BaseDataVariableType<LocalizedText> {
	protected PropertyVariableType<?> id = null;
	protected PropertyVariableType<QualifiedName> name = null;
	protected PropertyVariableType<UnsignedInteger> number = null;
	protected PropertyVariableType<LocalizedText> effectiveDisplayName = null;

	public StateVariableType(BaseNode parent) {
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
		return Identifiers.StateVariableType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opc.sdk.ua.classes.BaseVariableState#getDefaultDataTypeId(org.
	 * opcfoundation .ua.common.NamespaceTable)
	 */
	@Override
	protected NodeId getDefaultDataTypeId() {
		return Identifiers.LocalizedText;
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

	private PropertyVariableType<?> creatID(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace) {
			if (replacement == null) {
				setId(new PropertyVariableType(this));
			} else {
				setId((PropertyVariableType<?>) replacement);
			}
		}
		return this.id;
	}

	private PropertyVariableType<QualifiedName> creatName(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace) {
			if (replacement == null) {
				setName(new PropertyVariableType<QualifiedName>(this));
			} else {
				setName((PropertyVariableType<QualifiedName>) replacement);
			}
		}
		return this.name;
	}

	private PropertyVariableType<UnsignedInteger> creatNumber(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace) {
			if (replacement == null) {
				setNumber(new PropertyVariableType<UnsignedInteger>(this));
			} else {
				setNumber((PropertyVariableType<UnsignedInteger>) replacement);
			}
		}
		return this.number;
	}

	private PropertyVariableType<LocalizedText> creatEffectiveDisplayName(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace) {
			if (replacement == null) {
				setEffectiveDisplayName(new PropertyVariableType<LocalizedText>(this));
			} else {
				setEffectiveDisplayName((PropertyVariableType<LocalizedText>) replacement);
			}
		}
		return this.effectiveDisplayName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opc.sdk.ua.classes.NodeState#findChild(opc.sdk.core.context.
	 * ISystemContext , org.opcfoundation.ua.builtintypes.QualifiedName, boolean,
	 * opc.sdk.ua.classes.BaseInstanceState)
	 */
	@Override
	protected BaseInstance findChild(QualifiedName browseName, boolean createOrReplace, BaseInstance replacement) {
		if (QualifiedName.isNull(browseName)) {
			return null;
		}
		BaseInstance instance = null;
		switch (browseName.getName()) {
		case BrowseNames.ID:
			instance = creatID(createOrReplace, replacement);
			break;
		case BrowseNames.NAME:
			instance = creatName(createOrReplace, replacement);
			break;
		case BrowseNames.NUMBER:
			instance = creatNumber(createOrReplace, replacement);
			break;
		case BrowseNames.EFFECTIVEDISPLAYNAME:
			instance = creatEffectiveDisplayName(createOrReplace, replacement);
			break;
		default:
			break;
		}
		if (instance != null) {
			return instance;
		}
		return super.findChild(browseName, createOrReplace, replacement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opc.sdk.ua.classes.NodeState#getChildren()
	 */
	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.id != null) {
			children.add(this.id);
		}
		if (this.name != null) {
			children.add(this.name);
		}
		if (this.number != null) {
			children.add(this.number);
		}
		if (this.effectiveDisplayName != null) {
			children.add(this.effectiveDisplayName);
		}
		children.addAll(super.getChildren());
		return children;
	}

	/**
	 * @return the effectiveDisplayName
	 */
	public PropertyVariableType<LocalizedText> getEffectiveDisplayName() {
		return effectiveDisplayName;
	}

	/**
	 * @return the id
	 */
	public PropertyVariableType<?> getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public PropertyVariableType<QualifiedName> getName() {
		return name;
	}

	/**
	 * @return the number
	 */
	public PropertyVariableType<UnsignedInteger> getNumber() {
		return number;
	}

	/**
	 * @param effectiveDisplayName the effectiveDisplayName to set
	 */
	public void setEffectiveDisplayName(PropertyVariableType<LocalizedText> effectiveDisplayName) {
		if (this.effectiveDisplayName != effectiveDisplayName) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.effectiveDisplayName = effectiveDisplayName;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(PropertyVariableType<?> id) {
		if (this.id != id) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.id = id;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(PropertyVariableType<QualifiedName> name) {
		if (this.name != name) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.name = name;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(PropertyVariableType<UnsignedInteger> number) {
		if (this.number != number) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.number = number;
	}
}
