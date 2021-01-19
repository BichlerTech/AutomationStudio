package com.bichler.opc.comdrv.utils;

public class ComByteMessage {
	private byte[] buffer = new byte[0];

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}

	public void deleteFirstBytes(int count) {
		byte[] tmp = new byte[buffer.length - count];
		System.arraycopy(buffer, count, tmp, 0, buffer.length - count);
		buffer = tmp;
	}

	public void addBuffer(byte[] buffer) {
		byte[] tmp = new byte[this.buffer.length];
		// copy this.buffer content into temporary element
		System.arraycopy(this.buffer, 0, tmp, 0, this.buffer.length);
		this.buffer = new byte[tmp.length + buffer.length];
		System.arraycopy(tmp, 0, this.buffer, 0, tmp.length);
		System.arraycopy(buffer, 0, this.buffer, tmp.length, buffer.length);
	}
}
