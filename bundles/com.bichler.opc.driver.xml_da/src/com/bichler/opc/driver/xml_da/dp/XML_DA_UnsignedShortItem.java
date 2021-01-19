package com.bichler.opc.driver.xml_da.dp;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.webservices.XMLDA._1_0.ArrayOfUnsignedShort;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_MAPPING_TYPE;

public class XML_DA_UnsignedShortItem extends XML_DA_DPItem {
	/**
	 * set the length of the internal array
	 */
	@Override
	public void setArraylength(int arraylength) {
		super.setArraylength(arraylength);
		// now create also the xml-da array
		this.array = new UnsignedShort[arraylength];
		this.internArray = new org.apache.axis.types.UnsignedShort[arraylength];
	}

	private org.apache.axis.types.UnsignedShort[] internArray = null;
	private ArrayOfUnsignedShort intern = new ArrayOfUnsignedShort();

	@Override
	protected Object createDRVValueArray() {
		for (int i = 0; i < arraylength; i++) {
			this.internArray[i] = new org.apache.axis.types.UnsignedShort(
					((UnsignedInteger) this.array[i]).longValue());
		}
		intern.setUnsignedShort(internArray);
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
						ArrayOfUnsignedShort values = (ArrayOfUnsignedShort) data;
						// verify opc ua array length and xml-da length
						if (values.getUnsignedShort().length != this.arraylength) {
							String message = "";
							if (values.getUnsignedShort().length > this.arraylength) {
								message = "OPC UA internal array is longer than xml-da array!";
							} else {
								message = "OPC UA internal array is shorter than xml-da array!";
							}
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, message + " "
									+ this.getDisplayname()/*
															 * , CometModuls. INT_DRV, ComDRV.BUNDLEID, ComDRV.VERSIONID
															 */);
							this.value.setStatusCode(StatusCodes.Bad_OutOfRange);
							return this.value;
						}
						// create array of intern elements
						for (int i = 0; i < values.getUnsignedShort().length; i++) {
							this.array[i] = this.transform.transToIntern(values.getUnsignedShort(i));
						}
						this.value.setValue(new Variant(this.array));
					} catch (ClassCastException ex) {
						// we couldn't cast xml - da array to
						// ArrayOfUnsignedShort, so
						// log message and set statuscode Bad_TypeMismatch
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"Couldn't cast XML-DA value to OPC UA Value " + this.getDisplayname()
										+ " required datatype: ArrayOfUnsignedShort but we got: "
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
					ArrayOfUnsignedShort values = (ArrayOfUnsignedShort) data;
					// verify both length
					if (this.array.length != values.getUnsignedShort().length * 16) {
						String message = "";
						if (values.getUnsignedShort().length * 16 > this.arraylength) {
							message = "OPC UA internal array is longer than xml-da array!";
						} else {
							message = "OPC UA internal array is shorter than xml-da array!";
						}
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, message + " "
								+ this.getDisplayname()/*
														 * , CometModuls.INT_DRV, ComDRV.BUNDLEID, ComDRV.VERSIONID
														 */);
						this.value.setStatusCode(StatusCodes.Bad_OutOfRange);
						return this.value;
					}
					for (int i = 0; i < values.getUnsignedShort().length; i += 16) {
						short bb = values.getUnsignedShort(i).shortValue();
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
					}
				}
				this.value.setStatusCode(StatusCode.GOOD);
			}
		} catch (ClassCastException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Couldn't cast XML-DA value to OPC UA Value"/*
																 * , this. getDisplayname (), CometModuls. INT_DRV,
																 * ComDRV. BUNDLEID, ComDRV. VERSIONID
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
