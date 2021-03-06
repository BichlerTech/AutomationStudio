package opc.sdk.core.utils;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

public class DefaultSubscriptionSettings {
	public static final UnsignedInteger maxNotificationsPerPublsih = new UnsignedInteger(1000);
	public static final UnsignedByte priority = new UnsignedByte(0);
	public static final Boolean publishingEnabled = true;
	public static final UnsignedInteger lifetimeCount = new UnsignedInteger(100);
	public static final UnsignedInteger maxKeepaliveCount = new UnsignedInteger(5);
	public static final Double publishingInterval = 1000d;
}
