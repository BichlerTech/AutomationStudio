package com.bichler.opc.driver.siemens.transform;

import java.util.HashMap;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.UnsignedShort;

import com.bichler.opc.driver.siemens.dp.SiemensBooleanItem;
import com.bichler.opc.driver.siemens.dp.SiemensByteItem;
import com.bichler.opc.driver.siemens.dp.SiemensCharItem;
import com.bichler.opc.driver.siemens.dp.SiemensCounterItem;
import com.bichler.opc.driver.siemens.dp.SiemensDIntItem;
import com.bichler.opc.driver.siemens.dp.SiemensDPItem;
import com.bichler.opc.driver.siemens.dp.SiemensDWordItem;
import com.bichler.opc.driver.siemens.dp.SiemensDateAndTimeItem;
import com.bichler.opc.driver.siemens.dp.SiemensDateItem;
import com.bichler.opc.driver.siemens.dp.SiemensIntItem;
import com.bichler.opc.driver.siemens.dp.SiemensLRealItem;
import com.bichler.opc.driver.siemens.dp.SiemensRealItem;
import com.bichler.opc.driver.siemens.dp.SiemensStringItem;
import com.bichler.opc.driver.siemens.dp.SiemensTimeItem;
import com.bichler.opc.driver.siemens.dp.SiemensTimeOfDayItem;
import com.bichler.opc.driver.siemens.dp.SiemensTimerItem;
import com.bichler.opc.driver.siemens.dp.SiemensWordItem;
import com.bichler.opc.driver.siemens.transform.boolean_.SiemensBooleanTransform2Boolean;
import com.bichler.opc.driver.siemens.transform.boolean_.SiemensBooleanTransform2Byte;
import com.bichler.opc.driver.siemens.transform.boolean_.SiemensBooleanTransform2Double;
import com.bichler.opc.driver.siemens.transform.boolean_.SiemensBooleanTransform2Float;
import com.bichler.opc.driver.siemens.transform.boolean_.SiemensBooleanTransform2Integer;
import com.bichler.opc.driver.siemens.transform.boolean_.SiemensBooleanTransform2Long;
import com.bichler.opc.driver.siemens.transform.boolean_.SiemensBooleanTransform2Short;
import com.bichler.opc.driver.siemens.transform.boolean_.SiemensBooleanTransform2UnsignedByte;
import com.bichler.opc.driver.siemens.transform.boolean_.SiemensBooleanTransform2UnsignedInteger;
import com.bichler.opc.driver.siemens.transform.boolean_.SiemensBooleanTransform2UnsignedLong;
import com.bichler.opc.driver.siemens.transform.boolean_.SiemensBooleanTransform2UnsignedShort;
import com.bichler.opc.driver.siemens.transform.byte_.SiemensByteTransform2Boolean;
import com.bichler.opc.driver.siemens.transform.byte_.SiemensByteTransform2Byte;
import com.bichler.opc.driver.siemens.transform.byte_.SiemensByteTransform2Double;
import com.bichler.opc.driver.siemens.transform.byte_.SiemensByteTransform2Float;
import com.bichler.opc.driver.siemens.transform.byte_.SiemensByteTransform2Integer;
import com.bichler.opc.driver.siemens.transform.byte_.SiemensByteTransform2Long;
import com.bichler.opc.driver.siemens.transform.byte_.SiemensByteTransform2Short;
import com.bichler.opc.driver.siemens.transform.byte_.SiemensByteTransform2UnsignedByte;
import com.bichler.opc.driver.siemens.transform.byte_.SiemensByteTransform2UnsignedInteger;
import com.bichler.opc.driver.siemens.transform.byte_.SiemensByteTransform2UnsignedLong;
import com.bichler.opc.driver.siemens.transform.byte_.SiemensByteTransform2UnsignedShort;
import com.bichler.opc.driver.siemens.transform.char_.SiemensCharTransform2Boolean;
import com.bichler.opc.driver.siemens.transform.char_.SiemensCharTransform2Byte;
import com.bichler.opc.driver.siemens.transform.char_.SiemensCharTransform2Double;
import com.bichler.opc.driver.siemens.transform.char_.SiemensCharTransform2Float;
import com.bichler.opc.driver.siemens.transform.char_.SiemensCharTransform2Integer;
import com.bichler.opc.driver.siemens.transform.char_.SiemensCharTransform2Long;
import com.bichler.opc.driver.siemens.transform.char_.SiemensCharTransform2Short;
import com.bichler.opc.driver.siemens.transform.char_.SiemensCharTransform2UnsignedByte;
import com.bichler.opc.driver.siemens.transform.char_.SiemensCharTransform2UnsignedInteger;
import com.bichler.opc.driver.siemens.transform.char_.SiemensCharTransform2UnsignedLong;
import com.bichler.opc.driver.siemens.transform.char_.SiemensCharTransform2UnsignedShort;
import com.bichler.opc.driver.siemens.transform.counter.SiemensCounterTransform2Boolean;
import com.bichler.opc.driver.siemens.transform.counter.SiemensCounterTransform2Byte;
import com.bichler.opc.driver.siemens.transform.counter.SiemensCounterTransform2Double;
import com.bichler.opc.driver.siemens.transform.counter.SiemensCounterTransform2Float;
import com.bichler.opc.driver.siemens.transform.counter.SiemensCounterTransform2Integer;
import com.bichler.opc.driver.siemens.transform.counter.SiemensCounterTransform2Long;
import com.bichler.opc.driver.siemens.transform.counter.SiemensCounterTransform2Short;
import com.bichler.opc.driver.siemens.transform.counter.SiemensCounterTransform2UnsignedByte;
import com.bichler.opc.driver.siemens.transform.counter.SiemensCounterTransform2UnsignedInteger;
import com.bichler.opc.driver.siemens.transform.counter.SiemensCounterTransform2UnsignedLong;
import com.bichler.opc.driver.siemens.transform.counter.SiemensCounterTransform2UnsignedShort;
import com.bichler.opc.driver.siemens.transform.date.SiemensDateTransform2Boolean;
import com.bichler.opc.driver.siemens.transform.date.SiemensDateTransform2Byte;
import com.bichler.opc.driver.siemens.transform.date.SiemensDateTransform2DateTime;
import com.bichler.opc.driver.siemens.transform.date.SiemensDateTransform2Double;
import com.bichler.opc.driver.siemens.transform.date.SiemensDateTransform2Float;
import com.bichler.opc.driver.siemens.transform.date.SiemensDateTransform2Integer;
import com.bichler.opc.driver.siemens.transform.date.SiemensDateTransform2Long;
import com.bichler.opc.driver.siemens.transform.date.SiemensDateTransform2Short;
import com.bichler.opc.driver.siemens.transform.date.SiemensDateTransform2String;
import com.bichler.opc.driver.siemens.transform.date.SiemensDateTransform2UnsignedByte;
import com.bichler.opc.driver.siemens.transform.date.SiemensDateTransform2UnsignedInteger;
import com.bichler.opc.driver.siemens.transform.date.SiemensDateTransform2UnsignedLong;
import com.bichler.opc.driver.siemens.transform.date.SiemensDateTransform2UnsignedShort;
import com.bichler.opc.driver.siemens.transform.dateandtime.SiemensDateAndTimeTransform2Boolean;
import com.bichler.opc.driver.siemens.transform.dateandtime.SiemensDateAndTimeTransform2Byte;
import com.bichler.opc.driver.siemens.transform.dateandtime.SiemensDateAndTimeTransform2DateTime;
import com.bichler.opc.driver.siemens.transform.dateandtime.SiemensDateAndTimeTransform2Double;
import com.bichler.opc.driver.siemens.transform.dateandtime.SiemensDateAndTimeTransform2Float;
import com.bichler.opc.driver.siemens.transform.dateandtime.SiemensDateAndTimeTransform2Integer;
import com.bichler.opc.driver.siemens.transform.dateandtime.SiemensDateAndTimeTransform2Long;
import com.bichler.opc.driver.siemens.transform.dateandtime.SiemensDateAndTimeTransform2Short;
import com.bichler.opc.driver.siemens.transform.dateandtime.SiemensDateAndTimeTransform2UnsignedByte;
import com.bichler.opc.driver.siemens.transform.dateandtime.SiemensDateAndTimeTransform2UnsignedInteger;
import com.bichler.opc.driver.siemens.transform.dateandtime.SiemensDateAndTimeTransform2UnsignedLong;
import com.bichler.opc.driver.siemens.transform.dateandtime.SiemensDateAndTimeTransform2UnsignedShort;
import com.bichler.opc.driver.siemens.transform.dint.SiemensDIntTransform2Boolean;
import com.bichler.opc.driver.siemens.transform.dint.SiemensDIntTransform2Byte;
import com.bichler.opc.driver.siemens.transform.dint.SiemensDIntTransform2Double;
import com.bichler.opc.driver.siemens.transform.dint.SiemensDIntTransform2Float;
import com.bichler.opc.driver.siemens.transform.dint.SiemensDIntTransform2Integer;
import com.bichler.opc.driver.siemens.transform.dint.SiemensDIntTransform2Long;
import com.bichler.opc.driver.siemens.transform.dint.SiemensDIntTransform2Short;
import com.bichler.opc.driver.siemens.transform.dint.SiemensDIntTransform2UnsignedByte;
import com.bichler.opc.driver.siemens.transform.dint.SiemensDIntTransform2UnsignedInteger;
import com.bichler.opc.driver.siemens.transform.dint.SiemensDIntTransform2UnsignedLong;
import com.bichler.opc.driver.siemens.transform.dint.SiemensDIntTransform2UnsignedShort;
import com.bichler.opc.driver.siemens.transform.dword.SiemensDWordTransform2Boolean;
import com.bichler.opc.driver.siemens.transform.dword.SiemensDWordTransform2Byte;
import com.bichler.opc.driver.siemens.transform.dword.SiemensDWordTransform2Double;
import com.bichler.opc.driver.siemens.transform.dword.SiemensDWordTransform2Float;
import com.bichler.opc.driver.siemens.transform.dword.SiemensDWordTransform2Integer;
import com.bichler.opc.driver.siemens.transform.dword.SiemensDWordTransform2Long;
import com.bichler.opc.driver.siemens.transform.dword.SiemensDWordTransform2Short;
import com.bichler.opc.driver.siemens.transform.dword.SiemensDWordTransform2UnsignedByte;
import com.bichler.opc.driver.siemens.transform.dword.SiemensDWordTransform2UnsignedInteger;
import com.bichler.opc.driver.siemens.transform.dword.SiemensDWordTransform2UnsignedLong;
import com.bichler.opc.driver.siemens.transform.dword.SiemensDWordTransform2UnsignedShort;
import com.bichler.opc.driver.siemens.transform.int_.SiemensIntTransform2Boolean;
import com.bichler.opc.driver.siemens.transform.int_.SiemensIntTransform2Byte;
import com.bichler.opc.driver.siemens.transform.int_.SiemensIntTransform2Double;
import com.bichler.opc.driver.siemens.transform.int_.SiemensIntTransform2Float;
import com.bichler.opc.driver.siemens.transform.int_.SiemensIntTransform2Integer;
import com.bichler.opc.driver.siemens.transform.int_.SiemensIntTransform2Long;
import com.bichler.opc.driver.siemens.transform.int_.SiemensIntTransform2Short;
import com.bichler.opc.driver.siemens.transform.int_.SiemensIntTransform2UnsignedByte;
import com.bichler.opc.driver.siemens.transform.int_.SiemensIntTransform2UnsignedInteger;
import com.bichler.opc.driver.siemens.transform.int_.SiemensIntTransform2UnsignedLong;
import com.bichler.opc.driver.siemens.transform.int_.SiemensIntTransform2UnsignedShort;
import com.bichler.opc.driver.siemens.transform.lreal.SiemensLRealTransform2Boolean;
import com.bichler.opc.driver.siemens.transform.lreal.SiemensLRealTransform2Byte;
import com.bichler.opc.driver.siemens.transform.lreal.SiemensLRealTransform2Double;
import com.bichler.opc.driver.siemens.transform.lreal.SiemensLRealTransform2Float;
import com.bichler.opc.driver.siemens.transform.lreal.SiemensLRealTransform2Integer;
import com.bichler.opc.driver.siemens.transform.lreal.SiemensLRealTransform2Long;
import com.bichler.opc.driver.siemens.transform.lreal.SiemensLRealTransform2Short;
import com.bichler.opc.driver.siemens.transform.lreal.SiemensLRealTransform2UnsignedByte;
import com.bichler.opc.driver.siemens.transform.lreal.SiemensLRealTransform2UnsignedInteger;
import com.bichler.opc.driver.siemens.transform.lreal.SiemensLRealTransform2UnsignedLong;
import com.bichler.opc.driver.siemens.transform.lreal.SiemensLRealTransform2UnsignedShort;
import com.bichler.opc.driver.siemens.transform.real.SiemensRealTransform2Boolean;
import com.bichler.opc.driver.siemens.transform.real.SiemensRealTransform2Byte;
import com.bichler.opc.driver.siemens.transform.real.SiemensRealTransform2Double;
import com.bichler.opc.driver.siemens.transform.real.SiemensRealTransform2Float;
import com.bichler.opc.driver.siemens.transform.real.SiemensRealTransform2Integer;
import com.bichler.opc.driver.siemens.transform.real.SiemensRealTransform2Long;
import com.bichler.opc.driver.siemens.transform.real.SiemensRealTransform2Short;
import com.bichler.opc.driver.siemens.transform.real.SiemensRealTransform2UnsignedByte;
import com.bichler.opc.driver.siemens.transform.real.SiemensRealTransform2UnsignedInteger;
import com.bichler.opc.driver.siemens.transform.real.SiemensRealTransform2UnsignedLong;
import com.bichler.opc.driver.siemens.transform.real.SiemensRealTransform2UnsignedShort;
import com.bichler.opc.driver.siemens.transform.string_.SiemensStringTransform2Boolean;
import com.bichler.opc.driver.siemens.transform.string_.SiemensStringTransform2Byte;
import com.bichler.opc.driver.siemens.transform.string_.SiemensStringTransform2Double;
import com.bichler.opc.driver.siemens.transform.string_.SiemensStringTransform2Float;
import com.bichler.opc.driver.siemens.transform.string_.SiemensStringTransform2Integer;
import com.bichler.opc.driver.siemens.transform.string_.SiemensStringTransform2Long;
import com.bichler.opc.driver.siemens.transform.string_.SiemensStringTransform2Short;
import com.bichler.opc.driver.siemens.transform.string_.SiemensStringTransform2String;
import com.bichler.opc.driver.siemens.transform.string_.SiemensStringTransform2UnsignedByte;
import com.bichler.opc.driver.siemens.transform.string_.SiemensStringTransform2UnsignedInteger;
import com.bichler.opc.driver.siemens.transform.string_.SiemensStringTransform2UnsignedLong;
import com.bichler.opc.driver.siemens.transform.string_.SiemensStringTransform2UnsignedShort;
import com.bichler.opc.driver.siemens.transform.time.SiemensTimeTransform2Boolean;
import com.bichler.opc.driver.siemens.transform.time.SiemensTimeTransform2Byte;
import com.bichler.opc.driver.siemens.transform.time.SiemensTimeTransform2Double;
import com.bichler.opc.driver.siemens.transform.time.SiemensTimeTransform2Float;
import com.bichler.opc.driver.siemens.transform.time.SiemensTimeTransform2Integer;
import com.bichler.opc.driver.siemens.transform.time.SiemensTimeTransform2Long;
import com.bichler.opc.driver.siemens.transform.time.SiemensTimeTransform2Short;
import com.bichler.opc.driver.siemens.transform.time.SiemensTimeTransform2String;
import com.bichler.opc.driver.siemens.transform.time.SiemensTimeTransform2UnsignedByte;
import com.bichler.opc.driver.siemens.transform.time.SiemensTimeTransform2UnsignedInteger;
import com.bichler.opc.driver.siemens.transform.time.SiemensTimeTransform2UnsignedLong;
import com.bichler.opc.driver.siemens.transform.time.SiemensTimeTransform2UnsignedShort;
import com.bichler.opc.driver.siemens.transform.timeofday.SiemensTimeOfDayTransform2Boolean;
import com.bichler.opc.driver.siemens.transform.timeofday.SiemensTimeOfDayTransform2Byte;
import com.bichler.opc.driver.siemens.transform.timeofday.SiemensTimeOfDayTransform2Double;
import com.bichler.opc.driver.siemens.transform.timeofday.SiemensTimeOfDayTransform2Float;
import com.bichler.opc.driver.siemens.transform.timeofday.SiemensTimeOfDayTransform2Integer;
import com.bichler.opc.driver.siemens.transform.timeofday.SiemensTimeOfDayTransform2Long;
import com.bichler.opc.driver.siemens.transform.timeofday.SiemensTimeOfDayTransform2Short;
import com.bichler.opc.driver.siemens.transform.timeofday.SiemensTimeOfDayTransform2UnsignedByte;
import com.bichler.opc.driver.siemens.transform.timeofday.SiemensTimeOfDayTransform2UnsignedInteger;
import com.bichler.opc.driver.siemens.transform.timeofday.SiemensTimeOfDayTransform2UnsignedLong;
import com.bichler.opc.driver.siemens.transform.timeofday.SiemensTimeOfDayTransform2UnsignedShort;
import com.bichler.opc.driver.siemens.transform.timer.SiemensTimerTransform2Boolean;
import com.bichler.opc.driver.siemens.transform.timer.SiemensTimerTransform2Byte;
import com.bichler.opc.driver.siemens.transform.timer.SiemensTimerTransform2Double;
import com.bichler.opc.driver.siemens.transform.timer.SiemensTimerTransform2Float;
import com.bichler.opc.driver.siemens.transform.timer.SiemensTimerTransform2Integer;
import com.bichler.opc.driver.siemens.transform.timer.SiemensTimerTransform2Long;
import com.bichler.opc.driver.siemens.transform.timer.SiemensTimerTransform2Short;
import com.bichler.opc.driver.siemens.transform.timer.SiemensTimerTransform2UnsignedByte;
import com.bichler.opc.driver.siemens.transform.timer.SiemensTimerTransform2UnsignedInteger;
import com.bichler.opc.driver.siemens.transform.timer.SiemensTimerTransform2UnsignedLong;
import com.bichler.opc.driver.siemens.transform.timer.SiemensTimerTransform2UnsignedShort;
import com.bichler.opc.driver.siemens.transform.word.SiemensWordTransform2Boolean;
import com.bichler.opc.driver.siemens.transform.word.SiemensWordTransform2Byte;
import com.bichler.opc.driver.siemens.transform.word.SiemensWordTransform2Double;
import com.bichler.opc.driver.siemens.transform.word.SiemensWordTransform2Float;
import com.bichler.opc.driver.siemens.transform.word.SiemensWordTransform2Integer;
import com.bichler.opc.driver.siemens.transform.word.SiemensWordTransform2Long;
import com.bichler.opc.driver.siemens.transform.word.SiemensWordTransform2Short;
import com.bichler.opc.driver.siemens.transform.word.SiemensWordTransform2UnsignedByte;
import com.bichler.opc.driver.siemens.transform.word.SiemensWordTransform2UnsignedInteger;
import com.bichler.opc.driver.siemens.transform.word.SiemensWordTransform2UnsignedLong;
import com.bichler.opc.driver.siemens.transform.word.SiemensWordTransform2UnsignedShort;

public class SiemensTransformFactory {
	public static Map<Class<?>, SiemensTransformation> TRANS_BOOLEAN_MAP = new HashMap<Class<?>, SiemensTransformation>();
	public static Map<Class<?>, SiemensTransformation> TRANS_BYTE_MAP = new HashMap<Class<?>, SiemensTransformation>();
	public static Map<Class<?>, SiemensTransformation> TRANS_CHAR_MAP = new HashMap<Class<?>, SiemensTransformation>();
	public static Map<Class<?>, SiemensTransformation> TRANS_COUNTER_MAP = new HashMap<Class<?>, SiemensTransformation>();
	public static Map<Class<?>, SiemensTransformation> TRANS_DATEANDTIME_MAP = new HashMap<Class<?>, SiemensTransformation>();
	public static Map<Class<?>, SiemensTransformation> TRANS_DATE_MAP = new HashMap<Class<?>, SiemensTransformation>();
	public static Map<Class<?>, SiemensTransformation> TRANS_DINT_MAP = new HashMap<Class<?>, SiemensTransformation>();
	public static Map<Class<?>, SiemensTransformation> TRANS_DWORD_MAP = new HashMap<Class<?>, SiemensTransformation>();
	public static Map<Class<?>, SiemensTransformation> TRANS_INT_MAP = new HashMap<Class<?>, SiemensTransformation>();
	public static Map<Class<?>, SiemensTransformation> TRANS_REAL_MAP = new HashMap<Class<?>, SiemensTransformation>();
	public static Map<Class<?>, SiemensTransformation> TRANS_LREAL_MAP = new HashMap<Class<?>, SiemensTransformation>();
	public static Map<Class<?>, SiemensTransformation> TRANS_STRING_MAP = new HashMap<Class<?>, SiemensTransformation>();
	public static Map<Class<?>, SiemensTransformation> TRANS_TIME_MAP = new HashMap<Class<?>, SiemensTransformation>();
	public static Map<Class<?>, SiemensTransformation> TRANS_TIMEOFDAY_MAP = new HashMap<Class<?>, SiemensTransformation>();
	public static Map<Class<?>, SiemensTransformation> TRANS_TIMER_MAP = new HashMap<Class<?>, SiemensTransformation>();
	public static Map<Class<?>, SiemensTransformation> TRANS_WORD_MAP = new HashMap<Class<?>, SiemensTransformation>();
	static {
		TRANS_BOOLEAN_MAP.put(Boolean.class, new SiemensBooleanTransform2Boolean());
		TRANS_BOOLEAN_MAP.put(Byte.class, new SiemensBooleanTransform2Byte());
		TRANS_BOOLEAN_MAP.put(Double.class, new SiemensBooleanTransform2Double());
		TRANS_BOOLEAN_MAP.put(Float.class, new SiemensBooleanTransform2Float());
		TRANS_BOOLEAN_MAP.put(Integer.class, new SiemensBooleanTransform2Integer());
		TRANS_BOOLEAN_MAP.put(Long.class, new SiemensBooleanTransform2Long());
		TRANS_BOOLEAN_MAP.put(Short.class, new SiemensBooleanTransform2Short());
		TRANS_BOOLEAN_MAP.put(UnsignedByte.class, new SiemensBooleanTransform2UnsignedByte());
		TRANS_BOOLEAN_MAP.put(UnsignedLong.class, new SiemensBooleanTransform2UnsignedLong());
		TRANS_BOOLEAN_MAP.put(UnsignedShort.class, new SiemensBooleanTransform2UnsignedShort());
		TRANS_BOOLEAN_MAP.put(UnsignedInteger.class, new SiemensBooleanTransform2UnsignedInteger());
	}
	static {
		TRANS_BYTE_MAP.put(Boolean.class, new SiemensByteTransform2Boolean());
		TRANS_BYTE_MAP.put(Byte.class, new SiemensByteTransform2Byte());
		TRANS_BYTE_MAP.put(Double.class, new SiemensByteTransform2Double());
		TRANS_BYTE_MAP.put(Float.class, new SiemensByteTransform2Float());
		TRANS_BYTE_MAP.put(UnsignedByte.class, new SiemensByteTransform2UnsignedByte());
		TRANS_BYTE_MAP.put(Short.class, new SiemensByteTransform2Short());
		TRANS_BYTE_MAP.put(UnsignedShort.class, new SiemensByteTransform2UnsignedShort());
		TRANS_BYTE_MAP.put(Integer.class, new SiemensByteTransform2Integer());
		TRANS_BYTE_MAP.put(UnsignedInteger.class, new SiemensByteTransform2UnsignedInteger());
		TRANS_BYTE_MAP.put(Long.class, new SiemensByteTransform2Long());
		TRANS_BYTE_MAP.put(UnsignedLong.class, new SiemensByteTransform2UnsignedLong());
	}
	static {
		TRANS_CHAR_MAP.put(Boolean.class, new SiemensCharTransform2Boolean());
		TRANS_CHAR_MAP.put(Byte.class, new SiemensCharTransform2Byte());
		TRANS_CHAR_MAP.put(UnsignedByte.class, new SiemensCharTransform2UnsignedByte());
		TRANS_CHAR_MAP.put(Short.class, new SiemensCharTransform2Short());
		TRANS_CHAR_MAP.put(UnsignedShort.class, new SiemensCharTransform2UnsignedShort());
		TRANS_CHAR_MAP.put(Integer.class, new SiemensCharTransform2Integer());
		TRANS_CHAR_MAP.put(UnsignedInteger.class, new SiemensCharTransform2UnsignedInteger());
		TRANS_CHAR_MAP.put(Long.class, new SiemensCharTransform2Long());
		TRANS_CHAR_MAP.put(UnsignedLong.class, new SiemensCharTransform2UnsignedLong());
		TRANS_CHAR_MAP.put(Double.class, new SiemensCharTransform2Double());
		TRANS_CHAR_MAP.put(Float.class, new SiemensCharTransform2Float());
	}
	static {
		TRANS_COUNTER_MAP.put(Boolean.class, new SiemensCounterTransform2Boolean());
		TRANS_COUNTER_MAP.put(Byte.class, new SiemensCounterTransform2Byte());
		TRANS_COUNTER_MAP.put(UnsignedByte.class, new SiemensCounterTransform2UnsignedByte());
		TRANS_COUNTER_MAP.put(Short.class, new SiemensCounterTransform2Short());
		TRANS_COUNTER_MAP.put(UnsignedShort.class, new SiemensCounterTransform2UnsignedShort());
		TRANS_COUNTER_MAP.put(Integer.class, new SiemensCounterTransform2Integer());
		TRANS_COUNTER_MAP.put(UnsignedInteger.class, new SiemensCounterTransform2UnsignedInteger());
		TRANS_COUNTER_MAP.put(Long.class, new SiemensCounterTransform2Long());
		TRANS_COUNTER_MAP.put(UnsignedLong.class, new SiemensCounterTransform2UnsignedLong());
		TRANS_COUNTER_MAP.put(Double.class, new SiemensCounterTransform2Double());
		TRANS_COUNTER_MAP.put(Float.class, new SiemensCounterTransform2Float());
	}
	static {
		TRANS_DATEANDTIME_MAP.put(Boolean.class, new SiemensDateAndTimeTransform2Boolean());
		TRANS_DATEANDTIME_MAP.put(Byte.class, new SiemensDateAndTimeTransform2Byte());
		TRANS_DATEANDTIME_MAP.put(UnsignedByte.class, new SiemensDateAndTimeTransform2UnsignedByte());
		TRANS_DATEANDTIME_MAP.put(Short.class, new SiemensDateAndTimeTransform2Short());
		TRANS_DATEANDTIME_MAP.put(UnsignedShort.class, new SiemensDateAndTimeTransform2UnsignedShort());
		TRANS_DATEANDTIME_MAP.put(Integer.class, new SiemensDateAndTimeTransform2Integer());
		TRANS_DATEANDTIME_MAP.put(UnsignedInteger.class, new SiemensDateAndTimeTransform2UnsignedInteger());
		TRANS_DATEANDTIME_MAP.put(Long.class, new SiemensDateAndTimeTransform2Long());
		TRANS_DATEANDTIME_MAP.put(UnsignedLong.class, new SiemensDateAndTimeTransform2UnsignedLong());
		TRANS_DATEANDTIME_MAP.put(Double.class, new SiemensDateAndTimeTransform2Double());
		TRANS_DATEANDTIME_MAP.put(Float.class, new SiemensDateAndTimeTransform2Float());
		TRANS_DATEANDTIME_MAP.put(DateTime.class, new SiemensDateAndTimeTransform2DateTime());
	}
	static {
		TRANS_DATE_MAP.put(DateTime.class, new SiemensDateTransform2DateTime());
		TRANS_DATE_MAP.put(String.class, new SiemensDateTransform2String());
		TRANS_DATE_MAP.put(Boolean.class, new SiemensDateTransform2Boolean());
		TRANS_DATE_MAP.put(Byte.class, new SiemensDateTransform2Byte());
		TRANS_DATE_MAP.put(UnsignedByte.class, new SiemensDateTransform2UnsignedByte());
		TRANS_DATE_MAP.put(Short.class, new SiemensDateTransform2Short());
		TRANS_DATE_MAP.put(UnsignedShort.class, new SiemensDateTransform2UnsignedShort());
		TRANS_DATE_MAP.put(Integer.class, new SiemensDateTransform2Integer());
		TRANS_DATE_MAP.put(UnsignedInteger.class, new SiemensDateTransform2UnsignedInteger());
		TRANS_DATE_MAP.put(Long.class, new SiemensDateTransform2Long());
		TRANS_DATE_MAP.put(UnsignedLong.class, new SiemensDateTransform2UnsignedLong());
		TRANS_DATE_MAP.put(Double.class, new SiemensDateTransform2Double());
		TRANS_DATE_MAP.put(Float.class, new SiemensDateTransform2Float());
	}
	static {
		TRANS_DINT_MAP.put(Boolean.class, new SiemensDIntTransform2Boolean());
		TRANS_DINT_MAP.put(Byte.class, new SiemensDIntTransform2Byte());
		TRANS_DINT_MAP.put(UnsignedByte.class, new SiemensDIntTransform2UnsignedByte());
		TRANS_DINT_MAP.put(Short.class, new SiemensDIntTransform2Short());
		TRANS_DINT_MAP.put(UnsignedShort.class, new SiemensDIntTransform2UnsignedShort());
		TRANS_DINT_MAP.put(Integer.class, new SiemensDIntTransform2Integer());
		TRANS_DINT_MAP.put(UnsignedInteger.class, new SiemensDIntTransform2UnsignedInteger());
		TRANS_DINT_MAP.put(Long.class, new SiemensDIntTransform2Long());
		TRANS_DINT_MAP.put(UnsignedLong.class, new SiemensDIntTransform2UnsignedLong());
		TRANS_DINT_MAP.put(Double.class, new SiemensDIntTransform2Double());
		TRANS_DINT_MAP.put(Float.class, new SiemensDIntTransform2Float());
	}
	static {
		TRANS_INT_MAP.put(Boolean.class, new SiemensIntTransform2Boolean());
		TRANS_INT_MAP.put(Byte.class, new SiemensIntTransform2Byte());
		TRANS_INT_MAP.put(UnsignedByte.class, new SiemensIntTransform2UnsignedByte());
		TRANS_INT_MAP.put(Short.class, new SiemensIntTransform2Short());
		TRANS_INT_MAP.put(UnsignedShort.class, new SiemensIntTransform2UnsignedShort());
		TRANS_INT_MAP.put(Integer.class, new SiemensIntTransform2Integer());
		TRANS_INT_MAP.put(UnsignedInteger.class, new SiemensIntTransform2UnsignedInteger());
		TRANS_INT_MAP.put(Long.class, new SiemensIntTransform2Long());
		TRANS_INT_MAP.put(UnsignedLong.class, new SiemensIntTransform2UnsignedLong());
		TRANS_INT_MAP.put(Double.class, new SiemensIntTransform2Double());
		TRANS_INT_MAP.put(Float.class, new SiemensIntTransform2Float());
	}
	static {
		TRANS_REAL_MAP.put(Boolean.class, new SiemensRealTransform2Boolean());
		TRANS_REAL_MAP.put(Byte.class, new SiemensRealTransform2Byte());
		TRANS_REAL_MAP.put(UnsignedByte.class, new SiemensRealTransform2UnsignedByte());
		TRANS_REAL_MAP.put(Short.class, new SiemensRealTransform2Short());
		TRANS_REAL_MAP.put(UnsignedShort.class, new SiemensRealTransform2UnsignedShort());
		TRANS_REAL_MAP.put(Integer.class, new SiemensRealTransform2Integer());
		TRANS_REAL_MAP.put(UnsignedInteger.class, new SiemensRealTransform2UnsignedInteger());
		TRANS_REAL_MAP.put(Long.class, new SiemensRealTransform2Long());
		TRANS_REAL_MAP.put(UnsignedLong.class, new SiemensRealTransform2UnsignedLong());
		TRANS_REAL_MAP.put(Double.class, new SiemensRealTransform2Double());
		TRANS_REAL_MAP.put(Float.class, new SiemensRealTransform2Float());
	}
	static {
		TRANS_LREAL_MAP.put(Boolean.class, new SiemensLRealTransform2Boolean());
		TRANS_LREAL_MAP.put(Byte.class, new SiemensLRealTransform2Byte());
		TRANS_LREAL_MAP.put(UnsignedByte.class, new SiemensLRealTransform2UnsignedByte());
		TRANS_LREAL_MAP.put(Short.class, new SiemensLRealTransform2Short());
		TRANS_LREAL_MAP.put(UnsignedShort.class, new SiemensLRealTransform2UnsignedShort());
		TRANS_LREAL_MAP.put(Integer.class, new SiemensLRealTransform2Integer());
		TRANS_LREAL_MAP.put(UnsignedInteger.class, new SiemensLRealTransform2UnsignedInteger());
		TRANS_LREAL_MAP.put(Long.class, new SiemensLRealTransform2Long());
		TRANS_LREAL_MAP.put(UnsignedLong.class, new SiemensLRealTransform2UnsignedLong());
		TRANS_LREAL_MAP.put(Double.class, new SiemensLRealTransform2Double());
		TRANS_LREAL_MAP.put(Float.class, new SiemensLRealTransform2Float());
	}
	static {
		TRANS_STRING_MAP.put(Boolean.class, new SiemensStringTransform2Boolean());
		TRANS_STRING_MAP.put(Byte.class, new SiemensStringTransform2Byte());
		TRANS_STRING_MAP.put(UnsignedByte.class, new SiemensStringTransform2UnsignedByte());
		TRANS_STRING_MAP.put(Short.class, new SiemensStringTransform2Short());
		TRANS_STRING_MAP.put(UnsignedShort.class, new SiemensStringTransform2UnsignedShort());
		TRANS_STRING_MAP.put(Integer.class, new SiemensStringTransform2Integer());
		TRANS_STRING_MAP.put(UnsignedInteger.class, new SiemensStringTransform2UnsignedInteger());
		TRANS_STRING_MAP.put(Long.class, new SiemensStringTransform2Long());
		TRANS_STRING_MAP.put(UnsignedLong.class, new SiemensStringTransform2UnsignedLong());
		TRANS_STRING_MAP.put(Double.class, new SiemensStringTransform2Double());
		TRANS_STRING_MAP.put(Float.class, new SiemensStringTransform2Float());
		TRANS_STRING_MAP.put(String.class, new SiemensStringTransform2String());
	}
	static {
		TRANS_TIME_MAP.put(String.class, new SiemensTimeTransform2String());
		TRANS_TIME_MAP.put(Boolean.class, new SiemensTimeTransform2Boolean());
		TRANS_TIME_MAP.put(Byte.class, new SiemensTimeTransform2Byte());
		TRANS_TIME_MAP.put(UnsignedByte.class, new SiemensTimeTransform2UnsignedByte());
		TRANS_TIME_MAP.put(Short.class, new SiemensTimeTransform2Short());
		TRANS_TIME_MAP.put(UnsignedShort.class, new SiemensTimeTransform2UnsignedShort());
		TRANS_TIME_MAP.put(Integer.class, new SiemensTimeTransform2Integer());
		TRANS_TIME_MAP.put(UnsignedInteger.class, new SiemensTimeTransform2UnsignedInteger());
		TRANS_TIME_MAP.put(Long.class, new SiemensTimeTransform2Long());
		TRANS_TIME_MAP.put(UnsignedLong.class, new SiemensTimeTransform2UnsignedLong());
		TRANS_TIME_MAP.put(Double.class, new SiemensTimeTransform2Double());
		TRANS_TIME_MAP.put(Float.class, new SiemensTimeTransform2Float());
	}
	static {
		TRANS_TIMEOFDAY_MAP.put(Boolean.class, new SiemensTimeOfDayTransform2Boolean());
		TRANS_TIMEOFDAY_MAP.put(Byte.class, new SiemensTimeOfDayTransform2Byte());
		TRANS_TIMEOFDAY_MAP.put(UnsignedByte.class, new SiemensTimeOfDayTransform2UnsignedByte());
		TRANS_TIMEOFDAY_MAP.put(Short.class, new SiemensTimeOfDayTransform2Short());
		TRANS_TIMEOFDAY_MAP.put(UnsignedShort.class, new SiemensTimeOfDayTransform2UnsignedShort());
		TRANS_TIMEOFDAY_MAP.put(Integer.class, new SiemensTimeOfDayTransform2Integer());
		TRANS_TIMEOFDAY_MAP.put(UnsignedInteger.class, new SiemensTimeOfDayTransform2UnsignedInteger());
		TRANS_TIMEOFDAY_MAP.put(Long.class, new SiemensTimeOfDayTransform2Long());
		TRANS_TIMEOFDAY_MAP.put(UnsignedLong.class, new SiemensTimeOfDayTransform2UnsignedLong());
		TRANS_TIMEOFDAY_MAP.put(Double.class, new SiemensTimeOfDayTransform2Double());
		TRANS_TIMEOFDAY_MAP.put(Float.class, new SiemensTimeOfDayTransform2Float());
	}
	static {
		TRANS_TIMER_MAP.put(Boolean.class, new SiemensTimerTransform2Boolean());
		TRANS_TIMER_MAP.put(Byte.class, new SiemensTimerTransform2Byte());
		TRANS_TIMER_MAP.put(UnsignedByte.class, new SiemensTimerTransform2UnsignedByte());
		TRANS_TIMER_MAP.put(Short.class, new SiemensTimerTransform2Short());
		TRANS_TIMER_MAP.put(UnsignedShort.class, new SiemensTimerTransform2UnsignedShort());
		TRANS_TIMER_MAP.put(Integer.class, new SiemensTimerTransform2Integer());
		TRANS_TIMER_MAP.put(UnsignedInteger.class, new SiemensTimerTransform2UnsignedInteger());
		TRANS_TIMER_MAP.put(Long.class, new SiemensTimerTransform2Long());
		TRANS_TIMER_MAP.put(UnsignedLong.class, new SiemensTimerTransform2UnsignedLong());
		TRANS_TIMER_MAP.put(Double.class, new SiemensTimerTransform2Double());
		TRANS_TIMER_MAP.put(Float.class, new SiemensTimerTransform2Float());
	}
	static {
		TRANS_WORD_MAP.put(Boolean.class, new SiemensWordTransform2Boolean());
		TRANS_WORD_MAP.put(Byte.class, new SiemensWordTransform2Byte());
		TRANS_WORD_MAP.put(UnsignedByte.class, new SiemensWordTransform2UnsignedByte());
		TRANS_WORD_MAP.put(Short.class, new SiemensWordTransform2Short());
		TRANS_WORD_MAP.put(UnsignedShort.class, new SiemensWordTransform2UnsignedShort());
		TRANS_WORD_MAP.put(Integer.class, new SiemensWordTransform2Integer());
		TRANS_WORD_MAP.put(UnsignedInteger.class, new SiemensWordTransform2UnsignedInteger());
		TRANS_WORD_MAP.put(Long.class, new SiemensWordTransform2Long());
		TRANS_WORD_MAP.put(UnsignedLong.class, new SiemensWordTransform2UnsignedLong());
		TRANS_WORD_MAP.put(Double.class, new SiemensWordTransform2Double());
		TRANS_WORD_MAP.put(Float.class, new SiemensWordTransform2Float());
	}
	static {
		TRANS_DWORD_MAP.put(Boolean.class, new SiemensDWordTransform2Boolean());
		TRANS_DWORD_MAP.put(Byte.class, new SiemensDWordTransform2Byte());
		TRANS_DWORD_MAP.put(UnsignedByte.class, new SiemensDWordTransform2UnsignedByte());
		TRANS_DWORD_MAP.put(Short.class, new SiemensDWordTransform2Short());
		TRANS_DWORD_MAP.put(UnsignedShort.class, new SiemensDWordTransform2UnsignedShort());
		TRANS_DWORD_MAP.put(Integer.class, new SiemensDWordTransform2Integer());
		TRANS_DWORD_MAP.put(UnsignedInteger.class, new SiemensDWordTransform2UnsignedInteger());
		TRANS_DWORD_MAP.put(Long.class, new SiemensDWordTransform2Long());
		TRANS_DWORD_MAP.put(UnsignedLong.class, new SiemensDWordTransform2UnsignedLong());
		TRANS_DWORD_MAP.put(Double.class, new SiemensDWordTransform2Double());
		TRANS_DWORD_MAP.put(Float.class, new SiemensDWordTransform2Float());
	}

	public void createTransform(SiemensDPItem dp, Class<?> class_) {
		if (dp instanceof SiemensBooleanItem) {
			dp.setTransform(TRANS_BOOLEAN_MAP.get(class_));
		} else if (dp instanceof SiemensByteItem) {
			dp.setTransform(TRANS_BYTE_MAP.get(class_));
		} else if (dp instanceof SiemensCharItem) {
			dp.setTransform(TRANS_CHAR_MAP.get(class_));
		} else if (dp instanceof SiemensCounterItem) {
			dp.setTransform(TRANS_COUNTER_MAP.get(class_));
		} else if (dp instanceof SiemensDateItem) {
			dp.setTransform(TRANS_DATE_MAP.get(class_));
		} else if (dp instanceof SiemensDateAndTimeItem) {
			dp.setTransform(TRANS_DATEANDTIME_MAP.get(class_));
		} else if (dp instanceof SiemensDIntItem) {
			dp.setTransform(TRANS_DINT_MAP.get(class_));
		} else if (dp instanceof SiemensDWordItem) {
			dp.setTransform(TRANS_DWORD_MAP.get(class_));
		} else if (dp instanceof SiemensIntItem) {
			dp.setTransform(TRANS_INT_MAP.get(class_));
		} else if (dp instanceof SiemensRealItem) {
			dp.setTransform(TRANS_REAL_MAP.get(class_));
		} else if (dp instanceof SiemensLRealItem) {
			dp.setTransform(TRANS_LREAL_MAP.get(class_));
		} else if (dp instanceof SiemensStringItem) {
			dp.setTransform(TRANS_STRING_MAP.get(class_));
		} else if (dp instanceof SiemensTimeItem) {
			dp.setTransform(TRANS_TIME_MAP.get(class_));
		} else if (dp instanceof SiemensTimeOfDayItem) {
			dp.setTransform(TRANS_TIMEOFDAY_MAP.get(class_));
		} else if (dp instanceof SiemensTimerItem) {
			dp.setTransform(TRANS_TIMER_MAP.get(class_));
		} else if (dp instanceof SiemensWordItem) {
			dp.setTransform(TRANS_WORD_MAP.get(class_));
		}
	}
}
