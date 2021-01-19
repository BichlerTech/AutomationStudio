package com.bichler.astudio.opcua.opcmodeler.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.DesignerFormToolkit;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.errors.ArrayDimensionQuickFixProvider;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.ArrayDimensionFieldValidator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.ValidatingField;

import opc.sdk.core.enums.ValueRanks;

public class ArrayDimensionDialog extends Dialog {
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// TODO Auto-generated method stub
		super.createButtonsForButtonBar(parent);
		validate();
	}

	private DesignerFormToolkit controllCreationToolkit = null;
	private UnsignedInteger[] dimensions = null;
	private ValidatingField<UnsignedInteger[]> txt_arrayDimension;
	private ValueRanks valueRank = ValueRanks.Any;

	public ArrayDimensionDialog(Shell parentShell, UnsignedInteger[] dimensions, ValueRanks valueRank) {
		super(parentShell);
		this.dimensions = dimensions;
		this.valueRank = valueRank;
		this.controllCreationToolkit = new DesignerFormToolkit();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout());
		this.controllCreationToolkit.createLabel(composite, CustomString.getString(
				Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariableTypePart.lbl_arrayDimensions.text"));
		ArrayDimensionFieldValidator<UnsignedInteger[]> validator = new ArrayDimensionFieldValidator<UnsignedInteger[]>();
		validator.setValueRank(this.valueRank);
		ArrayDimensionQuickFixProvider<UnsignedInteger[]> quickFix = new ArrayDimensionQuickFixProvider<UnsignedInteger[]>(
				this.valueRank);
		this.txt_arrayDimension = this.controllCreationToolkit.createTextArrayDimensions(composite, valueRank,
				validator, quickFix);
		this.txt_arrayDimension.setContents(this.dimensions);
		((Text) this.txt_arrayDimension.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dimensions = txt_arrayDimension.getContents();
				validate();
			}
		});
		return composite;
	}

	@Override
	protected void okPressed() {
		super.okPressed();
	}

	public UnsignedInteger[] getArrayDimensions() {
		return this.dimensions;
	}

	private void validate() {
		boolean validArrayDimension = this.txt_arrayDimension.isValid();
		if (!validArrayDimension) {
			getButton(OK).setEnabled(false);
		} else {
			getButton(OK).setEnabled(true);
		}
	}
}
