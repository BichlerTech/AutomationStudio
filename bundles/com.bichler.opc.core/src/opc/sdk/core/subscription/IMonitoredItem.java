package opc.sdk.core.subscription;

import opc.sdk.core.enums.MonitoredItemTypeMask;
import opc.sdk.core.utils.NumericRange;
import opc.sdk.ua.classes.BaseEventType;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

public interface IMonitoredItem {
	public long getMonitoredItemId();

	public MonitoredItemTypeMask getTypeMask();

	public NumericRange getIndexRange();

	public boolean queueValueChange(DataValue value, DataValue lastValue);

	public long getSubscriptionId();

	public UnsignedInteger getClientHandle();

	public UnsignedInteger getAttributeId();

	public boolean queueEvent(BaseEventType state);
}
