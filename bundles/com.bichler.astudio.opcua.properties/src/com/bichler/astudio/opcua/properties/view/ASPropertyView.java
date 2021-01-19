package com.bichler.astudio.opcua.properties.view;

import org.eclipse.core.runtime.Adapters;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheet;

public class ASPropertyView extends PropertySheet {

  public static final String ID = "com.bichler.astudio.properties.view";
  
  @Override
  protected IPage createDefaultPage(PageBook book)
  {
    IPageBookViewPage page = (IPageBookViewPage) Adapters.adapt(this, IPropertySheetPage.class);
    if(page == null) {
      page = new ASPropertySheetPage();
      ((ASPropertySheetPage)page).setSorter(new ASProperySheetSorter());
    }
    initPage(page);
    page.createControl(book);
    
    return page;
  }
	
	@Override
	protected boolean isImportant(IWorkbenchPart part) {
		return super.isImportant(part);
	}

	
}
