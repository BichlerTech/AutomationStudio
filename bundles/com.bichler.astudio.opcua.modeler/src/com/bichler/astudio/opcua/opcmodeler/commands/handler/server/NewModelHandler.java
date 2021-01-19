package com.bichler.astudio.opcua.opcmodeler.commands.handler.server;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class NewModelHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ServerInstance.resetServer();
		return null;
	}
}
