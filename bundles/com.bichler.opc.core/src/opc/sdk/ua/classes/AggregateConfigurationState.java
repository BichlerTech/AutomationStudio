package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.IOPCContext;

public class AggregateConfigurationState extends BaseObjectType {
	protected PropertyVariableType<Boolean> treatUncertainAsBad;
	protected PropertyVariableType<Byte> percentDataBad;
	protected PropertyVariableType<Byte> percentDataGood;
	protected PropertyVariableType<Boolean> useSlopedExtrapolation;

	public AggregateConfigurationState(BaseNode parent) {
		super(parent);
	}

	@Override
	protected void initialize(IOPCContext context) {
		super.initialize(context);
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
		super.initializeChildren(context);
		this.treatUncertainAsBad = new PropertyVariableType<>(this);
		this.percentDataBad = new PropertyVariableType<>(this);
		this.percentDataGood = new PropertyVariableType<>(this);
		this.useSlopedExtrapolation = new PropertyVariableType<>(this);
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<BaseInstance>();
		if (this.treatUncertainAsBad != null) {
			children.add(this.treatUncertainAsBad);
		}
		if (this.percentDataBad != null) {
			children.add(this.percentDataBad);
		}
		if (this.percentDataGood != null) {
			children.add(this.percentDataGood);
		}
		if (this.useSlopedExtrapolation != null) {
			children.add(this.useSlopedExtrapolation);
		}
		children.addAll(super.getChildren());
		return children;
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.AggregateConfiguration;
	}
}
