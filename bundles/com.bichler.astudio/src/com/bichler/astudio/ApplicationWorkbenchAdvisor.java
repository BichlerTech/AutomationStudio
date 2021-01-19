package com.bichler.astudio;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.model.ContributionComparator;
import org.eclipse.ui.model.IContributionService;


/**
 * This workbench advisor creates the window advisor, and specifies the
 * perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {


  @Override
	public void initialize(IWorkbenchConfigurer configurer) {
		/**
		 * TODO: do not start comet studio from another perspective other than
		 * default
		 */
		// configurer.setSaveAndRestore(false);
	}

	@Override
	public ContributionComparator getComparatorFor(String contributionType) {
		ContributionComparator cc;

		if (contributionType.equals(IContributionService.TYPE_PROPERTY)) {
			cc = new StudioContributionComparator();
		} else {
			cc = super.getComparatorFor(contributionType);
		}

		return cc;
	}

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public String getInitialWindowPerspectiveId() {
	  return "com.bichler.astudio.perspective.studio";
	}

	@Override
	public boolean preShutdown() {

		boolean preShutdown = super.preShutdown();
		IWorkbenchWindow window = getWorkbenchConfigurer().getWorkbench().getActiveWorkbenchWindow();

		try {
			window.getWorkbench().showPerspective("com.bichler.astudio.perspective.studio", window);
		} catch (WorkbenchException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}

		return preShutdown;
	}
}
