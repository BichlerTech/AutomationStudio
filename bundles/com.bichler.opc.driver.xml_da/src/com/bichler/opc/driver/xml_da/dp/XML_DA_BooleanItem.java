package com.bichler.opc.driver.xml_da.dp;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.webservices.XMLDA._1_0.ArrayOfBoolean;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_MAPPING_TYPE;

/**
 * @author hannes bichler
 * @company hb-softsolution e.u.
 * @version 0.0.9
 * 
 *          This class represents an target datapoint
 */
public class XML_DA_BooleanItem extends XML_DA_DPItem {
	/**
	 * internal array to fill it by write service and send it to xml-da
	 */
	private boolean[] internArray = null;
	private ArrayOfBoolean intern = new ArrayOfBoolean();

	/**
	 * set the length of the internal array
	 */
	@Override
	public void setArraylength(int arraylength) {
		super.setArraylength(arraylength);
		// now create also the xml-da array
		this.array = new Boolean[arraylength];
		this.internArray = new boolean[arraylength];
	}

	@Override
	protected Object createDRVValueArray() {
		for (int i = 0; i < arraylength; i++) {
			this.internArray[i] = ((Boolean) this.array[i]).booleanValue();
		}
		intern.set_boolean(internArray);
		return this.intern;
	}

	/**
	 * Take xml-da data and try to map it to opc-ua datavalue.
	 * 
	 * StatusCodes which will be passed to opc ua variable
	 * 
	 * @param take xml-da data and try to map it to opc-ua datavalue.
	 * 
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
						ArrayOfBoolean values = (ArrayOfBoolean) data;
						// verify opc ua array length and xml-da length
						if (values.get_boolean().length != this.arraylength) {
							String message = "";
							if (values.get_boolean().length > this.arraylength) {
								message = "OPC UA internal array is larger than xml-da array!";
							} else {
								message = "OPC UA internal array is shorter than xml-da array!";
							}
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, message + " Data point: "
									+ this.getDisplayname()/*
															 * , "Comet communication driver module" , 1,
															 * ComDRV.BUNDLEID, ComDRV.VERSIONID
															 */);
							this.value.setStatusCode(StatusCodes.Bad_OutOfRange);
							return this.value;
						}
						// create array of intern elements
						for (int i = 0; i < values.get_boolean().length; i++) {
							this.array[i] = this.transform.transToIntern(values.get_boolean(i));
						}
						this.value.setValue(new Variant(this.array));
					} catch (ClassCastException ex) {
						// we couldn't cast xml - da array to ArrayOfBoolean, so
						// log message and set statuscode Bad_TypeMismatch
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"Couldn't cast XML-DA value to OPC UA Value! Data point: " + this.getDisplayname()
										+ " Required datatype: ArrayOfBoolean received: "
										+ data.getClass().getSimpleName()/*
																			 * , "Comet communication driver module" ,
																			 * CometModuls.INT_DRV, ComDRV.BUNDLEID,
																			 * ComDRV.VERSIONID
																			 */);
						this.value.setStatusCode(StatusCodes.Bad_TypeMismatch);
						return value;
					}
				}
				// no error so set statuscode
				this.value.setStatusCode(StatusCode.GOOD);
			}
		} catch (ClassCastException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Couldn't cast XML-DA value to OPC UA Value! Data point: " + this.getDisplayname());
		} catch (ValueOutOfRangeException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage() + " " + this.getDisplayname());
			this.value.setStatusCode(StatusCodes.Bad_OutOfRange);
		}
		return value;
	}
}
