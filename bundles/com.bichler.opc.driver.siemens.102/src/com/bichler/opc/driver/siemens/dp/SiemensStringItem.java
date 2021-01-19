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

public class SiemensStringItem extends SiemensDPItem {
	@Override
	public void setArraylength(int length) {
		// add 2 for blank and bytecount of string
		super.setArraylength(length + 2);
	}

	@Override
	public SiemensStringItem clone(SiemensDPItem clone) {
		return (SiemensStringItem) super.clone(new SiemensStringItem());
	}

	/**
	 * gets the length of the array, if i > -1, we got a request from create message
	 * 
	 * @param i
	 * @return
	 */
	public int getArraylength(int i) {
		if (i == -2)
			return this.arraylength - 2;
		if (i == -1)
			return this.arraylength;
		// the array length is limited by maxBytecount
		if (maxReadByteCount * (i + 1) < this.arraylength) {
			return maxReadByteCount;
		} else {
			return this.arraylength - maxReadByteCount * i;
		}
	}

	public SiemensStringItem() {
		this.arraylength = 1024;
		this.length = 1; // length in byte
		this.maxReadByteCount = 221;
		this.dataKind = SiemensDataKind.STRING;
		this.dataType = SiemensDataType.STRING;
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
			} else if (mapping == SIEMENS_MAPPING_TYPE.SCALAR_ARRAY) {
				// we have an scalar array transformation for string
				// representation
				String input = "";
				int length = message.getBuffer()[1];
				for (int i = 2; i < this.arraylength; i++) {
					input += (char) message.getBuffer()[i];
					if (i > length)
						break;
				}
				input = input.replace("\n", "");
				dv.setValue(new Variant(input));
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
	public byte[] prog2DRV(DataValue val) throws ValueOutOfRangeException {
		byte[] b = new byte[this.getArraylength(-1)];
		if (this.transform == null)
			return null;
		if (mapping == SIEMENS_MAPPING_TYPE.SCALAR_ARRAY) {
			byte[] bytes = null;
			if (val.getValue().getValue() instanceof String) {
				bytes = val.getValue().getValue().toString().getBytes();
			}
			if (bytes != null) {
				// fill the whole array
				b[0] = 32;
				b[1] = 16;
				for (int i = 2; i < this.arraylength; i++) {
					if (i < bytes.length) {
						b[i] = bytes[i];
					}
				}
				// now create the target specific array
				// val.setStatusCode(this.transform.getStatusCode());
				return b;
			}
			throw new ValueOutOfRangeException("No data to write to PLC!");
			// Object[] objects = (Object[]) val.getValue().();
		} else {
			throw new ValueOutOfRangeException(
					"For OPC UA String to PLC String[] write only SCALAR_ARRAY mapping is possible");
		}
	}
}
