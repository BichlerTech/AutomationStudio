package com.bichler.opc.driver.siemens.transform;

public abstract class SiemensTransform2Byte implements SiemensTransformation {
	@Override
	public Object[] createInternArray(int arrayLength) {
		return new Byte[arrayLength];
	}
}
