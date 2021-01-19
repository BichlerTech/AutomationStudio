package com.bichler.astudio.editor.allenbradley.model;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.editor.allenbradley.datenbaustein.AllenBradleyDBResourceManager;
import com.bichler.astudio.view.drivermodel.Activator;

public class AllenBradleyEntryNode extends AbstractAllenBradleyNode
{
  protected AllenBradleyEntryNode(AllenBradleyDBResourceManager structManager)
  {
    super(structManager);
  }

  @Override
  public AbstractAllenBradleyNode[] getMembers()
  {
    AbstractAllenBradleyNode[] c = super.getMembers();
    if (c.length > 0)
    {
      return c;
    }
    AbstractAllenBradleyNode structure = getStructureManager().getStructure(getDataType());
    if (structure != null)
    {
      AbstractAllenBradleyNode[] c1 = structure.getMembers();
      for (AbstractAllenBradleyNode child : c1)
      {
        // clones nodes with addrss index
        addMember(child.cloneNode(true));
      }
      return getMembers();
    }
    return c;
  }

  @Override
  public AbstractAllenBradleyNode cloneNode(boolean includeChildren)
  {
    AllenBradleyEntryNode sen = new AllenBradleyEntryNode(getStructureManager());
    sen.setActive(this.isActive());
    sen.setDataType(this.getDataType());
    sen.setDescription(this.getDescription());
    sen.setLabelImage(this.getLabelImage());
    sen.setName(this.getName());
    // sen.setSymbolName(this.getSymbolName());
    if (includeChildren)
    {
      for (AbstractAllenBradleyNode child : super.getMembers())
      {
        sen.addMember(child.cloneNode(includeChildren));
      }
    }
    return sen;
  }

  @Override
  public Image getDecorator()
  {
    return Activator.getImage(Activator.ICON_VARIABLEN);
  }
}
