package com.bichler.opc.driver.xml_da;

import java.util.ArrayList;

public class XML_DA_ItemList<E> extends ArrayList<E> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1462830964295982944L;
	private long lastRead = 0;

	public long getLastRead() {
		return lastRead;
	}

	public void setLastRead(long lastRead) {
		this.lastRead = lastRead;
	}
}
