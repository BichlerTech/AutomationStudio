package opc.client.application.runtime.model.node;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.Attributes;

public class AlarmsRuntimeItem extends AbstractMonitorRuntimeItem {
	public AlarmsRuntimeItem() {
		super();
	}

	@Override
	public UnsignedInteger getAttributeId() {
		return Attributes.EventNotifier;
	}
}
