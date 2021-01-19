/**
 * ArrayOfBoolean.java
 *
 * This file was auto-generated from WSDL by the Apache Axis 1.2.1 Jun 14, 2005
 * (09:15:57 EDT) WSDL2Java emitter.
 */
package org.opcfoundation.webservices.XMLDA._1_0;

public class ArrayOfBoolean implements java.io.Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -7796395791170986014L;
	private boolean[] _boolean;

	public ArrayOfBoolean() {
	}

	public ArrayOfBoolean(boolean[] _boolean) {
		this._boolean = _boolean;
	}

	/**
	 * Gets the _boolean value for this ArrayOfBoolean.
	 * 
	 * @return _boolean
	 */
	public boolean[] get_boolean() {
		return _boolean;
	}

	/**
	 * Sets the _boolean value for this ArrayOfBoolean.
	 * 
	 * @param _boolean
	 */
	public void set_boolean(boolean[] _boolean) {
		this._boolean = _boolean;
	}

	public boolean get_boolean(int i) {
		return this._boolean[i];
	}

	public void set_boolean(int i, boolean _value) {
		this._boolean[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ArrayOfBoolean))
			return false;
		ArrayOfBoolean other = (ArrayOfBoolean) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && ((this._boolean == null && other.get_boolean() == null)
				|| (this._boolean != null && java.util.Arrays.equals(this._boolean, other.get_boolean())));
		__equalsCalc = null;
		return _equals;
	}

	private boolean __hashCodeCalc = false;

	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		if (get_boolean() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(get_boolean()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(get_boolean(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			ArrayOfBoolean.class, true);
	static {
		typeDesc.setXmlType(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfBoolean"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("_boolean");
		elemField.setXmlName(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "boolean"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setMaxOccursUnbounded(true);
		typeDesc.addFieldDesc(elemField);
	}

	/**
	 * Return type metadata object
	 */
	public static org.apache.axis.description.TypeDesc getTypeDesc() {
		return typeDesc;
	}

	/**
	 * Get Custom Serializer
	 */
	public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType,
			java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
	}

	/**
	 * Get Custom Deserializer
	 */
	public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType,
			java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
	}
}
