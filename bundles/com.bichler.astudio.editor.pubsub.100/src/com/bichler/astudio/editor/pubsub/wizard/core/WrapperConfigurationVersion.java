package com.bichler.astudio.editor.pubsub.wizard.core;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;

public class WrapperConfigurationVersion implements IWrapper {

	protected UnsignedInteger MajorVersion = null;
	protected UnsignedInteger MinorVersion = null;

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
		if (MajorVersion != null) {
			obj.MajorVersion = new UnsignedInteger(MajorVersion);
		} else {
			obj.MajorVersion = null;
		}
		if (MinorVersion != null) {
			obj.MinorVersion = new UnsignedInteger(MinorVersion);
		} else {
			obj.MinorVersion = null;
		}

		return obj;
	}

	@Override
	public void reset() {
		MajorVersion = null;
		MinorVersion = null;
	}
}
