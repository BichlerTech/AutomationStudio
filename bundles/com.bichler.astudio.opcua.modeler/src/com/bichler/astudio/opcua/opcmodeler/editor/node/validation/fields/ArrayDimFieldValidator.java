package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import java.util.Map;

import opc.sdk.core.enums.ValueRanks;

import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

public class ArrayDimFieldValidator<T> implements IFieldValidator<UnsignedInteger[]> {
	// we have also to check the value rank
	private ValueRanks valueRank = ValueRanks.Any;
	private String errorMessage = "";
	private ValidatingField<UnsignedInteger[]> arrayDimField;
	private Text textbox;
	private ValidatingField<Variant> valueField;
	private Map<ValueRanks, UnsignedInteger[]> cached_arrayDimensions;

	public ArrayDimFieldValidator(Map<ValueRanks, UnsignedInteger[]> cache_arraydimension) {
		this.cached_arrayDimensions = cache_arraydimension;
	}

	public void setValueRank(ValueRanks rank) {
		this.valueRank = rank;
	}

	public ValueRanks getValueRanks() {
		return this.valueRank;
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
		boolean isValid = isUnsignedInteger(arrayDim);
		if (!isValid) {
			return isValid;
		}
		// set the correct error message
		if (this.valueRank.getValue() <= 0 && arrayDim.length > 0) {
			this.errorMessage = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"message.error.arraydim.empty");
			isValid = false;
		} else {
			if ((this.valueRank.getValue() <= 0 && arrayDim.length == 0)
					|| (this.valueRank.getValue() == arrayDim.length)) {
				isValid = true;
			} else {
				// there is a arraydim option to set
				this.errorMessage = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
						"message.error.arraydim.empty");
				isValid = false;
			}
		}
		if (isValid) {
			this.cached_arrayDimensions.put(this.valueRank, arrayDim);
		}
		return isValid;
	}

	private boolean isUnsignedInteger(UnsignedInteger[] arrayDim) {
		if (this.arrayDimField != null) {
			String text = ((Text) this.arrayDimField.getControl()).getText();
			switch (this.valueRank) {
			case Any:
			case OneOrMoreDimensions:
			case Scalar:
			case ScalarOrOneDimension:
				if (!text.isEmpty()) {
					this.errorMessage = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
							"message.error.arraydim.mustempty");
					return false;
				}
				break;
			default:
				try {
					UnsignedInteger[] dim = this.arrayDimField.getContents();
					if (dim.length <= 0) {
						new UnsignedInteger(text);
					}
				} catch (IllegalArgumentException e) {
					this.errorMessage = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
							"message.error.arraydim.invalid2");
					return false;
				}
				break;
			}
		} else if (this.textbox != null) {
			String text = this.textbox.getText();
			switch (this.valueRank) {
			case Any:
			case OneOrMoreDimensions:
			case Scalar:
			case ScalarOrOneDimension:
				if (arrayDim.length != 0) {
					this.errorMessage = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
							"message.error.arraydim.mustempty");
					return false;
				}
				if (!text.isEmpty()) {
					this.errorMessage = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
							"message.error.arraydim.mustempty");
					return false;
				}
				break;
			default:
				if (this.valueRank.getValue() != arrayDim.length) {
					this.errorMessage = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
							"message.error.arraydim.invalid2");
					return false;
				}
				break;
			}
		}
		return true;
	}

	@Override
	public boolean warningExist(UnsignedInteger[] arrayDim) {
		return false;
	}

	public void setArrayDimField(ValidatingField<UnsignedInteger[]> arrayDimField) {
		this.arrayDimField = arrayDimField;
	}

	public void setTextField(Text txtElement) {
		this.textbox = txtElement;
	}

	public void setValueField(ValidatingField<Variant> field) {
		this.valueField = field;
	}

	public ValidatingField<Variant> getValueField() {
		return this.valueField;
	}
}
