package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import java.util.HashMap;
import java.util.Map;

import opc.sdk.core.enums.ValueRanks;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

public class ArrayDimensionFieldValidator<T> extends ArrayDimFieldValidator<UnsignedInteger[]> {
	public ArrayDimensionFieldValidator() {
		super(new HashMap<ValueRanks, UnsignedInteger[]>());
	}

	// we have also to check the value rank
	private ValueRanks valueRank = ValueRanks.Any;
	private String errorMessage = "";

	public void setValueRank(ValueRanks rank) {
		this.valueRank = rank;
	}

	@Override
	public String getErrorMessage() {
		return this.errorMessage;
	}

	@Override
	public String getWarningMessage() {
		return null;
	}

	@Override
	public boolean isValid(UnsignedInteger[] arrayDim) {
		// values and meanings
		// ScalarOrOneDimension (-3)
		// Any (-2):
		// Scalar (-1):
		// OneOrMoreDimensions (0):
		// OneDimension (1):
		// TwoDimension (2):
		// set the correct error message
		if (this.valueRank == ValueRanks.ScalarOrOneDimension && arrayDim.length != 1) {
			this.errorMessage = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"message.error.scalaronedim");
			return false;
		} else if (this.valueRank == ValueRanks.Any && arrayDim.length == 0) {
			this.errorMessage = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.any");
			return false;
		} else if (this.valueRank == ValueRanks.Scalar) {
			this.errorMessage = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.scalar");
			return false;
		} else if (this.valueRank == ValueRanks.OneOrMoreDimensions && arrayDim.length == 0) {
			this.errorMessage = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"message.error.oneormoredim");
			return false;
		} else if (this.valueRank == ValueRanks.OneDimension && arrayDim.length != 1) {
			this.errorMessage = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.one");
			return false;
		} else if (this.valueRank == ValueRanks.TwoDimensions && arrayDim.length != 2) {
			this.errorMessage = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.two");
			return false;
		} else if (this.valueRank == ValueRanks.ThreeDimensions && arrayDim.length != 3) {
			this.errorMessage = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.three");
			return false;
		} else if (this.valueRank == ValueRanks.FourDimensions && arrayDim.length != 4) {
			this.errorMessage = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.four");
			return false;
		} else if (this.valueRank == ValueRanks.FiveDimensions && arrayDim.length != 5) {
			this.errorMessage = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.five");
			return false;
		}
		return true;
	}

	@Override
	public boolean warningExist(UnsignedInteger[] arrayDim) {
		return false;
	}
}
