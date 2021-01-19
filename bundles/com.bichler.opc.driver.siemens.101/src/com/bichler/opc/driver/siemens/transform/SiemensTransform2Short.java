package com.bichler.opc.driver.siemens.transform;

public abstract class SiemensTransform2Short implements SiemensTransformation {
	@Override
	public Object[] createInternArray(int arrayLength) {
		return new Short[arrayLength];
	}
}
