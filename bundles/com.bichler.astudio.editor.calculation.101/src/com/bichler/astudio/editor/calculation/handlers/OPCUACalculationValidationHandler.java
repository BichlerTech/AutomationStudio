package com.bichler.astudio.editor.calculation.handlers;

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

import com.bichler.opc.driver.calculation.CalculationDP;
import com.bichler.opc.driver.calculation.CalculationExpression;
import com.bichler.opc.driver.calculation.CalculationNode;
import com.bichler.opc.driver.calculation.CalculationObject;
import com.bichler.opc.driver.calculation.targets.CalculationTarget;
import com.bichler.astudio.editor.calculation.model.CalculationDPEditorImporter;
import com.bichler.astudio.opcua.handlers.events.OPCUAValidationDriverParameter;
import com.bichler.astudio.opcua.handlers.validation.AbstractOPCUAValidationHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverDPsModelNode;

public class OPCUACalculationValidationHandler extends AbstractOPCUAValidationHandler {

	public static final String ID = "com.bichler.astudio.editor.calculation.1.0.1.validate";
	// private static final Logger logger =
	// Logger.getLogger(OPCUACalculationValidationHandler.class);

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
		// input stream of datapoints.com file
		InputStream input = null;
		try {
			// path of datapoints.com file
			String datapointsPath = new Path(path).append("datapoints.com").toOSString();
			// datapoints.com file available
			if (!trigger.getFilesystem().isFile(datapointsPath)) {
				// skip
				return;
			}
			// open stream
			input = trigger.getFilesystem().readFile(datapointsPath);
			// driver root node
			// OPCUAServerModelNode node = trigger.getModelNode();
			// dp child node
			OPCUAServerDriverDPsModelNode dpNode = getDriverDPModelNode(trigger);

			// read items from datapoints.com
			CalculationDPEditorImporter importer = new CalculationDPEditorImporter();
			List<CalculationDP> items = importer.loadDPs(input,
					ServerInstance.getInstance().getServerInstance().getNamespaceUris(), null, -1);
			// validate items
			boolean valid = true;
			for (CalculationDP item : items) {
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

	private boolean validateItem(CalculationDP item) {
		CalculationTarget target = item.getTarget();
		// no target
		if (target == null) {
			return false;
		}
		// no target id
		if (NodeId.isNull(target.getTargetId())) {
			return false;
		}

		// validate empty expression
		List<CalculationObject> expressions = item.getCalculationExpressions();
		if (expressions.isEmpty()) {
			return false;
		}
		// validate calculation item
		for (CalculationObject cObj : expressions) {
			// validate expression
			if (cObj instanceof CalculationExpression) {
				String content = ((CalculationExpression) cObj).getContent();
				if (content == null) {
					return false;
				}
			}
			// validate opc ua node
			else if (cObj instanceof CalculationNode) {
				String content = ((CalculationNode) cObj).getContent();
				if (content == null) {
					return false;
				}
			}
		}

		return true;
	}

}
