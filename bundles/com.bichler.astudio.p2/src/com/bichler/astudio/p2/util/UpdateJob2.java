package com.bichler.astudio.p2.util;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.Update;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.statushandlers.StatusManager;

import com.bichler.astudio.p2.P2Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class UpdateJob2 extends Job
{
	  private IProvisioningAgent agent;
	  private IStatus result;
	private URI[] uris;

	  /**
	   * Base constructor.
	   * 
	   * @param agent
	   *          Provisioning agent
	   */
	  public UpdateJob2(IProvisioningAgent agent, URI...uris)
	  {
	    super("Checking for updates");
	    this.agent = agent;
	    this.uris = uris;
	  }

	  @Override
	  protected IStatus run(IProgressMonitor monitor)
	  {
		  ProvisioningSession session = new ProvisioningSession(agent);
			UpdateOperation operation = new UpdateOperation(session);
			operation.getProvisioningContext().setArtifactRepositories(this.uris);
			operation.getProvisioningContext().setMetadataRepositories(this.uris);
			SubMonitor sub = SubMonitor.convert(monitor, "Checking for application updates...", 200);
			IStatus status = operation.resolveModal(sub.newChild(100));
			
			ProvisioningJob provisioningJob = operation.getProvisioningJob(monitor);
			result = provisioningJob.runModal(monitor);
			Update[] updates = operation.getPossibleUpdates();
		  
		  
	    /*result = P2Util.checkForUpdates(agent, arg0);
	    if (result.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE)
	    {
	      popUpInformation("Nothing to update!");
	    }
	    else
	    {
	      installUpdates();
	    }*/
	    return result;
	  }

	  /**
	   * Pop up for updates installation and, if accepted, install updates.
	   */
	  private void installUpdates()
	  {
	    Display.getDefault().asyncExec(new Runnable()
	    {
	      @Override
	      public void run()
	      {
	        boolean install = MessageDialog.openQuestion(null,
	            CustomString.getString(P2Activator.getDefault().RESOURCE_BUNDLE, "com.bichler.astudio.p2.updatejob.title"),
	            CustomString.getString(P2Activator.getDefault().RESOURCE_BUNDLE,
	                "com.bichler.astudio.p2.updatejob.message"));
	        if (install)
	        {
	          ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
	          try
	          {
	            dialog.run(true, true, new IRunnableWithProgress()
	            {
	              @Override
	              public void run(IProgressMonitor arg0) throws InvocationTargetException, InterruptedException
	              {
	                P2Util.installUpdates(agent, arg0);
	                PlatformUI.getWorkbench().restart();
	              }
	            });
	          }
	          catch (Exception e)
	          {
	            e.printStackTrace();
	            result = new Status(Status.ERROR, P2Activator.PLUGIN_ID, "Update failed!", e);
	            StatusManager.getManager().handle(result);
	          }
	        }
	      }
	    });
	  }

	  /**
	   * Show a message dialog.
	   * 
	   * @param message
	   */
	  private void popUpInformation(final String message)
	  {
	    Display.getDefault().asyncExec(new Runnable()
	    {
	      @Override
	      public void run()
	      {
	        MessageDialog.openInformation(null, "Updates", message);
	      }
	    });
	  }
	}