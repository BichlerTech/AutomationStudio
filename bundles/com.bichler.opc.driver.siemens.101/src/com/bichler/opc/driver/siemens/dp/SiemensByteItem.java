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

public class SiemensByteItem extends SiemensDPItem {
	public SiemensByteItem() {
		this.length = 8; // length in bit
		this.dataType = SiemensDataType.BYTE;
		this.dataKind = SiemensDataKind.BYTE;
		// default bytecount
		this.maxReadByteCount = 221;
	}

	@Override
	public SiemensByteItem clone(SiemensDPItem clone) {
		return (SiemensByteItem) super.clone(new SiemensByteItem());
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
	protected byte[] createDRVValueArray() {
		if (this.internArray == null) {
			return null;
		}
		byte[] temp = new byte[arraylength];
		for (int i = 0; i < arraylength; i++) {
			temp[i] = internArray[i][0];
		}
		return temp;
	}

	@Override
	public DataValue drv2Prog(byte[] data) {
		DataValue dv = new DataValue();
		// set timestamp that we did any work
		dv.setSourceTimestamp(new DateTime());
		try {
			if (this.transform == null) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"Couldn't transform plc value to opc ua value, no transfomation found for datapoint: "
								+ this.nodeId + " - " + this.getDisplayName());
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
				this.array = new Boolean[this.arraylength * 8];
				// verify both length
				if (this.arraylength != message.getBuffer().length) {
					String error = "";
					if (this.arraylength > message.getBuffer().length) {
						error = "OPC UA internal array is longer than siemens array! Node: ";
					} else {
						error = "OPC UA internal array is shorter than siemens array! Node: ";
					}
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, error + this.getDisplayName());
					dv.setStatusCode(StatusCodes.Bad_OutOfRange);
					return dv;
				}
				for (int i = 0; i < message.getBuffer().length; i++) {
					byte bb = message.getBuffer()[i];
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
}
