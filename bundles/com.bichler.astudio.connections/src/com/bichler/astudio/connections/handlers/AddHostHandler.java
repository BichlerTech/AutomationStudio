package com.bichler.astudio.connections.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.filesystem.DataHubFileSystem;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.connections.ConnectionsHostManager;
import com.bichler.astudio.connections.nodes.HostConnectionsModelNode;
import com.bichler.astudio.connections.wizards.NewHostConnectionWizard;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.navigation.views.NavigationView;

public class AddHostHandler extends AbstractHandler
{
  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
    TreeSelection selection = (TreeSelection) HandlerUtil.getActiveWorkbenchWindow(event).getSelectionService()
        .getSelection(NavigationView.ID);
    NewHostConnectionWizard wizard = new NewHostConnectionWizard();
    if ((selection instanceof IStructuredSelection) || (selection == null))
      wizard.init(page.getWorkbenchWindow().getWorkbench(), (IStructuredSelection) selection);
    // Instantiates the wizard container with the wizard and opens it
    WizardDialog dialog = new WizardDialog(page.getActivePart().getSite().getShell(), wizard);
    dialog.create();
    if (dialog.open() == Dialog.OK)
    {
      IFileSystem fs = null;
      if (wizard.getFilesystem() instanceof SimpleFileSystem)
      {
        fs = new SimpleFileSystem();
      }
      else
      {
        fs = new DataHubFileSystem();
      }
      fs.setConnectionName(wizard.getFilesystem().getConnectionName());
      fs.setTimeOut(wizard.getFilesystem().getTimeOut());
      fs.setHostName(wizard.getFilesystem().getHostName());
      fs.setUser(wizard.getFilesystem().getUser());
      fs.setPassword(wizard.getFilesystem().getPassword());
      fs.setTargetFileSeparator(wizard.getFilesystem().getTargetFileSeparator());
      fs.setJavaPath(wizard.getFilesystem().getJavaPath());
      fs.setJavaArg(wizard.getFilesystem().getJavaArg());
      fs.setRootPath(wizard.getFilesystem().getRootPath());
      StudioModelNode node = (StudioModelNode) selection.getFirstElement();
      if (node instanceof HostConnectionsModelNode)
      {
        HostConnectionsModelNode c = (HostConnectionsModelNode) node;
        c.getChildrenList().clear();
        // if we have no connection manager, so we add one
        if (c.getConnectionsManager() == null)
        {
          c.setConnectionsManager(new ConnectionsHostManager());
        }
        c.getConnectionsManager().getStudioConnections().addConnection(fs.getConnectionName(), fs);
        ((HostConnectionsModelNode) node).getConnectionsManager().exportHosts();
      }
      NavigationView view = (NavigationView) page.findView(NavigationView.ID);
      node.setChildren(null);
      view.getViewer().refresh(node);
    }
    return null;
  }
}
