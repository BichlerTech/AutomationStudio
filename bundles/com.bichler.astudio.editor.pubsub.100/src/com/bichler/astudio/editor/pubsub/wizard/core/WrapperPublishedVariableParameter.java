package com.bichler.astudio.editor.pubsub.wizard.core;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;

public class WrapperPublishedVariableParameter implements IWrapper{

    protected NodeId PublishedVariable;
    protected UnsignedInteger AttributeId;
    protected Double SamplingIntervalHint;
    protected UnsignedInteger DeadbandType;
    protected Double DeadbandValue;
    protected String IndexRange;
    protected Variant SubstituteValue;
    protected QualifiedName[] MetaDataProperties;
    
    public WrapperPublishedVariableParameter() {
    	
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
    
    public WrapperPublishedVariableParameter clone() {
    	WrapperPublishedVariableParameter obj = new WrapperPublishedVariableParameter();
    	obj.AttributeId = new UnsignedInteger(getAttributeId());
    	obj.DeadbandType = new UnsignedInteger(getDeadbandType());
    	obj.DeadbandValue = getDeadbandValue();
    	obj.IndexRange = getIndexRange();
    	obj.MetaDataProperties = getMetaDataProperties();
    	obj.PublishedVariable = getPublishedVariable();
    	obj.SamplingIntervalHint = getSamplingIntervalHint();
    	obj.SubstituteValue = getSubstituteValue();
    	return obj;
    }
}
