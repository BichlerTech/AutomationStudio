package com.bichler.astudio.editor.pubsub.wizard.core;

import org.opcfoundation.ua.builtintypes.DataValue;

public class WrapperStaticValue implements IWrapper {

	protected boolean isFieldSourceEnabled = false;
	protected boolean isInformationModelNode = false;
	protected DataValue staticValueSource = new DataValue();

	public WrapperStaticValue() {

	}

	public boolean isFieldSourceEnabled() {
		return isFieldSourceEnabled;
	}

	public boolean isInformationModelNode() {
		return isInformationModelNode;
	}

	public DataValue getStaticValueSource() {
		return staticValueSource;
	}

	@Override
	public WrapperStaticValue clone() {
		WrapperStaticValue obj = new WrapperStaticValue();
		obj.isFieldSourceEnabled = isFieldSourceEnabled();
		obj.isInformationModelNode = isInformationModelNode();
		obj.staticValueSource = (DataValue) getStaticValueSource().clone();
		
		return obj;
	}

	@Override
	public void reset() {
		this.isFieldSourceEnabled = false;
		this.isInformationModelNode = false;
		this.staticValueSource = new DataValue();
	}

}
