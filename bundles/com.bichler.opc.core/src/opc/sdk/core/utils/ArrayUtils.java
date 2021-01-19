package opc.sdk.core.utils;

/**
 * Utils to append arrays.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public class ArrayUtils {
	public static byte[] append(byte[] array1, byte[] array2) {
		int size = 0;
		if (array1 != null) {
			size += array1.length;
		}
		if (array2 != null) {
			size += array2.length;
		}
		byte[] appendedArray = new byte[size];
		int lastIndex = -1;
		if (array1 != null && array1.length > 0) {
			for (int ii = 0; ii < array1.length; ii++) {
				lastIndex = ii;
				appendedArray[ii] = array1[ii];
			}
		}
		if (array2 != null && array2.length > 0) {
			for (int ii = 0; ii < array2.length; ii++) {
				appendedArray[++lastIndex] = array2[ii];
			}
		}
		return appendedArray;
	}
}
