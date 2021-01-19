package com.bichler.astudio.editor.pubsub.wizard.core;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedShort;

public class WrapperWriterDataSetMessage implements IWrapper {

	private MessageSettingType type;

	private UnsignedInteger DataSetMessageContentMask;
	private UnsignedShort ConfiguredSize;
	private UnsignedShort NetworkMessageNumber;
	private UnsignedShort DataSetOffset;

	private UnsignedInteger NetworkMessageContentMask;
	private UnsignedInteger JsonDataSetMessageContentMask;

	public WrapperWriterDataSetMessage() {

	}

	public MessageSettingType getType() {
		return type;
	}

	public UnsignedInteger getDataSetMessageContentMask() {
		return DataSetMessageContentMask;
	}

	public UnsignedShort getConfiguredSize() {
		return ConfiguredSize;
	}

	public UnsignedShort getNetworkMessageNumber() {
		return NetworkMessageNumber;
	}

	public UnsignedShort getDataSetOffset() {
		return DataSetOffset;
	}

	public UnsignedInteger getNetworkMessageContentMask() {
		return NetworkMessageContentMask;
	}

	public UnsignedInteger getJsonDataSetMessageContentMask() {
		return JsonDataSetMessageContentMask;
	}

	public void setType(MessageSettingType type) {
		this.type = type;
	}

	public void setDataSetMessageContentMask(UnsignedInteger dataSetMessageContentMask) {
		DataSetMessageContentMask = dataSetMessageContentMask;
	}

	public void setConfiguredSize(UnsignedShort configuredSize) {
		ConfiguredSize = configuredSize;
	}

	public void setNetworkMessageNumber(UnsignedShort networkMessageNumber) {
		NetworkMessageNumber = networkMessageNumber;
	}

	public void setDataSetOffset(UnsignedShort dataSetOffset) {
		DataSetOffset = dataSetOffset;
	}

	public void setNetworkMessageContentMask(UnsignedInteger networkMessageContentMask) {
		NetworkMessageContentMask = networkMessageContentMask;
	}

	public void setJsonDataSetMessageContentMask(UnsignedInteger jsonDataSetMessageContentMask) {
		JsonDataSetMessageContentMask = jsonDataSetMessageContentMask;
	}

	public WrapperWriterDataSetMessage clone() {
		WrapperWriterDataSetMessage obj = new WrapperWriterDataSetMessage();
		obj.type = this.type;
		obj.ConfiguredSize = this.ConfiguredSize;
		obj.DataSetMessageContentMask = this.DataSetMessageContentMask;
		obj.DataSetOffset = this.DataSetOffset;
		obj.NetworkMessageNumber = this.NetworkMessageNumber;
		obj.NetworkMessageContentMask = this.NetworkMessageContentMask;
		obj.JsonDataSetMessageContentMask = this.JsonDataSetMessageContentMask;
		return obj;
	}

}
