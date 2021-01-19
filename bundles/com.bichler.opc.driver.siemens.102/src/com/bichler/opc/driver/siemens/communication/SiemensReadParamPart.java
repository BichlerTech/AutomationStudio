package com.bichler.opc.driver.siemens.communication;

import java.util.ArrayList;
import java.util.List;

public class SiemensReadParamPart extends SiemensParamPart {
	private List<SiemensReadDataPart> dataParts = new ArrayList<SiemensReadDataPart>();

	public List<SiemensReadDataPart> getDataParts() {
		return dataParts;
	}

	public void setDataParts(List<SiemensReadDataPart> dataParts) {
		this.dataParts = dataParts;
	}

	/**
	 * add one datapart to list of data parts.
	 * 
	 * @param dataParts
	 */
	public void addDataPart(SiemensReadDataPart dataParts) {
		this.dataParts.add(dataParts);
	}
}
