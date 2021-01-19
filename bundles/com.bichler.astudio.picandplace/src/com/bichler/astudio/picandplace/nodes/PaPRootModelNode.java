package com.bichler.astudio.picandplace.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

import com.bichler.astudio.connections.ConnectionsHostManager;
import com.bichler.astudio.connections.nodes.HostConnectionsModelNode;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.picandplace.PaPActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class PaPRootModelNode extends HostConnectionsModelNode
{
  // protected Comet_ResourceManager manager = null;
  public PaPRootModelNode()
  {
    super();
  }

  @Override
  public void nodeDBLClicked()
  {
	// try {
	    ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
	        .getService(ICommandService.class);
	    IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
	        .getService(IHandlerService.class);
	    Command openCmd = commandService.getCommand("com.bichler.astudio.pickandplace.openPaPEditor");
	    Event evt = new Event();
	    ExecutionEvent executionOpenEvent = handlerService.createExecutionEvent(openCmd, evt);
	    try
	    {
	      openCmd.executeWithChecks(executionOpenEvent);
	    }
	    catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e)
	    {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
  }

  @Override
  public Image getLabelImage()
  {
    return StudioImageActivator.getImage(StudioImages.ICON_DATASERVER);
  }

  @Override
  public String getLabelText()
  {
    return CustomString.getString(PaPActivator.getDefault().RESOURCE_BUNDLE,
        this.getClass().getSimpleName() + ".LabelText");
  }

  @Override
  public Object[] getChildren()
  {
    if (this.getChildrenList() != null)
    {
      return this.getChildrenList().toArray();
    }
    if (connectionsManager == null)
    {
      connectionsManager = new ConnectionsHostManager();
      IPreferenceStore tsnstore = PaPActivator.getDefault().getPreferenceStore();
//      String runtimeFolder = tsnstore.getString(TSNConstants.ASTSNRuntime);
//      String projects = tsnstore.getString(TSNConstants.ASTSNProjectsPath);
//      connectionsManager.importHostsFromRuntimeStructure(runtimeFolder, projects);
    }
    List<StudioModelNode> children = new ArrayList<StudioModelNode>();
    if (connectionsManager.getStudioConnections() != null)
    {
//      for (IFileSystem fs : connectionsManager.getStudioConnections().getConnections().values())
//      {
//        TSNProjectModelNode node = new TSNProjectModelNode();
//        node.setServerName(fs.getConnectionName());
//        node.setFilesystem(fs);
//        children.add(node);
//      }
    }
    this.setChildren(children);
    return this.getChildrenList().toArray();
  }

  @Override
  public void refresh()
  {
    super.refresh();
    setConnectionsManager(null);
  }
}
