package com.bichler.astudio.editor.aggregated.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.editor.aggregated.dp.AggregatedDPItem;
import com.bichler.astudio.editor.aggregated.model.AggregatedDPEditorImporter;
import com.bichler.astudio.editor.aggregated.model.AggregatedDpModelNode;
import com.bichler.astudio.opcua.handlers.events.OPCUAValidationDriverParameter;
import com.bichler.astudio.opcua.handlers.validation.AbstractOPCUAValidationHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverDPsModelNode;

public class OPCUAAggregatedValidationHandler extends AbstractOPCUAValidationHandler {

	public static final String ID = "com.bichler.astudio.editor.aggregated.1.0.0.validate";
	// private static final Logger logger = Logger
	// .getLogger(OPCUAAggregatedValidationHandler.class);

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
	public void onValidateSettings(OPCUAValidationDriverParameter trigger, String driverpath) {
		// TODO Auto-generated method stub

		// super.onValidateSettings(trigger, driverpath);

		// read certificate validate expire time
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
			OPCUAServerDriverDPsModelNode dpNode = getDriverDPModelNode(trigger);

			// read items from datapoints.com
			AggregatedDPEditorImporter importer = new AggregatedDPEditorImporter();
			List<AggregatedDpModelNode> items = importer.loadDPs(input,
					ServerInstance.getInstance().getServerInstance().getNamespaceUris());
			// validate items
			boolean valid = true;
//			int index = 0;
			for (AggregatedDpModelNode item : items) {
				valid = validateItem(item);
				// if (!valid) {
				// CSLogActivator.getDefault().getLogger().error("Validating
				// row:" + index
				// + " encounters a problem!");
				// break;
				// }
//				index++;
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

	private boolean validateItem(AggregatedDpModelNode item) {
		AggregatedDPItem dp = item.getDPItem();

		NodeId serverId = dp.getServerNodeId();
		if (NodeId.isNull(serverId)) {
			return false;
		}
		NodeId targetId = dp.getTargetNodeId();
		if (NodeId.isNull(targetId)) {
			String loadedId = dp.getLoadedTargetNodeId();
			if (loadedId != null && !loadedId.isEmpty()) {
				return true;
			}
			return false;
		}
		return true;
	}

}
