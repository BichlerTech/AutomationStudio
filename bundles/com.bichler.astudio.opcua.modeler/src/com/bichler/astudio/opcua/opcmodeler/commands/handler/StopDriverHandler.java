package com.bichler.astudio.opcua.opcmodeler.commands.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Event;
import org.osgi.framework.BundleException;

public class StopDriverHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			((StopCommandContributionItem) ((Event) event.getTrigger()).widget.getData()).getBundle().stop();
		} catch (BundleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
