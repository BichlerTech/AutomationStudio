/**
 * SubscribeRequestItemList.java
 *
 * This file was auto-generated from WSDL by the Apache Axis 1.2.1 Jun 14, 2005
 * (09:15:57 EDT) WSDL2Java emitter.
 */
package org.opcfoundation.webservices.XMLDA._1_0;

public class SubscribeRequestItemList implements java.io.Serializable {
	private org.opcfoundation.webservices.XMLDA._1_0.SubscribeRequestItem[] items;
	private java.lang.String itemPath; // attribute
	private javax.xml.namespace.QName reqType; // attribute
	private float deadband; // attribute
	private int requestedSamplingRate; // attribute
	private boolean enableBuffering; // attribute

	public SubscribeRequestItemList() {
	}

	public SubscribeRequestItemList(org.opcfoundation.webservices.XMLDA._1_0.SubscribeRequestItem[] items,
			java.lang.String itemPath, javax.xml.namespace.QName reqType, float deadband, int requestedSamplingRate,
			boolean enableBuffering) {
		this.items = items;
		this.itemPath = itemPath;
		this.reqType = reqType;
		this.deadband = deadband;
		this.requestedSamplingRate = requestedSamplingRate;
		this.enableBuffering = enableBuffering;
	}

	/**
	 * Gets the items value for this SubscribeRequestItemList.
	 * 
	 * @return items
	 */
	public org.opcfoundation.webservices.XMLDA._1_0.SubscribeRequestItem[] getItems() {
		return items;
	}

	/**
	 * Sets the items value for this SubscribeRequestItemList.
	 * 
	 * @param items
	 */
	public void setItems(org.opcfoundation.webservices.XMLDA._1_0.SubscribeRequestItem[] items) {
		this.items = items;
	}

	public org.opcfoundation.webservices.XMLDA._1_0.SubscribeRequestItem getItems(int i) {
		return this.items[i];
	}

	public void setItems(int i, org.opcfoundation.webservices.XMLDA._1_0.SubscribeRequestItem _value) {
		this.items[i] = _value;
	}

	/**
	 * Gets the itemPath value for this SubscribeRequestItemList.
	 * 
	 * @return itemPath
	 */
	public java.lang.String getItemPath() {
		return itemPath;
	}

	/**
	 * Sets the itemPath value for this SubscribeRequestItemList.
	 * 
	 * @param itemPath
	 */
	public void setItemPath(java.lang.String itemPath) {
		this.itemPath = itemPath;
	}

	/**
	 * Gets the reqType value for this SubscribeRequestItemList.
	 * 
	 * @return reqType
	 */
	public javax.xml.namespace.QName getReqType() {
		return reqType;
	}

	/**
	 * Sets the reqType value for this SubscribeRequestItemList.
	 * 
	 * @param reqType
	 */
	public void setReqType(javax.xml.namespace.QName reqType) {
		this.reqType = reqType;
	}

	/**
	 * Gets the deadband value for this SubscribeRequestItemList.
	 * 
	 * @return deadband
	 */
	public float getDeadband() {
		return deadband;
	}

	/**
	 * Sets the deadband value for this SubscribeRequestItemList.
	 * 
	 * @param deadband
	 */
	public void setDeadband(float deadband) {
		this.deadband = deadband;
	}

	/**
	 * Gets the requestedSamplingRate value for this SubscribeRequestItemList.
	 * 
	 * @return requestedSamplingRate
	 */
	public int getRequestedSamplingRate() {
		return requestedSamplingRate;
	}

	/**
	 * Sets the requestedSamplingRate value for this SubscribeRequestItemList.
	 * 
	 * @param requestedSamplingRate
	 */
	public void setRequestedSamplingRate(int requestedSamplingRate) {
		this.requestedSamplingRate = requestedSamplingRate;
	}

	/**
	 * Gets the enableBuffering value for this SubscribeRequestItemList.
	 * 
	 * @return enableBuffering
	 */
	public boolean isEnableBuffering() {
		return enableBuffering;
	}

	/**
	 * Sets the enableBuffering value for this SubscribeRequestItemList.
	 * 
	 * @param enableBuffering
	 */
	public void setEnableBuffering(boolean enableBuffering) {
		this.enableBuffering = enableBuffering;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof SubscribeRequestItemList))
			return false;
		SubscribeRequestItemList other = (SubscribeRequestItemList) obj;
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
				&& ((this.itemPath == null && other.getItemPath() == null)
						|| (this.itemPath != null && this.itemPath.equals(other.getItemPath())))
				&& ((this.reqType == null && other.getReqType() == null)
						|| (this.reqType != null && this.reqType.equals(other.getReqType())))
				&& this.deadband == other.getDeadband()
				&& this.requestedSamplingRate == other.getRequestedSamplingRate()
				&& this.enableBuffering == other.isEnableBuffering();
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
		if (getItemPath() != null) {
			_hashCode += getItemPath().hashCode();
		}
		if (getReqType() != null) {
			_hashCode += getReqType().hashCode();
		}
		_hashCode += new Float(getDeadband()).hashCode();
		_hashCode += getRequestedSamplingRate();
		_hashCode += (isEnableBuffering() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			SubscribeRequestItemList.class, true);
	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/",
				"SubscribeRequestItemList"));
		org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("itemPath");
		attrField.setXmlName(new javax.xml.namespace.QName("", "ItemPath"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		typeDesc.addFieldDesc(attrField);
		attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("reqType");
		attrField.setXmlName(new javax.xml.namespace.QName("", "ReqType"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "QName"));
		typeDesc.addFieldDesc(attrField);
		attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("deadband");
		attrField.setXmlName(new javax.xml.namespace.QName("", "Deadband"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
		typeDesc.addFieldDesc(attrField);
		attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("requestedSamplingRate");
		attrField.setXmlName(new javax.xml.namespace.QName("", "RequestedSamplingRate"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		typeDesc.addFieldDesc(attrField);
		attrField = new org.apache.axis.description.AttributeDesc();
		attrField.setFieldName("enableBuffering");
		attrField.setXmlName(new javax.xml.namespace.QName("", "EnableBuffering"));
		attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		typeDesc.addFieldDesc(attrField);
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("items");
		elemField.setXmlName(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "Items"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/",
				"SubscribeRequestItem"));
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
