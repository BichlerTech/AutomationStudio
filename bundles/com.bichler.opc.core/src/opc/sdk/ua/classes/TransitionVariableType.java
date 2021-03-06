package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.IOPCContext;

public class TransitionVariableType extends BaseDataVariableType<LocalizedText> {
	protected PropertyVariableType<Object> id;
	protected PropertyVariableType<QualifiedName> name;
	protected PropertyVariableType<UnsignedInteger> number;
	protected PropertyVariableType<DateTime> transitionTime;
	protected PropertyVariableType<DateTime> effectiveTransitionTime;

	public TransitionVariableType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
		super.initializeChildren(context);
		this.id = new PropertyVariableType<>(this);
		this.name = new PropertyVariableType<>(this);
		this.number = new PropertyVariableType<>(this);
		this.transitionTime = new PropertyVariableType<>(this);
		this.effectiveTransitionTime = new PropertyVariableType<>(this);
	}

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
		if (this.transitionTime != null) {
			children.add(this.transitionTime);
		}
		if (this.effectiveTransitionTime != null) {
			children.add(this.effectiveTransitionTime);
		}
		children.addAll(super.getChildren());
		return children;
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.TransitionVariableType;
	}
}
