package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.errors;

import opc.sdk.core.enums.ValueRanks;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.IQuickFixProvider;
import com.richclientgui.toolbox.validation.ValidatingField;

public class ArrayDimQuickFixProvider<T> implements IQuickFixProvider<UnsignedInteger[]> {
	protected ValueRanks valueRank = ValueRanks.Any;

	public ArrayDimQuickFixProvider(ValueRanks valueRank) {
		this.valueRank = valueRank;
	}

	public void setValueRank(ValueRanks valueRank) {
		this.valueRank = valueRank;
	}

	@Override
	public boolean doQuickFix(ValidatingField<UnsignedInteger[]> value) {
		if (this.valueRank.getValue() <= 0) {
			value.setContents(new UnsignedInteger[0]);
			return true;
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
