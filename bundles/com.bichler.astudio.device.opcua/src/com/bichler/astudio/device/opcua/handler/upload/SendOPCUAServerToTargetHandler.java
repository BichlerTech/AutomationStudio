package com.bichler.astudio.device.opcua.handler.upload;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.device.core.transfer.AbstractInstallWizard;
import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.device.opcua.dialog.DoCancle;
import com.bichler.astudio.device.opcua.dialog.UploadProgressDialog;
import com.bichler.astudio.device.opcua.handler.AbstractOPCCompileHandler;
import com.bichler.astudio.device.opcua.options.EthernetUploadOption;
import com.bichler.astudio.device.opcua.options.SendOptions;
import com.bichler.astudio.device.opcua.wizard.DeviceTargetWizard;
import com.bichler.astudio.filesystem.DataHubFileSystem;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.licensemanagement.LicManActivator;
import com.bichler.astudio.licensemanagement.manager.LicenseCategory;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.addressspace.model.binary.AddressSpaceNodeModelFactory;
import com.bichler.astudio.opcua.addressspace.model.nosql.userauthority.NoSqlUtil;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.application.ApplicationConfiguration;

public class SendOPCUAServerToTargetHandler extends AbstractOPCCompileHandler {
	private Logger logger = Logger.getLogger(getClass().getName());
	public static final String ID = "com.bichler.astudio.device.opcua.sendopcuaservertotarget";
	private static final String LICENSE_DATAHUB = "com.hbsoft.license.datahub.jar";
	/**
	 * TODO: 90 min ohne lizenz evaluierungsversion + key 14 tage, demoversion
	 * 90min, default
	 * 
	 */
	private static final String LICENSE_DEFAULT = "com.hbsoft.license.jar";
	private static final String LICENSE_EVALUATION = "com.hbsoft.license.evaluation.jar";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPerspectiveDescriptor perspective = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
				.getPerspective();
		String perspectiveID = perspective.getId();
		StudioModelNode selectedElement = null;
		// find selection depending on the perspective
		switch (perspectiveID) {
		case "com.bichler.astudio.opcua.perspective":// OPCServerPerspective.ID:
			OPCNavigationView view = (OPCNavigationView) HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
					.findView(OPCNavigationView.ID);
			selectedElement = (StudioModelNode) view.getViewer().getInput();
			break;
		default:
			IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
			selectedElement = (StudioModelNode) selection.getFirstElement();
			break;
		}
		if (selectedElement != null) {
			Shell shell = HandlerUtil.getActiveShell(event);
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
			// start upload opc ua server
			send(shell, window, selectedElement);
		}
		return null;
	}

	/**
	 * Uploads an OPC UA server to target filesystem.
	 * 
	 * @param shell
	 * @param window
	 * @param server
	 */
	protected void send(final Shell shell, final IWorkbenchWindow window, StudioModelNode server) {
		final String servername = ((OPCUAServerModelNode) server).getServerName();
		final IFileSystem localfileSystem = server.getFilesystem();

		DeviceTargetWizard wizard = new DeviceTargetWizard(localfileSystem, (OPCUAServerModelNode) server,
				CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.device.opcua.handler.upload.wizard.target.title"));
		// Instantiates the wizard container with the wizard and opens it
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		if (WizardDialog.OK == dialog.open()) {
			final IFileSystem targetFileSystem = wizard.getTargetFileSystem();
			final Object[] namespaces2export = wizard.getNamespaces2Export();
			final boolean fullNs2export = wizard.isFullNsTable();
			final Object[] drivers = wizard.getDriver2export();
			final Object[] modules = wizard.getModules2export();
			final SendOptions options = wizard.getSendOptions();
			final EthernetUploadOption ethernetOptions = wizard.getEthernetOptions();
			final boolean isUploadNewCertificate = wizard.isUploadNewCertificate();
			options.setFilesystem(targetFileSystem);
			boolean isOK = MessageDialog.openConfirm(shell,
					CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.device.opcua.handler.upload.wizard.target.dialog.title"),
					CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.device.opcua.handler.upload.wizard.target.dialog.message"));
			if (!isOK) {
				return;
			}
			// progress monitor
			final UploadProgressDialog progressDialog = new UploadProgressDialog(shell);
			IRunnableWithProgress runnable = new IRunnableWithProgress() {
				boolean canceled = true;

				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.device.opcua.handler.upload.wizard.target.monitor.title"), 5);
					monitor.subTask(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.device.opcua.handler.upload.wizard.target.monitor.task.connect"));
					progressDialog.setCancle(new DoCancle() {
						@Override
						public void cancle() {
							cleanupCompilationJars(monitor, localfileSystem, drivers, modules);
							monitor.done();
						}
					});
					boolean isConnected = targetFileSystem.connect();
					try {
						if (!isConnected) {
							Display.getDefault().syncExec(new Runnable() {
								@Override
								public void run() {
									Logger.getLogger(getClass().getName()).log(Level.SEVERE,
											CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
													"upload.wizard.error.noconnection"));
									if (MessageDialog.openConfirm(shell, CustomString.getString(
											DeviceActivator.getDefault().RESOURCE_BUNDLE,
											"com.bichler.astudio.device.opcua.handler.target.install.monitor.title"),
											CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
													"com.bichler.astudio.device.opcua.handler.upload.wizard.target.monitor.error.connection.retry")))
										canceled = false;
								}
							});
							monitor.setCanceled(canceled);
						}
						// cancel dialog
						if (monitor.isCanceled()) {
							monitor.done();
							return;
						}
						/**
						 * TODO: if compile only take namespaces and drivers to export
						 */
						// create files to generate jar
						prepare(monitor, window, localfileSystem, namespaces2export, fullNs2export, drivers, modules);
						// combox upload
						if (options.isHBDatahubUpload()) {
							doStopComboxWatchdog(targetFileSystem);
						}
						// cancel operation
						if (monitor.isCanceled()) {
							monitor.done();
							return;
						}
						// copy files to target
						monitor.subTask(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.device.opcua.handler.upload.wizard.target.monitor.task.copy"));
						boolean failed = false;
						try {
							validateTargetFilePathStructure(targetFileSystem);
						} catch (IOException e) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, CustomString.getString(
									DeviceActivator.getDefault().RESOURCE_BUNDLE,
									"com.bichler.astudio.device.opcua.handler.upload.wizard.target.monitor.error.copy"),
									e);
							MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
									CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
											"com.bichler.astudio.device.opcua.handler.target.install.monitor.title"),
									CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
											"com.bichler.astudio.device.opcua.handler.upload.wizard.target.monitor.error.copy")
											+ " " + e.getMessage());
							failed = true;
						}
						monitor.worked(1);
						if (!failed) {
							do {
								finished = true;
								// do file size test
								// driver id for target
								AtomicInteger aDrvId = new AtomicInteger(0);
								// copy files to target
								try {
									copy(monitor, localfileSystem, localfileSystem.getRootPath(), targetFileSystem,
											servername, namespaces2export, drivers, aDrvId, options, ethernetOptions,
											isUploadNewCertificate);
									// cancel dialog
									if (monitor.isCanceled()) {
										monitor.done();
										break;
									}
									// cleanup files
									monitor.subTask(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
											"com.bichler.astudio.device.opcua.handler.upload.wizard.target.monitor.task.complete"));
									monitor.worked(1);
									cleanupCompilationJars(monitor, localfileSystem, drivers, modules);
								} catch (final IOException e) {
									Logger.getLogger(getClass().getName()).log(Level.SEVERE, CustomString.getString(
											DeviceActivator.getDefault().RESOURCE_BUNDLE,
											"com.bichler.astudio.device.opcua.handler.upload.wizard.target.monitor.error.complete"),
											e);
									if (monitor.isCanceled()) {
										monitor.done();
										break;
									}
									// trying to reconnect
									finished = false;
									// Open errror
									Display.getDefault().syncExec(new Runnable() {
										@Override
										public void run() {
											boolean errorQuestion = MessageDialog.openQuestion(
													PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
													CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
															"com.bichler.astudio.device.handler.target.install.monitor.title"),
													CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
															"com.bichler.astudio.device.opcua.handler.upload.wizard.target.monitor.error.connection.retry")
															+ " " + e.getMessage());
											if (!errorQuestion) {
												finished = true;
												cleanupCompilation(monitor, localfileSystem, drivers, modules);
												cleanupCompilationJars(monitor, localfileSystem, drivers, modules);
											} else {
												targetFileSystem.connect();
											}
										}
									});
								}
							} while (!finished);
						}
						// no local cleanup because we have the chance to
						// retransmitt
						// combox upload
						if (options.isHBDatahubUpload()) {
							doStartComboxWatchdog(targetFileSystem);
						}
						// default upload
						logger.info(servername + " " + CustomString.getString(
								DeviceActivator.getDefault().RESOURCE_BUNDLE, "upload.wizard.upload.finish"));
						monitor.done();
					} catch (final IOException e) {
						cleanupCompilation(monitor, localfileSystem, drivers, modules);
						cleanupCompilationJars(monitor, localfileSystem, drivers, modules);
						logger.log(Level.SEVERE, e.getMessage());
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								MessageDialog.openError(Display.getDefault().getActiveShell(), CustomString.getString(
										DeviceActivator.getDefault().RESOURCE_BUNDLE,
										"com.bichler.astudio.device.opcua.handler.target.install.monitor.title"),
										e.getMessage());
							}
						});
					} finally {
						targetFileSystem.disconnect();
						monitor.done();
					}
				}
			};
			try {
				progressDialog.run(true, true, runnable);
			} catch (InvocationTargetException | InterruptedException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
	}

	void doStartComboxWatchdog(IFileSystem targetFileSystem) {
		String separator = targetFileSystem.getTargetFileSeparator();
		// stop watchdog on target
		targetFileSystem.execCommand(
				"echo 0 > " + separator + "cometintern" + separator + "watchdog" + separator + "server.state");
		targetFileSystem.execCommand("sh " + separator + "hbin" + separator + "mkxmlsha.sh");
		targetFileSystem.execCommand("nohup " + separator + "hbin" + separator + "ciwd.sh start");
	}

	void doStopComboxWatchdog(IFileSystem targetFileSystem) {
		String separator = targetFileSystem.getTargetFileSeparator();
		// stop watchdog on target
		targetFileSystem.execCommand("sh " + separator + "hbin" + separator + "ciwd.sh stop");
		targetFileSystem.execCommand(
				"echo 4 > " + separator + "cometintern" + separator + "watchdog" + separator + "server.state");
	}

	@Override
	protected void copy(IProgressMonitor monitor, IFileSystem localfileSystem, String localPath,
			IFileSystem targetFileSystem, String servername, Object[] namespaces2export, Object[] drivers,
			AtomicInteger aDrvId, SendOptions options, EthernetUploadOption ethernetOptions,
			boolean isUploadNewCertificate) throws IOException {

		// target server workspace root
		String rootPathTarget = null;
		// check for separator
		if (targetFileSystem.getRootPath().endsWith(targetFileSystem.getTargetFileSeparator()))
			rootPathTarget = targetFileSystem.getRootPath();
		else {
			rootPathTarget = targetFileSystem.getRootPath() + targetFileSystem.getTargetFileSeparator();
		}

		if (targetFileSystem instanceof DataHubFileSystem) {
			// check if we have the main structure
			if (!targetFileSystem.isDir(targetFileSystem.getTargetFileSeparator() + HBS)) {
				targetFileSystem.addDir(targetFileSystem.getTargetFileSeparator() + HBS);
			}
			// hbs/comet
			if (!targetFileSystem.isDir(targetFileSystem.getTargetFileSeparator() + HBS + targetFileSystem.getTargetFileSeparator() + COMET)) {
				targetFileSystem.addDir(targetFileSystem.getTargetFileSeparator() + HBS + targetFileSystem.getTargetFileSeparator() + COMET);
			}
			// hbs/comet/opc_ua_server
			if (!targetFileSystem.isDir(targetFileSystem.getTargetFileSeparator() + HBS + targetFileSystem.getTargetFileSeparator() + COMET
					+ targetFileSystem.getTargetFileSeparator() + OPC_UA_SERVER)) {
				targetFileSystem.addDir(targetFileSystem.getTargetFileSeparator() + HBS + targetFileSystem.getTargetFileSeparator() + COMET
						+ targetFileSystem.getTargetFileSeparator() + OPC_UA_SERVER);
			}

			rootPathTarget = targetFileSystem.getTargetFileSeparator() + HBS + targetFileSystem.getTargetFileSeparator() + COMET
					+ targetFileSystem.getTargetFileSeparator() + OPC_UA_SERVER
					+ targetFileSystem.getTargetFileSeparator();
		}
		
		// workspace\[servers]
		String serverPathTarget = rootPathTarget + SERVERS;
		// add dir if not exist
		if (!targetFileSystem.isDir(serverPathTarget)) {
			targetFileSystem.addDir(serverPathTarget);
		}
		// workspace\servers\[startup.sh]
		String startupScriptPath = null;
		String startupScriptPathBackup = null;
		// initialize startup script path
		if (!serverPathTarget.endsWith(targetFileSystem.getTargetFileSeparator())) {
			startupScriptPath = serverPathTarget + targetFileSystem.getTargetFileSeparator() + "startup.sh";
			startupScriptPathBackup = serverPathTarget + targetFileSystem.getTargetFileSeparator() + "startup.sh_new";
		} else {
			startupScriptPath = serverPathTarget + "startup.sh";
			startupScriptPathBackup = serverPathTarget + "startup.sh_new";
		}
		// target path workspace\servers\[projectname]
		String projectPathTarget = null;
		// target path workspace\servers\[projectname_new] for backuping
		String projectPathTargetBackup = null;
		// wrap target server paths (ending with fileseperator)
		if (options.isHBDatahubUpload()) {
			// ! change servername rustical
			servername = SERVERNAME_HBSOFT;
		}
		// initialize project script path
		if (!serverPathTarget.endsWith(targetFileSystem.getTargetFileSeparator())) {
			projectPathTarget = serverPathTarget + targetFileSystem.getTargetFileSeparator() + servername;
		} else {
			projectPathTarget = serverPathTarget + servername;
		}
		projectPathTargetBackup = projectPathTarget + "_new";
		// cancel before modifying the file system
		if (monitor.isCanceled()) {
			monitor.done();
			return;
		}
		// remove corrupt folders (backup dir)
		if (targetFileSystem.isDir(projectPathTargetBackup)) {
			targetFileSystem.removeDir(projectPathTargetBackup, true);
		}
		// remove corrupt startup
		if (targetFileSystem.isFile(startupScriptPathBackup)) {
			targetFileSystem.removeFile(startupScriptPathBackup);
		}
		// add servers folder, root of all project (if not exist)
		if (!targetFileSystem.isDir(serverPathTarget)) {
			targetFileSystem.addDir(serverPathTarget);
		}
		// add project folder (if not exist)
		if (!targetFileSystem.isDir(projectPathTarget)) {
			targetFileSystem.addDir(projectPathTarget);
		}
		// add backup folder (if not exist)
		if (!targetFileSystem.isDir(projectPathTargetBackup)) {
			targetFileSystem.addDir(projectPathTargetBackup);
		}
		// start copying structure
		copyStructure(monitor, localfileSystem, localPath, targetFileSystem, projectPathTargetBackup, namespaces2export,
				drivers, options.isHBDatahubUpload(), false, false, false, false, false, false, false, false, false,
				false, false, aDrvId, ethernetOptions, isUploadNewCertificate);
		// cleans up
		if (monitor.isCanceled()) {
			// remove cancled project
			if (targetFileSystem.isDir(projectPathTargetBackup)) {
				targetFileSystem.removeDir(projectPathTargetBackup, true);
			}
			// remove cancled startup script
			if (targetFileSystem.isFile(startupScriptPathBackup)) {
				targetFileSystem.removeFile(startupScriptPathBackup);
			}
			monitor.done();
			return;
		}
		// add startup script backup (if there is no exsiting)
		if (!targetFileSystem.isFile(startupScriptPathBackup)) {
			targetFileSystem.addFile(startupScriptPathBackup);
		}
		String rp = targetFileSystem.getRootPath();
		if (rp != null && rp.endsWith(targetFileSystem.getTargetFileSeparator())) {
			rp = rp.substring(0, rp.length() - 1);
		}
		// write startup file to target destination
		String startup = options.buildStartup(rp, servername);
		OutputStream os = null;
		try {
			os = targetFileSystem.writeFile(startupScriptPathBackup);
			os.write(startup.getBytes());
			os.flush();
		} finally {
			if (os != null) {
				os.close();
			}
		}
		// cleans up
		if (monitor.isCanceled()) {
			// remove cancled project
			if (targetFileSystem.isDir(projectPathTargetBackup)) {
				targetFileSystem.removeDir(projectPathTargetBackup, true);
			}
			// remove cancled startup script
			if (targetFileSystem.isFile(startupScriptPathBackup)) {
				targetFileSystem.removeFile(startupScriptPathBackup);
			}
			monitor.done();
			return;
		}
		// replace datapoints.com
		if (targetFileSystem.isDir(projectPathTargetBackup)) {
			String[] subFolders = targetFileSystem.listDirs(projectPathTargetBackup);
			if (subFolders != null) {
				for (String subFolder : subFolders) {
					if ("drivers".equals(subFolder)) {
						String drvFolder = projectPathTargetBackup + targetFileSystem.getTargetFileSeparator()
								+ subFolder;
						String[] drv2change = targetFileSystem.listDirs(drvFolder);
						for (String drv : drv2change) {
							String path2change = drvFolder + targetFileSystem.getTargetFileSeparator() + drv;
							switch (drv) {
							case ".":
							case "..":
								continue;
							}
							doChangeDrvDatapointsOnDevice(targetFileSystem, path2change);
						}
					}
				}
			}
		}
		// datahub license
		if (options.isHBDatahubUpload()) {
			uploadLicense(monitor, localfileSystem, targetFileSystem, projectPathTargetBackup, LICENSE_DATAHUB);
		}
		// default license
		else {
			LicenseCategory license = LicManActivator.getDefault().getLicenseManager().getLicense();
			// no license version
			if (!license.isActive()) {
				uploadLicense(monitor, localfileSystem, targetFileSystem, projectPathTargetBackup, LICENSE_EVALUATION);
			}
			// license version
			else {
				switch (license) {
				case Evaluation:
					uploadLicense(monitor, localfileSystem, targetFileSystem, projectPathTargetBackup,
							LICENSE_EVALUATION);
					break;
				default:
					uploadLicense(monitor, localfileSystem, targetFileSystem, projectPathTargetBackup, LICENSE_DEFAULT);
					break;
				}
			}
		}
		// replace runtime project
		if (targetFileSystem.isDir(projectPathTarget)) {
			targetFileSystem.removeDir(projectPathTarget, true);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
		// rename created project to runtime project
		boolean rename = targetFileSystem.renameFile(projectPathTargetBackup, projectPathTarget);
		if (!rename) {
			throw new IOException(
					CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE, "upload.wizard.error.rename"));
		}
		String newScriptPath = options.nameScriptFile(startupScriptPath);
		// remove startup file
		if (targetFileSystem.isFile(startupScriptPath)) {
			targetFileSystem.removeFile(startupScriptPath);
		}
		if (targetFileSystem.isFile(newScriptPath)) {
			targetFileSystem.removeFile(newScriptPath);
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		// NEW
		targetFileSystem.renameFile(startupScriptPathBackup, newScriptPath);
		// make startupfile executeable
		if (options.isHBDatahubUpload()) {
			targetFileSystem.execCommand("chmod 777 " + startupScriptPath);
		}
	}

	private void doChangeDrvDatapointsOnDevice(IFileSystem targetFileSystem, String drvPath) {
		// replace datapoints
		replace(targetFileSystem, drvPath, "datapointsDevices.com", "datapoints.com");
	}

	private void replace(IFileSystem targetFileSystem, String drvPath, String oldname, String newname) {
		String replaced = drvPath + targetFileSystem.getTargetFileSeparator() + newname;
		try {
			if (targetFileSystem.isFile(replaced)) {
				targetFileSystem.removeFile(replaced);
			}
			targetFileSystem.renameFile(drvPath + targetFileSystem.getTargetFileSeparator() + oldname, replaced);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	/**
	 * Copies the file structur to the target.
	 * 
	 * @param monitor
	 * @param localfileSystem
	 * @param localPath
	 * @param targetFileSystem
	 * @param targetPath
	 * @param namespaces2export
	 * @param drivers2add
	 * @param defaultComboxUpload
	 * @param certstoreFolder
	 * @param driverFolder
	 * @param ecmascriptsFolder
	 * @param informationmodelFolder
	 * @param serverconfigFolder
	 * @param shellscriptsFolder
	 * @param usersFolder
	 * @param isDrvConfig
	 * @param aDrvId
	 * @throws Exception
	 */
	private void copyStructure(IProgressMonitor monitor, IFileSystem localfileSystem, String localPath,
			IFileSystem targetFileSystem, String targetPath, Object[] namespaces2export, Object[] drivers2add,
			boolean defaultComboxUpload, boolean certstoreFolder, boolean internalCerts, boolean driverFolder,
			boolean ecmascriptsFolder, boolean informationmodelFolder, boolean serverconfigFolder,
			boolean shellscriptsFolder, boolean usersFolder, boolean isDrvConfig, boolean publicCertFolder,
			boolean privateKeyFolder, AtomicInteger aDrvId, EthernetUploadOption ethernetOption,
			boolean isCopyNewCertificate) throws IOException {
		if (monitor.isCanceled()) {
			return;
		}
		String[] files = localfileSystem.listFiles(localPath);
		for (String fileUrl : files) {
			// cancled
			if (monitor.isCanceled()) {
				return;
			}
			if (fileUrl.compareTo("datapoints.com") == 0) {
				continue;
			}
			copyFile(monitor, localfileSystem, new Path(localPath).append(fileUrl).toOSString(), targetFileSystem,
					targetPath + targetFileSystem.getTargetFileSeparator() + fileUrl, fileUrl, defaultComboxUpload,
					serverconfigFolder, isDrvConfig, informationmodelFolder, usersFolder, namespaces2export,
					publicCertFolder, privateKeyFolder, aDrvId, ethernetOption, isCopyNewCertificate);
		}
		boolean certsInternal = false;
		boolean isDriverConfig = false;
		// check driver configuration
		String[] dirs = localfileSystem.listDirs(localPath);
		for (String directory : dirs) {
			if (monitor.isCanceled()) {
				return;
			}
			if (certstoreFolder) {
				ApplicationConfiguration configfile = readconfigfile(localfileSystem);
				String certstorepath = configfile.getApplicationCertificateStorePath();
				boolean isDefaultCertStorePath = checkDefaultCertStorePath(targetFileSystem, certstorepath);
				if (certstoreFolder && !internalCerts && !isDefaultCertStorePath) {
					String lastChar = "" + certstorepath.charAt(certstorepath.length() - 1);
					String separator = targetFileSystem.getTargetFileSeparator();
					if (lastChar.equals(separator)) {
						certstorepath = certstorepath.substring(0, certstorepath.length() - 1);
					}
					certsInternal = true;
					boolean isCreated = copyDirectory(targetFileSystem, certstorepath, directory);
					if (!isCreated) {
						throw new IOException(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
								"upload.wizard.error.certstore"));
					}
					copyStructure(monitor, localfileSystem, new Path(localPath).append(directory).toOSString(),
							targetFileSystem, certstorepath + targetFileSystem.getTargetFileSeparator() + directory,
							namespaces2export, drivers2add, defaultComboxUpload, certstoreFolder, certsInternal,
							driverFolder, ecmascriptsFolder, informationmodelFolder, serverconfigFolder,
							shellscriptsFolder, usersFolder, isDriverConfig, publicCertFolder, privateKeyFolder, aDrvId,
							ethernetOption, isCopyNewCertificate);
					continue;
				}
			} else if (informationmodelFolder) {
				if (!"compiled".equals(directory)) {
					copyFile(monitor, localfileSystem, new Path(localPath).append(directory).toOSString(),
							targetFileSystem, targetPath + targetFileSystem.getTargetFileSeparator() + directory,
							directory, defaultComboxUpload, serverconfigFolder, isDrvConfig, informationmodelFolder,
							usersFolder, namespaces2export, publicCertFolder, privateKeyFolder, aDrvId, ethernetOption,
							isCopyNewCertificate);
				}
			}
			// export allowed drivers
			else if (driverFolder) {
				boolean allowed = false;
				for (Object drvNode : drivers2add) {
					String drvName = ((OPCUAServerDriverModelNode) drvNode).getDriverName();
					if (directory.compareTo(drvName) == 0) {
						allowed = true;
					}
					if (allowed) {
						break;
					}
				}
				// check if driver is allowed to upload
				if (!allowed) {
					continue;
				}
				isDriverConfig = true;
			}
			boolean isDriverFolder = false;
			boolean isEcmascripts = false;
			boolean isInformationmodell = false;
			boolean isServerconfig = false;
			boolean isShellscripts = false;
			boolean isUsers = false;
			boolean isCertstore = false;
			boolean isPrivKey = false;
			boolean isPublicCert = false;
			// certificate folder
			if (!certstoreFolder && "certificatestore".equals(directory)) {
				isCertstore = true;
			}
			// drivers folder
			else if (!driverFolder && "drivers".equals(directory)) {
				isDriverFolder = true;
			}
			// ecmascripts folder
			else if (!ecmascriptsFolder && "ecmascripts".equals(directory)) {
				isEcmascripts = true;
			}
			// information model folder
			else if (!informationmodelFolder && "informationmodel".equals(directory)) {
				isInformationmodell = true;
			}
			// server configuration folder
			else if (!serverconfigFolder && "serverconfig".equals(directory)) {
				isServerconfig = true;
			}
			// shell scripts folder
			else if (!shellscriptsFolder && "shellscripts".equals(directory)) {
				isShellscripts = true;
			}
			// users folder
			else if (!usersFolder && "users".equals(directory)) {
				isUsers = true;
			} else if (!privateKeyFolder && "privatekey".equals(directory)) {
				isPrivKey = true;
			} else if (!publicCertFolder && "publiccert".equals(directory)) {
				isPublicCert = true;
			}
			// copies directory
			copyDirectory(targetFileSystem, targetPath, directory);
			// rek call
			copyStructure(monitor, localfileSystem, new Path(localPath).append(directory).toOSString(),
					targetFileSystem, targetPath + targetFileSystem.getTargetFileSeparator() + directory,
					namespaces2export, drivers2add, defaultComboxUpload, isCertstore, certsInternal, isDriverFolder,
					isEcmascripts, isInformationmodell, isServerconfig, isShellscripts, isUsers, isDriverConfig,
					isPublicCert, isPrivKey, aDrvId, ethernetOption, isCopyNewCertificate);
		}
		return;
	}

	public ApplicationConfiguration readconfigfile(IFileSystem filesystem) {
		/** load xml file */
		String path = new Path(filesystem.getRootPath()).append("serverconfig").append("server.config.xml")
				.toOSString();
		if (filesystem.isFile(path)) {
			try {
				return new ApplicationConfiguration(filesystem.readFile(path));
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
		return null;
	}

	public ApplicationConfiguration readconfigfileTXT(IFileSystem filesystem) {
		/** load xml file */
		String path = new Path(filesystem.getRootPath()).append("serverconfig").append("server.config.txt")
				.toOSString();
		if (filesystem.isFile(path)) {
			try {
				return new ApplicationConfiguration(filesystem.readFile(path));
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
		return null;
	}

	private boolean checkDefaultCertStorePath(IFileSystem targetFilesystem, String certstorePath) {
		Path p = new Path(certstorePath);
		String[] list = p.segments();
		List<String> defaultpath = new ArrayList<>();
		defaultpath.add("certificatestore");
		defaultpath.add("certs");
		boolean matches = true;
		for (String l : list) {
			if (!defaultpath.contains(l)) {
				matches = false;
				break;
			}
		}
		return matches;
	}

	private List<String> getPath(IFileSystem targetFileSystem, String targetPath) {
		Path path = new Path(targetPath);
		String device = path.getDevice();
		String[] segments = path.segments();
		List<String> list = new ArrayList<>();
		if (device != null) {
			list.add(device);
		}
		if (segments != null) {
			for (String segment : segments) {
				list.add(segment);
			}
		}
		return list;
	}

	private boolean copyDirectory(IFileSystem targetFileSystem, String targetPath, String directory)
			throws IOException {
		if (targetPath == null) {
			return false;
		}
		int begin = -1;
		int end = 0;
		List<String> list = getPath(targetFileSystem, targetPath);
		if (begin > 0) {
			String value = targetPath.substring(begin, end + 1);
			list.add(value);
		}
		StringBuilder current = new StringBuilder();
		for (String pathItem : list) {
			current.append(targetFileSystem.getTargetFileSeparator());
			current.append(pathItem);
			if (!targetFileSystem.isDir(current.toString())) {
				targetFileSystem.addDir(current.toString());
			}
		}
		String newPath = targetPath + targetFileSystem.getTargetFileSeparator() + directory;
		if (!targetFileSystem.isDir(newPath)) {
			targetFileSystem.addDir(newPath);
		}
		return targetFileSystem.isDir(newPath);
	}

	private void uploadLicense(IProgressMonitor monitor, IFileSystem localFileSystem, IFileSystem targetFileSystem,
			String targetPath, String license) throws IOException {
		try {
			File licenseFile = DeviceActivator.getDefault().getLicenseFile(license);
			writeBinary(monitor, localFileSystem, licenseFile.getAbsolutePath(), targetFileSystem,
					targetPath + targetFileSystem.getTargetFileSeparator() + LICENSE_DEFAULT);
		} catch (IOException e) {
			throw new IOException(
					CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE, "upload.wizard.error.license"),
					e);
		}
	}

	private void copyFile(IProgressMonitor monitor, IFileSystem localfileSystem, String localPath,
			IFileSystem targetFileSystem, String targetPath, String currentFile, boolean defaultComboxUpload,
			boolean isServerConfig, boolean isDrvConfig, boolean isInformationModel, boolean isUsersFolder,
			Object[] namespaces2export, boolean isPublicCertFolder, boolean isPrivateKeyFolder, AtomicInteger aDrvId,
			EthernetUploadOption ethernetOptions, boolean isCopyNewCertificate) throws IOException {
		boolean isDriverCom = false;
		if (isDrvConfig) {
			// driver file
			if (currentFile != null && "driver.com".equalsIgnoreCase(currentFile)) {
				isDriverCom = true;
			}
		}
		// write particular information model
		if (isInformationModel) {
			// only jar
			if ((AddressSpaceNodeModelFactory.PACKAGENAME + ".jar").equalsIgnoreCase(currentFile)) {
				write(monitor, localfileSystem, localPath, targetFileSystem, targetPath, defaultComboxUpload,
						isServerConfig, isDriverCom, aDrvId, ethernetOptions);
			} else if (("compiled").equalsIgnoreCase(currentFile)) {
				// if we found compiled folder and the correct element
				// this folder is only supported with ansi c files
				IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
				boolean doCompileAnsiC = store.getBoolean(OPCUAConstants.OPCUADoCompileAnsiC);
				if (doCompileAnsiC)
					write(monitor, localfileSystem, localPath, targetFileSystem, targetPath, defaultComboxUpload,
							isServerConfig, isDriverCom, aDrvId, ethernetOptions);
			}
		}
		// write particular user database
		else if (isUsersFolder) {
			if (NoSqlUtil.DB_USER.equals(currentFile)) {
				// write temporary users file
				String tempdb = localfileSystem.getRootPath() + File.separator + "users" + File.separator
						+ "userconfig_tmp.db";
				write(monitor, localfileSystem, localPath, localfileSystem, tempdb, defaultComboxUpload, isServerConfig,
						isDriverCom, aDrvId, ethernetOptions);
				// now remove all nodes from db file
				Connection connection = null;
				try {
					connection = NoSqlUtil.createConnection(tempdb);
					NoSqlUtil.truncateTable(connection, "NODES");
					NoSqlUtil.disconnect(connection);
				} catch (ClassNotFoundException | SQLException e) {
					logger.log(Level.SEVERE, e.getMessage());
				} finally {
					if (connection != null) {
						try {
							connection.close();
						} catch (SQLException e) {
							logger.log(Level.SEVERE, e.getMessage());
						}
					}
				}
				write(monitor, localfileSystem, tempdb, targetFileSystem, targetPath, defaultComboxUpload,
						isServerConfig, isDriverCom, aDrvId, ethernetOptions);
			}
		}
		// copy other files
		else {
			if (isPublicCertFolder && isCopyNewCertificate) {
				return;
			}
			if (isPrivateKeyFolder && isCopyNewCertificate) {
				return;
			}
			write(monitor, localfileSystem, localPath, targetFileSystem, targetPath, defaultComboxUpload,
					isServerConfig, isDriverCom, aDrvId, ethernetOptions);
		}
	}

	private void write(IProgressMonitor monitor, IFileSystem localfileSystem, String localPath,
			IFileSystem targetFileSystem, String targetPath, boolean defaultComboxUpload, boolean isServerConfig,
			boolean isDriverCom, AtomicInteger aDrvId, EthernetUploadOption ethernetOptions) throws IOException {
		Path path = new Path(localPath);
		String ext = path.getFileExtension();
		// copy straight file with no extension
		if (ext == null) {
			ext = "";
		}
		switch (ext) {
		case "com":
		case "xml":
			// skip com configuration
			if (isServerConfig && "com".equals(ext)) {
				break;
			}
			// character files to edit
			writeCharacter(monitor, localfileSystem, localPath, targetFileSystem, targetPath, defaultComboxUpload,
					isServerConfig, isDriverCom, aDrvId, ethernetOptions);
			break;
		default:
			// copy binary
			writeBinary(monitor, localfileSystem, localPath, targetFileSystem, targetPath);
			break;
		}
	}

	private void writeBinary(IProgressMonitor monitor, IFileSystem localfileSystem, String localPath,
			IFileSystem targetFileSystem, String targetPath) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = localfileSystem.readFile(localPath);
			// add file if not exist
			if (!targetFileSystem.isFile(targetPath)) {
				targetFileSystem.addFile(targetPath);
			}
			out = targetFileSystem.writeFile(targetPath);
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = in.read(buffer)) >= 0) {
				out.write(buffer, 0, length);
			}
			out.flush();
			monitor.subTask(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
					"upload.wizard.file.message") + " - "
					+ CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE, "upload.wizard.file.source")
					+ " " + localPath + " "
					+ CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE, "upload.wizard.file.target")
					+ " " + targetPath);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
			}
		}
	}

	private void writeCharacter(IProgressMonitor monitor, IFileSystem localfileSystem, String localPath,
			IFileSystem targetFileSystem, String targetPath, boolean defaultComboxUpload, boolean isServerConfig,
			boolean isDriverCom, AtomicInteger aDrvId, EthernetUploadOption ethernetOptions) throws IOException {
		/**
		 * write character files
		 */
		if (isDriverCom || (isServerConfig && defaultComboxUpload)) {
			// initialize streams
			BufferedReader readerLocal = null;
			BufferedWriter writerTarget = null;
			List<String> lines = new ArrayList<>();
			// write text file to target
			try {
				// input
				InputStream inLocal = localfileSystem.readFile(localPath);
				readerLocal = new BufferedReader(new InputStreamReader(inLocal));
				if (defaultComboxUpload && isServerConfig) {
					String tmp = targetPath.substring(0, targetPath.lastIndexOf("."));
					targetPath = tmp + ".tmp";
				}
				if (!targetFileSystem.isFile(targetPath)) {
					targetFileSystem.addFile(targetPath);
				}
				OutputStream outTarget = targetFileSystem.writeFile(targetPath);
				writerTarget = new BufferedWriter(new OutputStreamWriter(outTarget));
				// read local file and write to target
				String line = null;
				while ((line = readerLocal.readLine()) != null) {
					// find driverid and replace
					if (isDriverCom && "driverid".contentEquals(line)) {
						line = writeDriverConfigId(lines, line, writerTarget, readerLocal, aDrvId);
					} else if (isServerConfig && defaultComboxUpload && line.contains("<BaseAddresses>")) {
						line = writeServerConfigBaseAddresses(lines, line, writerTarget, readerLocal, ethernetOptions);
					}
					lines.add(line);
					writerTarget.write(line);
					writerTarget.newLine();
					writerTarget.flush();
					// TODO: Cancle
				}
				monitor.subTask(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
						"upload.wizard.file.message")
						+ " - "
						+ CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
								"upload.wizard.file.source")
						+ " " + localPath + " " + CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
								"upload.wizard.file.target")
						+ " " + targetPath);
			}
			// close streams
			finally {
				if (readerLocal != null) {
					try {
						readerLocal.close();
					} catch (IOException e) {
						logger.log(Level.SEVERE, e.getMessage());
					}
				}
				if (writerTarget != null) {
					try {
						writerTarget.close();
					} catch (IOException e) {
						logger.log(Level.SEVERE, e.getMessage());
					}
				}
			}
		}
		/**
		 * anyways write binary
		 */
		else {
			writeBinary(monitor, localfileSystem, localPath, targetFileSystem, targetPath);
		}
	}

	private String writeDriverConfigId(List<String> lines, String line, BufferedWriter writerTarget,
			BufferedReader readerLocal, AtomicInteger aDrvId) throws IOException {
		lines.add(line);
		// write line
		writerTarget.write(line);
		writerTarget.newLine();
		// read driverid line and skip it
		readerLocal.readLine();
		// get next id
		int id = aDrvId.getAndIncrement();
		line = "" + id;
		// return last line
		return line;
	}

	private String writeServerConfigBaseAddresses(List<String> lines, String line, BufferedWriter writerTarget,
			BufferedReader readerLocal, EthernetUploadOption ethernetOptions) throws IOException {
		// base addresses
		lines.add(line);
		writerTarget.write(line);
		writerTarget.newLine();
		writerTarget.flush();
		// skip addresses
		do {
			line = readerLocal.readLine();
		} while (!line.contains("</BaseAddresses>"));
		List<String> ethernetAddresses = new ArrayList<>();
		switch (ethernetOptions.upload) {
		case Mode_Default:
			ethernetAddresses.add("<ua:String>%serveraddr_eth0%</ua:String>");
			ethernetAddresses.add("<ua:String>%serveraddr_eth1%</ua:String>");
			break;
		case Mode_Single:
			ethernetAddresses.add("<ua:String>%serveraddr_eth1%</ua:String>");
			break;
		default:
			break;
		}
		for (String address : ethernetAddresses) {
			writerTarget.write(address);
			writerTarget.newLine();
			lines.add(address);
			writerTarget.flush();
		}
		// return last line
		return line;
	}

	@Override
	protected AbstractInstallWizard createWizard(IFileSystem filesystem) {
		// TODO Auto-generated method stub
		return null;
	}
}
