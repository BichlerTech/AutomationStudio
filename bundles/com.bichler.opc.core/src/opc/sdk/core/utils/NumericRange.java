package opc.sdk.core.utils;

import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.StatusCodes;

public class NumericRange extends org.opcfoundation.ua.utils.NumericRange {
	private NumericRange[] subranges;

	/**
	 * Checks if the defined range is empty.
	 * 
	 * @return true, if no indexes have been defined and Dimensions==1.
	 */
	@Override
	public boolean isEmpty() {
		return getDimensions() == 1 && getBegin() == -1 && this.subranges == null && this.subranges == null;
	}

	/**
	 * public methods
	 * 
	 * @return
	 */
	public int count() {
		if (this.getBegin() == -1) {
			return 0;
		}
		if (this.getEnd() == -1) {
			return 1;
		}
		return this.getEnd() - this.getBegin() + 1;
	}

	public NumericRange[] getSubranges() {
		return this.subranges;
	}

	public void setSubranges(NumericRange[] subranges) {
		this.subranges = subranges;
	}

	public static NumericRange getEmpty() {
		return new NumericRange();
	}

	/**
	 * Parses a string representing a numeric range.
	 * 
	 * @param textToParse
	 * @return numeric range
	 * @throws ServiceResultException in case the range is not in proper format
	 */
	public static NumericRange parse(String textToParse) throws ServiceResultException {
		if (textToParse == null || textToParse.length() == 0) {
			return NumericRange.getEmpty();
		}
		NumericRange range = new NumericRange();
		String[] dims = textToParse.split(",");
		range.setDimensions(dims.length);
		for (int d = 0; d < dims.length; d++)
			try {
				String dimStr = dims[d];
				String[] indexes = dimStr.split(":");
				if (indexes.length > 1) {
					range.setBegin(d, Integer.parseInt(indexes[0]));
					range.setEnd(d, Integer.parseInt(indexes[1]));
					if (range.getBegin(d) == range.getEnd(d))
						throw new IllegalArgumentException("Begin = End");
				} else {
					range.setBegin(d, Integer.parseInt(indexes[0]));
				}
			} catch (Exception e) {
				throw new ServiceResultException(StatusCodes.Bad_IndexRangeInvalid, e,
						"Cannot parse numeric range: " + textToParse + ".");
			}
		return range;
	}
}
