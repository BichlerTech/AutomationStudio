package com.bichler.astudio.editor.pubsub.wizard.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.opcfoundation.ua.builtintypes.LocalizedText;

public class WrapperDataSetMetaData implements IWrapper {

	private String Name;
	private LocalizedText Description;
	private WrapperFieldMetaData[] Fields;
	private UUID DataSetClassId;
	private WrapperConfigurationVersion ConfigurationVersion;

//	private String[] Namespaces;
//	private StructureDescription[] StructureDataTypes;
//	private EnumDescription[] EnumDataTypes;
//	private SimpleTypeDescription[] SimpleDataTypes;

	public WrapperDataSetMetaData() {

	}

	public void init() {
		this.Name = "";
		this.Description = new LocalizedText("");
		this.Fields = new WrapperFieldMetaData[0];
		this.DataSetClassId = UUID.randomUUID();
		// this.ConfigurationVersion = new WrapperConfigurationVersion();
	}

//	public String[] getNamespaces() {
//		return Namespaces;
//	}
//
//	public StructureDescription[] getStructureDataTypes() {
//		return StructureDataTypes;
//	}
//
//	public EnumDescription[] getEnumDataTypes() {
//		return EnumDataTypes;
//	}
//
//	public SimpleTypeDescription[] getSimpleDataTypes() {
//		return SimpleDataTypes;
//	}

	public String getName() {
		return Name;
	}

	public LocalizedText getDescription() {
		return Description;
	}

	public WrapperFieldMetaData[] getFields() {
		return Fields;
	}

	public UUID getDataSetClassId() {
		return DataSetClassId;
	}

	public WrapperConfigurationVersion getConfigurationVersion() {
		return ConfigurationVersion;
	}

//	public void setNamespaces(String[] namespaces) {
//		Namespaces = namespaces;
//	}
//
//	public void setStructureDataTypes(StructureDescription[] structureDataTypes) {
//		StructureDataTypes = structureDataTypes;
//	}
//
//	public void setEnumDataTypes(EnumDescription[] enumDataTypes) {
//		EnumDataTypes = enumDataTypes;
//	}
//
//	public void setSimpleDataTypes(SimpleTypeDescription[] simpleDataTypes) {
//		SimpleDataTypes = simpleDataTypes;
//	}

	public void setName(String name) {
		Name = name;
	}

	public void setDescription(LocalizedText description) {
		Description = description;
	}

	public void setFields(WrapperFieldMetaData[] fields) {
		Fields = fields;
	}

	public void setDataSetClassId(UUID dataSetClassId) {
		DataSetClassId = dataSetClassId;
	}

	public void setConfigurationVersion(WrapperConfigurationVersion configurationVersion) {
		ConfigurationVersion = configurationVersion;
	}

	@Override
	public WrapperDataSetMetaData clone() {
		WrapperDataSetMetaData metadata = new WrapperDataSetMetaData();
		metadata.ConfigurationVersion = this.ConfigurationVersion != null ? this.ConfigurationVersion.clone() : null;
		metadata.DataSetClassId = this.DataSetClassId;
		metadata.Description = new LocalizedText(this.Description.getText(), this.Description.getLocale());
		WrapperFieldMetaData[] fieldMetaData = new WrapperFieldMetaData[Fields.length];
		for (int i = 0; i < this.Fields.length; i++) {
			fieldMetaData[i] = new WrapperFieldMetaData();
			fieldMetaData[i].setArrayDimensions(Fields[i].getArrayDimensions());
			fieldMetaData[i].setBuiltInType(Fields[i].getBuiltInType());
			fieldMetaData[i].setDataSetFieldId(Fields[i].getDataSetFieldId());
			fieldMetaData[i].setDataType(Fields[i].getDataType());
			fieldMetaData[i].setDescription(Fields[i].getDescription());
			fieldMetaData[i].setFieldFlags(Fields[i].getFieldFlags());
			fieldMetaData[i].setMaxStringLength(Fields[i].getMaxStringLength());
			fieldMetaData[i].setName(Fields[i].getName());
			fieldMetaData[i].setProperties(Fields[i].getProperties());
			fieldMetaData[i].setValueRank(Fields[i].getValueRank());
		}

		metadata.Fields = fieldMetaData;
		metadata.Name = this.Name;

//		metadata.EnumDataTypes = this.EnumDataTypes;
//		metadata.Namespaces = this.Namespaces;
//		metadata.SimpleDataTypes = this.SimpleDataTypes;
//		metadata.StructureDataTypes = this.StructureDataTypes;

		return metadata;
	}

	public void addField(WrapperFieldMetaData field) {
		List<WrapperFieldMetaData> fieldMetaData = new ArrayList<>();
		for(int i = 0; i < this.Fields.length; i++) {
			fieldMetaData.add(this.Fields[i]);
		}
		fieldMetaData.add(field);
		this.Fields = fieldMetaData.toArray(new WrapperFieldMetaData[0]);
	}

	public void setField(WrapperFieldMetaData model, WrapperFieldMetaData originModel) {
		int index = -1;
		for(int i = 0; i < this.Fields.length; i++) {
			WrapperFieldMetaData field = this.Fields[i];
			if(field == originModel) {
				index = i;
				break;
			}
		}
		// replace
		if(index >= 0) {
			this.Fields[index] = model;
		}
	}
}
