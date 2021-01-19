package com.bichler.astudio.opcua.handlers.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.events.OPCUADPWriterParameter;

import opc.sdk.core.node.Node;

public abstract class AbstractOPCUADPWriterHandler extends AbstractHandler {
	public static final String ID = "com.bichler.astudio.editor.";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		OPCUADPWriterParameter trigger = getCommandParameter(event);

		Node[] nodes = trigger.getNodes();
		Map<NodeId, Object> dps = trigger.getDatapointList();
		IFileSystem fs = trigger.getFilesystem();
		String path = trigger.getPath();
		List<Object> datapoints = new ArrayList<>();
		// match nodes with device nodes
		if (nodes != null) {
			for (Node node : nodes) {
				NodeId nodeId = node.getNodeId();
				// no matching nodeid in mapping
				if (NodeId.isNull(nodeId)) {
					continue;
				}
				// no datapoint node
				Object value = dps.get(nodeId);
				if (value == null) {
					continue;
				}
				datapoints.add(value);
			}
		}
		// no device nodes
		else {
			datapoints.addAll(dps.values());
		}
		// write datapoints depending on device
		beginWriteDPs(fs, path, datapoints);
		return datapoints;
	}

	protected OPCUADPWriterParameter getCommandParameter(ExecutionEvent event) {
		IEvaluationContext evalCxt = (IEvaluationContext) event.getApplicationContext();
		OPCUADPWriterParameter trigger = (OPCUADPWriterParameter) evalCxt
				.getVariable(OPCUADPWriterParameter.PARAMETER_ID);
		return trigger;
	}

	private void beginWriteDPs(IFileSystem fs, String path, List<Object> datapoints) {
		// external path
		if (!fs.isFile(path)) {
			try {
				fs.addFile(path);
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
		}

		write(fs, path, datapoints);

	}

	protected abstract void write(IFileSystem fs, String path, List<Object> datapoints);
}
