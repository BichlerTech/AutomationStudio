package com.bichler.opc.driver.siemens.transform;

public abstract class SiemensTransform2String implements SiemensTransformation {
	@Override
	public Object[] createInternArray(int arrayLength) {
		return new String[arrayLength];
	}
}