package com.bichler.astudio.opcua.perspective;

import org.eclipse.gef.ui.views.palette.PaletteView;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class OPCServerPerspective implements IPerspectiveFactory {

	public static final String ID = "com.bichler.astudio.opcua.perspective";
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
//		String editorArea = 
		layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);
	}

	/**
   * Defines the Actions
   */
  private void defineActions( IPageLayout layout )
  {
    // Add "show views".
    layout.addShowViewShortcut( IPageLayout.ID_OUTLINE );
    layout.addShowViewShortcut( PaletteView.ID );
//    layout.addShowViewShortcut( AttributeView.ID );
//    layout.addShowViewShortcut( DataView.ID );
//    layout.addShowViewShortcut( LibraryExplorerView.ID );
    layout.addShowViewShortcut( IPageLayout.ID_PROP_SHEET );

//    layout.addPerspectiveShortcut( BIRT_REPORT_RCP_PERSPECTIVE );
  }
}
