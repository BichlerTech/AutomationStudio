package com.bichler.astudio.opcua.nodes;

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

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.opcua.handlers.OpenOPCUAServerLogHandler;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCUAServerLoggingModelNode extends OPCUAServerModelNode {
	private String serverName = "";

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public void nodeDBLClicked() {
		ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getService(ICommandService.class);
		IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getService(IHandlerService.class);
		Command openConfigCmd = commandService.getCommand(OpenOPCUAServerLogHandler.ID);
		ExecutionEvent executionOpenConfigEvent = handlerService.createExecutionEvent(openConfigCmd, new Event());
		try {
			openConfigCmd.executeWithChecks(executionOpenConfigEvent);
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (NotDefinedException e) {
			e.printStackTrace();
		} catch (NotEnabledException e) {
			e.printStackTrace();
		} catch (NotHandledException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Image getLabelImage() {
		return CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_16,
				CommonImagesActivator.LOG);
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
