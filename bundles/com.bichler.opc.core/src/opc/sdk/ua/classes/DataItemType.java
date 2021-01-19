package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import opc.sdk.ua.IOPCContext;

public class DataItemType<T> extends BaseDataVariableType<T> {
	protected PropertyVariableType<String> definition;
	protected PropertyVariableType<Double> valuePrecision;

	public DataItemType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
		super.initializeChildren(context);
		this.definition = new PropertyVariableType<>(this);
		this.valuePrecision = new PropertyVariableType<>(this);
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.definition != null) {
			children.add(this.definition);
		}
		if (this.valuePrecision != null) {
			children.add(this.valuePrecision);
		}
		children.addAll(super.getChildren());
		return children;
	}
}
