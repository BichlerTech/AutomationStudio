package com.bichler.astudio.editor.siemens.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.editor.siemens.xml.SiemensDPEditorImporter;
import com.bichler.astudio.editor.siemens.xml.SiemensDPItem;
import com.bichler.astudio.opcua.handlers.events.AdvancedDriverPersister;
import com.bichler.astudio.opcua.handlers.events.OPCUAValidationDriverParameter;
import com.bichler.astudio.opcua.handlers.validation.AbstractOPCUAValidationHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverDPsModelNode;
import com.bichler.astudio.opcua.widget.NodeToTrigger;

public class OPCUASiemensValidationHandler extends AbstractOPCUAValidationHandler {
	public static final String ID = "com.bichler.astudio.editor.siemens.1.0.0.validate";
	// private static final Logger logger =
	// Logger.getLogger(OPCUASiemensValidationHandler.class);
	private List<NodeToTrigger> triggernodes = new ArrayList<NodeToTrigger>();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (window == null) {
			return null;
		}
		IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			return null;
		}
		OPCUAValidationDriverParameter trigger = getCommandParameter(event);
		// OPCUAValidationDriverParameter trigger =
		// (OPCUAValidationDriverParameter) event
		// .getTrigger();
		executeValidateOPCUADriver(trigger);
		return trigger;
	}

	@Override
	public void onValidateDatapoints(OPCUAValidationDriverParameter trigger, String path) {
		InputStream input = null;
		try {
			String datapointsPath = new Path(path).append("datapoints.com").toOSString();
			if (!trigger.getFilesystem().isFile(datapointsPath)) {
				return;
			}
			input = trigger.getFilesystem().readFile(datapointsPath);
			// dp child node
			OPCUAServerDriverDPsModelNode dpNode = getDriverDPModelNode(trigger);
			// read items from datapoints.com
			SiemensDPEditorImporter importer = new SiemensDPEditorImporter();
			List<SiemensDPItem> items = importer.loadDPs(input,
					ServerInstance.getInstance().getServerInstance().getNamespaceUris());
			// now read all possible triggers
			this.triggernodes.clear();
			String triggerpath = new Path(path).append("triggernodes.com").toOSString();
			if (!trigger.getFilesystem().isFile(triggerpath)) {
				return;
			}
			AdvancedDriverPersister im = new AdvancedDriverPersister();
			this.triggernodes = im.importTriggerNodes(
					ServerInstance.getInstance().getServerInstance().getNamespaceUris(), trigger.getFilesystem(),
					triggerpath);
			// validate items
			boolean valid = true;
			for (SiemensDPItem item : items) {
				valid = validateItem(item);
				if (!valid) {
					break;
				}
			}
			// mark resource
			if (dpNode != null) {
				dpNode.setResourceValid(valid);
			}
			// CSLogActivator.getDefault().getLogger().info("Validate " +
			// trigger.getDrvName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean validateItem(SiemensDPItem item) {
		NodeId nodeId = item.getNodeId();
		if (NodeId.isNull(nodeId)) {
			return false;
		}
		// validate trigger node
		String triggername = item.getTriggerNode();
		if (triggername.isEmpty())
			return true;
		// now we should find the triggernode
		// boolean found = false;
		for (NodeToTrigger tr : triggernodes) {
			if (triggername.compareTo(tr.triggerName) == 0) {
				// trigger found, now ferify if it is activated
				if (tr.active) {
					return true;
				} else
					return false;
			}
		}
		// if(triggernodes.contains(item.getTriggerNode()))
		return false;
	}
}
