package com.bichler.astudio.editor.siemens.model;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;
import com.bichler.astudio.view.drivermodel.Activator;

public class SiemensEntryNode extends AbstractSiemensNode {
	protected SiemensEntryNode(SiemensDBResourceManager structureManager) {
		super(structureManager);
	}

	@Override
	public AbstractSiemensNode[] getChildren() {
		AbstractSiemensNode[] c = super.getChildren();
		if (c.length > 0) {
			return c;
		}
		AbstractSiemensNode structure = getStructureManager().getStructure(getDataType());
		if (structure != null) {
			AbstractSiemensNode[] c1 = structure.getChildren();
			for (AbstractSiemensNode child : c1) {
				// clones nodes with addrss index
				addChild(child.cloneNode(true, true));
			}
			return getChildren();
		}
		return c;
	}

	@Override
	public AbstractSiemensNode cloneNode(boolean calcIndex, boolean includeChildren) {
		SiemensEntryNode sen = new SiemensEntryNode(getStructureManager());
		sen.setActive(this.isActive());
		sen.setDataType(this.getDataType());
		sen.setDescription(this.getDescription());
		if (calcIndex) {
			sen.setIndex(this.getAddressIndex());
		} else {
			sen.setIndex(this.getIndex());
		}
		sen.setLabelImage(this.getLabelImage());
		sen.setName(this.getName());
		sen.setSymbolName(this.getSymbolName());
		if (includeChildren) {
			for (AbstractSiemensNode child : super.getChildren()) {
				sen.addChild(child.cloneNode(calcIndex, includeChildren));
			}
		}
		return sen;
	}

	@Override
	public int getChildIndex(AbstractSiemensNode child) {
		throw new IllegalArgumentException();
	}

	@Override
	public Image getDecorator() {
		return Activator.getImage(Activator.ICON_VARIABLEN);
	}
}
