package com.bichler.astudio.opcua.handlers.events;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.events.OPCUADPReaderParameter;

public abstract class AbstractOPCUADPReaderHandler extends AbstractHandler {
	public static final String ID = "com.bichler.astudio.editor.";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		OPCUADPReaderParameter trigger = getCommandParameter(event);
		Map<NodeId, Object> dps = new HashMap<>();
		if (!trigger.getFilesystem().isFile(trigger.getPath())) {
			return dps;
		}
		InputStream input = null;
		try {
			input = trigger.getFilesystem().readFile(trigger.getPath());
			List<Object> datapoints = readDatapoints(input);
			// datapoints
			for (Object dp : datapoints) {
				insertDatapoint(dps, dp);
			}
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
			}
		}
		return dps;
	}

	protected OPCUADPReaderParameter getCommandParameter(ExecutionEvent event) {
		IEvaluationContext evalCxt = (IEvaluationContext) event.getApplicationContext();
		return (OPCUADPReaderParameter) evalCxt.getVariable(OPCUADPReaderParameter.PARAMETER_ID);
	}

	protected abstract void insertDatapoint(Map<NodeId, Object> datapoints, Object dp);

	protected abstract List<Object> readDatapoints(InputStream input);
}
