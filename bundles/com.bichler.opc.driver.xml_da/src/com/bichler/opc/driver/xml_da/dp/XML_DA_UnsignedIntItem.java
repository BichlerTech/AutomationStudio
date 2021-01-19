package com.bichler.opc.driver.xml_da.dp;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.axis.types.UnsignedInt;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.webservices.XMLDA._1_0.ArrayOfUnsignedInt;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_MAPPING_TYPE;

public class XML_DA_UnsignedIntItem extends XML_DA_DPItem {
	/**
	 * set the length of the internal array
	 */
	@Override
	public void setArraylength(int arraylength) {
		super.setArraylength(arraylength);
		// now create also the xml-da array
		this.array = new UnsignedInteger[arraylength];
		this.internArray = new UnsignedInt[arraylength];
	}

	private UnsignedInt[] internArray = null;
	private ArrayOfUnsignedInt intern = new ArrayOfUnsignedInt();

	protected Object[] createValueArray() {
		return new UnsignedInteger[arraylength];
	}

	@Override
	protected Object createDRVValueArray() {
		for (int i = 0; i < arraylength; i++) {
			this.internArray[i] = new UnsignedInt(((UnsignedInteger) this.array[i]).longValue());
		}
		intern.setUnsignedInt(internArray);
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
				this.value.setSourceTimestamp(new DateTime());
				if (mapping == XML_DA_MAPPING_TYPE.SCALAR) {
					this.value.setValue(new Variant(this.transform.transToIntern(data)));
				} else if (mapping == XML_DA_MAPPING_TYPE.ARRAY_ARRAY) {
					try {
						ArrayOfUnsignedInt values = (ArrayOfUnsignedInt) data;
						// verify opc ua array length and xml-da length
						if (values.getUnsignedInt().length != this.arraylength) {
							String message = "";
							if (values.getUnsignedInt().length > this.arraylength) {
								message = "OPC UA internal array is longer than xml-da array!";
							} else {
								message = "OPC UA internal array is shorter than xml-da array!";
							}
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, message + " Data point: "
									+ this.getDisplayname()/*
															 * , "Comet communication driver module" , CometModuls.
															 * INT_DRV, ComDRV.BUNDLEID, ComDRV.VERSIONID
															 */);
							this.value.setStatusCode(StatusCodes.Bad_OutOfRange);
							return this.value;
						}
						// create array of intern elements
						for (int i = 0; i < values.getUnsignedInt().length; i++) {
							this.array[i] = this.transform.transToIntern(values.getUnsignedInt(i));
						}
						this.value.setValue(new Variant(this.array));
					} catch (ClassCastException ex) {
						// we couldn't cast xml - da array to
						// ArrayOfUnsignedInt, so
						// log message and set statuscode Bad_TypeMismatch
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"Couldn't cast XML-DA value to OPC UA Value! Data point: " + this.getDisplayname()
										+ " Required datatype: ArrayOfUnsignedInt Received: "
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
					// represents an boolean element
					//
					ArrayOfUnsignedInt values = (ArrayOfUnsignedInt) data;
					for (int i = 0; i < values.getUnsignedInt().length; i += 32) {
						long bb = values.getUnsignedInt(i).longValue();
						this.array[i] = (bb & 0x1) == 1;
						this.array[i + 1] = (bb & 0x2) == 2;
						this.array[i + 2] = (bb & 0x4) == 4;
						this.array[i + 3] = (bb & 0x8) == 8;
						this.array[i + 4] = (bb & 0x16) == 16;
						this.array[i + 5] = (bb & 0x32) == 32;
						this.array[i + 6] = (bb & 0x64) == 64;
						this.array[i + 7] = (bb & 0x128) == 128;
						this.array[i + 8] = (bb & 0x256) == 256;
						this.array[i + 9] = (bb & 0x512) == 512;
						this.array[i + 10] = (bb & 0x1024) == 1024;
						this.array[i + 11] = (bb & 0x2048) == 2048;
						this.array[i + 12] = (bb & 0x4096) == 4096;
						this.array[i + 13] = (bb & 0x8192) == 8192;
						this.array[i + 14] = (bb & 0x16384) == 16384;
						this.array[i + 15] = (bb & 0x32768) == 32768;
						// divide number by 65536
						bb /= 65536;
						this.array[i + 16] = (bb & 0x1) == 1;
						this.array[i + 17] = (bb & 0x2) == 2;
						this.array[i + 18] = (bb & 0x4) == 4;
						this.array[i + 19] = (bb & 0x8) == 8;
						this.array[i + 20] = (bb & 0x16) == 16;
						this.array[i + 21] = (bb & 0x32) == 32;
						this.array[i + 22] = (bb & 0x64) == 64;
						this.array[i + 23] = (bb & 0x128) == 128;
						this.array[i + 24] = (bb & 0x256) == 256;
						this.array[i + 25] = (bb & 0x512) == 512;
						this.array[i + 26] = (bb & 0x1024) == 1024;
						this.array[i + 27] = (bb & 0x2048) == 2048;
						this.array[i + 28] = (bb & 0x4096) == 4096;
						this.array[i + 29] = (bb & 0x8192) == 8192;
						this.array[i + 30] = (bb & 0x16384) == 16384;
						this.array[i + 31] = (bb & 0x32768) == 32768;
					}
				}
				this.value.setStatusCode(StatusCode.GOOD);
			}
		} catch (ClassCastException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Couldn't cast XML-DA value to OPC UA Value! Data point: " + " "
							+ this.getDisplayname()/*
													 * , CometModuls.INT_DRV, ComDRV.BUNDLEID, ComDRV.VERSIONID
													 */);
			this.value.setStatusCode(StatusCodes.Bad_TypeMismatch);
		} catch (ValueOutOfRangeException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage() + " "
					+ this.getDisplayname()/*
											 * , CometModuls.INT_DRV, ComDRV.BUNDLEID, ComDRV.VERSIONID
											 */);
			this.value.setStatusCode(StatusCodes.Bad_OutOfRange);
		}
		return value;
	}
}
