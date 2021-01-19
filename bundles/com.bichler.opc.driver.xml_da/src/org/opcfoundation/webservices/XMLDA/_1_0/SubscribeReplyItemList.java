/**
 * SubscribeReplyItemList.java
 *
 * This file was auto-generated from WSDL by the Apache Axis 1.2.1 Jun 14, 2005
 * (09:15:57 EDT) WSDL2Java emitter.
 */
package org.opcfoundation.webservices.XMLDA._1_0;

public class SubscribeReplyItemList implements java.io.Serializable {
	private org.opcfoundation.webservices.XMLDA._1_0.SubscribeItemValue[] items;
	private int revisedSamplingRate; // attribute

	public SubscribeReplyItemList() {
	}

	public SubscribeReplyItemList(org.opcfoundation.webservices.XMLDA._1_0.SubscribeItemValue[] items,
			int revisedSamplingRate) {
		this.items = items;
		this.revisedSamplingRate = revisedSamplingRate;
	}

	/**
	 * Gets the items value for this SubscribeReplyItemList.
	 * 
	 * @return items
	 */
	public org.opcfoundation.webservices.XMLDA._1_0.SubscribeItemValue[] getItems() {
		return items;
	}

	/**
	 * Sets the items value for this SubscribeReplyItemList.
	 * 
	 * @param items
	 */
	public void setItems(org.opcfoundation.webservices.XMLDA._1_0.SubscribeItemValue[] items) {
		this.items = items;
	}

	public org.opcfoundation.webservices.XMLDA._1_0.SubscribeItemValue getItems(int i) {
		return this.items[i];
	}

	public void setItems(int i, org.opcfoundation.webservices.XMLDA._1_0.SubscribeItemValue _value) {
		this.items[i] = _value;
	}

	/**
	 * Gets the revisedSamplingRate value for this SubscribeReplyItemList.
	 * 
	 * @return revisedSamplingRate
	 */
	public int getRevisedSamplingRate() {
		return revisedSamplingRate;
	}

	/**
	 * Sets the revisedSamplingRate value for this SubscribeReplyItemList.
	 * 
	 * @param revisedSamplingRate
	 */
	public void setRevisedSamplingRate(int revisedSamplingRate) {
		this.revisedSamplingRate = revisedSamplingRate;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof SubscribeReplyItemList))
			return false;
		SubscribeReplyItemList other = (SubscribeReplyItemList) obj;
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
				&& ((this.items == null && other.getItems() == null)
						|| (this.items != null && java.util.Arrays.equals(this.items, other.getItems())))
				&& this.revisedSamplingRate == other.getRevisedSamplingRate();
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
		if (getItems() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getItems()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getItems(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		_hashCode += getRevisedSamplingRate();
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			SubscribeReplyItemList.class, true);
	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/",
				"SubscribeReplyItemList"));
		org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("revisedSamplingRate");
		attrField.setXmlName(new javax.xml.namespace.QName("", "RevisedSamplingRate"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		typeDesc.addFieldDesc(attrField);
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("items");
		elemField.setXmlName(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "Items"));
		elemField.setXmlType(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "SubscribeItemValue"));
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
