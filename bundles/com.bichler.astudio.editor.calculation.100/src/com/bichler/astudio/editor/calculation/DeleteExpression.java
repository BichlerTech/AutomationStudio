package com.bichler.astudio.editor.calculation;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.bichler.astudio.editor.calculation.model.CalculationModelNode;

public class DeleteExpression extends Dialog {

	private CalculationModelNode dp = null;
	private Combo combo;
	private int index;

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @wbp.parser.constructor
	 */
	public DeleteExpression(Shell parentShell) {
		super(parentShell);
	}
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DeleteExpression(Shell parentShell, CalculationModelNode dp) {
		super(parentShell);
		this.dp  = dp;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Calculation Item");
		
		combo = new Combo(container, SWT.NONE);
		combo.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				setIndex(combo.getSelectionIndex());
			}
		});
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		this.fillItems();
		
		return container;
	}
	
	private void fillItems() {
		for(int i = 0; i < dp.getDp().getCalculationExpressions().size(); i++) {
			combo.add(i + ". Expression");
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
