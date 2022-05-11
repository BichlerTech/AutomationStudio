package com.bichler.opc.driver.ethernet_ip.transform.boolean_;

import java.util.logging.Level;
import org.opcfoundation.ua.builtintypes.Variant;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Boolean;
import etherip.types.CIPData;
import etherip.types.CIPData.Type;

/**
 * this class transforms a siemens boolean representation into opc ua boolean
 * representation we take the required bit from byte message
 * 
 * @author applemc207da
 *
 */
public class EthernetIPBooleanTransform2Boolean extends EthernetIPTransform2Boolean {
	
	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		boolean val = (Boolean) value;

		if (val)
			data.set(index, 0xFF);
		else
			data.set(index, 0);
		return;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.BOOL, array);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Variant getDefaultValue() {
		return new Variant(Boolean.FALSE);
	}

	@Override
	public Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException {
		Number val = null;
		if (value == null) {
			throw new ValueOutOfRangeException("Value can not be transformed into boolean represenation");
		}
		try {
			val = value.getNumber(index);
		} catch (Exception e) {
			throw new ValueOutOfRangeException("Value can not be transformed into boolean represenation");
		}
		logger.log(Level.FINE, "Transform Bool to Bool - value: " + val.intValue());
		return val.intValue() != 0;
	}
}
