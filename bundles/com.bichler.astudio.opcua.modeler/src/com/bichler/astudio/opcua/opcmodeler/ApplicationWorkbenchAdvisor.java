package com.bichler.astudio.opcua.opcmodeler;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.model.ContributionComparator;
import org.eclipse.ui.model.IContributionService;

import com.bichler.astudio.opcua.opcmodeler.perspective.DesignerPerspective;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return DesignerPerspective.ID;
	}

	@Override
	public ContributionComparator getComparatorFor(String contributionType) {
		ContributionComparator cc = null;
		if (contributionType.equals(IContributionService.TYPE_PROPERTY)) {
			// cc = new StudioContributionComparator();
		} else {
			cc = super.getComparatorFor(contributionType);
		}
		return cc;
	}
}
