package com.bichler.opc.driver.siemens.transform;

public abstract class SiemensTransform2Double implements SiemensTransformation {
	@Override
	public Object[] createInternArray(int arrayLength) {
		return new Double[arrayLength];
	}
}
