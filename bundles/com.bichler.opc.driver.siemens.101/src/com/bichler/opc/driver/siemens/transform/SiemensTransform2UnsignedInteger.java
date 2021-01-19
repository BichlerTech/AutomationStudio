package com.bichler.opc.driver.siemens.transform;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;

public abstract class SiemensTransform2UnsignedInteger implements SiemensTransformation {
	@Override
	public Object[] createInternArray(int arrayLength) {
		return new UnsignedInteger[arrayLength];
	}
}
