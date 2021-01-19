package com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.opcua.opcmodeler.Activator;

public class ReferenceLabelProvider extends LabelProvider {
	private Image imgRefType;
	private Image imgObj;
	private Image imgVar;
	private Image imgMethod;
	private Image imgObjType;
	private Image imgVarType;
	private Image imgDatatype;
	private Image imgView;

	public ReferenceLabelProvider() {
		this.imgRefType = Activator.getImageDescriptor("icons/browser_icons/referencetype_16.png").createImage();
		this.imgObj = Activator.getImageDescriptor("icons/browser_icons/object_16.png").createImage();
		this.imgObjType = Activator.getImageDescriptor("icons/browser_icons/objecttype_16.png").createImage();
		this.imgVar = Activator.getImageDescriptor("icons/browser_icons/variable_16.png").createImage();
		this.imgVarType = Activator.getImageDescriptor("icons/browser_icons/variabletype_16.png").createImage();
		this.imgMethod = Activator.getImageDescriptor("icons/browser_icons/method_16.png").createImage();
		this.imgDatatype = Activator.getImageDescriptor("icons/browser_icons/datatype_16.png").createImage();
		this.imgView = Activator.getImageDescriptor("icons/browser_icons/view_16.png").createImage();
	}

	@Override
	public Image getImage(Object element) {
		switch (((ReferenceModel) element).getNodeClass()) {
		case ReferenceType:
			return this.imgRefType;
		case Object:
			return this.imgObj;
		case Variable:
			return this.imgVar;
		case Method:
			return this.imgMethod;
		case ObjectType:
			return this.imgObjType;
		case VariableType:
			return this.imgVarType;
		case DataType:
			return this.imgDatatype;
		case View:
			return this.imgView;
		default:
			break;
		}
		return super.getImage(element);
	}

	@Override
	public String getText(Object element) {
		return ((ReferenceModel) element).getName();
	}

	@Override
	public void dispose() {
		if (imgRefType != null) {
			imgRefType.dispose();
		}
		if (imgObj != null) {
			imgObj.dispose();
		}
		if (imgObjType != null) {
			imgObjType.dispose();
		}
		if (imgVar != null) {
			imgVar.dispose();
		}
		if (imgVarType != null) {
			imgVarType.dispose();
		}
		if (imgMethod != null) {
			imgMethod.dispose();
		}
		if (imgDatatype != null) {
			imgDatatype.dispose();
		}
		if (imgView != null) {
			imgView.dispose();
		}
		super.dispose();
	}
}
