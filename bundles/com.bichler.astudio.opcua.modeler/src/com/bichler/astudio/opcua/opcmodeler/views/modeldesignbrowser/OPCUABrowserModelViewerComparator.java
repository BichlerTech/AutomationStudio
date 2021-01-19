package com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;

public class OPCUABrowserModelViewerComparator extends ViewerComparator {
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		if (e1 instanceof BrowserModelNode && e2 instanceof BrowserModelNode) {
			int ret = 0;
			String name1 = ((BrowserModelNode) e1).getNode().getDisplayName().getText();
			String name2 = ((BrowserModelNode) e2).getNode().getDisplayName().getText();
			try {
				ret = name1.toUpperCase().compareTo(name2.toUpperCase());
			} catch (NullPointerException ex) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
			}
			return ret;
		}
		return super.compare(viewer, e1, e2);
	}
}
