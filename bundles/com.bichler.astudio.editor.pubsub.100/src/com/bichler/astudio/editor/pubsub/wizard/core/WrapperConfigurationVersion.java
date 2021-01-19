package com.bichler.astudio.editor.pubsub.wizard.core;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;

public class WrapperConfigurationVersion implements IWrapper {

	protected UnsignedInteger MajorVersion;
	protected UnsignedInteger MinorVersion;

	public WrapperConfigurationVersion() {

	}

	public UnsignedInteger getMajorVersion() {
		return MajorVersion;
	}

	public UnsignedInteger getMinorVersion() {
		return MinorVersion;
	}
	
	

	public void setMajorVersion(UnsignedInteger majorVersion) {
		MajorVersion = majorVersion;
	}

	public void setMinorVersion(UnsignedInteger minorVersion) {
		MinorVersion = minorVersion;
	}

	public WrapperConfigurationVersion clone() {
		WrapperConfigurationVersion obj = new WrapperConfigurationVersion();
		obj.MajorVersion = new UnsignedInteger(MajorVersion);
		obj.MinorVersion = new UnsignedInteger(MinorVersion);

		return obj;
	}
}
