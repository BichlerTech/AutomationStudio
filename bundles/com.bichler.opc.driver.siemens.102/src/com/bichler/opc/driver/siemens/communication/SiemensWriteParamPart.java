package com.bichler.opc.driver.siemens.communication;

import java.util.ArrayList;
import java.util.List;

public class SiemensWriteParamPart extends SiemensParamPart {
	private List<SiemensWriteDataPart> dataParts = new ArrayList<SiemensWriteDataPart>();

	public List<SiemensWriteDataPart> getDataParts() {
		return dataParts;
	}

	public void setDataParts(List<SiemensWriteDataPart> dataParts) {
		this.dataParts = dataParts;
	}

	/**
	 * add one datapart to list of data parts.
	 * 
	 * @param dataParts
	 */
	public void addDataPart(SiemensWriteDataPart dataParts) {
		this.dataParts.add(dataParts);
	}
}
