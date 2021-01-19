package com.bichler.astudio.opcua.editor.providers;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.bichler.astudio.opcua.editor.input.OPCUAServerControlStartItem;
import com.bichler.astudio.opcua.editor.input.OPCUAServerControlStopItem;

public class OPCUAServerControlLabelProvider implements ILabelProvider, IColorProvider {

	@Override
	public Color getForeground(Object element) {
		if (element instanceof String) {
			return new Color(Display.getCurrent(), 0, 0, 0);
		}
		if (element instanceof OPCUAServerControlStartItem) {
			return new Color(Display.getCurrent(), 0, 255, 0);
		}
		if (element instanceof OPCUAServerControlStopItem) {
			return new Color(Display.getCurrent(), 255, 0, 0);
		}

		return new Color(Display.getCurrent(), 255, 255, 255);
	}

	@Override
	public Color getBackground(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getImage(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText(Object element) {
		// TODO Auto-generated method stub
		return element.toString();
	}

}
