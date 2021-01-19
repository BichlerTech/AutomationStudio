package com.bichler.opc.driver.siemens.transform;

public abstract class SiemensTransform2Integer implements SiemensTransformation {
	@Override
	public Object[] createInternArray(int arrayLength) {
		return new Integer[arrayLength];
	}
}
