package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.errors;

import opc.sdk.core.enums.ValueRanks;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.IQuickFixProvider;
import com.richclientgui.toolbox.validation.ValidatingField;

public class ArrayDimensionQuickFixProvider<T> extends ArrayDimQuickFixProvider<T> {
	public ArrayDimensionQuickFixProvider(ValueRanks valueRank) {
		super(valueRank);
	}

	public void setValueRank(ValueRanks valueRank) {
		this.valueRank = valueRank;
	}

	@Override
	public boolean doQuickFix(ValidatingField<UnsignedInteger[]> value) {
		// values and meanings
		// ScalarOrOneDimension (0)
		// Any (1):
		// Scalar (2):
		// OneOrMoreDimensions (3):
		// OneDimension (4):
		// TwoDimension (5):
		if (this.valueRank == ValueRanks.ScalarOrOneDimension || this.valueRank == ValueRanks.Any
				|| this.valueRank == ValueRanks.OneOrMoreDimensions) {
			UnsignedInteger[] content = new UnsignedInteger[1];
			content[0] = new UnsignedInteger(0);
			value.setContents(content);
			return true;
		} else if (valueRank == ValueRanks.Scalar
				|| this.valueRank.getValue() < ValueRanks.ScalarOrOneDimension.getValue()) {
			return false;
		}
		UnsignedInteger[] content = new UnsignedInteger[this.valueRank.getValue()];
		for (int i = 0; i < this.valueRank.getValue(); i++) {
			content[i] = new UnsignedInteger(0);
		}
		value.setContents(content);
		return true;
	}

	@Override
	public String getQuickFixMenuText() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.arraydim.empty");
	}

	@Override
	public boolean hasQuickFix(UnsignedInteger[] value) {
		/*
		 * if (value != null) { return false; }
		 */
		return true;
	}
}
