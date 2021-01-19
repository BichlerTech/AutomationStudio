package com.bichler.opc.driver.siemens.transform;

public abstract class SiemensTransform2Long implements SiemensTransformation {
	@Override
	public Object[] createInternArray(int arrayLength) {
		return new Long[arrayLength];
	}
}
