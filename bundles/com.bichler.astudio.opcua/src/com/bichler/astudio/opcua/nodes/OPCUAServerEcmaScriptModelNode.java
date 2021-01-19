package com.bichler.astudio.opcua.nodes;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;

public class OPCUAServerEcmaScriptModelNode extends OPCUAServerEcmaIntervalScriptsModelNode {
	private String scriptName = "";

	public OPCUAServerEcmaScriptModelNode() {
		super();
		// children = new ArrayList<VisuModelNode>();
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	private int interval = -1;

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	@Override
	public void nodeDBLClicked() {
		/* call select command */
		ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getService(ICommandService.class);
		IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getService(IHandlerService.class);
		Command openScriptCmd = commandService.getCommand("com.bichler.astudio.commands.openopcuaserverecmascript");
//    OpenOPCUAEcmaScriptEvent evt = new OpenOPCUAEcmaScriptEvent();
		// IPreferenceStore store =
		// ComponentsActivator.getDefault().getPreferenceStore();
		// IPreferenceStore hmistore =
		// HMIActivator.getDefault().getPreferenceStore();
//    String filepath = new Path(filesystem.getHMIRootPath()).append("scripts").append(scriptName + ".es").toOSString();
//     evt.setFilePath(filepath);
		ExecutionEvent executionOpenScriptEvent = handlerService.createExecutionEvent(openScriptCmd, null);
		try {
			openScriptCmd.executeWithChecks(executionOpenScriptEvent);
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
		return StudioImageActivator.getImage(StudioImages.ICON_SCRIPTS);
	}

	@Override
	public String getLabelText() {
		return this.getScriptName();
	}

	@Override
	public Object[] getChildren() {
		return new Object[0];
	}
}
