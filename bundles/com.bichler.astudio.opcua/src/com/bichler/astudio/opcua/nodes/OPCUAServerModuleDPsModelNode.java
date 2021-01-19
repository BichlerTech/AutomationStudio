package com.bichler.astudio.opcua.nodes;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCUAServerModuleDPsModelNode extends OPCUAServerModuleModelNode implements IValidationNode {
	
	@Override
	public void nodeDBLClicked() {
		/**
		 * call open script editor
		 */
		/* call select command */
		ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getService(ICommandService.class);
		IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getService(IHandlerService.class);
		Command openModuleDPCmd = commandService.getCommand("com.bichler.astudio.commands.module.openopcuadpconfig");
		ExecutionEvent executionOpenModuleDPEvent = handlerService.createExecutionEvent(openModuleDPCmd, new Event());
		try {
			openModuleDPCmd.executeWithChecks(executionOpenModuleDPEvent);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
	}

	@Override
	public Image getLabelImage() {
		if (isResourceValid()) {
			return StudioImageActivator.getImage(StudioImages.ICON_OPCUADRIVERDPS);
		} else {
			return getInvalidImage();
		}
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

	@Override
	public Image getInvalidImage() {
		return CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_16,
				CommonImagesActivator.X_DATAPOINTS);
	}
}
