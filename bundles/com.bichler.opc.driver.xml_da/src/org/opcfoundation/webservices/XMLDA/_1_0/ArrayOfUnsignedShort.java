/**
 * ArrayOfUnsignedShort.java
 *
 * This file was auto-generated from WSDL by the Apache Axis 1.2.1 Jun 14, 2005
 * (09:15:57 EDT) WSDL2Java emitter.
 */
package org.opcfoundation.webservices.XMLDA._1_0;

public class ArrayOfUnsignedShort implements java.io.Serializable {
	private org.apache.axis.types.UnsignedShort[] unsignedShort;

	public ArrayOfUnsignedShort() {
	}

	public ArrayOfUnsignedShort(org.apache.axis.types.UnsignedShort[] unsignedShort) {
		this.unsignedShort = unsignedShort;
	}

	/**
	 * Gets the unsignedShort value for this ArrayOfUnsignedShort.
	 * 
	 * @return unsignedShort
	 */
	public org.apache.axis.types.UnsignedShort[] getUnsignedShort() {
		return unsignedShort;
	}

	/**
	 * Sets the unsignedShort value for this ArrayOfUnsignedShort.
	 * 
	 * @param unsignedShort
	 */
	public void setUnsignedShort(org.apache.axis.types.UnsignedShort[] unsignedShort) {
		this.unsignedShort = unsignedShort;
	}

	public org.apache.axis.types.UnsignedShort getUnsignedShort(int i) {
		return this.unsignedShort[i];
	}

	public void setUnsignedShort(int i, org.apache.axis.types.UnsignedShort _value) {
		this.unsignedShort[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ArrayOfUnsignedShort))
			return false;
		ArrayOfUnsignedShort other = (ArrayOfUnsignedShort) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true
				&& ((this.unsignedShort == null && other.getUnsignedShort() == null) || (this.unsignedShort != null
						&& java.util.Arrays.equals(this.unsignedShort, other.getUnsignedShort())));
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
		if (getUnsignedShort() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getUnsignedShort()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getUnsignedShort(), i);
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
			ArrayOfUnsignedShort.class, true);
	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/",
				"ArrayOfUnsignedShort"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("unsignedShort");
		elemField.setXmlName(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "unsignedShort"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "unsignedShort"));
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