package opc.client.application.core.dataaccess;

import org.opcfoundation.ua.builtintypes.LocalizedText;

/**
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 *
 */
public enum DADisplayNames {
	EURANGE, INSTRUMENTRANGE, DEFINITION, VALUEPRECISION, ENGINEERINGUNITS, ENUMSTRINGS, FALSESTATE, TRUESTATE;

	public static DADisplayNames getFromName(LocalizedText propertyName) {
		if (propertyName == null || propertyName.getText() == null) {
			return null;
		}
		for (DADisplayNames v : values()) {
			if (v.name().equals(propertyName.getText())) {
				return v;
			}
		}
		return null;
	}
}
