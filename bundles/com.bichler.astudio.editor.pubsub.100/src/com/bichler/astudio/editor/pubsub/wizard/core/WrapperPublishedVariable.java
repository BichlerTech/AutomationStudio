package com.bichler.astudio.editor.pubsub.wizard.core;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;

public class WrapperPublishedVariable implements IWrapper {

	protected NodeId PublishedVariable;
	protected UnsignedInteger AttributeId;
	protected Double SamplingIntervalHint;
	protected UnsignedInteger DeadbandType;
	protected Double DeadbandValue;
	protected String IndexRange;
	protected Variant SubstituteValue;
	protected QualifiedName[] MetaDataProperties;

	public WrapperPublishedVariable() {

	}

	public NodeId getPublishedVariable() {
		return PublishedVariable;
	}

	public UnsignedInteger getAttributeId() {
		return AttributeId;
	}

	public Double getSamplingIntervalHint() {
		return SamplingIntervalHint;
	}

	public UnsignedInteger getDeadbandType() {
		return DeadbandType;
	}

	public Double getDeadbandValue() {
		return DeadbandValue;
	}

	public String getIndexRange() {
		return IndexRange;
	}

	public Variant getSubstituteValue() {
		return SubstituteValue;
	}

	public QualifiedName[] getMetaDataProperties() {
		return MetaDataProperties;
	}

	public void setPublishedVariable(NodeId publishedVariable) {
		PublishedVariable = publishedVariable;
	}

	public void setAttributeId(UnsignedInteger attributeId) {
		AttributeId = attributeId;
	}

	public void setSamplingIntervalHint(Double samplingIntervalHint) {
		SamplingIntervalHint = samplingIntervalHint;
	}

	public void setDeadbandType(UnsignedInteger deadbandType) {
		DeadbandType = deadbandType;
	}

	public void setDeadbandValue(Double deadbandValue) {
		DeadbandValue = deadbandValue;
	}

	public void setIndexRange(String indexRange) {
		IndexRange = indexRange;
	}

	public void setSubstituteValue(Variant substituteValue) {
		SubstituteValue = substituteValue;
	}

	public void setMetaDataProperties(QualifiedName[] metaDataProperties) {
		MetaDataProperties = metaDataProperties;
	}

	public WrapperPublishedVariable clone() {
		WrapperPublishedVariable obj = new WrapperPublishedVariable();
		obj.AttributeId = this.AttributeId;
		obj.DeadbandType = this.DeadbandType;
		obj.DeadbandValue = this.DeadbandValue;
		obj.IndexRange = this.IndexRange;
		obj.MetaDataProperties = this.MetaDataProperties;
		obj.PublishedVariable = this.PublishedVariable;
		obj.SamplingIntervalHint = this.SamplingIntervalHint;
		obj.SubstituteValue = this.SubstituteValue;
		
		return obj;
	}
}
