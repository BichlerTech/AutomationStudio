package opc.sdk.core.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.opcfoundation.ua.builtintypes.Enumeration;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

/**
 * Constants defined for the ValueRank attribute.
 * 
 * @author Thomas Z&ouml;bauer
 */
public enum ValueRanks implements Enumeration {
	/**
	 * The variable may be a scalar or a one dimensional array.
	 */
	ScalarOrOneDimension(-3),
	/**
	 * The variable may be a scalar or an array of any dimension.
	 */
	Any(-2),
	/**
	 * The variable is always a scalar.
	 */
	Scalar(-1),
	/**
	 * The variable is always an array with one or more dimensions.
	 */
	OneOrMoreDimensions(0),
	/**
	 * The variable is always one dimensional array.
	 */
	OneDimension(1),
	/**
	 * The variable is always an array with two dimensions.
	 */
	TwoDimensions(2),
	/**
	 * The variable is always an array with three dimensions.
	 */
	ThreeDimensions(3),
	/**
	 * The variable is always an array with two dimensions.
	 */
	FourDimensions(4),
	/**
	 * The variable is always an array with two dimensions.
	 */
	FiveDimensions(5);

	public static final EnumSet<ValueRanks> NONE = EnumSet.noneOf(ValueRanks.class);
	private Integer valueRank = null;

	ValueRanks(int valueRank) {
		this.valueRank = valueRank;
	}

	@Override
	public int getValue() {
		return this.valueRank;
	}

	/**
	 * Checks if the actual value rank is equals with the expected value rank.
	 * 
	 * @param actualValueRank
	 * @param expectedValueRank
	 * @return
	 */
	public static boolean IsValid(int actualValueRank, int expectedValueRank) {
		if (actualValueRank == expectedValueRank) {
			return true;
		}
		ValueRanks enum_val = ValueRanks.getValueRanks(expectedValueRank);
		if (enum_val == null)
			return false;
		switch (enum_val) {
		case Any: {
			return true;
		}
		case OneOrMoreDimensions: {
			if (actualValueRank < 0) {
				return false;
			}
			break;
		}
		case ScalarOrOneDimension: {
			if (actualValueRank != ValueRanks.Scalar.getValue()
					&& actualValueRank != ValueRanks.OneDimension.getValue()) {
				return false;
			}
			break;
		}
		case Scalar: {
			if (actualValueRank != ValueRanks.Scalar.getValue()) {
				return false;
			}
			break;
		}
		default: {
			return false;
		}
		}
		return true;
	}

	/**
	 * Checks if the actual array diminesions is compatible with the expected value
	 * rank and array dimensions.
	 * 
	 * @param actualArrayDimensions
	 * @param valueRank
	 * @param expectedArrayDimensions
	 * @return
	 */
	public static boolean isValid(List<UnsignedInteger> actualArrayDimensions, int valueRank,
			List<UnsignedInteger> expectedArrayDimensions) {
		// check if parameter omitted.
		if (actualArrayDimensions == null || actualArrayDimensions.isEmpty()) {
			return expectedArrayDimensions == null || expectedArrayDimensions.isEmpty();
		}
		// no array dimensions allowed for scalars.
		if (valueRank == ValueRanks.Scalar.getValue()) {
			return false;
		}
		// check if one dimension required.
		if ((valueRank == ValueRanks.OneDimension.getValue() || valueRank == ValueRanks.ScalarOrOneDimension.getValue())
				&& actualArrayDimensions.size() != 1) {
			return false;
		}
		// check number of dimensions.
		if (valueRank != ValueRanks.OneOrMoreDimensions.getValue() && actualArrayDimensions.size() != valueRank) {
			return false;
		}
		// nothing more to do if expected dimensions omitted.
		if (expectedArrayDimensions == null || expectedArrayDimensions.isEmpty()) {
			return true;
		}
		// check dimensions.
		if (expectedArrayDimensions.size() != actualArrayDimensions.size()) {
			return false;
		}
		// check length of each dimension.
		for (int ii = 0; ii < expectedArrayDimensions.size(); ii++) {
			if (expectedArrayDimensions.get(ii) != actualArrayDimensions.get(ii)
					&& !(expectedArrayDimensions.get(ii).equals(new UnsignedInteger(0)))) {
				return false;
			}
		}
		// everything ok.
		return true;
	}

	public static Set<ValueRanks> getSet(int mask) {
		List<ValueRanks> res = new ArrayList<>();
		for (ValueRanks l : ValueRanks.values())
			if ((mask & l.getValue()) == l.getValue())
				res.add(l);
		if (res.isEmpty())
			return NONE;
		return EnumSet.copyOf(res);
	}

	public static ValueRanks getValueRanks(int valueRank) {
		ValueRanks rank = null;
		for (ValueRanks l : ValueRanks.values()) {
			if (l.getValue() == valueRank) {
				rank = l;
			}
		}
		return rank;
	}
}
