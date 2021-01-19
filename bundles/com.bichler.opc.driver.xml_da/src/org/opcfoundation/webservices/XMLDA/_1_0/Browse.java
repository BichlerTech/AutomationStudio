/**
 * Browse.java
 *
 * This file was auto-generated from WSDL by the Apache Axis 1.2.1 Jun 14, 2005
 * (09:15:57 EDT) WSDL2Java emitter.
 */
package org.opcfoundation.webservices.XMLDA._1_0;

public class Browse implements java.io.Serializable {
	private javax.xml.namespace.QName[] propertyNames;
	private java.lang.String localeID; // attribute
	private java.lang.String clientRequestHandle; // attribute
	private java.lang.String itemPath; // attribute
	private java.lang.String itemName; // attribute
	private java.lang.String continuationPoint; // attribute
	private int maxElementsReturned; // attribute
	private org.opcfoundation.webservices.XMLDA._1_0.BrowseFilter browseFilter; // attribute
	private java.lang.String elementNameFilter; // attribute
	private java.lang.String vendorFilter; // attribute
	private boolean returnAllProperties; // attribute
	private boolean returnPropertyValues; // attribute
	private boolean returnErrorText; // attribute

	public Browse() {
	}

	public Browse(javax.xml.namespace.QName[] propertyNames, java.lang.String localeID,
			java.lang.String clientRequestHandle, java.lang.String itemPath, java.lang.String itemName,
			java.lang.String continuationPoint, int maxElementsReturned,
			org.opcfoundation.webservices.XMLDA._1_0.BrowseFilter browseFilter, java.lang.String elementNameFilter,
			java.lang.String vendorFilter, boolean returnAllProperties, boolean returnPropertyValues,
			boolean returnErrorText) {
		this.propertyNames = propertyNames;
		this.localeID = localeID;
		this.clientRequestHandle = clientRequestHandle;
		this.itemPath = itemPath;
		this.itemName = itemName;
		this.continuationPoint = continuationPoint;
		this.maxElementsReturned = maxElementsReturned;
		this.browseFilter = browseFilter;
		this.elementNameFilter = elementNameFilter;
		this.vendorFilter = vendorFilter;
		this.returnAllProperties = returnAllProperties;
		this.returnPropertyValues = returnPropertyValues;
		this.returnErrorText = returnErrorText;
	}

	/**
	 * Gets the propertyNames value for this Browse.
	 * 
	 * @return propertyNames
	 */
	public javax.xml.namespace.QName[] getPropertyNames() {
		return propertyNames;
	}

	/**
	 * Sets the propertyNames value for this Browse.
	 * 
	 * @param propertyNames
	 */
	public void setPropertyNames(javax.xml.namespace.QName[] propertyNames) {
		this.propertyNames = propertyNames;
	}

	public javax.xml.namespace.QName getPropertyNames(int i) {
		return this.propertyNames[i];
	}

	public void setPropertyNames(int i, javax.xml.namespace.QName _value) {
		this.propertyNames[i] = _value;
	}

	/**
	 * Gets the localeID value for this Browse.
	 * 
	 * @return localeID
	 */
	public java.lang.String getLocaleID() {
		return localeID;
	}

	/**
	 * Sets the localeID value for this Browse.
	 * 
	 * @param localeID
	 */
	public void setLocaleID(java.lang.String localeID) {
		this.localeID = localeID;
	}

	/**
	 * Gets the clientRequestHandle value for this Browse.
	 * 
	 * @return clientRequestHandle
	 */
	public java.lang.String getClientRequestHandle() {
		return clientRequestHandle;
	}

	/**
	 * Sets the clientRequestHandle value for this Browse.
	 * 
	 * @param clientRequestHandle
	 */
	public void setClientRequestHandle(java.lang.String clientRequestHandle) {
		this.clientRequestHandle = clientRequestHandle;
	}

	/**
	 * Gets the itemPath value for this Browse.
	 * 
	 * @return itemPath
	 */
	public java.lang.String getItemPath() {
		return itemPath;
	}

	/**
	 * Sets the itemPath value for this Browse.
	 * 
	 * @param itemPath
	 */
	public void setItemPath(java.lang.String itemPath) {
		this.itemPath = itemPath;
	}

	/**
	 * Gets the itemName value for this Browse.
	 * 
	 * @return itemName
	 */
	public java.lang.String getItemName() {
		return itemName;
	}

	/**
	 * Sets the itemName value for this Browse.
	 * 
	 * @param itemName
	 */
	public void setItemName(java.lang.String itemName) {
		this.itemName = itemName;
	}

	/**
	 * Gets the continuationPoint value for this Browse.
	 * 
	 * @return continuationPoint
	 */
	public java.lang.String getContinuationPoint() {
		return continuationPoint;
	}

	/**
	 * Sets the continuationPoint value for this Browse.
	 * 
	 * @param continuationPoint
	 */
	public void setContinuationPoint(java.lang.String continuationPoint) {
		this.continuationPoint = continuationPoint;
	}

	/**
	 * Gets the maxElementsReturned value for this Browse.
	 * 
	 * @return maxElementsReturned
	 */
	public int getMaxElementsReturned() {
		return maxElementsReturned;
	}

	/**
	 * Sets the maxElementsReturned value for this Browse.
	 * 
	 * @param maxElementsReturned
	 */
	public void setMaxElementsReturned(int maxElementsReturned) {
		this.maxElementsReturned = maxElementsReturned;
	}

	/**
	 * Gets the browseFilter value for this Browse.
	 * 
	 * @return browseFilter
	 */
	public org.opcfoundation.webservices.XMLDA._1_0.BrowseFilter getBrowseFilter() {
		return browseFilter;
	}

	/**
	 * Sets the browseFilter value for this Browse.
	 * 
	 * @param browseFilter
	 */
	public void setBrowseFilter(org.opcfoundation.webservices.XMLDA._1_0.BrowseFilter browseFilter) {
		this.browseFilter = browseFilter;
	}

	/**
	 * Gets the elementNameFilter value for this Browse.
	 * 
	 * @return elementNameFilter
	 */
	public java.lang.String getElementNameFilter() {
		return elementNameFilter;
	}

	/**
	 * Sets the elementNameFilter value for this Browse.
	 * 
	 * @param elementNameFilter
	 */
	public void setElementNameFilter(java.lang.String elementNameFilter) {
		this.elementNameFilter = elementNameFilter;
	}

	/**
	 * Gets the vendorFilter value for this Browse.
	 * 
	 * @return vendorFilter
	 */
	public java.lang.String getVendorFilter() {
		return vendorFilter;
	}

	/**
	 * Sets the vendorFilter value for this Browse.
	 * 
	 * @param vendorFilter
	 */
	public void setVendorFilter(java.lang.String vendorFilter) {
		this.vendorFilter = vendorFilter;
	}

	/**
	 * Gets the returnAllProperties value for this Browse.
	 * 
	 * @return returnAllProperties
	 */
	public boolean isReturnAllProperties() {
		return returnAllProperties;
	}

	/**
	 * Sets the returnAllProperties value for this Browse.
	 * 
	 * @param returnAllProperties
	 */
	public void setReturnAllProperties(boolean returnAllProperties) {
		this.returnAllProperties = returnAllProperties;
	}

	/**
	 * Gets the returnPropertyValues value for this Browse.
	 * 
	 * @return returnPropertyValues
	 */
	public boolean isReturnPropertyValues() {
		return returnPropertyValues;
	}

	/**
	 * Sets the returnPropertyValues value for this Browse.
	 * 
	 * @param returnPropertyValues
	 */
	public void setReturnPropertyValues(boolean returnPropertyValues) {
		this.returnPropertyValues = returnPropertyValues;
	}

	/**
	 * Gets the returnErrorText value for this Browse.
	 * 
	 * @return returnErrorText
	 */
	public boolean isReturnErrorText() {
		return returnErrorText;
	}

	/**
	 * Sets the returnErrorText value for this Browse.
	 * 
	 * @param returnErrorText
	 */
	public void setReturnErrorText(boolean returnErrorText) {
		this.returnErrorText = returnErrorText;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof Browse))
			return false;
		Browse other = (Browse) obj;
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
				&& ((this.propertyNames == null && other.getPropertyNames() == null) || (this.propertyNames != null
						&& java.util.Arrays.equals(this.propertyNames, other.getPropertyNames())))
				&& ((this.localeID == null && other.getLocaleID() == null)
						|| (this.localeID != null && this.localeID.equals(other.getLocaleID())))
				&& ((this.clientRequestHandle == null && other.getClientRequestHandle() == null)
						|| (this.clientRequestHandle != null
								&& this.clientRequestHandle.equals(other.getClientRequestHandle())))
				&& ((this.itemPath == null && other.getItemPath() == null)
						|| (this.itemPath != null && this.itemPath.equals(other.getItemPath())))
				&& ((this.itemName == null && other.getItemName() == null)
						|| (this.itemName != null && this.itemName.equals(other.getItemName())))
				&& ((this.continuationPoint == null && other.getContinuationPoint() == null)
						|| (this.continuationPoint != null
								&& this.continuationPoint.equals(other.getContinuationPoint())))
				&& this.maxElementsReturned == other.getMaxElementsReturned()
				&& ((this.browseFilter == null && other.getBrowseFilter() == null)
						|| (this.browseFilter != null && this.browseFilter.equals(other.getBrowseFilter())))
				&& ((this.elementNameFilter == null && other.getElementNameFilter() == null)
						|| (this.elementNameFilter != null
								&& this.elementNameFilter.equals(other.getElementNameFilter())))
				&& ((this.vendorFilter == null && other.getVendorFilter() == null)
						|| (this.vendorFilter != null && this.vendorFilter.equals(other.getVendorFilter())))
				&& this.returnAllProperties == other.isReturnAllProperties()
				&& this.returnPropertyValues == other.isReturnPropertyValues()
				&& this.returnErrorText == other.isReturnErrorText();
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
		if (getPropertyNames() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getPropertyNames()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getPropertyNames(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		if (getLocaleID() != null) {
			_hashCode += getLocaleID().hashCode();
		}
		if (getClientRequestHandle() != null) {
			_hashCode += getClientRequestHandle().hashCode();
		}
		if (getItemPath() != null) {
			_hashCode += getItemPath().hashCode();
		}
		if (getItemName() != null) {
			_hashCode += getItemName().hashCode();
		}
		if (getContinuationPoint() != null) {
			_hashCode += getContinuationPoint().hashCode();
		}
		_hashCode += getMaxElementsReturned();
		if (getBrowseFilter() != null) {
			_hashCode += getBrowseFilter().hashCode();
		}
		if (getElementNameFilter() != null) {
			_hashCode += getElementNameFilter().hashCode();
		}
		if (getVendorFilter() != null) {
			_hashCode += getVendorFilter().hashCode();
		}
		_hashCode += (isReturnAllProperties() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += (isReturnPropertyValues() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += (isReturnErrorText() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			Browse.class, true);
	static {
		typeDesc.setXmlType(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">Browse"));
		org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("localeID");
		attrField.setXmlName(new javax.xml.namespace.QName("", "LocaleID"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		typeDesc.addFieldDesc(attrField);
		attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("clientRequestHandle");
		attrField.setXmlName(new javax.xml.namespace.QName("", "ClientRequestHandle"));
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
		attrField.setFieldName("continuationPoint");
		attrField.setXmlName(new javax.xml.namespace.QName("", "ContinuationPoint"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		typeDesc.addFieldDesc(attrField);
		attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("maxElementsReturned");
		attrField.setXmlName(new javax.xml.namespace.QName("", "MaxElementsReturned"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		typeDesc.addFieldDesc(attrField);
		attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("browseFilter");
		attrField.setXmlName(new javax.xml.namespace.QName("", "BrowseFilter"));
		attrField.setXmlType(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "browseFilter"));
		typeDesc.addFieldDesc(attrField);
		attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("elementNameFilter");
		attrField.setXmlName(new javax.xml.namespace.QName("", "ElementNameFilter"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		typeDesc.addFieldDesc(attrField);
		attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("vendorFilter");
		attrField.setXmlName(new javax.xml.namespace.QName("", "VendorFilter"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		typeDesc.addFieldDesc(attrField);
		attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("returnAllProperties");
		attrField.setXmlName(new javax.xml.namespace.QName("", "ReturnAllProperties"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		typeDesc.addFieldDesc(attrField);
		attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("returnPropertyValues");
		attrField.setXmlName(new javax.xml.namespace.QName("", "ReturnPropertyValues"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		typeDesc.addFieldDesc(attrField);
		attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("returnErrorText");
		attrField.setXmlName(new javax.xml.namespace.QName("", "ReturnErrorText"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		typeDesc.addFieldDesc(attrField);
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("propertyNames");
		elemField.setXmlName(
				new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "PropertyNames"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "QName"));
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
