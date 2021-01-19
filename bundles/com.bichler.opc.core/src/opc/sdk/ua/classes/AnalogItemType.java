package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.EUInformation;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.Range;

import opc.sdk.ua.IOPCContext;

public class AnalogItemType<T> extends DataItemType<T> {
	protected PropertyVariableType<Range> instrumentRange;
	protected PropertyVariableType<Range> eURange;
	protected PropertyVariableType<EUInformation> engineeringUnits;

	public AnalogItemType(BaseNode parent) {
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
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<BaseInstance>();
		if (this.instrumentRange != null) {
			children.add(this.instrumentRange);
		}
		if (this.eURange != null) {
			children.add(this.eURange);
		}
		if (this.engineeringUnits != null) {
			children.add(this.engineeringUnits);
		}
		children.addAll(super.getChildren());
		return children;
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.AnalogItemType;
	}
}
