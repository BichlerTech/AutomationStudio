package com.bichler.astudio.opcua.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.DeleteNodesItem;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.licensemanagement.LicManActivator;
import com.bichler.astudio.licensemanagement.exception.ASStudioLicenseException;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.navigation.views.IFileSystemNavigator;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class DeleteOPCUAServerDriverHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		MessageDialog dia = new MessageDialog(page.getActivePart().getSite().getShell(),
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "handler.message.deletedriver"),
				null,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
						"handler.message.deletedriver.confirm"),
				MessageDialog.QUESTION,
				new String[] { CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.yes"),
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.no") },
				0);

		int result = dia.open();
		if (result != Dialog.OK) {
			return null;
		}
		TreeSelection selection = (TreeSelection) HandlerUtil.getCurrentSelection(event);
		Object selectedNode = selection.getFirstElement();

		if (selectedNode instanceof OPCUAServerDriverModelNode) {
			OPCUAServerDriverModelNode node = (OPCUAServerDriverModelNode) selectedNode;
			IFileSystem filesystem = node.getFilesystem();
			// String serverName = node.getServerName();
			String drvName = node.getDriverName();
			IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
			// we have the correct node so delete it
			String driverPath = new Path(filesystem.getRootPath())
					.append(store.getString(OPCUAConstants.ASOPCUADriversFolder)).append(drvName).toOSString();

			// Remove related nodes for driver
			File[] files = new File(driverPath).listFiles();
			if (files != null) {
				File driverCom = null;
				for (File file : files) {
					String name = file.getName();
					if ("driver.com".equalsIgnoreCase(name)) {
						driverCom = file;
						break;
					}
				}

				if (driverCom != null) {
					BufferedReader reader = null;
					List<DeleteNodesItem> nodeids = new ArrayList<>();
					try {
						reader = new BufferedReader(new InputStreamReader(new FileInputStream(driverCom)));
						String line = null;
						boolean drvstatus = false;
						// driverstatus is always appended
						while ((line = reader.readLine()) != null) {
							if (line.equalsIgnoreCase("driverstatus")) {
								drvstatus = true;
							} else if (drvstatus) {
								if (line.trim().isEmpty()) {
									drvstatus = false;
									break;
								}

								int start = line.indexOf("=");
								String nodeid = line.substring(start + 1);
								String[] nodeid2parse = nodeid.split(";");
								NodeId parsedId = NodeId.NULL;
								try {
									if (nodeid2parse.length == 1) {
										parsedId = NodeId.parseNodeId(nodeid);
									} else {
										String ns = nodeid2parse[0];
										String idValue = nodeid2parse[1];
										int index = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
												.getIndex(ns);

										parsedId = NodeId.parseNodeId("ns=" + index + ";" + idValue);
									}

									DeleteNodesItem node2delete = new DeleteNodesItem();
									node2delete.setDeleteTargetReferences(true);
									node2delete.setNodeId(parsedId);
									nodeids.add(node2delete);
								} catch (IllegalArgumentException e) {
									// skip invalid nodeids
								}
							}
						}

						if (!nodeids.isEmpty()) {
							ServerInstance.getInstance().getServerInstance().getMaster()
									.deleteNodes(nodeids.toArray(new DeleteNodesItem[0]), null);

							// refresh viewer
							ModelBrowserView opcBrowser = (ModelBrowserView) page.findView(ModelBrowserView.ID);
							opcBrowser.refresh();
							opcBrowser.setDirty(true);
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ServiceResultException e) {
						e.printStackTrace();
					} finally {
						if (reader != null) {
							try {
								reader.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

			try {
				filesystem.removeDir(driverPath, true);
				// after delete, refresh parent in project explorer
				IFileSystemNavigator view = (IFileSystemNavigator) page.getActivePart();
				view.refresh((StudioModelNode) node.getParent());

				// refresh viewer
				// ModelBrowserView opcBrowser = (ModelBrowserView)
				// page.findView(ModelBrowserView.ID);
				// opcBrowser.refresh();
				// opcBrowser.setDirty(true);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			LicManActivator.getDefault().getLicenseManager().getLicense().validateDeleteDriver(1, false);
		} catch (ASStudioLicenseException e) {
			// should never happen
			e.printStackTrace();
		}

		return null;
	}

}
