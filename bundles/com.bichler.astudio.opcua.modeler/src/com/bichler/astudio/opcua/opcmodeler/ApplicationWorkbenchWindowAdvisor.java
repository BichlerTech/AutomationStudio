package com.bichler.astudio.opcua.opcmodeler;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.bichler.astudio.utils.internationalization.CustomString;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(1200, 1000));
		configurer.setShowCoolBar(false);
		/** Make status line visible */
		configurer.setShowStatusLine(true);
//    configurer.setShowFastViewBars(true);
		configurer.setShowProgressIndicator(true);
		configurer.setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "window.title")); //$NON-NLS-1$
		configurer.setShowPerspectiveBar(true);
	}
}
