package com.bichler.astudio.navigation.views.providers;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.navigation.nodes.StudioModelNode;

public class StudioModelLabelProvider extends LabelProvider {

	/**
	 * Returns the image for the item
	 */
	@Override
	public Image getImage(Object element) {
		if(element != null && element instanceof StudioModelNode) {
			return ((StudioModelNode)element).getLabelImage();
		}
		
		return null;//resolveImage(Path.ROOT.append("icons"),
				//"comet_icon_16.png");
				 
	}
	
	/**
	 * Returns the label for the item
	 */
	@Override
	public String getText(Object element) {
		if(element != null && element instanceof StudioModelNode) {
			return ((StudioModelNode)element).getLabelText();
		}
		
		return "";
	}

}
