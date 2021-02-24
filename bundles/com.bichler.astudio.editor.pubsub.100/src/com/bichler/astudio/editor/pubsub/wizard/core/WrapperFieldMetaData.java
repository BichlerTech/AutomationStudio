package com.bichler.astudio.editor.pubsub.wizard.core;

import java.util.UUID;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.core.KeyValuePair;

public class WrapperFieldMetaData implements IWrapper {

	private String Name;
	private LocalizedText Description;
	private UnsignedShort FieldFlags;
	private UnsignedByte BuiltInType;
	private NodeId DataType;
	private Integer ValueRank;
	private UnsignedInteger[] ArrayDimensions;
	private UnsignedInteger MaxStringLength;
	private UUID DataSetFieldId;
	private WrapperKeyValuePair[] Properties;

	public WrapperFieldMetaData() {

	}

	
	public String getName() {
		return Name;
	}

	public LocalizedText getDescription() {
		return Description;
	}

	public UnsignedShort getFieldFlags() {
		return FieldFlags;
	}

	public UnsignedByte getBuiltInType() {
		return BuiltInType;
	}

	public NodeId getDataType() {
		return DataType;
	}

	public Integer getValueRank() {
		return ValueRank;
	}

	public UnsignedInteger[] getArrayDimensions() {
		return ArrayDimensions;
	}

	public UnsignedInteger getMaxStringLength() {
		return MaxStringLength;
	}

	public UUID getDataSetFieldId() {
		return DataSetFieldId;
	}

	public WrapperKeyValuePair[] getProperties() {
		return Properties;
	}

	public void setName(String name) {
		Name = name;
	}

	public void setDescription(LocalizedText description) {
		Description = description;
	}

	public void setFieldFlags(UnsignedShort fieldFlags) {
		FieldFlags = fieldFlags;
	}

	public void setBuiltInType(UnsignedByte builtInType) {
		BuiltInType = builtInType;
	}

	public void setDataType(NodeId dataType) {
		DataType = dataType;
	}

	public void setValueRank(Integer valueRank) {
		ValueRank = valueRank;
	}

	public void setArrayDimensions(UnsignedInteger[] arrayDimensions) {
		ArrayDimensions = arrayDimensions;
	}

	public void setMaxStringLength(UnsignedInteger maxStringLength) {
		MaxStringLength = maxStringLength;
	}

	public void setDataSetFieldId(UUID dataSetFieldId) {
		DataSetFieldId = dataSetFieldId;
	}

	public void setProperties(WrapperKeyValuePair[] properties) {
		Properties = properties;
	}

	@Override
	public WrapperFieldMetaData clone() {
		WrapperFieldMetaData obj = new WrapperFieldMetaData();
		obj.ArrayDimensions = this.ArrayDimensions;
		obj.BuiltInType = this.BuiltInType;
		obj.DataSetFieldId = this.DataSetFieldId;
		obj.DataType = this.DataType;
		obj.Description = this.Description;
		obj.FieldFlags = this.FieldFlags;
		obj.MaxStringLength = this.MaxStringLength;
		obj.Name = this.Name;
		obj.Properties = this.Properties;
		obj.ValueRank = this.ValueRank;

		return obj;
	}


	@Override
	public void reset() {
		this.ArrayDimensions = null;
		this.BuiltInType = null;
		this.DataSetFieldId = null;
		this.DataType = null;
		this.Description = null;
		this.FieldFlags = null;
		this.MaxStringLength = null;
		this.Name = null;
		this.Properties = null;
		this.ValueRank = null;
	}
}
