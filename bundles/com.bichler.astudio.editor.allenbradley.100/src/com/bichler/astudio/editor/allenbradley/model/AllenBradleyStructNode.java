package com.bichler.astudio.editor.allenbradley.model;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.editor.allenbradley.datenbaustein.AllenBradleyDBResourceManager;
import com.bichler.astudio.view.drivermodel.Activator;

public class AllenBradleyStructNode extends AbstractAllenBradleyNode
{
  public AllenBradleyStructNode(AllenBradleyDBResourceManager structManager)
  {
    super(structManager);
  }

  @Override
  public AbstractAllenBradleyNode cloneNode(boolean includeChildren)
  {
    AllenBradleyStructNode ssn = new AllenBradleyStructNode(getStructureManager());
    ssn.setActive(this.isActive());
    ssn.setDataType(this.getDataType());
    ssn.setDescription(this.getDescription());
    ssn.setLabelImage(this.getLabelImage());
    ssn.setName(this.getName());
    ssn.setStructLength(this.getStructLength());
    if (includeChildren)
    {
      for (AbstractAllenBradleyNode child : getMembers())
      {
        ssn.addMember(child.cloneNode(includeChildren));
      }
    }
    return ssn;
  }
  private float structlength;
  private String dependencyType;
  private String tag;

  public void setStructLength(float structLength)
  {
    this.structlength = structLength;
  }

  public float getStructLength()
  {
    return this.structlength;
  }

  public String getDependancyType()
  {
    return this.dependencyType;
  }

  public void setDependancyType(String value)
  {
    this.dependencyType = value;
  }

  public void setTag(String tag)
  {
    this.tag = tag;
  }

  @Override
  public Image getDecorator()
  {
    return Activator.getImage(Activator.ICON_STRUCTUR);
  }

  public String getTag()
  {
    return this.tag;
  }
}
