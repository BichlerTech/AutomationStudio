package com.bichler.astudio.editor.calculation;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.bichler.astudio.editor.calculation.model.CalculationModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class AddCalculationItemDialog extends Dialog
{
  private int selection = 0;
  private int index = 0;
  private CalculationModelNode dp = null;
  private Combo combo1;

  /**
   * Create the dialog.
   * 
   * @param parentShell
   */
  public AddCalculationItemDialog(Shell parentShell)
  {
    super(parentShell);
  }

  /**
   * Create contents of the dialog.
   * 
   * @param parent
   */
  @Override
  protected Control createDialogArea(Composite parent)
  {
    Composite container = (Composite) super.createDialogArea(parent);
    GridLayout gridLayout = (GridLayout) container.getLayout();
    gridLayout.numColumns = 2;
    Label lblNewLabel = new Label(container, SWT.NONE);
    lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblNewLabel.setText(CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.calculation.dialog.additem.type"));
    final Combo combo = new Combo(container, SWT.READ_ONLY);
    combo.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        selection = combo.getSelectionIndex();
      }
    });
    combo.setItems(
        CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.editor.calculation.dialog.additem.expression"),
        CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.editor.calculation.dialog.additem.node"));
    combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    combo.select(0);
    Label lblNewLabel1 = new Label(container, SWT.NONE);
    lblNewLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblNewLabel1.setText(CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.calculation.dialog.additem.index"));
    combo1 = new Combo(container, SWT.READ_ONLY);
    combo1.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        index = combo1.getSelectionIndex();
      }
    });
    combo1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    this.fillIndex();
    return container;
  }

  /**
   * Create contents of the button bar.
   * 
   * @param parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  private void fillIndex()
  {
    for (int i = 0; i < dp.getDp().getCalculationExpressions().size(); i++)
    {
      combo1.add((i + 1) + "");
    }
    combo1.add((dp.getDp().getCalculationExpressions().size() + 1) + "");
    combo1.select(dp.getDp().getCalculationExpressions().size());
    index = dp.getDp().getCalculationExpressions().size();
  }

  /**
   * Return the initial size of the dialog.
   */
  @Override
  protected Point getInitialSize()
  {
    return new Point(450, 300);
  }

  public int getSelection()
  {
    return selection;
  }

  public void setSelection(int selection)
  {
    this.selection = selection;
  }

  public CalculationModelNode getDp()
  {
    return dp;
  }

  public void setDp(CalculationModelNode dp)
  {
    this.dp = dp;
  }

  public int getIndex()
  {
    return index;
  }

  public void setIndex(int index)
  {
    this.index = index;
  }
}
