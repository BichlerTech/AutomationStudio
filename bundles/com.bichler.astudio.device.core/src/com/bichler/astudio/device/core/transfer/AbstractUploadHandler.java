package com.bichler.astudio.device.core.transfer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.service.prefs.Preferences;

import com.bichler.astudio.components.ui.ComponentsUIActivator;
import com.bichler.astudio.device.core.DevCoreActivator;
import com.bichler.astudio.device.core.preference.DevicePreferenceConstants;
import com.bichler.astudio.filesystem.DataHubFileSystem;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.utils.constants.StudioConstants;
import com.bichler.astudio.utils.internationalization.CustomString;

public abstract class AbstractUploadHandler extends AbstractHandler {

	protected static final String BTBIN = "btbin";
	protected static final String COMET = "comet";
	protected static final String DRIVER = "driver";
	protected static final String DRIVERS = "drivers";
	protected static final String HBS = "hbs";
	protected static final String OPC_UA_SERVER = "opc_ua_server";
	protected static final String RUNTIME = "runtime";
	protected static final String VISU = "visu";
	protected static final String WWW = "www";
	protected static final String FILEPOLICY_SEPARATOR = "/";

	private TargetExecutor targets;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final Shell shell = HandlerUtil.getActiveShell(event);
		
		IFileSystem fs = findFileSystem(event);
		AbstractInstallWizard wizard = createWizard(fs);
		WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), wizard);
		int open = dialog.open();
		if (WizardDialog.OK != open) {
			return null;
		}
		
		final IFileSystem filesystem = recatchFilesystem(fs, wizard);
		
		this.targets = wizard.getTargets();
		// Aufbau der Runtime Struktur
					
		// connect
		Job job = new Job(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				this.titleJob())) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					install(monitor, filesystem, shell);
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
					IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					if (window != null) {
						MessageDialog.openError(window.getShell(),
								CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
										"com.bichler.astudio.device.opcua.handler.target.install.monitor.title"),
								CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
										"com.bichler.astudio.device.opcua.handler.target.install.monitor.error.install"));
					}
				} finally {
					monitor.worked(35);
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
		
		return null;
	}
	
	/**
	 * Finds a filesystem used for the wizard. Return NULL if no filesystem is needed.
	 * 
	 * @param event
	 * 
	 * @return Filesystem to be used
	 */
	protected IFileSystem findFileSystem(ExecutionEvent event) {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		Object element = selection.getFirstElement();
		final IFileSystem filesystem = createFileSystem((Preferences) element);
		return filesystem;
	}

	private IFileSystem createFileSystem(Preferences element) {
		IFileSystem filesystem = null;
		int type = ((Preferences) element).getInt(DevicePreferenceConstants.PREFERENCE_DEVICE_FILETYPE, -1);
		switch (type) {
		case 0:
			filesystem = new DataHubFileSystem();
			break;
		case 1:
			filesystem = new SimpleFileSystem();
			break;
		}
		String connectionName = ((Preferences) element).name();
		int timeout = ((Preferences) element).getInt(DevicePreferenceConstants.PREFERENCE_DEVICE_TIMEOUT, -1);
		String host = ((Preferences) element).get(DevicePreferenceConstants.PREFERENCE_DEVICE_HOST, "");
		String user = ((Preferences) element).get(DevicePreferenceConstants.PREFERENCE_DEVICE_USER, "");
		String password = ((Preferences) element).get(DevicePreferenceConstants.PREFERENCE_DEVICE_PASSWORD, "");
		String rootPath = ((Preferences) element).get(DevicePreferenceConstants.PREFERENCE_DEVICE_ROOTPATH, "");
		String separator = ((Preferences) element).get(DevicePreferenceConstants.PREFERENCE_DEVICE_FILESEPARATOR,
				"");
		filesystem.setConnectionName(connectionName);
		filesystem.setHostName(host);
		filesystem.setUser(user);
		filesystem.setPassword(password);
		filesystem.setTargetFileSeparator(separator);
		filesystem.setTimeOut(timeout);
		filesystem.setRootPath(rootPath);
		return filesystem;
	}

	protected String getBIN() {
		return ComponentsUIActivator.getDefault().getPreferenceStore().getString(StudioConstants.BinCommand);
	}

	protected void addDirectoryTransfer(IFileSystem targetFileSystem, String targetDirectoryPath) throws IOException {
		if (!targetFileSystem.isDir(targetDirectoryPath)) {
			targetFileSystem.addDir(targetDirectoryPath);
		}
	}

	protected void addFileTransfer(IFileSystem targetFileSystem, String targetFilePath) throws IOException {
		// remove file if exists
		if (targetFileSystem.isFile(targetFilePath)) {
			targetFileSystem.removeFile(targetFilePath);
		}

		if (!targetFileSystem.isFile(targetFilePath)) {
			targetFileSystem.addFile(targetFilePath);
		}
	}

	protected abstract String descriptionJob();
	
	protected abstract String titleJob();
	
	protected abstract AbstractInstallWizard createWizard(IFileSystem filesystem);

	protected void preInstall(IFileSystem filesystem, File root) throws IOException {
		
	}
	
	protected void postInstall(IFileSystem filesystem, File root) throws IOException {
		
	}
	/**
	 * Upload files to a target device
	 * 
	 * @param monitor
	 * @param fs
	 * @param shell
	 * @return
	 * @throws IOException
	 */
	private Object install(IProgressMonitor monitor, IFileSystem fs, final Shell shell) throws IOException {
		monitor.beginTask(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				descriptionJob()), 100);
		monitor.worked(5);
		boolean isConnected = fs.connect();
		if (!isConnected) {
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					MessageDialog.openError(shell,
							CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
									"com.bichler.astudio.device.opcua.handler.target.install.monitor.title"),
							CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
									"com.bichler.astudio.device.opcua.handler.upload.opc.wizard.target.monitor.error.connection"));
				}
			});
			return Status.OK_STATUS;
		}
		// remove path
		monitor.worked(60);

		preInstall(this.targets.getTargetFileSystem(), this.targets.getRoot());
		
		try {
			if (this.targets != null) {
				this.targets.execute(fs);
			}
			
			postInstall(this.targets.getTargetFileSystem(), this.targets.getRoot());
		} finally {
			if (fs != null) {
				fs.disconnect();
			}
		}

		return Status.OK_STATUS;
	}

	protected void validateTargetFilePathStructure(IFileSystem fs) throws IOException {
		// /hbs/....
		String rootPath = fs.getRootPath();
		String separator = fs.getTargetFileSeparator();

		if (rootPath == null) {
			return;
		}

		if (fs.isDir(rootPath)) {
			return;
		}

		if (separator == null) {
			separator = "";
		}
		// end rootpath with separator
		if (!rootPath.endsWith(separator)) {
			rootPath += separator;
		}
		// find structure
		List<String> subs = new ArrayList<>();
		// initial root
		StringBuilder help = new StringBuilder(separator);
		// find path
		for (int i = 0; i < rootPath.length(); i++) {
			String letter = rootPath.substring(i, i + 1);

			if (letter.equals(separator)) {
				subs.add(help.toString());
				help = new StringBuilder();
				continue;
			}
			help.append(letter);
		}
		// clear
		help = new StringBuilder();
		// add missing directories
		for (String sub : subs) {
			help.append(sub);
			// skip
			if (!fs.isDir(help.toString())) {
				fs.addDir(help.toString());
				// if we are in hbs folder
				if (help.toString().replaceAll(separator, "").compareTo("hbs") == 0) {
					// add default config if not exists
					addDefaultConfig(fs, help);
				}
			}

			if (!help.toString().endsWith(separator)) {
				help.append(separator);
			}
		}

	}
	
	private IFileSystem recatchFilesystem(IFileSystem fs, AbstractInstallWizard wizard) {
		// return filesystem if exists
		if(fs != null) {
			return fs;
		}
		
		// return filesystem from wizard
		return wizard.getTargetFilesystem();
	}

	private void addDefaultConfig(IFileSystem fs, StringBuilder path) {
		if (fs instanceof DataHubFileSystem) {
			if (!path.toString().endsWith(fs.getTargetFileSeparator()))
				path.append(fs.getTargetFileSeparator());
			path.append("conf");
			if (!fs.isDir(path.toString()))
				try {
					fs.addDir(path.toString());
					path.append(fs.getTargetFileSeparator());
					path.append("config.txt");
					if (!fs.isDir(path.toString()))
						fs.addDir(path.toString());
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
		}
	}
}
