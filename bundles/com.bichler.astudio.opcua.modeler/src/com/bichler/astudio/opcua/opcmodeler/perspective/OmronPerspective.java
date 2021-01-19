package com.bichler.astudio.opcua.opcmodeler.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

public class OmronPerspective implements IPerspectiveFactory {
	public static final String ID = "com.hbsoft.designer.omronspective";

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
		IFolderLayout folder = layout.createFolder("com.example.somefolder", IPageLayout.BOTTOM, 0.75f,
				layout.getEditorArea());
		folder.addView("org.eclipse.pde.runtime.LogView");
		folder.addView(IConsoleConstants.ID_CONSOLE_VIEW);
	}
}
