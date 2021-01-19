package com.bichler.astudio.editor.siemens.model;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;
import com.bichler.astudio.view.drivermodel.Activator;

public class SiemensStructNode extends AbstractSiemensNode {
	private boolean isRoot = false;
	private float structlength;
	private boolean isArrayEntry = false;

	protected SiemensStructNode(SiemensDBResourceManager structureManager) {
		super(structureManager);
	}

	@Override
	public AbstractSiemensNode cloneNode(boolean inlcudeAddressIndex, boolean includeChildren) {
		SiemensStructNode ssn = new SiemensStructNode(getStructureManager());
		ssn.setActive(this.isActive());
		ssn.setDataType(this.getDataType());
		ssn.setDescription(this.getDescription());
		if (inlcudeAddressIndex) {
			ssn.setIndex(this.getAddressIndex());
		} else {
			ssn.setIndex(this.getIndex());
		}
		ssn.setLabelImage(this.getLabelImage());
		ssn.setName(this.getName());
		ssn.setSymbolName(this.getSymbolName());
		ssn.setArrayEntry(this.isArrayEntry());
		ssn.setRoot(this.isRoot());
		ssn.setStructLength(this.getStructLength());
		// ssn.setParent(this.getParent());
		if (includeChildren) {
			for (AbstractSiemensNode child : getChildren()) {
				ssn.addChild(child.cloneNode(inlcudeAddressIndex, includeChildren));
			}
		}
		return ssn;
	}

	public int getChildIndex(AbstractSiemensNode child) {
		if (this.children != null) {
			return this.children.indexOf(child);
		}
		return -1;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public boolean isArrayEntry() {
		return isArrayEntry;
	}

	public void setArrayEntry(boolean isArrayEntry) {
		this.isArrayEntry = isArrayEntry;
	}

	public void setStructLength(float structLength) {
		this.structlength = structLength;
	}

	public float getStructLength() {
		return this.structlength;
	}

	@Override
	public Image getDecorator() {
		return Activator.getImage(Activator.ICON_STRUCTUR);
	}
}
