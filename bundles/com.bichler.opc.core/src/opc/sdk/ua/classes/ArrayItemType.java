package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.AxisScaleEnumeration;
import org.opcfoundation.ua.core.EUInformation;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.Range;

import opc.sdk.ua.IOPCContext;

public class ArrayItemType<T> extends DataItemType<T> {
	protected PropertyVariableType<Range> instrumentRange;
	protected PropertyVariableType<Range> eURange;
	protected PropertyVariableType<EUInformation> engineeringUnits;
	protected PropertyVariableType<LocalizedText> title;
	protected PropertyVariableType<AxisScaleEnumeration> axisScaleType;

	public ArrayItemType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected void initialize(IOPCContext context) {
		super.initialize(context);
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
		super.initializeChildren(context);
		this.instrumentRange = new PropertyVariableType<>(this);
		this.eURange = new PropertyVariableType<>(this);
		this.engineeringUnits = new PropertyVariableType<>(this);
		this.title = new PropertyVariableType<>(this);
		this.axisScaleType = new PropertyVariableType<>(this);
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<BaseInstance>();
		if (this.instrumentRange != null) {
			this.instrumentRange = new PropertyVariableType<>(this);
		}
		if (this.eURange != null) {
			this.eURange = new PropertyVariableType<>(this);
		}
		if (this.engineeringUnits != null) {
			this.engineeringUnits = new PropertyVariableType<>(this);
		}
		if (this.title != null) {
			this.title = new PropertyVariableType<>(this);
		}
		if (this.axisScaleType != null) {
			this.axisScaleType = new PropertyVariableType<>(this);
		}
		children.addAll(super.getChildren());
		return children;
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.ArrayItemType;
	}
}
