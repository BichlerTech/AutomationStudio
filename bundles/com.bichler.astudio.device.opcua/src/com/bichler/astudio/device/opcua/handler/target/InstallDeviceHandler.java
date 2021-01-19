package com.bichler.astudio.device.opcua.handler.target;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
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

import com.bichler.astudio.device.core.preference.DevicePreferenceConstants;
import com.bichler.astudio.device.core.transfer.AbstractInstallWizard;
import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.device.opcua.wizard.DeviceInstallWizard;
import com.bichler.astudio.filesystem.DataHubFileSystem;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.utils.activator.InternationalActivator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.device.core.transfer.AbstractUploadHandler;

public class InstallDeviceHandler extends AbstractUploadHandler {
	enum StudioRuntimes {
		opcua;
	}

	public static final String ID = "com.bichler.astudio.device.opcua.install";
	private Logger logger = Logger.getLogger(getClass().getName());
	private final int BUFFER = 2048;
	private Object[] selectedDrivers = new Object[0];

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		final Shell shell = HandlerUtil.getActiveShell(event);
		Object element = selection.getFirstElement();
		DeviceInstallWizard wizard = new DeviceInstallWizard();
		WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), wizard);
		int open = dialog.open();
		if (WizardDialog.OK != open) {
			return null;
		}
		final boolean isInternal = wizard.isInternalRuntime();
		final String externalPath = wizard.getExternalPath();
		selectedDrivers = wizard.getSelectedDrivers();
		// Aufbau der Runtime Struktur
		IFileSystem filesystem = null;
		if (element instanceof Preferences) {
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
			// connect
			final IFileSystem fs = filesystem;
			Job job = new Job(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
					"com.bichler.astudio.device.opcua.handler.target.install.job.runtime")) {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						installOPCUaRuntime(monitor, fs, shell, isInternal, externalPath);
					} catch (IOException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
						IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
						if (window != null) {
							MessageDialog.openError(window.getShell(),
									CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
											"com.bichler.astudio.device.opcua.handler.target.install.monitor.title"),
									CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
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
		}
		return null;
	}

	/**
	 * install opc ua server runtime to remote or local device
	 * 
	 * @param monitor
	 * @param fs
	 * @param shell
	 * @param isInternal
	 * @param externalPath
	 * @return
	 * @throws IOException
	 */
	private Object installOPCUaRuntime(IProgressMonitor monitor, IFileSystem fs, final Shell shell, boolean isInternal,
			String externalPath) throws IOException {
		monitor.beginTask(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.target.install.monitor.upload"), 100);
		monitor.worked(5);
		boolean isConnected = fs.connect();
		if (!isConnected) {
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					MessageDialog.openError(shell,
							CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
									"com.bichler.astudio.device.opcua.handler.target.install.monitor.title"),
							CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
									"com.bichler.astudio.device.opcua.handler.upload.opc.wizard.target.monitor.error.connection"));
				}
			});
			return Status.OK_STATUS;
		}
		// remove path
		monitor.worked(60);
		// stop watchdog on target
		if (fs instanceof DataHubFileSystem) {
			fs.execCommand("sh /hbin/ciwd.sh stop");
			// give server time to shutdown
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
			fs.execCommand("echo 4 > /cometintern/watchdog/server.state");
		}
		File opcRuntimeRoot = null;
		// export internal runtime
		if (isInternal) {
			opcRuntimeRoot = DeviceActivator.getDefault().getOPCRuntimeFile();
		}
		// use external runtime
		else {
			try (FileInputStream fis = new FileInputStream(externalPath);
					ZipInputStream zin = new ZipInputStream(new BufferedInputStream(fis));) {

				Path extPath = new Path(externalPath);
				IPath dstPath = extPath.removeLastSegments(1);
				ZipEntry entry = null;
				while ((entry = zin.getNextEntry()) != null) {
					int count;
					byte[] data = new byte[BUFFER];

					// create new directory
					if (entry.isDirectory()) {
						File dict = new File(dstPath.append(entry.getName()).toOSString());
						if (!dict.exists()) {
							dict.mkdir();
						}
						// fetch root runtime directory
						if (opcRuntimeRoot == null) {
							opcRuntimeRoot = dict;
						}
					} else {
						File file = new File(dstPath.append(entry.getName()).toOSString());
						if (!file.exists()) {
							file.createNewFile();
						}
						try (FileOutputStream fos = new FileOutputStream(file);
								BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);) {
							while ((count = zin.read(data, 0, BUFFER)) != -1) {
								dest.write(data, 0, count);
							}
							dest.flush();
						}

					}
				}
			}

		}
		// initializes device root structure if needed
		validateTargetFilePathStructure(fs);
		// add default structure
		addDefaultStructure(fs);
		// clear runtime folder
		cleanRuntime(fs);
		copyFiles(true, fs, opcRuntimeRoot, "");
		cleanupIfNeeded(isInternal, externalPath, opcRuntimeRoot);
		// now copy all selected drivers
		this.copyDrivers(fs);
		if (fs instanceof DataHubFileSystem) {
			fs.execCommand("echo 0 > /cometintern/watchdog/server.state");
		}
		return Status.OK_STATUS;
	}

	private void cleanupIfNeeded(boolean isInternal, String externalPath, File opcRuntimeRoot) {
		if (!isInternal) {
			removeCascade(opcRuntimeRoot.getPath());
		}
	}

	/**
	 * Creates default runtime folder structure.
	 * 
	 * @param fs target filesystem
	 * @throws IOException
	 */
	private void addDefaultStructure(IFileSystem fs) throws IOException {
		if (fs instanceof DataHubFileSystem) {
			String rootPathTarget = getRootPath(fs);
			// check if we have the main structure
			if (!fs.isDir(rootPathTarget + HBS)) {
				fs.addDir(rootPathTarget + HBS);
			}
			// hbs/comet
			if (!fs.isDir(rootPathTarget + HBS + fs.getTargetFileSeparator() + COMET)) {
				fs.addDir(rootPathTarget + HBS + fs.getTargetFileSeparator() + COMET);
			}
			// hbs/comet/opc_ua_server
			if (!fs.isDir(rootPathTarget + HBS + fs.getTargetFileSeparator() + COMET + fs.getTargetFileSeparator()
					+ OPC_UA_SERVER)) {
				fs.addDir(rootPathTarget + HBS + fs.getTargetFileSeparator() + COMET + fs.getTargetFileSeparator()
						+ OPC_UA_SERVER);
			}
		}
	}

	private String getDefaultStructurePath(IFileSystem fs) {
		if (fs instanceof DataHubFileSystem) {
			return fs.getTargetFileSeparator() + HBS + fs.getTargetFileSeparator() + COMET + fs.getTargetFileSeparator()
					+ OPC_UA_SERVER + fs.getTargetFileSeparator();
		}

		return getRootPath(fs);
	}

	/**
	 * Removes /hbs/comet/opc_ua_server/runtime folder.
	 * 
	 * @param fs target filesystem
	 */
	private void cleanRuntime(IFileSystem fs) {
		// String runtimePath = fs.getRootPath();
		// if (!runtimePath.endsWith(fs.getTargetFileSeparator())) {
		// runtimePath += fs.getTargetFileSeparator();
		// }

		String runtimePath = getDefaultStructurePath(fs);
		// runtimePath += RUNTIME;
		if (fs.isDir(runtimePath + RUNTIME)) {
			try {
				fs.removeDir(runtimePath + RUNTIME, true);
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	private String getRootPath(IFileSystem targetFileSystem) {
		// HACK FOR HBS DATAHUB DEVICE
		if (targetFileSystem instanceof DataHubFileSystem) {
			return "/";
		}

		// target server workspace root
		String rootPathTarget = null;
		// check for separator
		if (targetFileSystem.getRootPath().endsWith(targetFileSystem.getTargetFileSeparator()))
			rootPathTarget = targetFileSystem.getRootPath();
		else {
			rootPathTarget = targetFileSystem.getRootPath() + targetFileSystem.getTargetFileSeparator();
		}
		return rootPathTarget;
	}

	private void copyDrivers(IFileSystem fs) {
		if (this.selectedDrivers.length <= 0) {
			return;
		}

		// first create drivers folder
		String drvPath = getDefaultStructurePath(fs);
		if (!drvPath.endsWith(fs.getTargetFileSeparator())) {
			drvPath += fs.getTargetFileSeparator();
		}
		drvPath += RUNTIME + fs.getTargetFileSeparator() + DRIVERS;
		try {
			fs.addDir(drvPath);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		String versionPath;
		String drvName;
		// now copy all selected drivers
		for (Object drv : this.selectedDrivers) {
			InternationalActivator driver = (InternationalActivator) drv;
			// copy driver to target
			try {
				drvName = driver.getBundle().getSymbolicName().replace("com.bichler.astudio.editor.", "");
				drvName = drvName.substring(0, drvName.indexOf('.'));
				drvPath = getDefaultStructurePath(fs);
				if (!drvPath.endsWith(fs.getTargetFileSeparator())) {
					drvPath += fs.getTargetFileSeparator();
				}

				drvPath += RUNTIME + fs.getTargetFileSeparator() + DRIVERS + fs.getTargetFileSeparator() + drvName;
				if (!fs.isDir(drvPath)) {
					fs.addDir(drvPath);
				}

				versionPath = getDefaultStructurePath(fs);
				if (!versionPath.endsWith(fs.getTargetFileSeparator())) {
					versionPath += fs.getTargetFileSeparator();
				}

				versionPath += RUNTIME + fs.getTargetFileSeparator() + DRIVERS + fs.getTargetFileSeparator() + drvName
						+ fs.getTargetFileSeparator() + driver.getBundle().getVersion().toString().substring(0,
								driver.getBundle().getVersion().toString().lastIndexOf('.'));
				if (!fs.isDir(versionPath)) {
					fs.addDir(versionPath);
				}

				File ff = driver.getFile(driver.getBundle(), Path.ROOT.append(DRIVER));
				copyFiles(false, fs, ff,
						fs.getTargetFileSeparator() + RUNTIME + fs.getTargetFileSeparator() + DRIVERS
								+ fs.getTargetFileSeparator() + drvName + fs.getTargetFileSeparator()
								+ driver.getBundle().getVersion().toString().substring(0,
										driver.getBundle().getVersion().toString().lastIndexOf('.')));
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	public void removeCascade(String path) {
		File f = new File(path);
		if (f.exists()) {
			String[] list = f.list();
			if (list.length == 0) {
				if (f.delete()) {
					return;
				}
			} else {
				for (int i = 0; i < list.length; i++) {
					File f1 = new File(path + "\\" + list[i]);
					if (f1.isFile() && f1.exists()) {
						f1.delete();
					}
					if (f1.isDirectory()) {
						removeCascade("" + f1);
					}
				}
				removeCascade(path);
			}
		}
	}

	private void copyFiles(boolean root, IFileSystem targetFileSystem, File localFile, String targetPath)
			throws IOException {

		File[] runtimeChildren = localFile.listFiles();
		if (runtimeChildren == null) {
			return;
		}

		for (File child : runtimeChildren) {
			if (child.isHidden()) {
				continue;
			}
			Path childPath = new Path(child.getPath());
			String newPath = targetPath + targetFileSystem.getTargetFileSeparator() + childPath.lastSegment();
			// write file
			if (child.isFile()) {
				// targetpath
				StringBuilder targetFilePath = new StringBuilder(getDefaultStructurePath(targetFileSystem));
				if (!targetFilePath.toString().endsWith(targetFileSystem.getTargetFileSeparator())) {
					targetFilePath.append(targetFileSystem.getTargetFileSeparator());
				}
				targetFilePath.append(newPath);
				// copy files to filesystem

				try {
					if (!targetFileSystem.isFile(targetFilePath.toString())) {
						targetFileSystem.addFile(targetFilePath.toString());
					}
				} catch (IOException ex) {
					logger.log(Level.SEVERE, ex.getMessage(), ex);
				}
				try (InputStream in = new FileInputStream(child);
						OutputStream out = targetFileSystem.writeFile(targetFilePath.toString());) {

					logger.log(Level.INFO, "Start Upload file - Src: {0} Target: {1}",
							new String[] { child.getAbsolutePath(), targetFilePath.toString() });

					byte[] buffer = new byte[1024];
					int read = -1;
					while ((read = in.read(buffer, 0, buffer.length)) != -1) {
						out.write(buffer, 0, read);
					}
					out.flush();
					logger.log(Level.INFO, "Finished Upload file - Src: {0} Target: {1}.",
							new String[] { child.getAbsolutePath(), targetFilePath.toString() });
				}
			} else if (child.isDirectory()) {
				if (childPath.lastSegment().compareTo("opcua") == 0) {
					continue;
				}
				String directory = null;
				directory = getDefaultStructurePath(targetFileSystem) + newPath;
				// remove initial directory
				if (targetFileSystem.isDir(directory)) {
					targetFileSystem.removeDir(directory, true);
				}
				targetFileSystem.addDir(directory);
				copyFiles(false, targetFileSystem, child, newPath);
			}
		}
	}

	@Override
	protected AbstractInstallWizard createWizard(IFileSystem filesystem) {
		return null;
	}

	@Override
	protected String titleJob() {
		return "com.bichler.astudio.device.core.monitor.upload.title";
	}

	@Override
	protected String descriptionJob() {
		return "com.bichler.astudio.device.website.install.monitor.upload";
	}
}
