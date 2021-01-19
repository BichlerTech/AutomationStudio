/**
 * ArrayOfUnsignedLong.java
 *
 * This file was auto-generated from WSDL by the Apache Axis 1.2.1 Jun 14, 2005
 * (09:15:57 EDT) WSDL2Java emitter.
 */
package org.opcfoundation.webservices.XMLDA._1_0;

public class ArrayOfUnsignedLong implements java.io.Serializable {
	private org.apache.axis.types.UnsignedLong[] unsignedLong;

	public ArrayOfUnsignedLong() {
	}

	public ArrayOfUnsignedLong(org.apache.axis.types.UnsignedLong[] unsignedLong) {
		this.unsignedLong = unsignedLong;
	}

	/**
	 * Gets the unsignedLong value for this ArrayOfUnsignedLong.
	 * 
	 * @return unsignedLong
	 */
	public org.apache.axis.types.UnsignedLong[] getUnsignedLong() {
		return unsignedLong;
	}

	/**
	 * Sets the unsignedLong value for this ArrayOfUnsignedLong.
	 * 
	 * @param unsignedLong
	 */
	public void setUnsignedLong(org.apache.axis.types.UnsignedLong[] unsignedLong) {
		this.unsignedLong = unsignedLong;
	}

	public org.apache.axis.types.UnsignedLong getUnsignedLong(int i) {
		return this.unsignedLong[i];
	}

	public void setUnsignedLong(int i, org.apache.axis.types.UnsignedLong _value) {
		this.unsignedLong[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ArrayOfUnsignedLong))
			return false;
		ArrayOfUnsignedLong other = (ArrayOfUnsignedLong) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && ((this.unsignedLong == null && other.getUnsignedLong() == null)
				|| (this.unsignedLong != null && java.util.Arrays.equals(this.unsignedLong, other.getUnsignedLong())));
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
		if (getUnsignedLong() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getUnsignedLong()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getUnsignedLong(), i);
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
			ArrayOfUnsignedLong.class, true);
	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/",
				"ArrayOfUnsignedLong"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("unsignedLong");
		elemField.setXmlName(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "unsignedLong"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "unsignedLong"));
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
