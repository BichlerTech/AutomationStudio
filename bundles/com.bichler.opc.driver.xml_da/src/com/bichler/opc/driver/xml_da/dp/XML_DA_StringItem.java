package com.bichler.opc.driver.xml_da.dp;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.webservices.XMLDA._1_0.ArrayOfString;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_MAPPING_TYPE;

public class XML_DA_StringItem extends XML_DA_DPItem {
	/**
	 * set the length of the internal array
	 */
	@Override
	public void setArraylength(int arraylength) {
		super.setArraylength(arraylength);
		// now create also the xml-da array
		this.array = new String[arraylength];
		this.internArray = new String[arraylength];
	}

	private String[] internArray = null;
	private ArrayOfString intern = new ArrayOfString();

	@Override
	protected Object createDRVValueArray() {
		for (int i = 0; i < arraylength; i++) {
			this.internArray[i] = ((String) this.array[i]).toString();
		}
		intern.setString(internArray);
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
						ArrayOfString values = (ArrayOfString) data;
						// verify opc ua array length and xml-da length
						if (values.getString().length != this.arraylength) {
							String message = "";
							if (values.getString().length > this.arraylength) {
								message = "OPC UA internal array is longer than xml-da array!";
							} else {
								message = "OPC UA internal array is shorter than xml-da array!";
							}
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, message + " Data point: "
									+ this.getDisplayname()/*
															 * , "Comet communication driver module",
															 * CometModuls.INT_DRV, ComDRV.BUNDLEID, ComDRV.VERSIONID
															 */);
							this.value.setStatusCode(StatusCodes.Bad_OutOfRange);
							return this.value;
						}
						// create array of intern elements
						for (int i = 0; i < values.getString().length; i++) {
							this.array[i] = this.transform.transToIntern(values.getString(i));
						}
						this.value.setValue(new Variant(this.array));
					} catch (ClassCastException ex) {
						// we couldn't cast xml - da array to ArrayOfString, so
						// log message and set statuscode Bad_TypeMismatch
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"Couldn't cast XML-DA value to OPC UA Value! Data point: " + this.getDisplayname()
										+ " required datatype: ArrayOfString but we got: "
										+ data.getClass().getSimpleName()/*
																			 * , "Comet communication driver module",
																			 * CometModuls.INT_DRV, ComDRV.BUNDLEID,
																			 * ComDRV.VERSIONID
																			 */);
						this.value.setStatusCode(StatusCodes.Bad_TypeMismatch);
						return value;
					}
				}
				this.value.setStatusCode(StatusCode.GOOD);
			}
		} catch (ClassCastException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Couldn't cast XML-DA value to OPC UA Value! Data point: "
							+ this.getDisplayname()/*
													 * , "Comet communication driver module", CometModuls.INT_DRV,
													 * ComDRV.BUNDLEID, ComDRV.VERSIONID
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
