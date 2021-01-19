package com.bichler.opc.driver.siemens.transform;

import org.opcfoundation.ua.builtintypes.UnsignedByte;

public abstract class SiemensTransform2UnsignedByte implements SiemensTransformation {
	@Override
	public Object[] createInternArray(int arrayLength) {
		return new UnsignedByte[arrayLength];
	}
}
