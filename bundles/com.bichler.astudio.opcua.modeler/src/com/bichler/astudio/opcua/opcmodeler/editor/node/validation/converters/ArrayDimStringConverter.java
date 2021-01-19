package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.richclientgui.toolbox.validation.converter.IContentsStringConverter;

public class ArrayDimStringConverter implements IContentsStringConverter<UnsignedInteger[]> {
	@Override
	public UnsignedInteger[] convertFromString(String arrayDim) {
		// int length = -1;
		if (arrayDim.isEmpty()) {
			return new UnsignedInteger[0];
		} else {
			// TODO we have to check for illegal input
			if (arrayDim.startsWith(",")) {
				arrayDim = arrayDim.replaceFirst(",", "");
			}
			if (arrayDim.endsWith(",")) {
				arrayDim = arrayDim.substring(0, arrayDim.length() - 1);
			}
			String[] array = arrayDim.split(",");
			UnsignedInteger[] ints = new UnsignedInteger[array.length];
			try {
				for (int i = 0; i < array.length; i++) {
					ints[i] = new UnsignedInteger(array[i]);
				}
			} catch (IllegalArgumentException ex) {
				ints = new UnsignedInteger[0];
			}
			return ints;
			// length = Integer.parseInt(arrayDim);
		}
	}

	@Override
	public String convertToString(UnsignedInteger[] arrayDim) {
		String converted = "";
		String comma = "";
		if (arrayDim == null) {
			return "";
		}
		for (UnsignedInteger item : arrayDim) {
			converted = converted + comma + item;
			comma = ",";
		}
		return converted;
	}
}
