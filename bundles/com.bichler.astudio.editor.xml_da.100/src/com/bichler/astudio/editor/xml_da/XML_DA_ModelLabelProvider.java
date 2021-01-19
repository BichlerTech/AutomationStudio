package com.bichler.astudio.editor.xml_da;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.editor.xml_da.xml.XMLDAModelNode;
import com.bichler.astudio.view.drivermodel.handler.util.AbstractDriverModelViewNode;
import com.bichler.astudio.view.drivermodel.handler.util.IDriverModelViewLabelProvider;

public class XML_DA_ModelLabelProvider extends LabelProvider
    implements ITableLabelProvider, IDriverModelViewLabelProvider
{
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
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getColumnText(Object element, int columnIndex)
  {
    switch (columnIndex)
    {
    case 0:
      return ((XMLDAModelNode) element).getSymbolName();
    case 1:
      return ((XMLDAModelNode) element).getDataType();
    case 2:
      return ((XMLDAModelNode) element).getItemPath();
    case 3:
      return ((XMLDAModelNode) element).getItemName();
    case 4:
      return ((XMLDAModelNode) element).getDescription();
    default:
      break;
    }
    // TODO Auto-generated method stub
    return "";
  }
}
