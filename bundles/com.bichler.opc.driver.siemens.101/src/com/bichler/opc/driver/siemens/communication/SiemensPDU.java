package com.bichler.opc.driver.siemens.communication;

import com.bichler.opc.comdrv.utils.ComByteMessage;

public class SiemensPDU {
	/**
	 * start flag for each message, value is not defined
	 */
	private byte start = 0;
	private SiemensPDUType type = SiemensPDUType.UNKNOWN;
	private byte[] reserved = new byte[] { 0, 0 };
	/**
	 * sequence number
	 */
	private int sequence = 0;
	/**
	 * parameter length (in byte)
	 */
	private int parameterlength = 0;
	/**
	 * data length (in byte)
	 */
	private int datalength = 0;
	/**
	 * error code (only available in PDU type 2 and 3)
	 */
	private int errorcode = 0;
	private SiemensParamPart param = null;
	private SiemensDataPart data = null;

	public byte getStart() {
		return start;
	}

	public void setStart(byte start) {
		this.start = start;
	}

	public SiemensPDUType getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = SiemensPDUType.fromValue(type);
	}

	public byte[] getReserved() {
		return reserved;
	}

	public void setReserved(byte[] reserved) {
		this.reserved = reserved;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public void setSequence(byte[] sequence) {
		int i = sequence[0];
		int k = sequence[1];
		if (i < 0)
			i += 256;
		if (k < 0)
			k += 256;
		this.sequence = (256 * i + k);
	}

	public int getParameterlength() {
		return parameterlength;
	}

	public void setParameterlength(byte[] parameterlength) {
		int i = parameterlength[0];
		int k = parameterlength[1];
		if (i < 0)
			i += 256;
		if (k < 0)
			k += 256;
		this.parameterlength = (256 * i + k);
	}

	public void setParameterlength(int parameterlength) {
		this.parameterlength = parameterlength;
	}

	public int getDatalength() {
		return datalength;
	}

	public void setDatalength(int datalength) {
		this.datalength = datalength;
	}

	public void setDatalength(byte[] datalength) {
		int i = datalength[0];
		int k = datalength[1];
		if (i < 0)
			i += 256;
		if (k < 0)
			k += 256;
		this.datalength = (256 * i + k);
	}

	public int getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}

	public void setErrorcode(byte[] errorcode) {
		int i = errorcode[0];
		int k = errorcode[1];
		if (i < 0)
			i += 256;
		if (k < 0)
			k += 256;
		this.errorcode = (256 * i + k);
	}

	/**
	 * validates a message if it is a correct incoming response
	 * 
	 * @param message
	 * @return
	 */
	public int validate(ComByteMessage message) {
		return -1;
	}

	public SiemensParamPart getParam() {
		return param;
	}

	public void setParam(SiemensParamPart param) {
		this.param = param;
	}

	public SiemensDataPart getData() {
		return data;
	}

	public void setData(SiemensDataPart data) {
		this.data = data;
	}
}
