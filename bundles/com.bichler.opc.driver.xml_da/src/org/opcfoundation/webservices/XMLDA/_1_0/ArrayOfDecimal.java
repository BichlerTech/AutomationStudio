/**
 * ArrayOfDecimal.java
 *
 * This file was auto-generated from WSDL by the Apache Axis 1.2.1 Jun 14, 2005
 * (09:15:57 EDT) WSDL2Java emitter.
 */
package org.opcfoundation.webservices.XMLDA._1_0;

public class ArrayOfDecimal implements java.io.Serializable {
	private java.math.BigDecimal[] decimal;

	public ArrayOfDecimal() {
	}

	public ArrayOfDecimal(java.math.BigDecimal[] decimal) {
		this.decimal = decimal;
	}

	/**
	 * Gets the decimal value for this ArrayOfDecimal.
	 * 
	 * @return decimal
	 */
	public java.math.BigDecimal[] getDecimal() {
		return decimal;
	}

	/**
	 * Sets the decimal value for this ArrayOfDecimal.
	 * 
	 * @param decimal
	 */
	public void setDecimal(java.math.BigDecimal[] decimal) {
		this.decimal = decimal;
	}

	public java.math.BigDecimal getDecimal(int i) {
		return this.decimal[i];
	}

	public void setDecimal(int i, java.math.BigDecimal _value) {
		this.decimal[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ArrayOfDecimal))
			return false;
		ArrayOfDecimal other = (ArrayOfDecimal) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && ((this.decimal == null && other.getDecimal() == null)
				|| (this.decimal != null && java.util.Arrays.equals(this.decimal, other.getDecimal())));
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
		if (getDecimal() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getDecimal()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getDecimal(), i);
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
			ArrayOfDecimal.class, true);
	static {
		typeDesc.setXmlType(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfDecimal"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("decimal");
		elemField.setXmlName(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "decimal"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
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
