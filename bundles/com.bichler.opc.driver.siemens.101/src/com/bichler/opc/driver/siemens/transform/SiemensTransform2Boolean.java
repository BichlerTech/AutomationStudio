package com.bichler.opc.driver.siemens.transform;

public abstract class SiemensTransform2Boolean implements SiemensTransformation {
	@Override
	public Object[] createInternArray(int arrayLength) {
		return new Boolean[arrayLength];
	}
}
