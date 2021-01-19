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
import com.bichler.astudio.connections.nodes.HostConnectionModelNode;
import com.bichler.astudio.connections.nodes.HostConnectionsModelNode;
import com.bichler.astudio.connections.wizards.NewHostConnectionWizard;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.navigation.views.NavigationView;

public class HostPropertyHandler extends AbstractHandler
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
    StudioModelNode node = (StudioModelNode) selection.getFirstElement();
    if (node instanceof HostConnectionModelNode)
    {
      // Instantiates the wizard container with the wizard and opens it
      WizardDialog dialog = new WizardDialog(page.getActivePart().getSite().getShell(), wizard);
      dialog.create();
      // set already known properties
      IFileSystem fs = ((HostConnectionModelNode) node).getFilesystem();
      IFileSystem filesystem = null;
      if (fs instanceof SimpleFileSystem)
      {
        filesystem = new SimpleFileSystem();
      }
      else
      {
        filesystem = new DataHubFileSystem();
      }
      filesystem.setConnectionName(fs.getConnectionName());
      filesystem.setTimeOut(fs.getTimeOut());
      filesystem.setHostName(fs.getHostName());
      filesystem.setUser(fs.getUser());
      filesystem.setPassword(fs.getPassword());
      filesystem.setJavaPath(fs.getJavaPath());
      filesystem.setJavaArg(fs.getJavaArg());
      filesystem.setRootPath(fs.getRootPath());
      filesystem.setTargetFileSeparator(fs.getTargetFileSeparator());
      wizard.setTargetFilesystem(filesystem);
      wizard.setEdit();
      if (dialog.open() == Dialog.OK)
      {
        if (filesystem.getClass() != fs.getClass())
        {
          ((HostConnectionModelNode) node).setFilesystem(filesystem);
        }
        else
        {
          fs.setConnectionName(filesystem.getConnectionName());
          fs.setTimeOut(filesystem.getTimeOut());
          fs.setHostName(filesystem.getHostName());
          fs.setUser(filesystem.getUser());
          fs.setPassword(filesystem.getPassword());
          fs.setTargetFileSeparator(filesystem.getTargetFileSeparator());
          fs.setJavaPath(filesystem.getJavaPath());
          fs.setJavaArg(filesystem.getJavaArg());
          fs.setRootPath(filesystem.getRootPath());
        }
        NavigationView view = (NavigationView) page.findView(NavigationView.ID);
        node.setChildren(null);
        view.getViewer().refresh(node);
        // now export config
        ((HostConnectionsModelNode) node.getParent()).getConnectionsManager().exportHosts();
      }
    }
    return null;
  }
}
