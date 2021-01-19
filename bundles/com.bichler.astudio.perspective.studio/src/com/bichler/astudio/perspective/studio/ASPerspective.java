package com.bichler.astudio.perspective.studio;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class ASPerspective implements IPerspectiveFactory {

	public static final String ID = "com.bichler.astudio.perspective.studio";
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
//		String editorArea = 
		layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);
	}

}
