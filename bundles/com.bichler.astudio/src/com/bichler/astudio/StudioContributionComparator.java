package com.bichler.astudio;

import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.model.ContributionComparator;
import org.eclipse.ui.model.IComparableContribution;

public class StudioContributionComparator extends ContributionComparator {
	@Override
	public int compare(IComparableContribution c1,
	        IComparableContribution c2) {

	    int result = super.compare(c1, c2);

	    IPluginContribution pc1 = (IPluginContribution)c1;
	    IPluginContribution pc2 = (IPluginContribution)c2;

	    String id1 = pc1.getLocalId().substring(0,3);
	    String id2 = pc2.getLocalId().substring(0,3);

	    result = id1.compareTo(id2);

	    return result;
	}
}
