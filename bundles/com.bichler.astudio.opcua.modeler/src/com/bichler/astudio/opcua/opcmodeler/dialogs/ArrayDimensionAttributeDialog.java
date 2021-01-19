package com.bichler.astudio.opcua.opcmodeler.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridEditor;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.DesignerFormToolkit;
import com.bichler.astudio.utils.internationalization.CustomString;
//import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.ValidatingField;

import opc.sdk.core.enums.ValueRanks;

public class ArrayDimensionAttributeDialog extends Dialog {
	final DesignerFormToolkit controllCreationToolkit = new DesignerFormToolkit();
	private Text txt_valueRank;
	private ValueRanks valueRanks;
	private GridEditor editor;
	private Text newEditor;
	private Grid grid;
	private int dimensionCount = 0;
	private UnsignedInteger[] currentArrayDim;
	private boolean ignoreEditingAllowed;

	public ArrayDimensionAttributeDialog(Shell parentShell, ValueRanks valueRanks, UnsignedInteger[] arrayDim,
			boolean ignoreEditingAllowed) {
		super(parentShell);
		this.valueRanks = valueRanks;
		this.currentArrayDim = arrayDim;
		this.ignoreEditingAllowed = ignoreEditingAllowed;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) composite.getLayout();
		gridLayout.numColumns = 2;
		Label lbl_valueRank = new Label(composite, SWT.NONE);
		lbl_valueRank.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_valueRank.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"CreateVariableDialog.lbl_valueRank.text"));
		txt_valueRank = new Text(composite, SWT.BORDER);
		txt_valueRank.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txt_valueRank.setText(this.valueRanks.name());
		this.grid = new Grid(composite, SWT.BORDER | SWT.V_SCROLL | SWT.VIRTUAL | SWT.H_SCROLL);
		grid.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		grid.setSize(422, 143);
		grid.setCellSelectionEnabled(true);
		grid.setHeaderVisible(true);
		grid.setRowHeaderVisible(true);
		// column
		GridColumn gridColumn = new GridColumn(grid, SWT.NONE);
		gridColumn.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.dimension"));
		gridColumn.setWidth(150);
		editor = new GridEditor(grid);
		this.grid.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final Point pt = ((Grid) e.getSource()).getCellSelection()[0];
				Control oldEditor = editor.getEditor();
				if (oldEditor != null)
					oldEditor.dispose();
				GridItem item = (GridItem) e.item;
				if (item == null)
					return;
				final UnsignedInteger aDim = (UnsignedInteger) item.getData();
				if (!ignoreEditingAllowed && aDim.intValue() != 0) {
					e.doit = false;
					return;
				}
				ValidatingField<Integer> int32val = controllCreationToolkit.createTextInt32(grid, 0);
				newEditor = (Text) int32val.getControl();
				newEditor.setText(item.getText(pt.x));
				newEditor.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						Text text = (Text) editor.getEditor();
						String txtVal = text.getText();
						editor.getItem().setText(pt.x, txtVal);
					}
				});
				newEditor.selectAll();
				newEditor.setFocus();
				editor.setEditor(newEditor, item, pt.x);
			}
		});
		// rows
		switch (this.valueRanks) {
		case OneDimension:
			dimensionCount = 1;
			break;
		case TwoDimensions:
			dimensionCount = 2;
			break;
		case ThreeDimensions:
			dimensionCount = 3;
			break;
		case FourDimensions:
			dimensionCount = 4;
			break;
		case FiveDimensions:
			dimensionCount = 5;
			break;
		case OneOrMoreDimensions:
		case Any:
		case ScalarOrOneDimension:
		case Scalar:
			dimensionCount = 0;
			break;
		}
		/**
		 * TODO: depends on dimension
		 */
		grid.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GridItem item = (GridItem) e.item;
				UnsignedInteger dim = (UnsignedInteger) item.getData();
				if (dim != null && dim.intValue() != 0) {
					e.doit = false;
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		for (int i = 0; i < dimensionCount; i++) {
			GridItem item = new GridItem(grid, SWT.NONE);
			try {
				UnsignedInteger dim = this.currentArrayDim[i];
				item.setText(dim.toString());
				item.setData(dim);
			} catch (Exception e) {
				item.setText("0");
			}
		}
		editor.grabHorizontal = true;
		editor.grabVertical = true;
		return composite;
	}

	@Override
	protected void okPressed() {
		int rows = this.grid.getItemCount();
		int columns = this.grid.getColumnCount();
		UnsignedInteger[] newArrayDim = new UnsignedInteger[rows];
		for (int i = 0; i < rows; i++) {
			GridItem item = this.grid.getItem(i);
			newArrayDim[i] = new UnsignedInteger(item.getText());
		}
		this.currentArrayDim = newArrayDim;
		super.okPressed();
	}

	private void expandEditTable(int dimensions) {
		int actrows = this.grid.getItemCount();
		// int actcolumns = this.grid.getColumnCount();
		int rows = dimensions;
		// int columns = 1;
		this.dimensionCount = dimensions;
		if (actrows > rows) {
			// we have to delete some rows
			for (int i = actrows; i > rows; i--) {
				this.grid.remove(i - 1);
			}
		}
		GridItem item = null;
		GridColumn column = null;
		Text text = null;
		if (actrows < rows) {
			// now create all rows
			for (int i = actrows; i < rows; i++) {
				item = new GridItem(grid, SWT.NONE);
				item.setText("0");
			}
		}
		editor = new GridEditor(grid);
		editor.grabHorizontal = true;
		editor.grabVertical = true;
	}

	public UnsignedInteger[] getArrayDimension() {
		return this.currentArrayDim;
	}
}
