package com.bichler.opc.driver.siemens.transform;

import org.opcfoundation.ua.builtintypes.UnsignedShort;

public abstract class SiemensTransform2UnsignedShort implements SiemensTransformation {
	@Override
	public Object[] createInternArray(int arrayLength) {
		return new UnsignedShort[arrayLength];
	}
}
