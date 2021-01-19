package com.bichler.astudio.editor.siemens.model;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;
import com.bichler.astudio.view.drivermodel.Activator;

public class SiemensArrayNode extends AbstractSiemensNode {
	private int[] dim = new int[] { 0, 0 };
	private String arrayType = "";

	protected SiemensArrayNode(SiemensDBResourceManager structureManager) {
		super(structureManager);
	}

	public void setDimension(String arrayDim) {
		if (arrayDim == null) {
			this.dim = new int[] { 0, 0 };
			return;
		}
		int startIndex = arrayDim.indexOf("[");
		int endIndex = arrayDim.indexOf("]");
		String aDim = arrayDim.substring(startIndex + 1, endIndex);
		String[] entries = aDim.split("\\..");
		this.dim[0] = Integer.parseInt(entries[0]);
		this.dim[1] = Integer.parseInt(entries[1]);
	}

	private void setDimension(int[] dim) {
		this.dim = dim;
	}

	public int getDimension() {
		return dim[1] - dim[0] + 1;
	}

	@Override
	public AbstractSiemensNode cloneNode(boolean calcIndex, boolean includeChildren) {
		SiemensArrayNode san = new SiemensArrayNode(getStructureManager());
		san.setActive(this.isActive());
		san.setDataType(super.getDataType());
		san.setArrayType(this.getArrayType());
		san.setDescription(this.getDescription());
		san.setDimension(this.dim);
		if (calcIndex) {
			san.setIndex(this.getAddressIndex());
		} else {
			san.setIndex(this.getIndex());
		}
		san.setLabelImage(this.getLabelImage());
		san.setName(this.getName());
		san.setSymbolName(this.getSymbolName());
		if (includeChildren) {
			for (AbstractSiemensNode child : getChildren()) {
				san.addChild(child.cloneNode(calcIndex, includeChildren));
			}
		}
		return san;
	}

	public int getChildIndex(AbstractSiemensNode child) {
		return this.children.indexOf(child);
	}

	public void setArrayType(String arrayType) {
		this.arrayType = arrayType;
	}

	public String getArrayType() {
		return this.arrayType;
	}

	@Override
	public String getDataType() {
		String dataType = super.getDataType();
		if (dataType != null) {
			return dataType.replace(SiemensModelParser.DATATYPE_ARRAY, this.arrayType);
		}
		return dataType;
	}

	@Override
	public Image getDecorator() {
		return Activator.getImage(Activator.ICON_ARRAY);
	}
}
