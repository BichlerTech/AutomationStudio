package com.bichler.opc.driver.siemens.communication;

public class SiemensTPDUHeader {
	/**
	 * ISO-TCP header
	 */
	private SiemensISOTCPHeader header = null;
	/**
	 * length (in byte) of TPDU header (without this byte and possible user data)
	 */
	private byte length = 0;
	/**
	 * DT code (1111) & credit (always 0000)
	 */
	private byte code = 0;
	/**
	 * TPDU number & EOT
	 */
	private byte number = 0;
	private SiemensPDU pdu = null;

	public SiemensISOTCPHeader getHeader() {
		return header;
	}

	public void setHeader(SiemensISOTCPHeader header) {
		this.header = header;
	}

	public byte getLength() {
		return length;
	}

	public void setLength(byte length) {
		this.length = length;
	}

	public byte getCode() {
		return code;
	}

	public void setCode(byte code) {
		this.code = code;
	}

	public byte getNumber() {
		return number;
	}

	public void setNumber(byte number) {
		this.number = number;
	}

	public SiemensPDU getPdu() {
		return pdu;
	}

	public void setPdu(SiemensPDU pdu) {
		this.pdu = pdu;
	}
}
