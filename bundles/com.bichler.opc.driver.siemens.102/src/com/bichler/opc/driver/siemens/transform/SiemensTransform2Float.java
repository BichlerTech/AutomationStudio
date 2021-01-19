package com.bichler.opc.driver.siemens.transform;

public abstract class SiemensTransform2Float implements SiemensTransformation {
	@Override
	public Object[] createInternArray(int arrayLength) {
		return new Float[arrayLength];
	}
}
