package com.bichler.opc.driver.ethernet_ip.transform;

import java.util.HashMap;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.UnsignedShort;

import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPBooleanItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPDIntItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPDPItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPIntItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPLIntItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPRealItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPSIntItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPStringItem;
import com.bichler.opc.driver.ethernet_ip.transform.boolean_.EthernetIPBooleanTransform2Boolean;
import com.bichler.opc.driver.ethernet_ip.transform.boolean_.EthernetIPBooleanTransform2Byte;
import com.bichler.opc.driver.ethernet_ip.transform.boolean_.EthernetIPBooleanTransform2Double;
import com.bichler.opc.driver.ethernet_ip.transform.boolean_.EthernetIPBooleanTransform2Float;
import com.bichler.opc.driver.ethernet_ip.transform.boolean_.EthernetIPBooleanTransform2Integer;
import com.bichler.opc.driver.ethernet_ip.transform.boolean_.EthernetIPBooleanTransform2Long;
import com.bichler.opc.driver.ethernet_ip.transform.boolean_.EthernetIPBooleanTransform2Short;
import com.bichler.opc.driver.ethernet_ip.transform.boolean_.EthernetIPBooleanTransform2UnsignedByte;
import com.bichler.opc.driver.ethernet_ip.transform.boolean_.EthernetIPBooleanTransform2UnsignedInteger;
import com.bichler.opc.driver.ethernet_ip.transform.boolean_.EthernetIPBooleanTransform2UnsignedLong;
import com.bichler.opc.driver.ethernet_ip.transform.boolean_.EthernetIPBooleanTransform2UnsignedShort;
import com.bichler.opc.driver.ethernet_ip.transform.dint.EthernetIPDIntTransform2Boolean;
import com.bichler.opc.driver.ethernet_ip.transform.dint.EthernetIPDIntTransform2Byte;
import com.bichler.opc.driver.ethernet_ip.transform.dint.EthernetIPDIntTransform2Double;
import com.bichler.opc.driver.ethernet_ip.transform.dint.EthernetIPDIntTransform2Float;
import com.bichler.opc.driver.ethernet_ip.transform.dint.EthernetIPDIntTransform2Integer;
import com.bichler.opc.driver.ethernet_ip.transform.dint.EthernetIPDIntTransform2Long;
import com.bichler.opc.driver.ethernet_ip.transform.dint.EthernetIPDIntTransform2Short;
import com.bichler.opc.driver.ethernet_ip.transform.dint.EthernetIPDIntTransform2UnsignedByte;
import com.bichler.opc.driver.ethernet_ip.transform.dint.EthernetIPDIntTransform2UnsignedInteger;
import com.bichler.opc.driver.ethernet_ip.transform.dint.EthernetIPDIntTransform2UnsignedLong;
import com.bichler.opc.driver.ethernet_ip.transform.dint.EthernetIPDIntTransform2UnsignedShort;
import com.bichler.opc.driver.ethernet_ip.transform.int_.EthernetIPIntTransform2Boolean;
import com.bichler.opc.driver.ethernet_ip.transform.int_.EthernetIPIntTransform2Byte;
import com.bichler.opc.driver.ethernet_ip.transform.int_.EthernetIPIntTransform2Double;
import com.bichler.opc.driver.ethernet_ip.transform.int_.EthernetIPIntTransform2Float;
import com.bichler.opc.driver.ethernet_ip.transform.int_.EthernetIPIntTransform2Integer;
import com.bichler.opc.driver.ethernet_ip.transform.int_.EthernetIPIntTransform2Long;
import com.bichler.opc.driver.ethernet_ip.transform.int_.EthernetIPIntTransform2Short;
import com.bichler.opc.driver.ethernet_ip.transform.int_.EthernetIPIntTransform2UnsignedByte;
import com.bichler.opc.driver.ethernet_ip.transform.int_.EthernetIPIntTransform2UnsignedInteger;
import com.bichler.opc.driver.ethernet_ip.transform.int_.EthernetIPIntTransform2UnsignedLong;
import com.bichler.opc.driver.ethernet_ip.transform.int_.EthernetIPIntTransform2UnsignedShort;
import com.bichler.opc.driver.ethernet_ip.transform.lint_.EthernetIPLIntTransform2Boolean;
import com.bichler.opc.driver.ethernet_ip.transform.lint_.EthernetIPLIntTransform2Byte;
import com.bichler.opc.driver.ethernet_ip.transform.lint_.EthernetIPLIntTransform2Double;
import com.bichler.opc.driver.ethernet_ip.transform.lint_.EthernetIPLIntTransform2Float;
import com.bichler.opc.driver.ethernet_ip.transform.lint_.EthernetIPLIntTransform2Integer;
import com.bichler.opc.driver.ethernet_ip.transform.lint_.EthernetIPLIntTransform2Long;
import com.bichler.opc.driver.ethernet_ip.transform.lint_.EthernetIPLIntTransform2Short;
import com.bichler.opc.driver.ethernet_ip.transform.lint_.EthernetIPLIntTransform2UnsignedByte;
import com.bichler.opc.driver.ethernet_ip.transform.lint_.EthernetIPLIntTransform2UnsignedInteger;
import com.bichler.opc.driver.ethernet_ip.transform.lint_.EthernetIPLIntTransform2UnsignedLong;
import com.bichler.opc.driver.ethernet_ip.transform.lint_.EthernetIPLIntTransform2UnsignedShort;
import com.bichler.opc.driver.ethernet_ip.transform.real.EthernetIPRealTransform2Boolean;
import com.bichler.opc.driver.ethernet_ip.transform.real.EthernetIPRealTransform2Byte;
import com.bichler.opc.driver.ethernet_ip.transform.real.EthernetIPRealTransform2Double;
import com.bichler.opc.driver.ethernet_ip.transform.real.EthernetIPRealTransform2Float;
import com.bichler.opc.driver.ethernet_ip.transform.real.EthernetIPRealTransform2Integer;
import com.bichler.opc.driver.ethernet_ip.transform.real.EthernetIPRealTransform2Long;
import com.bichler.opc.driver.ethernet_ip.transform.real.EthernetIPRealTransform2Short;
import com.bichler.opc.driver.ethernet_ip.transform.real.EthernetIPRealTransform2UnsignedByte;
import com.bichler.opc.driver.ethernet_ip.transform.real.EthernetIPRealTransform2UnsignedInteger;
import com.bichler.opc.driver.ethernet_ip.transform.real.EthernetIPRealTransform2UnsignedLong;
import com.bichler.opc.driver.ethernet_ip.transform.real.EthernetIPRealTransform2UnsignedShort;
import com.bichler.opc.driver.ethernet_ip.transform.sint.EthernetIPSIntTransform2Boolean;
import com.bichler.opc.driver.ethernet_ip.transform.sint.EthernetIPSIntTransform2Byte;
import com.bichler.opc.driver.ethernet_ip.transform.sint.EthernetIPSIntTransform2Double;
import com.bichler.opc.driver.ethernet_ip.transform.sint.EthernetIPSIntTransform2Float;
import com.bichler.opc.driver.ethernet_ip.transform.sint.EthernetIPSIntTransform2Integer;
import com.bichler.opc.driver.ethernet_ip.transform.sint.EthernetIPSIntTransform2Long;
import com.bichler.opc.driver.ethernet_ip.transform.sint.EthernetIPSIntTransform2Short;
import com.bichler.opc.driver.ethernet_ip.transform.sint.EthernetIPSIntTransform2UnsignedByte;
import com.bichler.opc.driver.ethernet_ip.transform.sint.EthernetIPSIntTransform2UnsignedInteger;
import com.bichler.opc.driver.ethernet_ip.transform.sint.EthernetIPSIntTransform2UnsignedLong;
import com.bichler.opc.driver.ethernet_ip.transform.sint.EthernetIPSIntTransform2UnsignedShort;
import com.bichler.opc.driver.ethernet_ip.transform.string.EthernetIPStringTransform2Boolean;
import com.bichler.opc.driver.ethernet_ip.transform.string.EthernetIPStringTransform2Byte;
import com.bichler.opc.driver.ethernet_ip.transform.string.EthernetIPStringTransform2Double;
import com.bichler.opc.driver.ethernet_ip.transform.string.EthernetIPStringTransform2Float;
import com.bichler.opc.driver.ethernet_ip.transform.string.EthernetIPStringTransform2Integer;
import com.bichler.opc.driver.ethernet_ip.transform.string.EthernetIPStringTransform2Long;
import com.bichler.opc.driver.ethernet_ip.transform.string.EthernetIPStringTransform2Short;
import com.bichler.opc.driver.ethernet_ip.transform.string.EthernetIPStringTransform2String;
import com.bichler.opc.driver.ethernet_ip.transform.string.EthernetIPStringTransform2UnsignedByte;
import com.bichler.opc.driver.ethernet_ip.transform.string.EthernetIPStringTransform2UnsignedInteger;
import com.bichler.opc.driver.ethernet_ip.transform.string.EthernetIPStringTransform2UnsignedLong;
import com.bichler.opc.driver.ethernet_ip.transform.string.EthernetIPStringTransform2UnsignedShort;

public class EthernetIPTransformFactory {

	public static Map<Class<?>, EthernetIPTransformation> TRANS_BOOLEAN_MAP = new HashMap<Class<?>, EthernetIPTransformation>();
	public static Map<Class<?>, EthernetIPTransformation> TRANS_BYTE_MAP = new HashMap<Class<?>, EthernetIPTransformation>();
	public static Map<Class<?>, EthernetIPTransformation> TRANS_CHAR_MAP = new HashMap<Class<?>, EthernetIPTransformation>();
	public static Map<Class<?>, EthernetIPTransformation> TRANS_COUNTER_MAP = new HashMap<Class<?>, EthernetIPTransformation>();
	public static Map<Class<?>, EthernetIPTransformation> TRANS_DATEANDTIME_MAP = new HashMap<Class<?>, EthernetIPTransformation>();
	public static Map<Class<?>, EthernetIPTransformation> TRANS_DATE_MAP = new HashMap<Class<?>, EthernetIPTransformation>();
	public static Map<Class<?>, EthernetIPTransformation> TRANS_DINT_MAP = new HashMap<Class<?>, EthernetIPTransformation>();
	public static Map<Class<?>, EthernetIPTransformation> TRANS_SINT_MAP = new HashMap<Class<?>, EthernetIPTransformation>();
	public static Map<Class<?>, EthernetIPTransformation> TRANS_INT_MAP = new HashMap<Class<?>, EthernetIPTransformation>();
	public static Map<Class<?>, EthernetIPTransformation> TRANS_LINT_MAP = new HashMap<Class<?>, EthernetIPTransformation>();
	public static Map<Class<?>, EthernetIPTransformation> TRANS_REAL_MAP = new HashMap<Class<?>, EthernetIPTransformation>();
	public static Map<Class<?>, EthernetIPTransformation> TRANS_STRING_MAP = new HashMap<Class<?>, EthernetIPTransformation>();
	public static Map<Class<?>, EthernetIPTransformation> TRANS_TIME_MAP = new HashMap<Class<?>, EthernetIPTransformation>();
	public static Map<Class<?>, EthernetIPTransformation> TRANS_TIMEOFDAY_MAP = new HashMap<Class<?>, EthernetIPTransformation>();
	public static Map<Class<?>, EthernetIPTransformation> TRANS_TIMER_MAP = new HashMap<Class<?>, EthernetIPTransformation>();

	static {
		TRANS_BOOLEAN_MAP.put(Boolean.class, new EthernetIPBooleanTransform2Boolean());
		TRANS_BOOLEAN_MAP.put(Byte.class, new EthernetIPBooleanTransform2Byte());
		TRANS_BOOLEAN_MAP.put(Double.class, new EthernetIPBooleanTransform2Double());
		TRANS_BOOLEAN_MAP.put(Float.class, new EthernetIPBooleanTransform2Float());
		TRANS_BOOLEAN_MAP.put(Integer.class, new EthernetIPBooleanTransform2Integer());
		TRANS_BOOLEAN_MAP.put(Long.class, new EthernetIPBooleanTransform2Long());
		TRANS_BOOLEAN_MAP.put(Short.class, new EthernetIPBooleanTransform2Short());
		TRANS_BOOLEAN_MAP.put(UnsignedByte.class, new EthernetIPBooleanTransform2UnsignedByte());
		TRANS_BOOLEAN_MAP.put(UnsignedLong.class, new EthernetIPBooleanTransform2UnsignedLong());
		TRANS_BOOLEAN_MAP.put(UnsignedShort.class, new EthernetIPBooleanTransform2UnsignedShort());
		TRANS_BOOLEAN_MAP.put(UnsignedInteger.class, new EthernetIPBooleanTransform2UnsignedInteger());
	}

	static {
		TRANS_STRING_MAP.put(Boolean.class, new EthernetIPStringTransform2Boolean());
		TRANS_STRING_MAP.put(Byte.class, new EthernetIPStringTransform2Byte());
		TRANS_STRING_MAP.put(Double.class, new EthernetIPStringTransform2Double());
		TRANS_STRING_MAP.put(Float.class, new EthernetIPStringTransform2Float());
		TRANS_STRING_MAP.put(Integer.class, new EthernetIPStringTransform2Integer());
		TRANS_STRING_MAP.put(Long.class, new EthernetIPStringTransform2Long());
		TRANS_STRING_MAP.put(Short.class, new EthernetIPStringTransform2Short());
		TRANS_STRING_MAP.put(UnsignedByte.class, new EthernetIPStringTransform2UnsignedByte());
		TRANS_STRING_MAP.put(UnsignedLong.class, new EthernetIPStringTransform2UnsignedLong());
		TRANS_STRING_MAP.put(UnsignedShort.class, new EthernetIPStringTransform2UnsignedShort());
		TRANS_STRING_MAP.put(UnsignedInteger.class, new EthernetIPStringTransform2UnsignedInteger());
		TRANS_STRING_MAP.put(String.class, new EthernetIPStringTransform2String());
	}

	static {
		TRANS_DINT_MAP.put(Boolean.class, new EthernetIPDIntTransform2Boolean());
		TRANS_DINT_MAP.put(Byte.class, new EthernetIPDIntTransform2Byte());
		TRANS_DINT_MAP.put(UnsignedByte.class, new EthernetIPDIntTransform2UnsignedByte());
		TRANS_DINT_MAP.put(Short.class, new EthernetIPDIntTransform2Short());
		TRANS_DINT_MAP.put(UnsignedShort.class, new EthernetIPDIntTransform2UnsignedShort());
		TRANS_DINT_MAP.put(Integer.class, new EthernetIPDIntTransform2Integer());
		TRANS_DINT_MAP.put(UnsignedInteger.class, new EthernetIPDIntTransform2UnsignedInteger());
		TRANS_DINT_MAP.put(Long.class, new EthernetIPDIntTransform2Long());
		TRANS_DINT_MAP.put(UnsignedLong.class, new EthernetIPDIntTransform2UnsignedLong());
		TRANS_DINT_MAP.put(Double.class, new EthernetIPDIntTransform2Double());
		TRANS_DINT_MAP.put(Float.class, new EthernetIPDIntTransform2Float());
	}

	static {
		TRANS_LINT_MAP.put(Boolean.class, new EthernetIPLIntTransform2Boolean());
		TRANS_LINT_MAP.put(Byte.class, new EthernetIPLIntTransform2Byte());
		TRANS_LINT_MAP.put(UnsignedByte.class, new EthernetIPLIntTransform2UnsignedByte());
		TRANS_LINT_MAP.put(Short.class, new EthernetIPLIntTransform2Short());
		TRANS_LINT_MAP.put(UnsignedShort.class, new EthernetIPLIntTransform2UnsignedShort());
		TRANS_LINT_MAP.put(Integer.class, new EthernetIPLIntTransform2Integer());
		TRANS_LINT_MAP.put(UnsignedInteger.class, new EthernetIPLIntTransform2UnsignedInteger());
		TRANS_LINT_MAP.put(Long.class, new EthernetIPLIntTransform2Long());
		TRANS_LINT_MAP.put(UnsignedLong.class, new EthernetIPLIntTransform2UnsignedLong());
		TRANS_LINT_MAP.put(Double.class, new EthernetIPLIntTransform2Double());
		TRANS_LINT_MAP.put(Float.class, new EthernetIPLIntTransform2Float());
	}

	static {
		TRANS_SINT_MAP.put(Boolean.class, new EthernetIPSIntTransform2Boolean());
		TRANS_SINT_MAP.put(Byte.class, new EthernetIPSIntTransform2Byte());
		TRANS_SINT_MAP.put(UnsignedByte.class, new EthernetIPSIntTransform2UnsignedByte());
		TRANS_SINT_MAP.put(Short.class, new EthernetIPSIntTransform2Short());
		TRANS_SINT_MAP.put(UnsignedShort.class, new EthernetIPSIntTransform2UnsignedShort());
		TRANS_SINT_MAP.put(Integer.class, new EthernetIPSIntTransform2Integer());
		TRANS_SINT_MAP.put(UnsignedInteger.class, new EthernetIPSIntTransform2UnsignedInteger());
		TRANS_SINT_MAP.put(Long.class, new EthernetIPSIntTransform2Long());
		TRANS_SINT_MAP.put(UnsignedLong.class, new EthernetIPSIntTransform2UnsignedLong());
		TRANS_SINT_MAP.put(Double.class, new EthernetIPSIntTransform2Double());
		TRANS_SINT_MAP.put(Float.class, new EthernetIPSIntTransform2Float());
	}

	static {
		TRANS_INT_MAP.put(Boolean.class, new EthernetIPIntTransform2Boolean());
		TRANS_INT_MAP.put(Byte.class, new EthernetIPIntTransform2Byte());
		TRANS_INT_MAP.put(UnsignedByte.class, new EthernetIPIntTransform2UnsignedByte());
		TRANS_INT_MAP.put(Short.class, new EthernetIPIntTransform2Short());
		TRANS_INT_MAP.put(UnsignedShort.class, new EthernetIPIntTransform2UnsignedShort());
		TRANS_INT_MAP.put(Integer.class, new EthernetIPIntTransform2Integer());
		TRANS_INT_MAP.put(UnsignedInteger.class, new EthernetIPIntTransform2UnsignedInteger());
		TRANS_INT_MAP.put(Long.class, new EthernetIPIntTransform2Long());
		TRANS_INT_MAP.put(UnsignedLong.class, new EthernetIPIntTransform2UnsignedLong());
		TRANS_INT_MAP.put(Double.class, new EthernetIPIntTransform2Double());
		TRANS_INT_MAP.put(Float.class, new EthernetIPIntTransform2Float());
	}

	static {
		TRANS_REAL_MAP.put(Boolean.class, new EthernetIPRealTransform2Boolean());
		TRANS_REAL_MAP.put(Byte.class, new EthernetIPRealTransform2Byte());
		TRANS_REAL_MAP.put(UnsignedByte.class, new EthernetIPRealTransform2UnsignedByte());
		TRANS_REAL_MAP.put(Short.class, new EthernetIPRealTransform2Short());
		TRANS_REAL_MAP.put(UnsignedShort.class, new EthernetIPRealTransform2UnsignedShort());
		TRANS_REAL_MAP.put(Integer.class, new EthernetIPRealTransform2Integer());
		TRANS_REAL_MAP.put(UnsignedInteger.class, new EthernetIPRealTransform2UnsignedInteger());
		TRANS_REAL_MAP.put(Long.class, new EthernetIPRealTransform2Long());
		TRANS_REAL_MAP.put(UnsignedLong.class, new EthernetIPRealTransform2UnsignedLong());
		TRANS_REAL_MAP.put(Double.class, new EthernetIPRealTransform2Double());
		TRANS_REAL_MAP.put(Float.class, new EthernetIPRealTransform2Float());
	}

	public void createTransform(EthernetIPDPItem dp, Class<?> class_) {
		if (dp instanceof EthernetIPBooleanItem) {
			dp.setTransform(TRANS_BOOLEAN_MAP.get(class_));
		} else if (dp instanceof EthernetIPDIntItem) {
			dp.setTransform(TRANS_DINT_MAP.get(class_));
		} else if (dp instanceof EthernetIPSIntItem) {
			dp.setTransform(TRANS_SINT_MAP.get(class_));
		} else if (dp instanceof EthernetIPIntItem) {
			dp.setTransform(TRANS_INT_MAP.get(class_));
		} else if (dp instanceof EthernetIPRealItem) {
			dp.setTransform(TRANS_REAL_MAP.get(class_));
		} else if (dp instanceof EthernetIPStringItem) {
			dp.setTransform(TRANS_STRING_MAP.get(class_));
		} else if (dp instanceof EthernetIPLIntItem) {
			dp.setTransform(TRANS_LINT_MAP.get(class_));
		}
	}
}
