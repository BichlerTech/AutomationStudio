/**
 * ArrayOfDateTime.java
 *
 * This file was auto-generated from WSDL by the Apache Axis 1.2.1 Jun 14, 2005
 * (09:15:57 EDT) WSDL2Java emitter.
 */
package org.opcfoundation.webservices.XMLDA._1_0;

public class ArrayOfDateTime implements java.io.Serializable {
	private java.util.Calendar[] dateTime;

	public ArrayOfDateTime() {
	}

	public ArrayOfDateTime(java.util.Calendar[] dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * Gets the dateTime value for this ArrayOfDateTime.
	 * 
	 * @return dateTime
	 */
	public java.util.Calendar[] getDateTime() {
		return dateTime;
	}

	/**
	 * Sets the dateTime value for this ArrayOfDateTime.
	 * 
	 * @param dateTime
	 */
	public void setDateTime(java.util.Calendar[] dateTime) {
		this.dateTime = dateTime;
	}

	public java.util.Calendar getDateTime(int i) {
		return this.dateTime[i];
	}

	public void setDateTime(int i, java.util.Calendar _value) {
		this.dateTime[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ArrayOfDateTime))
			return false;
		ArrayOfDateTime other = (ArrayOfDateTime) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && ((this.dateTime == null && other.getDateTime() == null)
				|| (this.dateTime != null && java.util.Arrays.equals(this.dateTime, other.getDateTime())));
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
		if (getDateTime() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getDateTime()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getDateTime(), i);
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
			ArrayOfDateTime.class, true);
	static {
		typeDesc.setXmlType(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfDateTime"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("dateTime");
		elemField.setXmlName(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "dateTime"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
