package com.bichler.astudio.opcua.properties.handler;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.properties.view.ASDocuView;

public class ShowDocuViewHandler extends AbstractHandler {
	private IViewPart viewpart;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			Command command = event.getCommand();
			boolean oldValue = HandlerUtil.toggleCommandState(command);
			if (!oldValue) {
				viewpart = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().showView(ASDocuView.ID);
			} else {
				if (viewpart == null) {
					viewpart = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().findView(ASDocuView.ID);
				}
				HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().hideView(viewpart);
			}
		} catch (PartInitException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
		return null;
	}
}
