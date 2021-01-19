package com.bichler.astudio.editor.allenbradley;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.editor.allenbradley.model.AbstractAllenBradleyNode;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyEntryModelNode;
import com.bichler.astudio.view.drivermodel.handler.util.AbstractDriverModelViewNode;
import com.bichler.astudio.view.drivermodel.handler.util.IDriverModelViewLabelProvider;

public class AllenBradleyModelLabelProvider extends LabelProvider
    implements ITableLabelProvider, IDriverModelViewLabelProvider
{
  public AllenBradleyModelLabelProvider()
  {
  }

  /**
   * Returns the image for the item
   */
  @Override
  public Image getImage(Object element)
  {
    if (element instanceof AbstractDriverModelViewNode)
    {
      return ((AbstractDriverModelViewNode) element).getDecorator();
    }
    return null;
  }

  @Override
  public Image getColumnImage(Object element, int columnIndex)
  {
    return null;
  }

  @Override
  public String getColumnText(Object element, int columnIndex)
  {
    switch (columnIndex)
    {
    case 0:
      return ((AllenBradleyEntryModelNode) element).getSymbolName();
    case 1:
      return ((AllenBradleyEntryModelNode) element).getDataType();
    case 4:
      return ((AllenBradleyEntryModelNode) element).getDisplayname();
    default:
      break;
    }
    return "";
  }

  @Override
  public String getText(Object element)
  {
    if (element instanceof AbstractAllenBradleyNode)
    {
      return ((AbstractAllenBradleyNode) element).getName();
    }
    return super.getText(element);
  }

  @Override
  public void dispose()
  {
    super.dispose();
  }
}
