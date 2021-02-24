package com.bichler.astudio.editor.pubsub.wizard.core;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.DataSetOrderingType;

public class WrapperWriterGroupMessage implements IWrapper {
	
	private MessageSettingType type = null;
	
	private UnsignedInteger GroupVersion;
	private DataSetOrderingType DataSetOrdering;
	private UnsignedInteger NetworkMessageContentMask;
	private Double SamplingOffset;
	private Double[] PublishingOffset;
	
	private UnsignedInteger JsonNetworkMessageContentMask;
	
	public WrapperWriterGroupMessage() {
		
	}

	public MessageSettingType getType() {
		return type;
	}

	public UnsignedInteger getGroupVersion() {
		return GroupVersion;
	}

	public DataSetOrderingType getDataSetOrdering() {
		return DataSetOrdering;
	}

	public UnsignedInteger getNetworkMessageContentMask() {
		return NetworkMessageContentMask;
	}

	public Double getSamplingOffset() {
		return SamplingOffset;
	}

	public Double[] getPublishingOffset() {
		return PublishingOffset;
	}

	public UnsignedInteger getJsonNetworkMessageContentMask() {
		return JsonNetworkMessageContentMask;
	}

	public void setType(MessageSettingType type) {
		this.type = type;
	}

	public void setGroupVersion(UnsignedInteger groupVersion) {
		GroupVersion = groupVersion;
	}

	public void setDataSetOrdering(DataSetOrderingType dataSetOrdering) {
		DataSetOrdering = dataSetOrdering;
	}

	public void setNetworkMessageContentMask(UnsignedInteger networkMessageContentMask) {
		NetworkMessageContentMask = networkMessageContentMask;
	}

	public void setSamplingOffset(Double samplingOffset) {
		SamplingOffset = samplingOffset;
	}

	public void setPublishingOffset(Double[] publishingOffset) {
		PublishingOffset = publishingOffset;
	}

	public void setJsonNetworkMessageContentMask(UnsignedInteger jsonNetworkMessageContentMask) {
		JsonNetworkMessageContentMask = jsonNetworkMessageContentMask;
	}

	@Override
	public WrapperWriterGroupMessage clone() {
		WrapperWriterGroupMessage obj = new WrapperWriterGroupMessage();
		obj.type = this.type;
		obj.GroupVersion = this.GroupVersion;
		obj.DataSetOrdering = this.DataSetOrdering;
		obj.NetworkMessageContentMask = this.NetworkMessageContentMask;
		obj.SamplingOffset = this.SamplingOffset;
		obj.PublishingOffset = this.PublishingOffset;
		obj.JsonNetworkMessageContentMask = this.JsonNetworkMessageContentMask;
		
		return obj;
	}

	@Override
	public void reset() {
		this.type = null;
		this.GroupVersion = null;
		this.DataSetOrdering = null;
		this.NetworkMessageContentMask = null;
		this.SamplingOffset = null;
		this.PublishingOffset = null;
		this.JsonNetworkMessageContentMask = null;
	}
	
	
}
