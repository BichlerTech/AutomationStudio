package com.bichler.astudio.editor.pubsub.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.editor.pubsub.PubSubDPEditor;
import com.bichler.astudio.opcua.handlers.AbstractOPCCreateDriverModel;

public class AddOPCUAPubSubPublishedDataSetHandler extends AbstractOPCCreateDriverModel
{
  public static final String ID = "com.bichler.astudio.editor.pubsub.1.0.0.addpubsubpublisheddataset";

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException
  { 
	  PubSubDPEditor pubsub = null;
	  IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
	  IEditorPart editor = page.getActiveEditor();
	  
	  if(editor instanceof PubSubDPEditor) {
		  pubsub = (PubSubDPEditor)editor;
		  pubsub.addPubSubPublishedDataSet();
	  }
    return null;
  }
}
