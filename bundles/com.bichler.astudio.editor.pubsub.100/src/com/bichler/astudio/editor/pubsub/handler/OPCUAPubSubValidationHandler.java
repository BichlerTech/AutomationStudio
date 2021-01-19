package com.bichler.astudio.editor.pubsub.handler;

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

import com.bichler.astudio.opcua.handlers.events.OPCUAValidationDriverParameter;
import com.bichler.astudio.opcua.handlers.validation.AbstractOPCUAValidationHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverDPsModelNode;

public class OPCUAPubSubValidationHandler extends AbstractOPCUAValidationHandler
{
  public static final String ID = "com.bichler.astudio.editor.events.1.0.0.validate";
  // private static final Logger logger = Logger
  // .getLogger(OPCUAEventsValidationHandler.class);

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
    if (window == null)
    {
      return null;
    }
    IWorkbenchPage page = window.getActivePage();
    if (page == null)
    {
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
  public void onValidateDatapoints(OPCUAValidationDriverParameter trigger, String path)
  {
    InputStream input = null;
    try
    {
      String datapointsPath = new Path(path).append("datapoints.com").toOSString();
      if (!trigger.getFilesystem().isFile(datapointsPath))
      {
        return;
      }
      input = trigger.getFilesystem().readFile(datapointsPath);
      // dp child node
      OPCUAServerDriverDPsModelNode dpNode = getDriverDPModelNode(trigger);
      // read items from datapoints.com
//      EventsImporter importer = new EventsImporter();
//      List<EventDpItemModel> items = importer.loadDPs(input,
//          ServerInstance.getInstance().getServerInstance().getNamespaceUris());
//      // validate items
//      boolean valid = true;
//      for (EventDpItemModel item : items)
//      {
//        valid = validateItem(item);
//        if (!valid)
//        {
//          break;
//        }
//        for (AbstractEventModel child : item.getChildren())
//        {
//          valid = validateItem(child);
//          if (!valid)
//          {
//            break;
//          }
//        }
//      }
      // mark resource
//      if (dpNode != null)
//      {
//        dpNode.setResourceValid(valid);
//      }
      // CSLogActivator.getDefault().getLogger().info("Validate " +
      // trigger.getDrvName());
      // LogActivator.getDefault().getLogManager()
      // .logInfo("Validate " + trigger.getDrvName());
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (input != null)
      {
        try
        {
          input.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
  }

//  private boolean validateItem(AbstractEventModel item)
//  {
//    if (item instanceof EventDpItemModel)
//    {
//      NodeId nodeId = ((EventDpItemModel) item).getNodeId();
//      if (NodeId.isNull(nodeId))
//      {
//        return false;
//      }
//    }
//    // else if (item instanceof EventDpSyntax) {
//    // NodeId nodeId = ((EventDpSyntax) item).getSourceId();
//    // if (NodeId.isNull(nodeId)) {
//    // return false;
//    // }
//    // NodeId sourceId = ((EventDpSyntax) item).get
//    // if (NodeId.isNull(sourceId)) {
//    // return false;
//    // }
//    // }
//    return true;
//  }
}
