package com.bichler.opc.driver.siemens.dp;

import java.util.ArrayList;

public class SiemensPackageList<E> extends ArrayList<E> {
	/**
	 * 
	 */
	public static final long serialVersionUID = 5267365308402669886L;
	private long lastRead = 0;

	public long getLastRead() {
		return lastRead;
	}

	public void setLastRead(long lastRead) {
		this.lastRead = lastRead;
	}
}
