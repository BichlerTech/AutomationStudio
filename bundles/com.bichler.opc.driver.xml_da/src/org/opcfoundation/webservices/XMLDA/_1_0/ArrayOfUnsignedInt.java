/**
 * ArrayOfUnsignedInt.java
 *
 * This file was auto-generated from WSDL by the Apache Axis 1.2.1 Jun 14, 2005
 * (09:15:57 EDT) WSDL2Java emitter.
 */
package org.opcfoundation.webservices.XMLDA._1_0;

public class ArrayOfUnsignedInt implements java.io.Serializable {
	private org.apache.axis.types.UnsignedInt[] unsignedInt;

	public ArrayOfUnsignedInt() {
	}

	public ArrayOfUnsignedInt(org.apache.axis.types.UnsignedInt[] unsignedInt) {
		this.unsignedInt = unsignedInt;
	}

	/**
	 * Gets the unsignedInt value for this ArrayOfUnsignedInt.
	 * 
	 * @return unsignedInt
	 */
	public org.apache.axis.types.UnsignedInt[] getUnsignedInt() {
		return unsignedInt;
	}

	/**
	 * Sets the unsignedInt value for this ArrayOfUnsignedInt.
	 * 
	 * @param unsignedInt
	 */
	public void setUnsignedInt(org.apache.axis.types.UnsignedInt[] unsignedInt) {
		this.unsignedInt = unsignedInt;
	}

	public org.apache.axis.types.UnsignedInt getUnsignedInt(int i) {
		return this.unsignedInt[i];
	}

	public void setUnsignedInt(int i, org.apache.axis.types.UnsignedInt _value) {
		this.unsignedInt[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ArrayOfUnsignedInt))
			return false;
		ArrayOfUnsignedInt other = (ArrayOfUnsignedInt) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && ((this.unsignedInt == null && other.getUnsignedInt() == null)
				|| (this.unsignedInt != null && java.util.Arrays.equals(this.unsignedInt, other.getUnsignedInt())));
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
		if (getUnsignedInt() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getUnsignedInt()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getUnsignedInt(), i);
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
			ArrayOfUnsignedInt.class, true);
	static {
		typeDesc.setXmlType(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfUnsignedInt"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("unsignedInt");
		elemField.setXmlName(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "unsignedInt"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "unsignedInt"));
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
