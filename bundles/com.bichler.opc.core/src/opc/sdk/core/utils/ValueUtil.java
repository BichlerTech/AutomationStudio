package opc.sdk.core.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.sdk.core.enums.BuiltinType;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.XmlElement;

public class ValueUtil {
	public static Object createBuiltinValue(NodeId dataType, Class<?> reflactory, Object value) {
		if (NodeId.isNull(dataType)) {
			return null;
		}
		if (value == null) {
			return null;
		}
		BuiltinType builtinType = BuiltinType.getType(dataType);
		return createBuiltinValue(builtinType, reflactory, value);
	}

	public static Object createBuiltinValue(BuiltinType builtinType, Class<?> reflactory, Object value) {
		Object reflactoryValue = null;
		try {
			switch (builtinType) {
			case Boolean:
			case Byte:
			case Double:
			case Float:
			case Int16:
			case Int32:
			case Int64:
			case Integer:
			case Number:
			case UInt16:
			case UInt32:
			case UInt64:
			case UInteger:
			case SByte:
			case String:
				reflactoryValue = reflactory.getConstructor(value.getClass()).newInstance(value);
				return reflactoryValue;
			case ByteString:
			case DataValue:
			case DateTime:
				// backwards converting from toString()
				if (value instanceof String) {
					return OPCDateFormat.parseDateTime(value.toString());
				}
				if (value instanceof Calendar) {
					return new DateTime((Calendar) value);
				}
				if (value instanceof Long) {
					return new DateTime((Long) value);
				}
				break;
			case DiagnosticInfo:
			case Enumeration:
			case ExpandedNodeId:
			case ExtensionObject:
			case Guid:
				if (value instanceof String) {
					try {
						return UUID.fromString((String) value);
					} catch (Exception e) {
						return null;
					}
				}
				break;
			case LocalizedText:
			case NodeId:
			case Null:
			case QualifiedName:
			case Variant:
				break;
			case StatusCode:
				return new StatusCode((UnsignedInteger) value);
			case XmlElement:
				return new XmlElement((String) value);
			}
		} catch (IllegalArgumentException | SecurityException | InstantiationException | IllegalAccessException
				| InvocationTargetException | NoSuchMethodException e) {
			Logger.getLogger(ValueUtil.class.getName()).log(Level.SEVERE, null, e);
		}
		return reflactoryValue;
	}
}
