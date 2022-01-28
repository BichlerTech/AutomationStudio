package com.bichler.astudio.editor.allenbradley.model;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.editor.allenbradley.datenbaustein.AllenBradleyDBResourceManager;
import com.bichler.astudio.view.drivermodel.Activator;

public class AllenBradleyArrayNode extends AbstractAllenBradleyNode
{
  private int dim = 0;
  private String arrayType = "";

  protected AllenBradleyArrayNode(AllenBradleyDBResourceManager structManager)
  {
    super(structManager);
  }

  public void setDimension(String arrayDim)
  {
    if (arrayDim == null)
    {
      this.dim = 0;
      return;
    }
    this.dim = Integer.parseInt(arrayDim);
  }

  private void setDimension(int dim)
  {
    this.dim = dim;
  }

  public int getDimension()
  {
    return dim;
  }

  @Override
  public AbstractAllenBradleyNode cloneNode(boolean includeChildren)
  {
    AllenBradleyArrayNode san = new AllenBradleyArrayNode(getStructureManager());
    san.setActive(this.isActive());
    san.setDataType(super.getDataType());
    san.setArrayType(this.getArrayType());
    san.setDescription(this.getDescription());
    san.setDimension(this.getDimension());
    san.setDimension(this.dim);
    san.setLabelImage(this.getLabelImage());
    san.setName(this.getName());
    if (includeChildren)
    {
      for (AbstractAllenBradleyNode child : getMembers())
      {
        san.addMember(child.cloneNode(includeChildren));
      }
    }
    return san;
  }

  // public int getChildIndex(AbstractallenbradleyNode child) {
  // return this.members.indexOf(child);
  // }
  public void setArrayType(String arrayType)
  {
    this.arrayType = arrayType;
  }

  public String getArrayType()
  {
    return this.arrayType;
  }

  @Override
  public String getDataType()
  {
    String dataType = super.getDataType();
    if (dataType != null)
    {
      // return dataType.replace(allenbradleyModelParser.DATATYPE_ARRAY,
      // this.arrayType);
    }
    return dataType;
  }

  @Override
  public Image getDecorator()
  {
    return Activator.getImage(Activator.ICON_ARRAY);
  }
}
