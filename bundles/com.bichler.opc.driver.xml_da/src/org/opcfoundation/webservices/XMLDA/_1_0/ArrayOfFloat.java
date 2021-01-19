/**
 * ArrayOfFloat.java
 *
 * This file was auto-generated from WSDL by the Apache Axis 1.2.1 Jun 14, 2005
 * (09:15:57 EDT) WSDL2Java emitter.
 */
package org.opcfoundation.webservices.XMLDA._1_0;

public class ArrayOfFloat implements java.io.Serializable {
	private float[] _float;

	public ArrayOfFloat() {
	}

	public ArrayOfFloat(float[] _float) {
		this._float = _float;
	}

	/**
	 * Gets the _float value for this ArrayOfFloat.
	 * 
	 * @return _float
	 */
	public float[] get_float() {
		return _float;
	}

	/**
	 * Sets the _float value for this ArrayOfFloat.
	 * 
	 * @param _float
	 */
	public void set_float(float[] _float) {
		this._float = _float;
	}

	public float get_float(int i) {
		return this._float[i];
	}

	public void set_float(int i, float _value) {
		this._float[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ArrayOfFloat))
			return false;
		ArrayOfFloat other = (ArrayOfFloat) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && ((this._float == null && other.get_float() == null)
				|| (this._float != null && java.util.Arrays.equals(this._float, other.get_float())));
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
		if (get_float() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(get_float()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(get_float(), i);
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
			ArrayOfFloat.class, true);
	static {
		typeDesc.setXmlType(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfFloat"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("_float");
		elemField.setXmlName(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "float"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
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
