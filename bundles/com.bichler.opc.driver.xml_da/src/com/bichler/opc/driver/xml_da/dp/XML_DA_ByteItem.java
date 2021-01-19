package com.bichler.opc.driver.xml_da.dp;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.webservices.XMLDA._1_0.ArrayOfByte;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_MAPPING_TYPE;

public class XML_DA_ByteItem extends XML_DA_DPItem {
	/**
	 * set the length of the internal array
	 */
	@Override
	public void setArraylength(int arraylength) {
		super.setArraylength(arraylength);
		// now create also the xml-da array
		this.array = new Byte[arraylength];
		this.internArray = new byte[arraylength];
	}

	private byte[] internArray = null;
	private ArrayOfByte intern = new ArrayOfByte();

	@Override
	protected Object createDRVValueArray() {
		for (int i = 0; i < arraylength; i++) {
			this.internArray[i] = ((Byte) this.array[i]).byteValue();
		}
		intern.set_byte(internArray);
		return this.intern;
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	public DataValue drv2Prog(Object data) {
		try {
			if (this.transform != null) {
				// first set timestamp to to data value
				this.value.setSourceTimestamp(new DateTime());
				if (mapping == XML_DA_MAPPING_TYPE.SCALAR) {
					this.value.setValue(new Variant(this.transform.transToIntern(data)));
				} else if (mapping == XML_DA_MAPPING_TYPE.ARRAY_ARRAY) {
					try {
						byte[] values = null;
						// try to cast an byte array or an ArrayOfByte(xml-da
						// specific)
						// siemens simotion always use byte array instead of
						// ArrayOfBytes)
						try {
							values = (byte[]) data;
						} catch (ClassCastException ex) {
							// we couldn't cast data to byte array, so we try to
							// cast it to ArrayOfByte
							values = ((ArrayOfByte) data).get_byte();
						}
						if (values.length != this.arraylength) {
							String message = "";
							if (values.length > this.arraylength) {
								message = "OPC UA internal array is longer than xml-da array!";
							} else {
								message = "OPC UA internal array is shorter than xml-da array!";
							}
							Logger.getLogger(getClass().getName()).log(Level.SEVERE,
									message + " Data point: " + this.getDisplayname());
							this.value.setStatusCode(StatusCodes.Bad_OutOfRange);
							return this.value;
						}
						// create array of intern elements
						for (int i = 0; i < values.length; i++) {
							this.array[i] = this.transform.transToIntern(values[i]);
						}
						this.value.setValue(new Variant(this.array));
					} catch (ClassCastException ex) {
						// we couldn't cast xml - da array to ArrayOfByte, so
						// log message and set statuscode Bad_TypeMismatch
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"Couldn't cast XML-DA value to OPC UA Value! Data point: " + this.getDisplayname()
										+ " Required datatype: ArrayOfByte or []byte received: "
										+ data.getClass().getSimpleName()/*
																			 * , "Comet communication driver module" ,
																			 * CometModuls.INT_DRV, ComDRV.BUNDLEID,
																			 * ComDRV.VERSIONID
																			 */);
						this.value.setStatusCode(StatusCodes.Bad_TypeMismatch);
						return value;
					}
				} else if (mapping == XML_DA_MAPPING_TYPE.ALARM_MAPPING) {
					// we have an mapping between an byte array where each bit
					// represents an boolean element in opc ua
					byte[] values = null;
					// try to cast an byte array or an ArrayOfBytes(xml-da
					// specific)
					// siemens simotion always use byte array instead of
					// ArrayOfBytes)
					try {
						values = (byte[]) data;
					} catch (ClassCastException ex) {
						// we couldn't cast data to byte array, so we try to
						// cast it to ArrayOfByte
						values = ((ArrayOfByte) data).get_byte();
					}
					this.array = new Boolean[this.array.length];
					// verify both length
					if (this.array.length != values.length * 8) {
						String message = "";
						if (values.length * 8 > this.arraylength) {
							message = "OPC UA internal array is longer than xml-da array!";
						} else {
							message = "OPC UA internal array is shorter than xml-da array!";
						}
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, message + " Data point: "
								+ this.getDisplayname()/*
														 * , "Comet communication driver module" , CometModuls.INT_DRV,
														 * ComDRV.BUNDLEID, ComDRV.VERSIONID
														 */);
						this.value.setStatusCode(StatusCodes.Bad_OutOfRange);
						return this.value;
					}
					for (int i = 0; i < values.length; i++) {
						byte bb = values[i];
						this.array[i * 8] = (bb & 0x1) == 1;
						this.array[i * 8 + 1] = (bb & 0x2) == 2;
						this.array[i * 8 + 2] = (bb & 0x4) == 4;
						this.array[i * 8 + 3] = (bb & 0x8) == 8;
						this.array[i * 8 + 4] = (bb & 0x16) == 16;
						this.array[i * 8 + 5] = (bb & 0x32) == 32;
						this.array[i * 8 + 6] = (bb & 0x64) == 64;
						this.array[i * 8 + 7] = (bb & 0x128) == 128;
					}
					this.value.setValue(new Variant(this.array));
				}
				this.value.setStatusCode(StatusCode.GOOD);
			}
		} catch (ClassCastException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Couldn't cast XML-DA value to OPC UA Value! Data point: "
							+ this.getDisplayname()/*
													 * , "Comet communication driver module" , CometModuls.INT_DRV,
													 * ComDRV.BUNDLEID, ComDRV.VERSIONID
													 */);
			this.value.setStatusCode(StatusCodes.Bad_TypeMismatch);
		} catch (ValueOutOfRangeException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					e.getMessage()/*
									 * , this.getDisplayname(), CometModuls.INT_DRV, ComDRV.BUNDLEID,
									 * ComDRV.VERSIONID
									 */);
			this.value.setStatusCode(StatusCodes.Bad_OutOfRange);
		}
		return value;
	}
}
