package com.bichler.opc.driver.siemens.transform;

import org.opcfoundation.ua.builtintypes.DateTime;

public abstract class SiemensTransform2DateTime implements SiemensTransformation {
	@Override
	public Object[] createInternArray(int arrayLength) {
		return new DateTime[arrayLength];
	}
}
