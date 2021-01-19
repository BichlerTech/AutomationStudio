package opc.client.application.runtime.model.node;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.Attributes;

public class EventsRuntimeItem extends AbstractMonitorRuntimeItem {
	public EventsRuntimeItem() {
		super();
	}

	@Override
	public UnsignedInteger getAttributeId() {
		return Attributes.EventNotifier;
	}
}
