package com.bichler.astudio.opcua.opcmodeler.commands.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Event;
import org.osgi.framework.BundleException;

public class StartDriverHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			((StartCommandContributionItem) ((Event) event.getTrigger()).widget.getData()).getBundle().start();
		} catch (BundleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
