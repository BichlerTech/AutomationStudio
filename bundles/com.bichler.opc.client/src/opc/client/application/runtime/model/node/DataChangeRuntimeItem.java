package opc.client.application.runtime.model.node;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.Attributes;

public class DataChangeRuntimeItem extends AbstractMonitorRuntimeItem {
	public DataChangeRuntimeItem() {
		super();
	}

	@Override
	public UnsignedInteger getAttributeId() {
		return Attributes.Value;
	}
}
