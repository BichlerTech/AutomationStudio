package com.bichler.astudio.editor.siemens.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Identifiers;

public class SiemensDataValueFactory {
	private String datatype = "";
	private String classname = "";
	private String defaultValue = "";
	private NodeId datatypeId;
	private DataValue datavalue;

	protected SiemensDataValueFactory() {
	}

	public NodeId getDatatypeId() {
		return this.datatypeId;
	}

	public DataValue getDatavalue() {
		return this.datavalue;
	}

	public void setDatatypeId(NodeId datatypeId) {
		this.datatypeId = datatypeId;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public static DataValue createDataValue(String classname, String initValue) {
		switch (classname) {
		case "org.opcfoundation.ua.builtintypes.DateTime":
			try {
				return new DataValue(new Variant(new DateTime(Long.parseLong(initValue))));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
			break;
		default:
			try {
				Class<?> class2create = Class.forName(classname);
				Constructor<?> constructor = class2create.getConstructor(String.class);
				Object instance = constructor.newInstance(initValue);
				Variant v = new Variant(instance);
				DataValue value = new DataValue(v);
				return value;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			break;
		}
		return new DataValue(Variant.NULL);
	}

	// private static SiemensDataValueFactory createDataType(String type, String
	// initValue) {
	// SiemensDataValueFactory plctype =
	// SiemensDataValueFactory.createDatatype(type);
	// DataValue value = createDataValue(plctype.getClassname(), initValue);
	// plctype.setDatavalue(value);
	// return plctype;
	// }
	public static SiemensDataValueFactory createDatatype(String type) {
		// # type: Siemens-Type
		// # vdtype: OPC-Type
		// # clname: Java-Type
		// type="${1}"
		SiemensDataValueFactory plctype = new SiemensDataValueFactory();
		switch (type.toUpperCase()) {
		case "BOOL":
			plctype.setDatatype("Boolean");
			plctype.setDatatypeId(Identifiers.Boolean);
			plctype.setClassname("java.lang.Boolean");
			plctype.setDefaultValue("false");
			break;
		case "BOOLEAN":
			plctype.setDatatype("Boolean");
			plctype.setDatatypeId(Identifiers.Boolean);
			plctype.setClassname("java.lang.Boolean");
			plctype.setDefaultValue("false");
			break;
		case "BYTE":
			plctype.setDatatype("Byte");
			plctype.setDatatypeId(Identifiers.Byte);
			plctype.setClassname("java.lang.Byte");
			plctype.setDefaultValue("0");
			break;
		case "INT":
			plctype.setDatatype("Int16");
			plctype.setDatatypeId(Identifiers.Int16);
			plctype.setClassname("java.lang.Short");
			plctype.setDefaultValue("0");
			break;
		case "DINT":
			plctype.setDatatype("Int32");
			plctype.setDatatypeId(Identifiers.Int32);
			plctype.setClassname("java.lang.Integer");
			plctype.setDefaultValue("0");
			break;
		case "WORD":
			plctype.setDatatype("UInt16");
			plctype.setDatatypeId(Identifiers.UInt16);
			plctype.setClassname("org.opcfoundation.ua.builtintypes.UnsignedShort");
			plctype.setDefaultValue("0");
			break;
		case "DWORD":
			plctype.setDatatype("UInt32");
			plctype.setDatatypeId(Identifiers.UInt32);
			plctype.setClassname("org.opcfoundation.ua.builtintypes.UnsignedInteger");
			plctype.setDefaultValue("0");
			break;
		case "REAL":
			plctype.setDatatype("Float");
			plctype.setDatatypeId(Identifiers.Float);
			plctype.setClassname("java.lang.Float");
			plctype.setDefaultValue("0.0f");
			break;
		case "STRING":
			plctype.setDatatype("String");
			plctype.setDatatypeId(Identifiers.String);
			plctype.setClassname("java.lang.String");
			plctype.setDefaultValue("");
			break;
		case "DATE_AND_TIME":
			plctype.setDatatype("Date_and_Time");
			plctype.setDatatypeId(Identifiers.DateTime);
			plctype.setClassname("org.opcfoundation.ua.builtintypes.DateTime");
			plctype.setDefaultValue("" + DateTime.MIN_VALUE.getValue());
			break;
		default:
			break;
		}
		return plctype;
	}
}
