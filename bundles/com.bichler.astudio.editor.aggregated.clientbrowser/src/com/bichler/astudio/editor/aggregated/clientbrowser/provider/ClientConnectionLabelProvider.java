package com.bichler.astudio.editor.aggregated.clientbrowser.provider;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.editor.aggregated.clientbrowser.model.AbstractCCModel;
import com.bichler.astudio.view.drivermodel.handler.util.AbstractDriverModelViewNode;
import com.bichler.astudio.view.drivermodel.handler.util.IDriverModelViewLabelProvider;

public class ClientConnectionLabelProvider extends LabelProvider implements
		IDriverModelViewLabelProvider {

	@Override
	public Image getImage(Object element) {

		if (element instanceof AbstractDriverModelViewNode) {
			return ((AbstractDriverModelViewNode) element).getDecorator();
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof AbstractCCModel) {
			return ((AbstractCCModel) element).getDisplayname();
		}

		return super.getText(element);
	}

}
