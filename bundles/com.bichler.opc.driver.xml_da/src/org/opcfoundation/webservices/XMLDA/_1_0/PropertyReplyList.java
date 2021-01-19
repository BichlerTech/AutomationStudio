/**
 * PropertyReplyList.java
 *
 * This file was auto-generated from WSDL by the Apache Axis 1.2.1 Jun 14, 2005
 * (09:15:57 EDT) WSDL2Java emitter.
 */
package org.opcfoundation.webservices.XMLDA._1_0;

public class PropertyReplyList implements java.io.Serializable {
	private org.opcfoundation.webservices.XMLDA._1_0.ItemProperty[] properties;
	private java.lang.String itemPath; // attribute
	private java.lang.String itemName; // attribute
	private javax.xml.namespace.QName resultID; // attribute

	public PropertyReplyList() {
	}

	public PropertyReplyList(org.opcfoundation.webservices.XMLDA._1_0.ItemProperty[] properties,
			java.lang.String itemPath, java.lang.String itemName, javax.xml.namespace.QName resultID) {
		this.properties = properties;
		this.itemPath = itemPath;
		this.itemName = itemName;
		this.resultID = resultID;
	}

	/**
	 * Gets the properties value for this PropertyReplyList.
	 * 
	 * @return properties
	 */
	public org.opcfoundation.webservices.XMLDA._1_0.ItemProperty[] getProperties() {
		return properties;
	}

	/**
	 * Sets the properties value for this PropertyReplyList.
	 * 
	 * @param properties
	 */
	public void setProperties(org.opcfoundation.webservices.XMLDA._1_0.ItemProperty[] properties) {
		this.properties = properties;
	}

	public org.opcfoundation.webservices.XMLDA._1_0.ItemProperty getProperties(int i) {
		return this.properties[i];
	}

	public void setProperties(int i, org.opcfoundation.webservices.XMLDA._1_0.ItemProperty _value) {
		this.properties[i] = _value;
	}

	/**
	 * Gets the itemPath value for this PropertyReplyList.
	 * 
	 * @return itemPath
	 */
	public java.lang.String getItemPath() {
		return itemPath;
	}

	/**
	 * Sets the itemPath value for this PropertyReplyList.
	 * 
	 * @param itemPath
	 */
	public void setItemPath(java.lang.String itemPath) {
		this.itemPath = itemPath;
	}

	/**
	 * Gets the itemName value for this PropertyReplyList.
	 * 
	 * @return itemName
	 */
	public java.lang.String getItemName() {
		return itemName;
	}

	/**
	 * Sets the itemName value for this PropertyReplyList.
	 * 
	 * @param itemName
	 */
	public void setItemName(java.lang.String itemName) {
		this.itemName = itemName;
	}

	/**
	 * Gets the resultID value for this PropertyReplyList.
	 * 
	 * @return resultID
	 */
	public javax.xml.namespace.QName getResultID() {
		return resultID;
	}

	/**
	 * Sets the resultID value for this PropertyReplyList.
	 * 
	 * @param resultID
	 */
	public void setResultID(javax.xml.namespace.QName resultID) {
		this.resultID = resultID;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof PropertyReplyList))
			return false;
		PropertyReplyList other = (PropertyReplyList) obj;
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
				&& ((this.properties == null && other.getProperties() == null)
						|| (this.properties != null && java.util.Arrays.equals(this.properties, other.getProperties())))
				&& ((this.itemPath == null && other.getItemPath() == null)
						|| (this.itemPath != null && this.itemPath.equals(other.getItemPath())))
				&& ((this.itemName == null && other.getItemName() == null)
						|| (this.itemName != null && this.itemName.equals(other.getItemName())))
				&& ((this.resultID == null && other.getResultID() == null)
						|| (this.resultID != null && this.resultID.equals(other.getResultID())));
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
		if (getProperties() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getProperties()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getProperties(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		if (getItemPath() != null) {
			_hashCode += getItemPath().hashCode();
		}
		if (getItemName() != null) {
			_hashCode += getItemName().hashCode();
		}
		if (getResultID() != null) {
			_hashCode += getResultID().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			PropertyReplyList.class, true);
	static {
		typeDesc.setXmlType(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "PropertyReplyList"));
		org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("itemPath");
		attrField.setXmlName(new javax.xml.namespace.QName("", "ItemPath"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		typeDesc.addFieldDesc(attrField);
		attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("itemName");
		attrField.setXmlName(new javax.xml.namespace.QName("", "ItemName"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		typeDesc.addFieldDesc(attrField);
		attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("resultID");
		attrField.setXmlName(new javax.xml.namespace.QName("", "ResultID"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "QName"));
		typeDesc.addFieldDesc(attrField);
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("properties");
		elemField.setXmlName(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "Properties"));
		elemField.setXmlType(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ItemProperty"));
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
