package com.bichler.opc.driver.siemens.communication;

public class SiemensISOTCPHeader {
	private byte version = 0x03;
	private byte reserved = 0x00;
	private byte[] messagesize = new byte[2];

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public byte getReserved() {
		return reserved;
	}

	public void setReserved(byte reserved) {
		this.reserved = reserved;
	}

	public byte[] getMessagesizeinBytes() {
		return messagesize;
	}

	public int getMessagesize() {
		int i = messagesize[0];
		int j = messagesize[1];
		if (i < 0)
			i += 256;
		if (j < 0)
			j += 256;
		return i * 256 + j;
	}

	public void setMessagesize(byte[] messagesize) {
		this.messagesize = messagesize;
	}

	public void setMessagesize(int size) {
		size += 4;
		this.messagesize[0] = (byte) (size / 0x100);
		this.messagesize[1] = (byte) (size % 0x100);
	}

	/**
	 * get the header as byte array
	 * 
	 * @return
	 */
	public byte[] toBytes() {
		byte[] bytes = new byte[4];
		bytes[0] = version;
		bytes[1] = reserved;
		bytes[2] = messagesize[0];
		bytes[3] = messagesize[1];
		return bytes;
	}
}
