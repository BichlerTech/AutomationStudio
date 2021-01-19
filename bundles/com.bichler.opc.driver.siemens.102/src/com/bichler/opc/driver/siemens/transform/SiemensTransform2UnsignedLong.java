package com.bichler.opc.driver.siemens.transform;

import org.opcfoundation.ua.builtintypes.UnsignedLong;

public abstract class SiemensTransform2UnsignedLong implements SiemensTransformation {
	@Override
	public Object[] createInternArray(int arrayLength) {
		return new UnsignedLong[arrayLength];
	}
}
