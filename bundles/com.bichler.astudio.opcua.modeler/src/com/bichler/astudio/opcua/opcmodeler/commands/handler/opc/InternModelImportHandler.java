package com.bichler.astudio.opcua.opcmodeler.commands.handler.opc;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.bichler.astudio.opcua.opcmodeler.Activator;

public class InternModelImportHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.opcua.modeler.intern.model.import";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		File nodeset2 = Activator.getDefault().getNodeset2File();
		
		
		return null;
	}

}
