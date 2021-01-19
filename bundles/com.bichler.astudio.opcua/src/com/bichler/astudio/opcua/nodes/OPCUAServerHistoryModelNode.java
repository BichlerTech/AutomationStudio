package com.bichler.astudio.opcua.nodes;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.opcua.handlers.OpenOPCUAServerHistoryHandler;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.OPCUAActivator;

public class OPCUAServerHistoryModelNode extends OPCUAServerModelNode {

	@Override
	public void nodeDBLClicked() {
		ICommandService commandService = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getService(ICommandService.class);
		IHandlerService handlerService = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getService(IHandlerService.class);
		Command openConfigCmd = commandService.getCommand(OpenOPCUAServerHistoryHandler.ID);
		ExecutionEvent executionOpenConfigEvent = handlerService.createExecutionEvent(openConfigCmd, new Event());
		try {
			openConfigCmd.executeWithChecks(executionOpenConfigEvent);
		} catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
	}

	@Override
	public Image getLabelImage() {
		return StudioImageActivator.getImage(StudioImages.ICON_OPCUAHISTORY);
	}

	@Override
	public String getLabelText() {
		return CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				this.getClass().getSimpleName() + ".LabelText");
	}

	@Override
	public Object[] getChildren() {
		return new Object[0];
	}
}
