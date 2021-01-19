package opc.sdk.core.enums;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

/**
 * Flags that can be set for the EventNotifier attribute.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public enum EventNotifiers {
	/**
	 * The Object or View produces no event and has no event history.
	 */
	None((byte) 0x0),
	/**
	 * The Object or View produces event notifications.
	 */
	SubscribeToEvents((byte) 0x1),
	/**
	 * The Object has an event history which may be read.
	 */
	HistoryRead((byte) 0x4),
	/**
	 * The Object has an event history which may be updated.
	 */
	HistoryWrite((byte) 0x8);

	/**
	 * Byte Value
	 */
	private final byte value;

	EventNotifiers(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return this.value;
	}

	public static EnumSet<EventNotifiers> getSet(UnsignedByte mask) {
		return getSet(mask.intValue());
	}

	public static EnumSet<EventNotifiers> getSet(int mask) {
		List<EventNotifiers> res = new ArrayList<EventNotifiers>();
		for (EventNotifiers l : EventNotifiers.values())
			if ((mask & l.value) == l.value)
				res.add(l);
		return EnumSet.copyOf(res);
	}

	public static UnsignedInteger getMask(EventNotifiers... list) {
		int result = 0;
		for (EventNotifiers c : list)
			result |= c.value;
		return UnsignedInteger.getFromBits(result);
	}

	public static UnsignedInteger getMask(Collection<EventNotifiers> list) {
		int result = 0;
		for (EventNotifiers c : list)
			result |= c.value;
		return UnsignedInteger.getFromBits(result);
	}
}
