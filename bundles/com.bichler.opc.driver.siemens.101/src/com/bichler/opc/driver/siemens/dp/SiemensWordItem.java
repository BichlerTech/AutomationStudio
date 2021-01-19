package com.bichler.opc.driver.siemens.dp;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.communication.SiemensDataKind;
import com.bichler.opc.driver.siemens.communication.SiemensDataType;
import com.bichler.opc.driver.siemens.transform.SIEMENS_MAPPING_TYPE;
import com.bichler.opc.driver.siemens.transform.word.SiemensWordTransform2Integer;

public class SiemensWordItem extends SiemensDPItem {
	public SiemensWordItem() {
		this.length = 16; // length in bit
		this.dataType = SiemensDataType.WORD;
		this.dataKind = SiemensDataKind.WORD;
		this.transform = new SiemensWordTransform2Integer();
		this.maxReadByteCount = 111;
	}

	@Override
	public SiemensWordItem clone(SiemensDPItem clone) {
		return (SiemensWordItem) super.clone(new SiemensWordItem());
	}

	@Override
	public int getReadIndexInBit(int i) {
		if (i < 0)
			return ((int) this.index) * 8;
		return ((int) this.index) * 8 + maxReadByteCount * 8 * i;
	}

	@Override
	public int getWriteIndexInBit(int i) {
		if (i < 0)
			return ((int) this.index) * 8;
		return ((int) this.index) * 8 + maxWriteByteCount * 8 * i;
	}

	@Override
	public int getLength() {
		return length / 8;
	}

	@Override
	public int getLengthInBit() {
		return length;
	}

	@Override
	public DataValue drv2Prog(byte[] data) {
		DataValue dv = new DataValue();
		// set timestamp that we did any work
		dv.setSourceTimestamp(new DateTime());
		try {
			if (this.transform == null) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"Couldn't transform plc value to opc ua value, no transfomation found for node: " + this.nodeId
								+ " - " + this.displayName);
				dv.setStatusCode(StatusCodes.Bad_DataEncodingInvalid);
				return dv;
			}
			/**
			 * now create message to delete read bytes
			 */
			ComByteMessage message = new ComByteMessage();
			message.setBuffer(data);
			if (mapping == SIEMENS_MAPPING_TYPE.SCALAR) {
				// we have an scalar transformation
				Object obj = this.transform.transToIntern(message);
				dv.setValue(new Variant(obj));
			} else if (mapping == SIEMENS_MAPPING_TYPE.ARRAY_ARRAY) {
				this.array = this.transform.createInternArray(arraylength);
				// we have an array transformation
				for (int i = 0; i < this.arraylength; i++) {
					this.array[i] = this.transform.transToIntern(message);
				}
				dv.setValue(new Variant(this.array));
			} else if (mapping == SIEMENS_MAPPING_TYPE.ALARM) {
				// we have an mapping between an byte array where each bit
				// represents an boolean element in opc ua
				this.array = new Boolean[this.array.length];
				// verify both length
				if (this.array.length != message.getBuffer().length * 16) {
					String error = "";
					if (message.getBuffer().length * 16 > this.arraylength) {
						error = "OPC UA internal array is longer than xml-da array!";
					} else {
						error = "OPC UA internal array is shorter than xml-da array!";
					}
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, error + " Node: " + this.displayName);
					dv.setStatusCode(StatusCodes.Bad_OutOfRange);
					return dv;
				}
				byte bb = 0;
				for (int i = 0; i < message.getBuffer().length; i += 2) {
					bb = message.getBuffer()[i];
					this.array[i * 8 + 8] = (bb & 0x01) == 0x01;
					this.array[i * 8 + 9] = (bb & 0x02) == 0x02;
					this.array[i * 8 + 10] = (bb & 0x04) == 0x04;
					this.array[i * 8 + 11] = (bb & 0x08) == 0x08;
					this.array[i * 8 + 12] = (bb & 0x10) == 0x10;
					this.array[i * 8 + 13] = (bb & 0x20) == 0x20;
					this.array[i * 8 + 14] = (bb & 0x40) == 0x40;
					this.array[i * 8 + 15] = (bb & 0x80) == 0x80;
					bb = message.getBuffer()[i + 1];
					this.array[i * 8] = (bb & 0x01) == 0x01;
					this.array[i * 8 + 1] = (bb & 0x02) == 0x02;
					this.array[i * 8 + 2] = (bb & 0x04) == 0x04;
					this.array[i * 8 + 3] = (bb & 0x08) == 0x08;
					this.array[i * 8 + 4] = (bb & 0x10) == 0x10;
					this.array[i * 8 + 5] = (bb & 0x20) == 0x20;
					this.array[i * 8 + 6] = (bb & 0x40) == 0x40;
					this.array[i * 8 + 7] = (bb & 0x80) == 0x80;
				}
				dv.setValue(new Variant(this.array));
			}
			dv.setStatusCode(StatusCode.GOOD);
		} catch (ClassCastException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Couldn't cast Siemens value to OPC UA Value! Node: " + this.displayName);
			dv.setStatusCode(StatusCodes.Bad_TypeMismatch);
		} catch (ValueOutOfRangeException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage() + " Node: " + this.displayName);
			dv.setStatusCode(StatusCodes.Bad_OutOfRange);
		}
		return dv;
	}

	@Override
	protected byte[] createDRVValueArray() {
		if (this.internArray == null) {
			return null;
		}
		byte[] temp = new byte[2 * arraylength];
		int index = 0;
		for (int i = 0; i < arraylength; i++) {
			temp[index] = internArray[i][0];
			temp[index + 1] = internArray[i][1];
			index += 2;
		}
		return temp;
	}
}
