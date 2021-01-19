/**
 * BrowseElement.java
 *
 * This file was auto-generated from WSDL by the Apache Axis 1.2.1 Jun 14, 2005
 * (09:15:57 EDT) WSDL2Java emitter.
 */
package org.opcfoundation.webservices.XMLDA._1_0;

public class BrowseElement implements java.io.Serializable {
	private org.opcfoundation.webservices.XMLDA._1_0.ItemProperty[] properties;
	private java.lang.String name; // attribute
	private java.lang.String itemPath; // attribute
	private java.lang.String itemName; // attribute
	private boolean isItem; // attribute
	private boolean hasChildren; // attribute

	public BrowseElement() {
	}

	public BrowseElement(org.opcfoundation.webservices.XMLDA._1_0.ItemProperty[] properties, java.lang.String name,
			java.lang.String itemPath, java.lang.String itemName, boolean isItem, boolean hasChildren) {
		this.properties = properties;
		this.name = name;
		this.itemPath = itemPath;
		this.itemName = itemName;
		this.isItem = isItem;
		this.hasChildren = hasChildren;
	}

	/**
	 * Gets the properties value for this BrowseElement.
	 * 
	 * @return properties
	 */
	public org.opcfoundation.webservices.XMLDA._1_0.ItemProperty[] getProperties() {
		return properties;
	}

	/**
	 * Sets the properties value for this BrowseElement.
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
	 * Gets the name value for this BrowseElement.
	 * 
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this BrowseElement.
	 * 
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the itemPath value for this BrowseElement.
	 * 
	 * @return itemPath
	 */
	public java.lang.String getItemPath() {
		return itemPath;
	}

	/**
	 * Sets the itemPath value for this BrowseElement.
	 * 
	 * @param itemPath
	 */
	public void setItemPath(java.lang.String itemPath) {
		this.itemPath = itemPath;
	}

	/**
	 * Gets the itemName value for this BrowseElement.
	 * 
	 * @return itemName
	 */
	public java.lang.String getItemName() {
		return itemName;
	}

	/**
	 * Sets the itemName value for this BrowseElement.
	 * 
	 * @param itemName
	 */
	public void setItemName(java.lang.String itemName) {
		this.itemName = itemName;
	}

	/**
	 * Gets the isItem value for this BrowseElement.
	 * 
	 * @return isItem
	 */
	public boolean isIsItem() {
		return isItem;
	}

	/**
	 * Sets the isItem value for this BrowseElement.
	 * 
	 * @param isItem
	 */
	public void setIsItem(boolean isItem) {
		this.isItem = isItem;
	}

	/**
	 * Gets the hasChildren value for this BrowseElement.
	 * 
	 * @return hasChildren
	 */
	public boolean isHasChildren() {
		return hasChildren;
	}

	/**
	 * Sets the hasChildren value for this BrowseElement.
	 * 
	 * @param hasChildren
	 */
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof BrowseElement))
			return false;
		BrowseElement other = (BrowseElement) obj;
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
				&& ((this.name == null && other.getName() == null)
						|| (this.name != null && this.name.equals(other.getName())))
				&& ((this.itemPath == null && other.getItemPath() == null)
						|| (this.itemPath != null && this.itemPath.equals(other.getItemPath())))
				&& ((this.itemName == null && other.getItemName() == null)
						|| (this.itemName != null && this.itemName.equals(other.getItemName())))
				&& this.isItem == other.isIsItem() && this.hasChildren == other.isHasChildren();
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
		if (getName() != null) {
			_hashCode += getName().hashCode();
		}
		if (getItemPath() != null) {
			_hashCode += getItemPath().hashCode();
		}
		if (getItemName() != null) {
			_hashCode += getItemName().hashCode();
		}
		_hashCode += (isIsItem() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += (isHasChildren() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			BrowseElement.class, true);
	static {
		typeDesc.setXmlType(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "BrowseElement"));
		org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("name");
		attrField.setXmlName(new javax.xml.namespace.QName("", "Name"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		typeDesc.addFieldDesc(attrField);
		attrField = new org.apache.axis.description.AttributeDesc();
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
		attrField.setFieldName("isItem");
		attrField.setXmlName(new javax.xml.namespace.QName("", "IsItem"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		typeDesc.addFieldDesc(attrField);
		attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("hasChildren");
		attrField.setXmlName(new javax.xml.namespace.QName("", "HasChildren"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
