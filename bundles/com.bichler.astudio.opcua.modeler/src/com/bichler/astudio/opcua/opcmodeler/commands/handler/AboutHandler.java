package com.bichler.astudio.opcua.opcmodeler.commands.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.opcmodeler.dialogs.DesignerAboutDialog;

public class AboutHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		new DesignerAboutDialog(HandlerUtil.getActiveShellChecked(event)).open();
		return null;
	}
}
