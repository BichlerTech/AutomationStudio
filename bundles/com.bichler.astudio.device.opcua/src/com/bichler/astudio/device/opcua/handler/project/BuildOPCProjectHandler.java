package com.bichler.astudio.device.opcua.handler.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.device.core.transfer.AbstractInstallWizard;
import com.bichler.astudio.device.opcua.handler.AbstractOPCCompileHandler;
import com.bichler.astudio.device.opcua.options.DefaultSendOptions;
import com.bichler.astudio.device.opcua.options.EthernetUploadOption;
import com.bichler.astudio.device.opcua.options.SendOptions;
import com.bichler.astudio.device.opcua.wizard.BuildLocalProjectWizard;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.driver.OPCDriverUtil;
import com.bichler.astudio.opcua.driver.OPCModuleUtil;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;

public class BuildOPCProjectHandler extends AbstractOPCCompileHandler {
	public static final String ID = "com.bichler.astudio.device.opcua.build";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		OPCNavigationView view = (OPCNavigationView) HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
				.findView(OPCNavigationView.ID);
		OPCUAServerModelNode selectedElement = (OPCUAServerModelNode) view.getViewer().getInput();
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (selectedElement != null) {
			Shell shell = HandlerUtil.getActiveShell(event);
			build(shell, window, selectedElement);
		}
		return null;
	}

	void build(final Shell shell, final IWorkbenchWindow window, OPCUAServerModelNode server) {
		// Instantiates
		final String servername = server.getServerName();
		final IFileSystem localfileSystem = server.getFilesystem();
		final IFileSystem targetFileSystem = server.getTargetFilesystem();
		final String serverpath = localfileSystem.getRootPath();
		// select namespaces
		BuildLocalProjectWizard wizard = new BuildLocalProjectWizard(server);
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		if (WizardDialog.OK == dialog.open()) {
			final Object[] namespaces2export = wizard.getNamespaces2Export();
			final boolean isFullNsTableExport = wizard.isFullNsTableExport();
			final Object[] drivers = OPCDriverUtil.getAllDriverNamesFromOPCProject(server);
			final Object[] modules = OPCModuleUtil.getAllModuleNamesFromOPCProject(server);
			final EthernetUploadOption ethernetOptions = new EthernetUploadOption();
			//
			final DefaultSendOptions options = new DefaultSendOptions();
			boolean isOK = MessageDialog.openConfirm(shell, "Build OPC", "BUILD OPC PROJECT?");
			if (!isOK) {
				return;
			}
			ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(shell);
			IRunnableWithProgress runnable = new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Build ...", IProgressMonitor.UNKNOWN);
					monitor.subTask("building project ...");
					// cancel dialog
					if (monitor.isCanceled()) {
						monitor.done();
						return;
					}
					// compile & create jars
					try {
						prepare(monitor, window, localfileSystem, namespaces2export, isFullNsTableExport,
								drivers, modules);
					} catch (IOException e1) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage());

					}
					// copy files to target
					monitor.subTask("...");
					// do file size test

					// driver id for target
					AtomicInteger aDrvId = new AtomicInteger(0);
					// copy files to target
					try {
						copy(monitor, localfileSystem, serverpath, null, servername, namespaces2export, drivers, aDrvId,
								options, ethernetOptions, false);
					} catch (IOException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
					}
					// cleanup files
					monitor.subTask("Abschlieﬂen...");
					monitor.done();
				}
			};
			try {
				progressDialog.run(true, true, runnable);
			} catch (InterruptedException | InvocationTargetException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
		}
	}

	/**
	 * Changes driverids in driver.com
	 */
	@Override
	protected void copy(IProgressMonitor monitor, IFileSystem localfileSystem, String localPath,
			IFileSystem targetFileSystem, String servername, Object[] namespaces2export, Object[] drivers,
			AtomicInteger aDrvId, SendOptions isDefaultComboxUpload, EthernetUploadOption ethernetOptions,
			boolean isCopyCertificateStore) throws IOException {
		// start copying structure
		copyStructure(monitor, localfileSystem, localPath, namespaces2export, drivers, false, false, false, false,
				false, false, false, aDrvId);
	}

	private void copyStructure(IProgressMonitor monitor, IFileSystem localfileSystem, String localPath,
			Object[] namespaces2export, Object[] drivers2add, boolean certstoreFolder, boolean driverFolder,
			boolean ecmascriptsFolder, boolean informationmodelFolder, boolean serverconfigFolder,
			boolean shellscriptsFolder, boolean isDrvConfig, AtomicInteger aDrvId) throws IOException {
		if (monitor.isCanceled()) {
			return;
		}
		String[] files = localfileSystem.listFiles(localPath);
		for (String fileUrl : files) {
			// cancled
			if (monitor.isCanceled()) {
				return;
			}
			copyFile(localfileSystem, new Path(localPath).append(fileUrl).toOSString(), fileUrl, isDrvConfig,
					informationmodelFolder, namespaces2export, aDrvId);
		}
		boolean isDriverConfig = false;
		String[] dirs = localfileSystem.listDirs(localPath);
		for (String directory : dirs) {
			if (monitor.isCanceled()) {
				return;
			}
			// export allowed drivers
			if (driverFolder) {
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
			boolean isCertstore = false;
			boolean isDriverFolder = false;
			boolean isEcmascripts = false;
			boolean isInformationmodell = false;
			boolean isServerconfig = false;
			boolean isShellscripts = false;
			if (!certstoreFolder && "certificatestore".equals(directory)) {
				isCertstore = true;
			} else if (!driverFolder && "drivers".equals(directory)) {
				isDriverFolder = true;
			} else if (!ecmascriptsFolder && "ecmascripts".equals(directory)) {
				isEcmascripts = true;
			} else if (!informationmodelFolder && "informationmodel".equals(directory)) {
				isInformationmodell = true;
			} else if (!serverconfigFolder && "serverconfig".equals(directory)) {
				isServerconfig = true;
			} else if (!shellscriptsFolder && "shellscripts".equals(directory)) {
				isShellscripts = true;
			}
			// copies directory
			// rek call
			copyStructure(monitor, localfileSystem, new Path(localPath).append(directory).toOSString(),
					namespaces2export, drivers2add, isCertstore, isDriverFolder, isEcmascripts, isInformationmodell,
					isServerconfig, isShellscripts, isDriverConfig, aDrvId);
		}
		return;
	}

	private void copyFile(IFileSystem localfileSystem, String localPath, String currentFile, boolean isDrvConfig,
			boolean isInformationModel, Object[] namespaces2export, AtomicInteger aDrvId) {
		boolean isDriverCom = false;
		if (isDrvConfig) {
			// driver file
			if (currentFile != null && "driver.com".equalsIgnoreCase(currentFile)) {
				isDriverCom = true;
			}
		}
		if (isDriverCom) {
			write(localfileSystem, localPath, aDrvId);
		}
	}

	private void write(IFileSystem localfileSystem, String localPath, AtomicInteger aDrvId) {
		Path lPath = new Path(localPath);
		String ext = lPath.getFileExtension();
		switch (ext) {
		case "com":
		case "xml":
			// character files to edit
			writeCharacter(localfileSystem, localPath, aDrvId);
			break;
		default:
			// copy binary
			break;
		}
	}

	private void writeCharacter(IFileSystem localfileSystem, String localPath, AtomicInteger aDrvId) {
		// initialize streams
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			// input
			InputStream in = localfileSystem.readFile(localPath);
			reader = new BufferedReader(new InputStreamReader(in));
			List<String> lines = new ArrayList<>();
			String line = "";
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			// output
			OutputStream out = localfileSystem.writeFile(localPath);
			writer = new BufferedWriter(new OutputStreamWriter(out));
			Iterator<String> iterator = lines.iterator();
			// write driver.com
			while (iterator.hasNext()) {
				line = iterator.next();
				if ("driverid".contentEquals(line)) {
					// write line
					writer.write(line);
					writer.newLine();
					// read driverid line and skip it
					iterator.next();
					// next driver id
					int id = aDrvId.getAndIncrement();
					line = "" + id;
				}
				writer.write(line);
				writer.newLine();
			}
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
			}
		}
	}

	@Override
	protected AbstractInstallWizard createWizard(IFileSystem filesystem) {
		// TODO Auto-generated method stub
		return null;
	}
}
