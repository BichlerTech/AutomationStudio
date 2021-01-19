package com.bichler.astudio.device.opcua.handler;

import java.io.File;
import java.io.FileFilter;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.addressspace.model.binary.AddressSpaceNodeModelFactory;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriversModelNode;

public class CompileHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.device.compile";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPerspectiveDescriptor perspective = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
				.getPerspective();
		String perspectiveID = perspective.getId();
		StudioModelNode selectedElement = null;

		Object[] drivers = null;

		switch (perspectiveID) {
		case "com.bichler.astudio.opcua.perspective"://OPCServerPerspective.ID:
			OPCNavigationView view = (OPCNavigationView) HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
					.findView(OPCNavigationView.ID);
			selectedElement = (StudioModelNode) view.getViewer().getInput();

			Object[] children = selectedElement.getChildren();
			for (Object child : children) {
				if (child instanceof OPCUAServerDriversModelNode) {
					drivers = ((OPCUAServerDriversModelNode) child).getChildren();
					break;
				}
			}

			break;
		default:
			IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
			selectedElement = (StudioModelNode) selection.getFirstElement();
			break;
		}

		if (selectedElement == null) {
			return null;
		}

		
		final IFileSystem localfileSystem = selectedElement.getFilesystem();
		
		// compile information model jar
		AddressSpaceNodeModelFactory mf = new AddressSpaceNodeModelFactory();
		NamespaceTable serverNS = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

		//AddressSpaceNodeModelFactoryC mfc = new AddressSpaceNodeModelFactoryC();
		//NamespaceTable serverNS = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		// compiles information model
		// compileInformationModel(imFolder, namespaces2export, serverNS, mf);
		// compile devices
		CompileHandlerUtil.compileDevices(drivers, serverNS, mf);

		// cleans up java and class files
		cleanup(null, localfileSystem, drivers);

		return null;
	}

	private void deleteFiles(File file, FileFilter filefilter) {
		File[] files = file.listFiles(filefilter);
		if (files != null) {
			for (File file2remove : files) {
				if (file2remove.isDirectory()) {
					deleteFiles(file2remove, filefilter);
				}

				file2remove.delete();
			}
		}
	}

	private void cleanup(IProgressMonitor monitor, IFileSystem filesystem, Object[] drivers) {

		String rootPath = filesystem.getRootPath();

		// drivers
		IPath driverPath = new Path(rootPath).append("drivers");
		for (Object driver : drivers) {
			String drvName = ((OPCUAServerDriverModelNode) driver).getDriverName();
			IPath dstDriver = driverPath.append(drvName).append("devices");

			File file = dstDriver.toFile();
			if (!file.exists()) {
				continue;
			}

			FileFilter filefilter = new FileFilter() {

				@Override
				public boolean accept(File file) {
					String extension = new Path(file.getPath()).getFileExtension();

					if ("jar".equals(extension)) {
						return false;
					}

					return true;
				}
			};
			// delete information model files
			deleteFiles(file, filefilter);
		}
		// information model
		IPath imFolder = new Path(filesystem.getRootPath()).append("informationmodel");
		File file = imFolder.toFile();
		if (!file.exists()) {
			return;
		}

		FileFilter filefilter = new FileFilter() {

			@Override
			public boolean accept(File file) {
				String extension = new Path(file.getPath()).getFileExtension();

				if ("jar".equals(extension)) {
					return false;
				} else if ("xml".equals(extension)) {
					return false;
				}

				return true;
			}
		};
		// delete information model files
		deleteFiles(file, filefilter);
	}

}
