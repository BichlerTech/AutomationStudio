package com.bichler.opc.driver.xml_da.transform;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.UnsignedShort;

import com.bichler.opc.driver.xml_da.dp.XML_DA_BooleanItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_ByteItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_DPItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_DoubleItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_FloatItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_IntegerItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_LongItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_ShortItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_StringItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_UnsignedByteItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_UnsignedIntItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_UnsignedLongItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_UnsignedShortItem;
import com.bichler.opc.driver.xml_da.transform.boolean_.XML_DA_TransformBoolean2Boolean;
import com.bichler.opc.driver.xml_da.transform.boolean_.XML_DA_TransformBoolean2Byte;
import com.bichler.opc.driver.xml_da.transform.boolean_.XML_DA_TransformBoolean2Double;
import com.bichler.opc.driver.xml_da.transform.boolean_.XML_DA_TransformBoolean2Float;
import com.bichler.opc.driver.xml_da.transform.boolean_.XML_DA_TransformBoolean2Integer;
import com.bichler.opc.driver.xml_da.transform.boolean_.XML_DA_TransformBoolean2Long;
import com.bichler.opc.driver.xml_da.transform.boolean_.XML_DA_TransformBoolean2Short;
import com.bichler.opc.driver.xml_da.transform.boolean_.XML_DA_TransformBoolean2UnsignedByte;
import com.bichler.opc.driver.xml_da.transform.boolean_.XML_DA_TransformBoolean2UnsignedInteger;
import com.bichler.opc.driver.xml_da.transform.boolean_.XML_DA_TransformBoolean2UnsignedLong;
import com.bichler.opc.driver.xml_da.transform.boolean_.XML_DA_TransformBoolean2UnsignedShort;
import com.bichler.opc.driver.xml_da.transform.byte_.XML_DA_TransformByte2Boolean;
import com.bichler.opc.driver.xml_da.transform.byte_.XML_DA_TransformByte2Byte;
import com.bichler.opc.driver.xml_da.transform.byte_.XML_DA_TransformByte2Double;
import com.bichler.opc.driver.xml_da.transform.byte_.XML_DA_TransformByte2Float;
import com.bichler.opc.driver.xml_da.transform.byte_.XML_DA_TransformByte2Integer;
import com.bichler.opc.driver.xml_da.transform.byte_.XML_DA_TransformByte2Long;
import com.bichler.opc.driver.xml_da.transform.byte_.XML_DA_TransformByte2Short;
import com.bichler.opc.driver.xml_da.transform.byte_.XML_DA_TransformByte2UnsignedByte;
import com.bichler.opc.driver.xml_da.transform.byte_.XML_DA_TransformByte2UnsignedInteger;
import com.bichler.opc.driver.xml_da.transform.byte_.XML_DA_TransformByte2UnsignedLong;
import com.bichler.opc.driver.xml_da.transform.byte_.XML_DA_TransformByte2UnsignedShort;
import com.bichler.opc.driver.xml_da.transform.double_.XML_DA_TransformDouble2Boolean;
import com.bichler.opc.driver.xml_da.transform.double_.XML_DA_TransformDouble2Byte;
import com.bichler.opc.driver.xml_da.transform.double_.XML_DA_TransformDouble2Double;
import com.bichler.opc.driver.xml_da.transform.double_.XML_DA_TransformDouble2Float;
import com.bichler.opc.driver.xml_da.transform.double_.XML_DA_TransformDouble2Integer;
import com.bichler.opc.driver.xml_da.transform.double_.XML_DA_TransformDouble2Long;
import com.bichler.opc.driver.xml_da.transform.double_.XML_DA_TransformDouble2Short;
import com.bichler.opc.driver.xml_da.transform.double_.XML_DA_TransformDouble2UnsignedByte;
import com.bichler.opc.driver.xml_da.transform.double_.XML_DA_TransformDouble2UnsignedInteger;
import com.bichler.opc.driver.xml_da.transform.double_.XML_DA_TransformDouble2UnsignedLong;
import com.bichler.opc.driver.xml_da.transform.double_.XML_DA_TransformDouble2UnsignedShort;
import com.bichler.opc.driver.xml_da.transform.float_.XML_DA_TransformFloat2Boolean;
import com.bichler.opc.driver.xml_da.transform.float_.XML_DA_TransformFloat2Byte;
import com.bichler.opc.driver.xml_da.transform.float_.XML_DA_TransformFloat2Double;
import com.bichler.opc.driver.xml_da.transform.float_.XML_DA_TransformFloat2Float;
import com.bichler.opc.driver.xml_da.transform.float_.XML_DA_TransformFloat2Integer;
import com.bichler.opc.driver.xml_da.transform.float_.XML_DA_TransformFloat2Long;
import com.bichler.opc.driver.xml_da.transform.float_.XML_DA_TransformFloat2Short;
import com.bichler.opc.driver.xml_da.transform.float_.XML_DA_TransformFloat2UnsignedByte;
import com.bichler.opc.driver.xml_da.transform.float_.XML_DA_TransformFloat2UnsignedInteger;
import com.bichler.opc.driver.xml_da.transform.float_.XML_DA_TransformFloat2UnsignedLong;
import com.bichler.opc.driver.xml_da.transform.float_.XML_DA_TransformFloat2UnsignedShort;
import com.bichler.opc.driver.xml_da.transform.int_.XML_DA_TransformInteger2Boolean;
import com.bichler.opc.driver.xml_da.transform.int_.XML_DA_TransformInteger2Byte;
import com.bichler.opc.driver.xml_da.transform.int_.XML_DA_TransformInteger2Double;
import com.bichler.opc.driver.xml_da.transform.int_.XML_DA_TransformInteger2Float;
import com.bichler.opc.driver.xml_da.transform.int_.XML_DA_TransformInteger2Integer;
import com.bichler.opc.driver.xml_da.transform.int_.XML_DA_TransformInteger2Long;
import com.bichler.opc.driver.xml_da.transform.int_.XML_DA_TransformInteger2Short;
import com.bichler.opc.driver.xml_da.transform.int_.XML_DA_TransformInteger2UnsignedByte;
import com.bichler.opc.driver.xml_da.transform.int_.XML_DA_TransformInteger2UnsignedInteger;
import com.bichler.opc.driver.xml_da.transform.int_.XML_DA_TransformInteger2UnsignedLong;
import com.bichler.opc.driver.xml_da.transform.int_.XML_DA_TransformInteger2UnsignedShort;
import com.bichler.opc.driver.xml_da.transform.long_.XML_DA_TransformLong2Boolean;
import com.bichler.opc.driver.xml_da.transform.long_.XML_DA_TransformLong2Byte;
import com.bichler.opc.driver.xml_da.transform.long_.XML_DA_TransformLong2Double;
import com.bichler.opc.driver.xml_da.transform.long_.XML_DA_TransformLong2Float;
import com.bichler.opc.driver.xml_da.transform.long_.XML_DA_TransformLong2Integer;
import com.bichler.opc.driver.xml_da.transform.long_.XML_DA_TransformLong2Long;
import com.bichler.opc.driver.xml_da.transform.long_.XML_DA_TransformLong2Short;
import com.bichler.opc.driver.xml_da.transform.long_.XML_DA_TransformLong2UnsignedByte;
import com.bichler.opc.driver.xml_da.transform.long_.XML_DA_TransformLong2UnsignedInteger;
import com.bichler.opc.driver.xml_da.transform.long_.XML_DA_TransformLong2UnsignedLong;
import com.bichler.opc.driver.xml_da.transform.long_.XML_DA_TransformLong2UnsignedShort;
import com.bichler.opc.driver.xml_da.transform.short_.XML_DA_TransformShort2Boolean;
import com.bichler.opc.driver.xml_da.transform.short_.XML_DA_TransformShort2Byte;
import com.bichler.opc.driver.xml_da.transform.short_.XML_DA_TransformShort2Double;
import com.bichler.opc.driver.xml_da.transform.short_.XML_DA_TransformShort2Float;
import com.bichler.opc.driver.xml_da.transform.short_.XML_DA_TransformShort2Integer;
import com.bichler.opc.driver.xml_da.transform.short_.XML_DA_TransformShort2Long;
import com.bichler.opc.driver.xml_da.transform.short_.XML_DA_TransformShort2Short;
import com.bichler.opc.driver.xml_da.transform.short_.XML_DA_TransformShort2UnsignedByte;
import com.bichler.opc.driver.xml_da.transform.short_.XML_DA_TransformShort2UnsignedInteger;
import com.bichler.opc.driver.xml_da.transform.short_.XML_DA_TransformShort2UnsignedLong;
import com.bichler.opc.driver.xml_da.transform.short_.XML_DA_TransformShort2UnsignedShort;
import com.bichler.opc.driver.xml_da.transform.string_.XML_DA_TransformString2String;
import com.bichler.opc.driver.xml_da.transform.unsignedbyte.XML_DA_TransformUnsignedByte2Boolean;
import com.bichler.opc.driver.xml_da.transform.unsignedbyte.XML_DA_TransformUnsignedByte2Byte;
import com.bichler.opc.driver.xml_da.transform.unsignedbyte.XML_DA_TransformUnsignedByte2Double;
import com.bichler.opc.driver.xml_da.transform.unsignedbyte.XML_DA_TransformUnsignedByte2Float;
import com.bichler.opc.driver.xml_da.transform.unsignedbyte.XML_DA_TransformUnsignedByte2Integer;
import com.bichler.opc.driver.xml_da.transform.unsignedbyte.XML_DA_TransformUnsignedByte2Long;
import com.bichler.opc.driver.xml_da.transform.unsignedbyte.XML_DA_TransformUnsignedByte2Short;
import com.bichler.opc.driver.xml_da.transform.unsignedbyte.XML_DA_TransformUnsignedByte2UnsignedByte;
import com.bichler.opc.driver.xml_da.transform.unsignedbyte.XML_DA_TransformUnsignedByte2UnsignedInteger;
import com.bichler.opc.driver.xml_da.transform.unsignedbyte.XML_DA_TransformUnsignedByte2UnsignedLong;
import com.bichler.opc.driver.xml_da.transform.unsignedbyte.XML_DA_TransformUnsignedByte2UnsignedShort;
import com.bichler.opc.driver.xml_da.transform.unsignedint.XML_DA_TransformUnsignedInt2Boolean;
import com.bichler.opc.driver.xml_da.transform.unsignedint.XML_DA_TransformUnsignedInt2Byte;
import com.bichler.opc.driver.xml_da.transform.unsignedint.XML_DA_TransformUnsignedInt2Double;
import com.bichler.opc.driver.xml_da.transform.unsignedint.XML_DA_TransformUnsignedInt2Float;
import com.bichler.opc.driver.xml_da.transform.unsignedint.XML_DA_TransformUnsignedInt2Integer;
import com.bichler.opc.driver.xml_da.transform.unsignedint.XML_DA_TransformUnsignedInt2Long;
import com.bichler.opc.driver.xml_da.transform.unsignedint.XML_DA_TransformUnsignedInt2Short;
import com.bichler.opc.driver.xml_da.transform.unsignedint.XML_DA_TransformUnsignedInt2UnsignedByte;
import com.bichler.opc.driver.xml_da.transform.unsignedint.XML_DA_TransformUnsignedInt2UnsignedInteger;
import com.bichler.opc.driver.xml_da.transform.unsignedint.XML_DA_TransformUnsignedInt2UnsignedLong;
import com.bichler.opc.driver.xml_da.transform.unsignedint.XML_DA_TransformUnsignedInt2UnsignedShort;
import com.bichler.opc.driver.xml_da.transform.unsignedlong.XML_DA_TransformUnsignedLong2Boolean;
import com.bichler.opc.driver.xml_da.transform.unsignedlong.XML_DA_TransformUnsignedLong2Byte;
import com.bichler.opc.driver.xml_da.transform.unsignedlong.XML_DA_TransformUnsignedLong2Double;
import com.bichler.opc.driver.xml_da.transform.unsignedlong.XML_DA_TransformUnsignedLong2Float;
import com.bichler.opc.driver.xml_da.transform.unsignedlong.XML_DA_TransformUnsignedLong2Integer;
import com.bichler.opc.driver.xml_da.transform.unsignedlong.XML_DA_TransformUnsignedLong2Long;
import com.bichler.opc.driver.xml_da.transform.unsignedlong.XML_DA_TransformUnsignedLong2Short;
import com.bichler.opc.driver.xml_da.transform.unsignedlong.XML_DA_TransformUnsignedLong2UnsignedByte;
import com.bichler.opc.driver.xml_da.transform.unsignedlong.XML_DA_TransformUnsignedLong2UnsignedInteger;
import com.bichler.opc.driver.xml_da.transform.unsignedlong.XML_DA_TransformUnsignedLong2UnsignedLong;
import com.bichler.opc.driver.xml_da.transform.unsignedlong.XML_DA_TransformUnsignedLong2UnsignedShort;
import com.bichler.opc.driver.xml_da.transform.unsignedshort.XML_DA_TransformUnsignedShort2Boolean;
import com.bichler.opc.driver.xml_da.transform.unsignedshort.XML_DA_TransformUnsignedShort2Byte;
import com.bichler.opc.driver.xml_da.transform.unsignedshort.XML_DA_TransformUnsignedShort2Double;
import com.bichler.opc.driver.xml_da.transform.unsignedshort.XML_DA_TransformUnsignedShort2Float;
import com.bichler.opc.driver.xml_da.transform.unsignedshort.XML_DA_TransformUnsignedShort2Integer;
import com.bichler.opc.driver.xml_da.transform.unsignedshort.XML_DA_TransformUnsignedShort2Long;
import com.bichler.opc.driver.xml_da.transform.unsignedshort.XML_DA_TransformUnsignedShort2Short;
import com.bichler.opc.driver.xml_da.transform.unsignedshort.XML_DA_TransformUnsignedShort2UnsignedByte;
import com.bichler.opc.driver.xml_da.transform.unsignedshort.XML_DA_TransformUnsignedShort2UnsignedInteger;
import com.bichler.opc.driver.xml_da.transform.unsignedshort.XML_DA_TransformUnsignedShort2UnsignedLong;
import com.bichler.opc.driver.xml_da.transform.unsignedshort.XML_DA_TransformUnsignedShort2UnsignedShort;

public class XML_DA_TransformFactory {
	public static Map<Class<?>, XML_DA_Transformation> TRANS_BOOLEAN_MAP = new HashMap<Class<?>, XML_DA_Transformation>();
	public static Map<Class<?>, XML_DA_Transformation> TRANS_BYTE_MAP = new HashMap<Class<?>, XML_DA_Transformation>();
	public static Map<Class<?>, XML_DA_Transformation> TRANS_SHORT_MAP = new HashMap<Class<?>, XML_DA_Transformation>();
	public static Map<Class<?>, XML_DA_Transformation> TRANS_INT_MAP = new HashMap<Class<?>, XML_DA_Transformation>();
	public static Map<Class<?>, XML_DA_Transformation> TRANS_LONG_MAP = new HashMap<Class<?>, XML_DA_Transformation>();
	public static Map<Class<?>, XML_DA_Transformation> TRANS_FLOAT_MAP = new HashMap<Class<?>, XML_DA_Transformation>();
	public static Map<Class<?>, XML_DA_Transformation> TRANS_DOUBLE_MAP = new HashMap<Class<?>, XML_DA_Transformation>();
	public static Map<Class<?>, XML_DA_Transformation> TRANS_UNSIGNEDBYTE_MAP = new HashMap<Class<?>, XML_DA_Transformation>();
	public static Map<Class<?>, XML_DA_Transformation> TRANS_UNSIGNEDSHORT_MAP = new HashMap<Class<?>, XML_DA_Transformation>();
	public static Map<Class<?>, XML_DA_Transformation> TRANS_UNSIGNEDINT_MAP = new HashMap<Class<?>, XML_DA_Transformation>();
	public static Map<Class<?>, XML_DA_Transformation> TRANS_UNSIGNEDLONG_MAP = new HashMap<Class<?>, XML_DA_Transformation>();
	public static Map<Class<?>, XML_DA_Transformation> TRANS_STRING_MAP = new HashMap<Class<?>, XML_DA_Transformation>();
	static {
		TRANS_STRING_MAP.put(String.class, new XML_DA_TransformString2String());
	}
	static {
		TRANS_BOOLEAN_MAP.put(Boolean.class, new XML_DA_TransformBoolean2Boolean());
		TRANS_BOOLEAN_MAP.put(Byte.class, new XML_DA_TransformBoolean2Byte());
		TRANS_BOOLEAN_MAP.put(UnsignedByte.class, new XML_DA_TransformBoolean2UnsignedByte());
		TRANS_BOOLEAN_MAP.put(Short.class, new XML_DA_TransformBoolean2Short());
		TRANS_BOOLEAN_MAP.put(UnsignedShort.class, new XML_DA_TransformBoolean2UnsignedShort());
		TRANS_BOOLEAN_MAP.put(Integer.class, new XML_DA_TransformBoolean2Integer());
		TRANS_BOOLEAN_MAP.put(UnsignedInteger.class, new XML_DA_TransformBoolean2UnsignedInteger());
		TRANS_BOOLEAN_MAP.put(Long.class, new XML_DA_TransformBoolean2Long());
		TRANS_BOOLEAN_MAP.put(UnsignedLong.class, new XML_DA_TransformBoolean2UnsignedLong());
		TRANS_BOOLEAN_MAP.put(Double.class, new XML_DA_TransformBoolean2Double());
		TRANS_BOOLEAN_MAP.put(Float.class, new XML_DA_TransformBoolean2Float());
	}
	static {
		TRANS_BYTE_MAP.put(Boolean.class, new XML_DA_TransformByte2Boolean());
		TRANS_BYTE_MAP.put(Byte.class, new XML_DA_TransformByte2Byte());
		TRANS_BYTE_MAP.put(UnsignedByte.class, new XML_DA_TransformByte2UnsignedByte());
		TRANS_BYTE_MAP.put(Short.class, new XML_DA_TransformByte2Short());
		TRANS_BYTE_MAP.put(UnsignedShort.class, new XML_DA_TransformByte2UnsignedShort());
		TRANS_BYTE_MAP.put(Integer.class, new XML_DA_TransformByte2Integer());
		TRANS_BYTE_MAP.put(UnsignedInteger.class, new XML_DA_TransformByte2UnsignedInteger());
		TRANS_BYTE_MAP.put(Long.class, new XML_DA_TransformByte2Long());
		TRANS_BYTE_MAP.put(UnsignedLong.class, new XML_DA_TransformByte2UnsignedLong());
		TRANS_BYTE_MAP.put(Double.class, new XML_DA_TransformByte2Double());
		TRANS_BYTE_MAP.put(Float.class, new XML_DA_TransformByte2Float());
	}
	static {
		TRANS_SHORT_MAP.put(Boolean.class, new XML_DA_TransformShort2Boolean());
		TRANS_SHORT_MAP.put(Byte.class, new XML_DA_TransformShort2Byte());
		TRANS_SHORT_MAP.put(UnsignedByte.class, new XML_DA_TransformShort2UnsignedByte());
		TRANS_SHORT_MAP.put(Short.class, new XML_DA_TransformShort2Short());
		TRANS_SHORT_MAP.put(UnsignedShort.class, new XML_DA_TransformShort2UnsignedShort());
		TRANS_SHORT_MAP.put(Integer.class, new XML_DA_TransformShort2Integer());
		TRANS_SHORT_MAP.put(UnsignedInteger.class, new XML_DA_TransformShort2UnsignedInteger());
		TRANS_SHORT_MAP.put(Long.class, new XML_DA_TransformShort2Long());
		TRANS_SHORT_MAP.put(UnsignedLong.class, new XML_DA_TransformShort2UnsignedLong());
		TRANS_SHORT_MAP.put(Double.class, new XML_DA_TransformShort2Double());
		TRANS_SHORT_MAP.put(Float.class, new XML_DA_TransformShort2Float());
	}
	static {
		TRANS_INT_MAP.put(Boolean.class, new XML_DA_TransformInteger2Boolean());
		TRANS_INT_MAP.put(Byte.class, new XML_DA_TransformInteger2Byte());
		TRANS_INT_MAP.put(UnsignedByte.class, new XML_DA_TransformInteger2UnsignedByte());
		TRANS_INT_MAP.put(Short.class, new XML_DA_TransformInteger2Short());
		TRANS_INT_MAP.put(UnsignedShort.class, new XML_DA_TransformInteger2UnsignedShort());
		TRANS_INT_MAP.put(Integer.class, new XML_DA_TransformInteger2Integer());
		TRANS_INT_MAP.put(UnsignedInteger.class, new XML_DA_TransformInteger2UnsignedInteger());
		TRANS_INT_MAP.put(Long.class, new XML_DA_TransformInteger2Long());
		TRANS_INT_MAP.put(UnsignedLong.class, new XML_DA_TransformInteger2UnsignedLong());
		TRANS_INT_MAP.put(Double.class, new XML_DA_TransformInteger2Double());
		TRANS_INT_MAP.put(Float.class, new XML_DA_TransformInteger2Float());
	}
	static {
		TRANS_LONG_MAP.put(Boolean.class, new XML_DA_TransformLong2Boolean());
		TRANS_LONG_MAP.put(Byte.class, new XML_DA_TransformLong2Byte());
		TRANS_LONG_MAP.put(UnsignedByte.class, new XML_DA_TransformLong2UnsignedByte());
		TRANS_LONG_MAP.put(Short.class, new XML_DA_TransformLong2Short());
		TRANS_LONG_MAP.put(UnsignedShort.class, new XML_DA_TransformLong2UnsignedShort());
		TRANS_LONG_MAP.put(Integer.class, new XML_DA_TransformLong2Integer());
		TRANS_LONG_MAP.put(UnsignedInteger.class, new XML_DA_TransformLong2UnsignedInteger());
		TRANS_LONG_MAP.put(Long.class, new XML_DA_TransformLong2Long());
		TRANS_LONG_MAP.put(UnsignedLong.class, new XML_DA_TransformLong2UnsignedLong());
		TRANS_LONG_MAP.put(Double.class, new XML_DA_TransformLong2Double());
		TRANS_LONG_MAP.put(Float.class, new XML_DA_TransformLong2Float());
	}
	static {
		TRANS_FLOAT_MAP.put(Boolean.class, new XML_DA_TransformFloat2Boolean());
		TRANS_FLOAT_MAP.put(Byte.class, new XML_DA_TransformFloat2Byte());
		TRANS_FLOAT_MAP.put(UnsignedByte.class, new XML_DA_TransformFloat2UnsignedByte());
		TRANS_FLOAT_MAP.put(Short.class, new XML_DA_TransformFloat2Short());
		TRANS_FLOAT_MAP.put(UnsignedShort.class, new XML_DA_TransformFloat2UnsignedShort());
		TRANS_FLOAT_MAP.put(Integer.class, new XML_DA_TransformFloat2Integer());
		TRANS_FLOAT_MAP.put(UnsignedInteger.class, new XML_DA_TransformFloat2UnsignedInteger());
		TRANS_FLOAT_MAP.put(Long.class, new XML_DA_TransformFloat2Long());
		TRANS_FLOAT_MAP.put(UnsignedLong.class, new XML_DA_TransformFloat2UnsignedLong());
		TRANS_FLOAT_MAP.put(Double.class, new XML_DA_TransformFloat2Double());
		TRANS_FLOAT_MAP.put(Float.class, new XML_DA_TransformFloat2Float());
	}
	static {
		TRANS_DOUBLE_MAP.put(Boolean.class, new XML_DA_TransformDouble2Boolean());
		TRANS_DOUBLE_MAP.put(Byte.class, new XML_DA_TransformDouble2Byte());
		TRANS_DOUBLE_MAP.put(UnsignedByte.class, new XML_DA_TransformDouble2UnsignedByte());
		TRANS_DOUBLE_MAP.put(Short.class, new XML_DA_TransformDouble2Short());
		TRANS_DOUBLE_MAP.put(UnsignedShort.class, new XML_DA_TransformDouble2UnsignedShort());
		TRANS_DOUBLE_MAP.put(Integer.class, new XML_DA_TransformDouble2Integer());
		TRANS_DOUBLE_MAP.put(UnsignedInteger.class, new XML_DA_TransformDouble2UnsignedInteger());
		TRANS_DOUBLE_MAP.put(Long.class, new XML_DA_TransformDouble2Long());
		TRANS_DOUBLE_MAP.put(UnsignedLong.class, new XML_DA_TransformDouble2UnsignedLong());
		TRANS_DOUBLE_MAP.put(Double.class, new XML_DA_TransformDouble2Double());
		TRANS_DOUBLE_MAP.put(Float.class, new XML_DA_TransformDouble2Float());
	}
	static {
		TRANS_UNSIGNEDBYTE_MAP.put(Boolean.class, new XML_DA_TransformUnsignedByte2Boolean());
		TRANS_UNSIGNEDBYTE_MAP.put(Byte.class, new XML_DA_TransformUnsignedByte2Byte());
		TRANS_UNSIGNEDBYTE_MAP.put(UnsignedByte.class, new XML_DA_TransformUnsignedByte2UnsignedByte());
		TRANS_UNSIGNEDBYTE_MAP.put(Short.class, new XML_DA_TransformUnsignedByte2Short());
		TRANS_UNSIGNEDBYTE_MAP.put(UnsignedShort.class, new XML_DA_TransformUnsignedByte2UnsignedShort());
		TRANS_UNSIGNEDBYTE_MAP.put(Integer.class, new XML_DA_TransformUnsignedByte2Integer());
		TRANS_UNSIGNEDBYTE_MAP.put(UnsignedInteger.class, new XML_DA_TransformUnsignedByte2UnsignedInteger());
		TRANS_UNSIGNEDBYTE_MAP.put(Long.class, new XML_DA_TransformUnsignedByte2Long());
		TRANS_UNSIGNEDBYTE_MAP.put(UnsignedLong.class, new XML_DA_TransformUnsignedByte2UnsignedLong());
		TRANS_UNSIGNEDBYTE_MAP.put(Double.class, new XML_DA_TransformUnsignedByte2Double());
		TRANS_UNSIGNEDBYTE_MAP.put(Float.class, new XML_DA_TransformUnsignedByte2Float());
	}
	static {
		TRANS_UNSIGNEDSHORT_MAP.put(Boolean.class, new XML_DA_TransformUnsignedShort2Boolean());
		TRANS_UNSIGNEDSHORT_MAP.put(Byte.class, new XML_DA_TransformUnsignedShort2Byte());
		TRANS_UNSIGNEDSHORT_MAP.put(UnsignedByte.class, new XML_DA_TransformUnsignedShort2UnsignedByte());
		TRANS_UNSIGNEDSHORT_MAP.put(Short.class, new XML_DA_TransformUnsignedShort2Short());
		TRANS_UNSIGNEDSHORT_MAP.put(UnsignedShort.class, new XML_DA_TransformUnsignedShort2UnsignedShort());
		TRANS_UNSIGNEDSHORT_MAP.put(Integer.class, new XML_DA_TransformUnsignedShort2Integer());
		TRANS_UNSIGNEDSHORT_MAP.put(UnsignedInteger.class, new XML_DA_TransformUnsignedShort2UnsignedInteger());
		TRANS_UNSIGNEDSHORT_MAP.put(Long.class, new XML_DA_TransformUnsignedShort2Long());
		TRANS_UNSIGNEDSHORT_MAP.put(UnsignedLong.class, new XML_DA_TransformUnsignedShort2UnsignedLong());
		TRANS_UNSIGNEDSHORT_MAP.put(Double.class, new XML_DA_TransformUnsignedShort2Double());
		TRANS_UNSIGNEDSHORT_MAP.put(Float.class, new XML_DA_TransformUnsignedShort2Float());
	}
	static {
		TRANS_UNSIGNEDINT_MAP.put(Boolean.class, new XML_DA_TransformUnsignedInt2Boolean());
		TRANS_UNSIGNEDINT_MAP.put(Byte.class, new XML_DA_TransformUnsignedInt2Byte());
		TRANS_UNSIGNEDINT_MAP.put(UnsignedByte.class, new XML_DA_TransformUnsignedInt2UnsignedByte());
		TRANS_UNSIGNEDINT_MAP.put(Short.class, new XML_DA_TransformUnsignedInt2Short());
		TRANS_UNSIGNEDINT_MAP.put(UnsignedShort.class, new XML_DA_TransformUnsignedInt2UnsignedShort());
		TRANS_UNSIGNEDINT_MAP.put(Integer.class, new XML_DA_TransformUnsignedInt2Integer());
		TRANS_UNSIGNEDINT_MAP.put(UnsignedInteger.class, new XML_DA_TransformUnsignedInt2UnsignedInteger());
		TRANS_UNSIGNEDINT_MAP.put(Long.class, new XML_DA_TransformUnsignedInt2Long());
		TRANS_UNSIGNEDINT_MAP.put(UnsignedLong.class, new XML_DA_TransformUnsignedInt2UnsignedLong());
		TRANS_UNSIGNEDINT_MAP.put(Double.class, new XML_DA_TransformUnsignedInt2Double());
		TRANS_UNSIGNEDINT_MAP.put(Float.class, new XML_DA_TransformUnsignedInt2Float());
	}
	static {
		TRANS_UNSIGNEDLONG_MAP.put(Boolean.class, new XML_DA_TransformUnsignedLong2Boolean());
		TRANS_UNSIGNEDLONG_MAP.put(Byte.class, new XML_DA_TransformUnsignedLong2Byte());
		TRANS_UNSIGNEDLONG_MAP.put(UnsignedByte.class, new XML_DA_TransformUnsignedLong2UnsignedByte());
		TRANS_UNSIGNEDLONG_MAP.put(Short.class, new XML_DA_TransformUnsignedLong2Short());
		TRANS_UNSIGNEDLONG_MAP.put(UnsignedShort.class, new XML_DA_TransformUnsignedLong2UnsignedShort());
		TRANS_UNSIGNEDLONG_MAP.put(Integer.class, new XML_DA_TransformUnsignedLong2Integer());
		TRANS_UNSIGNEDLONG_MAP.put(UnsignedInteger.class, new XML_DA_TransformUnsignedLong2UnsignedInteger());
		TRANS_UNSIGNEDLONG_MAP.put(Long.class, new XML_DA_TransformUnsignedLong2Long());
		TRANS_UNSIGNEDLONG_MAP.put(UnsignedLong.class, new XML_DA_TransformUnsignedLong2UnsignedLong());
		TRANS_UNSIGNEDLONG_MAP.put(Double.class, new XML_DA_TransformUnsignedLong2Double());
		TRANS_UNSIGNEDLONG_MAP.put(Float.class, new XML_DA_TransformUnsignedLong2Float());
	}

	public static void createTransform(XML_DA_DPItem dp, Class<?> class_) {
		if (dp instanceof XML_DA_BooleanItem) {
			dp.setTransform(TRANS_BOOLEAN_MAP.get(class_));
		} else if (dp instanceof XML_DA_ByteItem) {
			dp.setTransform(TRANS_BYTE_MAP.get(class_));
		} else if (dp instanceof XML_DA_ShortItem) {
			dp.setTransform(TRANS_SHORT_MAP.get(class_));
		} else if (dp instanceof XML_DA_IntegerItem) {
			dp.setTransform(TRANS_INT_MAP.get(class_));
		} else if (dp instanceof XML_DA_LongItem) {
			dp.setTransform(TRANS_LONG_MAP.get(class_));
		} else if (dp instanceof XML_DA_FloatItem) {
			dp.setTransform(TRANS_FLOAT_MAP.get(class_));
		} else if (dp instanceof XML_DA_DoubleItem) {
			dp.setTransform(TRANS_DOUBLE_MAP.get(class_));
		} else if (dp instanceof XML_DA_UnsignedByteItem) {
			dp.setTransform(TRANS_UNSIGNEDBYTE_MAP.get(class_));
		} else if (dp instanceof XML_DA_UnsignedShortItem) {
			dp.setTransform(TRANS_UNSIGNEDSHORT_MAP.get(class_));
		} else if (dp instanceof XML_DA_UnsignedIntItem) {
			dp.setTransform(TRANS_UNSIGNEDINT_MAP.get(class_));
		} else if (dp instanceof XML_DA_UnsignedLongItem) {
			dp.setTransform(TRANS_UNSIGNEDLONG_MAP.get(class_));
		} else if (dp instanceof XML_DA_StringItem) {
			dp.setTransform(TRANS_STRING_MAP.get(class_));
		}
		if (dp.getTransform() == null) {
			Logger.getLogger(class_.getName())
					.log(Level.SEVERE, "Couldn't find Transformation for dp: " + dp.getDisplayname()
							+ " datapointtype: "
							+ dp.getClass().getSimpleName()/*
															 * , "Comet communication driver module",
															 * CometModuls.INT_DRV, ComDRV.BUNDLEID, ComDRV.VERSIONID
															 */);
		} else {
			Logger.getLogger(class_.getName())
					.info("Create transformation: " + dp.getTransform().getClass().getSimpleName() + " for datapoint: "
							+ dp.getDisplayname() + " dp-Type: "
							+ dp.getClass().getSimpleName()/*
															 * , "Comet communication driver module" ,
															 * CometModuls.INT_DRV, ComDRV.BUNDLEID, ComDRV.VERSIONID
															 */);
		}
	}
}
