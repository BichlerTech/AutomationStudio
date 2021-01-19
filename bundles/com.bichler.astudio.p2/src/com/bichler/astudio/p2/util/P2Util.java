package com.bichler.astudio.p2.util;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.ui.IWorkbench;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.bichler.astudio.p2.P2Activator;

/**
 * Source code from
 * <ul>
 * <li><a href=
 * "http://wiki.eclipse.org/Equinox/p2/Adding_Self-Update_to_an_RCP_Application">Equinox
 * Wiki pages, Adding self update</a></li>
 * </ul>
 * 
 * @author mahieddine.ichir@free.fr
 */
public class P2Util {

	  private static String UPDATE_SITE = "http://localhost/repository";
	  
	  /*public static void setRepositories() throws InvocationTargetException {
	    try {
	      final MetadataRepositoryElement element = new MetadataRepositoryElement(null, new URI(UPDATE_SITE), true);
	      ElementUtils.updateRepositoryUsingElements(new MetadataRepositoryElement[] {element}, null);
	    } catch (URISyntaxException e) {
	      e.printStackTrace();
	      throw new InvocationTargetException(e);
	    }
	  }*/
	
	public static void workbenchStartup(IWorkbench workbench) throws InvocationTargetException {
		BundleContext bundleContext = P2Activator.getDefault().getBundle().getBundleContext();
		ServiceReference<IProvisioningAgent> serviceReference = bundleContext
				.getServiceReference(IProvisioningAgent.class);
		
		IProvisioningAgent provisioningAgent = bundleContext.getService(serviceReference);
		if (provisioningAgent == null) {
			System.out.println(">> no agent loaded!");
			return;
		}
		/*
		UpdateHandler uh = new UpdateHandler();
		uh.execute(provisioningAgent, workbench);
		
		//Load repository manager
		IMetadataRepositoryManager metadataManager = (IMetadataRepositoryManager) provisioningAgent.getService(IMetadataRepositoryManager.SERVICE_NAME);
		if(metadataManager == null) {
		Throwable throwable = new Throwable( " Metadata Repository Manager");
		throwable.fillInStackTrace();
		throw new InvocationTargetException(throwable);
		}

		//Load artifact manager
		IArtifactRepositoryManager artifactManager = (IArtifactRepositoryManager) provisioningAgent.getService(IArtifactRepositoryManager.SERVICE_NAME);
		if(artifactManager == null) {
		Throwable throwable = new Throwable( " Artifact Repository Manager");
		throwable.fillInStackTrace();
		throw new InvocationTargetException(throwable);
		}
		
	
		//Load repo
		try {
			// /features
			//String urlString = "http://localhost/repository";
			String urlString = "jar:file:/C:/xampp/htdocs/repository/UpdateSite.zip!/";
			URL url = new URL(urlString);
	        System.out.println("URL is: " + url.toString());

	        URI uri = url.toURI();
	        System.out.println("URI is: " + uri.toString());

	        if(uri.getAuthority() != null && uri.getAuthority().length() > 0) {
	            // Hack for UNC Path
	            uri = (new URL("file://" + urlString.substring("file:".length()))).toURI();
	        }
		        
			URI repoLocation = uri;
			
			UpdateJob2 job2 = new UpdateJob2(provisioningAgent, repoLocation);
			job2.schedule();
			
			
			
			
			metadataManager.loadRepository(repoLocation, null);
			artifactManager.loadRepository(repoLocation, null);
		} catch (ProvisionException pe) {
			throw new InvocationTargetException(pe);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
		
		// Adding the repositories to explore
		// if (! P2Util.addRepository(agent,
		// "http://bichler.tech/automation_studio/update")) {
		// System.out.println(">> could not add repostory!");
		// return;
		// }
		// scheduling job for updates
	
		
		UpdateJob updateJob = new UpdateJob(provisioningAgent);
		updateJob.schedule();
	}

	/**
	 * Check for application updates.
	 * 
	 * @param agent
	 * @param monitor
	 * @return
	 * @throws OperationCanceledException
	 */
	public static IStatus checkForUpdates(IProvisioningAgent agent, IProgressMonitor monitor)
			throws OperationCanceledException {
		System.out.println(">> checkForUpdates");
		ProvisioningSession session = new ProvisioningSession(agent);
		UpdateOperation operation = new UpdateOperation(session);
		SubMonitor sub = SubMonitor.convert(monitor, "Checking for application updates...", 200);
		return operation.resolveModal(sub.newChild(100));
	}

	/**
	 * Download and install application updates.
	 * 
	 * @param agent
	 * @param monitor
	 * @return
	 * @throws OperationCanceledException
	 */
	public static IStatus installUpdates(IProvisioningAgent agent, IProgressMonitor monitor)
			throws OperationCanceledException {
		ProvisioningSession session = new ProvisioningSession(agent);
		UpdateOperation operation = new UpdateOperation(session);
		SubMonitor sub = SubMonitor.convert(monitor, "Installing updates ...", 200);
		operation.resolveModal(sub.newChild(100));
		ProvisioningJob job = operation.getProvisioningJob(monitor);
		return job.runModal(sub.newChild(100));
	}

	/**
	 * Add a repository to declared updates repositories.
	 * 
	 * @param repo
	 * @return
	 */
	public static boolean addRepository(IProvisioningAgent agent, String repo) {
		System.out.println(">> adding repository " + repo);
		IMetadataRepositoryManager metadataManager = (IMetadataRepositoryManager) agent
				.getService(IMetadataRepositoryManager.SERVICE_NAME);
		IArtifactRepositoryManager artifactManager = (IArtifactRepositoryManager) agent
				.getService(IArtifactRepositoryManager.SERVICE_NAME);
		if (metadataManager == null) {
			System.out.println("metadataManager is null!!!");
			return false;
		}
		if (artifactManager == null) {
			System.out.println("artifactManager is null!!!");
			return false;
		}
		try {
			URI uri = new URI(repo);
			metadataManager.addRepository(uri);
			artifactManager.addRepository(uri);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
