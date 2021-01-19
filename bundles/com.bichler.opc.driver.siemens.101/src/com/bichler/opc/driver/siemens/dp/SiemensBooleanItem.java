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

public class SiemensBooleanItem extends SiemensDPItem {
	@Override
	public SiemensBooleanItem clone(SiemensDPItem clone) {
		return (SiemensBooleanItem) super.clone(new SiemensBooleanItem());
	}

	public SiemensBooleanItem() {
		this.length = 1; // length in byte
		this.dataKind = SiemensDataKind.BOOL;
		this.dataType = SiemensDataType.BOOL;
		// default
		this.maxReadByteCount = 221;
	}

	public int getLengthInBit() {
		return length;// * 8;
	}

	@Override
	public SiemensDataType getDataType() {
		if (arraylength > 1)
			return SiemensDataType.BYTE;
		return super.getDataType();
	}

	@Override
	public int getReadArraylength(int i) {
		if (i <= -1)
			return this.arraylength;
		int al = 1;
		if (arraylength > 1) {
			al = arraylength / 8;
			if (arraylength % 8 > 0)
				al++;
			// the array length is limited by maxBytecount
			if (maxReadByteCount * (i + 1) < al) {
				return maxReadByteCount;
			} else {
				return al - maxReadByteCount * i;
			}
		} else
			return 1;
	}

	@Override
	public int getWriteArraylength(int i) {
		int al = 1;
		if (arraylength > 1) {
			al = arraylength / 8;
			if (arraylength % 8 > 0)
				al++;
			if (i <= -1) {
				return al;
			} else {
				// the array length is limited by maxBytecount
				if (maxWriteByteCount * (i + 1) < al) {
					return maxWriteByteCount;
				} else {
					return al - maxWriteByteCount * i;
				}
			}
		} else
			return 1;
	}

	// @Override
	// public int getWriteArraylengthinByte(int i) {
	// int al = 1;
	// if (i <= -1) {
	// al = arraylength / 8;
	// if (arraylength % 8 > 0)
	// al++;
	// return al;
	// }
	//
	// else {
	// al = arraylength / 8;
	// if (arraylength % 8 > 0)
	// al++;
	//
	// // the array length is limited by maxBytecount
	// if (maxWriteByteCount * (i + 1) < al) {
	// return maxWriteByteCount;
	// } else {
	// return al - maxWriteByteCount * i;
	// }
	// }
	// }
	@Override
	public SiemensDataKind getDataKind() {
		if (arraylength > 1)
			return SiemensDataKind.BYTE;
		return super.getDataKind();
	}

	@Override
	public int getReadIndexInBit(int i) {
		if (i < 0)
			return ((int) this.index) * 8 + ((int) (this.index * 10)) % 10;
		return ((int) this.index) * 8 + ((int) (this.index * 10)) % 10 + maxReadByteCount * 8 * i;
	}

	@Override
	public int getWriteIndexInBit(int i) {
		if (i < 0)
			return ((int) this.index) * 8 + ((int) (this.index * 10)) % 10;
		return ((int) this.index) * 8 + +((int) (this.index * 10)) % 10 + maxWriteByteCount * 8 * i;
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
								+ this.nodeId + " - " + this.displayName);
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
				// we have an mapping between an byte array where each bit
				// represents an boolean element in opc ua
				// this.array = new Boolean[this.array.length];
				int index = 0;
				ComByteMessage m = new ComByteMessage();
				for (int i = 0; i < message.getBuffer().length; i++) {
					int bb = message.getBuffer()[i];
					for (int j = 0; j < 8; j++) {
						byte[] b = new byte[] { (byte) (bb & 0x1) };
						m.addBuffer(b);
						this.array[i * 8 + j] = this.transform.transToIntern(m);
						// this.array[i * 8 + j] = (bb & 0x1) == 1;
						bb = bb >> 1;
						index++;
						// break main loop if all bits transfered
						if (index >= arraylength) {
							break;
						}
					}
				}
				dv.setValue(new Variant(this.array));
			}
			dv.setStatusCode(StatusCode.GOOD);
		} catch (ClassCastException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Couldn't cast Siemens value to OPC UA Value of node: " + this.displayName);
			dv.setStatusCode(StatusCodes.Bad_TypeMismatch);
		} catch (ValueOutOfRangeException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage() + " for node: " + this.displayName);
			dv.setStatusCode(StatusCodes.Bad_OutOfRange);
		}
		return dv;
	}

	@Override
	public byte[] prog2DRV(DataValue val) throws ValueOutOfRangeException {
		byte[] ret = null;
		if (this.transform == null)
			return null;
		if (mapping == SIEMENS_MAPPING_TYPE.SCALAR) {
			ret = this.transform.transToDevice(val.getValue().getValue());
			return ret;
		} else if (mapping == SIEMENS_MAPPING_TYPE.ARRAY_ARRAY) {
			Object[] objects = (Object[]) val.getValue().getValue();
			int index = 0;
			// calculate array length of write array
			int len = this.arraylength / 8;
			if (this.arraylength % 8 != 0) {
				len++;
			}
			ret = new byte[len];
			int is = 0;
			// fill the whole array
			for (int i = 0; i < this.arraylength; i++) {
				byte[] b = this.transform.transToDevice(objects[i]);
				// if(objects[i] instanceof Boolean) {
				// if( (Boolean)(objects[i])) {
				if (b[0] == 1) {
					is = is | 1 << i % 8;
					// }
					// ret[i/8] = ret[i/8] | value;
				}
				if (((i + 1) % 8 == 0 && i > 0) || i == this.arraylength - 1) {
					ret[index] = (byte) is;
					index++;
					is = 0;
				}
				// value = this.transform.transToDevice(objects[i]);
			}
		}
		return ret;
	}

	/**
	 * get count how many messages will be requried to read the whole array
	 * 
	 * @return
	 */
	@Override
	public int getReadMessageCount() {
		if (this.arraylength > 1) {
			return (this.arraylength * this.getLength() / 8) / maxReadByteCount + 1;
		}
		return this.arraylength * this.getLength() / maxReadByteCount + 1;
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
}
