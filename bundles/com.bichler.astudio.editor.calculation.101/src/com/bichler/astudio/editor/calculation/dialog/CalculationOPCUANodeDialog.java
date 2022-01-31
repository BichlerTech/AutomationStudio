package com.bichler.astudio.editor.calculation.dialog;

import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.components.ui.dialogs.OPCTreeViewerItem;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUANodeDialog;

public class CalculationOPCUANodeDialog extends OPCUANodeDialog
{
  private Spinner spinner_valueIndex;
  private int initIndex;
  private int index;

  public CalculationOPCUANodeDialog(Shell parentShell, int index)
  {
    super(parentShell);
    this.initIndex = index;
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    Composite c = (Composite) super.createDialogArea(parent);
    getFormBody().setLayout(new GridLayout(2, false));
    GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
    gd.heightHint = 150;
    getTreeViewer().getTree().setLayoutData(gd);
    Label lbl_valueIndex = new Label(getFormBody(), SWT.NONE);
    lbl_valueIndex.setText("Index");
    this.spinner_valueIndex = new Spinner(getFormBody(), SWT.BORDER);
    this.spinner_valueIndex.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    initialize();
    c.layout(true);
    return c;
  }

  private void initialize()
  {
    OPCTreeViewerItem bo = (OPCTreeViewerItem) ((IStructuredSelection) getTreeViewer().getSelection())
        .getFirstElement();
    this.spinner_valueIndex.setSelection(this.initIndex);
    selectionChange(bo);
  }

  @Override
  protected void okPressed()
  {
    index = this.spinner_valueIndex.getSelection();
    super.okPressed();
  }

  @Override
  protected void selectionChange(OPCTreeViewerItem item)
  {
    // skip selection
    if (item == null)
    {
      return;
    }
    NodeClass nodeClass = item.getNodeClass();
    Variant variant = null;
    switch (nodeClass)
    {
    case Variable:
      variant = ((UAVariableNode) item.getNode()).getValue();
      break;
    case VariableType:
      variant = ((UAVariableTypeNode) item.getNode()).getValue();
      break;
    default:
      break;
    }
    // spinner value index max
    doSetSpinner(item, variant);
  }

  private void doSetSpinner(OPCTreeViewerItem item, Variant variant)
  {
    if (this.spinner_valueIndex == null)
    {
      return;
    }
    int min = -1, max = -1;
    if (variant == null)
    {
      min = -1;
      max = -1;
    }
    else if (variant.isEmpty())
    {
      min = -1;
      max = -1;
    }
    else if (variant.isArray())
    {
      // Multidimensional array
      int[] dims = variant.getArrayDimensions();
      int dim = variant.getDimension();
      min = 0;
      max = dims[dim - 1];
      max = max - 1;
    }
    this.spinner_valueIndex.setRedraw(true);
    this.spinner_valueIndex.setMaximum(max);
    this.spinner_valueIndex.setMinimum(min);
    int selection = spinner_valueIndex.getSelection();
    // wrap lower value to min
    if (selection < min)
    {
      selection = min;
    }
    // wrap upper value to max
    if (selection > max)
    {
      selection = max;
    }
    this.spinner_valueIndex.setSelection(selection);
  }

  @Override
  protected Point getInitialSize()
  {
    return new Point(450, 350);
  }

  public int getIndex()
  {
    return this.index;
  }
}
