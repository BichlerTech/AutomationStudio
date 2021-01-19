package com.bichler.astudio.opcua.wizard.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddNodesResult;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.IdType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.VariableAttributes;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;
import org.osgi.framework.Bundle;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.log.ASLogActivator;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.licensemanagement.LicManActivator;
import com.bichler.astudio.licensemanagement.exception.ASStudioLicenseException;
import com.bichler.astudio.navigation.views.NavigationView;
import com.bichler.astudio.opcua.events.CreateOPCUADriverParameter;
import com.bichler.astudio.opcua.events.CreateOPCUAModuleParameter;
import com.bichler.astudio.opcua.handlers.AbstractOPCCreateDriverModel;
import com.bichler.astudio.opcua.handlers.AbstractOPCCreateModuleModel;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.nodes.OPCUARootModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriversModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerModulesModelNode;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.dialogs.PickWorkspaceDialog;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdMode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.persistence.xml.SaxNodeWriter;
import opc.sdk.core.types.TypeTable;
import opc.sdk.server.core.OPCInternalServer;
import opc.server.hbserver.OPCUADriverServer;

public class OPCWizardUtil {
	public static void newOPCServerProject(IWorkbenchPage page, final String serverName, String version, String history,
			String externalModel) {
		try {
			LicManActivator.getDefault().getLicenseManager().getLicense().validateAddOpcuaServer(1, false);
			final NavigationView view = (NavigationView) page.findView(NavigationView.ID);
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(page.getActivePart().getSite().getShell());
			try {
				// run progress dialog to create new opc ua server
				dialog.run(true, true, new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) {
						monitor.beginTask(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
								"wizard.monitor.newserver") + "...", IProgressMonitor.UNKNOWN);
						try {
							IPreferenceStore opcuastore = OPCUAActivator.getDefault().getPreferenceStore();
							// create filesystem
							IFileSystem filesystem = new SimpleFileSystem();
							filesystem.setTargetFileSeparator(File.separator);
							Preferences _preferences = Preferences.userNodeForPackage(PickWorkspaceDialog.class);
							String wsRoot = _preferences.get(PickWorkspaceDialog._KeyWorkspaceRootDir, "");
							IPath runtimePath = new Path(wsRoot)
									.append(opcuastore.getString(OPCUAConstants.OPCUARuntime));
							filesystem.setRootPath(runtimePath.toString());
							IPath server = null;
							try {
								if (!filesystem.isDir(filesystem.getRootPath())) {
									filesystem.addDir(filesystem.getRootPath());
								}
								// create server directory
								IPath opcServers = new Path(filesystem.getRootPath())
										.append(opcuastore.getString(OPCUAConstants.ASOPCUAServersPath));
								if (!filesystem.isDir(opcServers.toOSString())) {
									filesystem.addDir(opcServers.toOSString());
								}
								// create server project
								server = opcServers.append(serverName);
								if (!filesystem.isDir(server.toOSString())) {
									filesystem.addDir(server.toOSString());
								}
								createSectionProjectCertificates(filesystem, server);
								createSectionProjectEcmaScripts(filesystem, server);
								createSectionProjectLocalization(filesystem, server);
								createSectionProjectServerConfig(filesystem, server);
								createSectionProjectDrivers(filesystem, server);
								createSectionProjectShellScripts(filesystem, server);
								createSectionProjectInfomodel(filesystem, server, externalModel);
								createSectionProjectUsers(filesystem, server);
								createSectionProjectLogging(filesystem, server);
								createSectionProjectHistory(filesystem, server, version, history);
								Display.getDefault().asyncExec(new Runnable() {
									@Override
									public void run() {
										view.refresh(OPCUARootModelNode.class, false);
									}
								});
							} catch (IOException ex) {
								Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
								ASLogActivator.getDefault().getLogger().log(Level.SEVERE, CustomString.getString(
										OPCUAActivator.getDefault().RESOURCE_BUNDLE, "studio.error.noopcproject"), ex);
								// removes project resource
								if (server != null) {
									if (filesystem.isDir(server.toOSString())) {
										try {
											filesystem.removeDir(server.toOSString(), true);
										} catch (IOException e) {
											Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE,
													e.getMessage(), e);
										}
									}
								}
							}
						} finally {
							monitor.done();
						}
					}
				});
			} catch (InvocationTargetException | InterruptedException e) {
				Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		} catch (ASStudioLicenseException e1) {
			// prevents to add a new opc ua server project
			Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e1.getMessage(), e1);
		}
	}

	public static void newOPCUADriver(final IWorkbenchPage page, final String drvName, final String drvType,
			final String version) {
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(page.getActivePart().getSite().getShell());
		final OPCNavigationView view = (OPCNavigationView) page.findView(OPCNavigationView.ID);
		try {
			LicManActivator.getDefault().getLicenseManager().getLicense().validateAddDriver(1, false);
			IRunnableWithProgress runnable = new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
							"wizard.monitor.newdriver"), IProgressMonitor.UNKNOWN);
					/**
					 * call create driver structure
					 */
					try {
						/* call select command */
						ICommandService commandService = page.getWorkbenchWindow().getService(ICommandService.class);
						IHandlerService handlerService = page.getWorkbenchWindow().getService(IHandlerService.class);
						Command openDriverDPCmd = commandService.getCommand(
								"com.bichler.astudio.editor." + drvType + "." + version + ".createdrivercommand");
						// now create the new driver tree nodes
						final OPCUAServerModelNode element = (OPCUAServerModelNode) view.getViewer().getInput();
						IFileSystem filesystem = element.getFilesystem();
						try {
							IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
							String driverPath = new Path(filesystem.getRootPath())
									.append(store.getString(OPCUAConstants.ASOPCUADriversFolder)).append(drvName)
									.toOSString();
							CreateOPCUADriverParameter evt = new CreateOPCUADriverParameter();
							evt.setFilesystem(filesystem);
							evt.setDriverpath(driverPath);
							evt.setShell(dialog.getShell());
							ExecutionEvent executionOpenDriverDPEvent = handlerService
									.createExecutionEvent(openDriverDPCmd, null);
							IEvaluationContext evalCtx = (IEvaluationContext) executionOpenDriverDPEvent
									.getApplicationContext();
							evalCtx.getParent().addVariable(AbstractOPCCreateDriverModel.PARAMETER_ID, evt);
							openDriverDPCmd.executeWithChecks(executionOpenDriverDPEvent);
							Display.getDefault().syncExec(() -> view.refresh(OPCUAServerDriversModelNode.class, false));
						} catch (NotDefinedException nde) {
							ASLogActivator.getDefault().getLogger().log(Level.SEVERE, nde.getMessage());
							// could not find driver
							MessageDialog.openError(page.getWorkbenchWindow().getShell(),
									CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
											"wizard.monitor.error"),
									CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
											"wizard.monitor.notdriver"));
						} catch (ExecutionException | NotEnabledException | NotHandledException e) {
							Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
						}
					} finally {
						monitor.done();
					}
				}
			};
			try {
				dialog.run(true, true, runnable);
			} catch (InvocationTargetException | InterruptedException e) {
				Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		} catch (ASStudioLicenseException e1) {
			Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e1.getMessage(), e1);
		}
	}

	public static void newOPCUAModule(final IWorkbenchPage page, final String modName, final String modType,
			final String version) {
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(page.getActivePart().getSite().getShell());
		final OPCNavigationView view = (OPCNavigationView) page.findView(OPCNavigationView.ID);
		try {
			LicManActivator.getDefault().getLicenseManager().getLicense().validateAddModule(1, false);
			IRunnableWithProgress runnable = new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
							"wizard.monitor.newmodule"), IProgressMonitor.UNKNOWN);
					/**
					 * call create driver structure
					 */
					try {
						/* call select command */
						ICommandService commandService = page.getWorkbenchWindow().getService(ICommandService.class);
						IHandlerService handlerService = page.getWorkbenchWindow().getService(IHandlerService.class);
						Command openModuleDPCmd = commandService.getCommand(
								"com.bichler.astudio.editor." + modType + "." + version + ".createmodulecommand");
						// now create the new driver tree nodes
						final OPCUAServerModelNode element = (OPCUAServerModelNode) view.getViewer().getInput();
						IFileSystem filesystem = element.getFilesystem();
						try {
							IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
							String modulePath = new Path(filesystem.getRootPath())
									.append(store.getString(OPCUAConstants.ASOPCUAModulesFolder)).append(modName)
									.toOSString();
							CreateOPCUAModuleParameter evt = new CreateOPCUAModuleParameter();
							evt.setFilesystem(filesystem);
							evt.setModulePath(modulePath);
							evt.setShell(dialog.getShell());
							ExecutionEvent executionOpenModuleDPEvent = handlerService
									.createExecutionEvent(openModuleDPCmd, null);
							IEvaluationContext evalCtx = (IEvaluationContext) executionOpenModuleDPEvent
									.getApplicationContext();
							evalCtx.getParent().addVariable(AbstractOPCCreateModuleModel.PARAMETER_ID, evt);
							openModuleDPCmd.executeWithChecks(executionOpenModuleDPEvent);
							Display.getDefault().syncExec(() -> view.refresh(OPCUAServerModulesModelNode.class, false));
						} catch (NotDefinedException nde) {
							ASLogActivator.getDefault().getLogger().log(Level.SEVERE, nde.getMessage());
							// could not find driver
							MessageDialog.openError(page.getWorkbenchWindow().getShell(),
									CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
											"wizard.monitor.error"),
									CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
											"wizard.monitor.notdriver"));
						} catch (ExecutionException | NotEnabledException | NotHandledException e) {
							Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
						}
					} finally {
						monitor.done();
					}
				}
			};
			try {
				dialog.run(true, true, runnable);
			} catch (InvocationTargetException | InterruptedException e) {
				Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		} catch (ASStudioLicenseException e1) {
			Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e1.getMessage(), e1);
		}
	}

	public static void upgradeOPCUADriver(final IWorkbenchPage page, final String drvName, final String drvType,
			final String version) {
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(page.getActivePart().getSite().getShell());
		final OPCNavigationView view = (OPCNavigationView) page.findView(OPCNavigationView.ID);
		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
						"wizard.monitor.upgradedriver"), IProgressMonitor.UNKNOWN);
				/**
				 * call create driver structure
				 */
				try {
					/* call select command */
					ICommandService commandService = page.getWorkbenchWindow().getService(ICommandService.class);
					IHandlerService handlerService = page.getWorkbenchWindow().getService(IHandlerService.class);
					Command openDriverDPCmd = commandService.getCommand(
							"com.bichler.astudio.editor." + drvType + "." + version + ".upgradedrivercommand");
					// now create the new driver tree nodes
					final OPCUAServerModelNode element = (OPCUAServerModelNode) view.getViewer().getInput();
					IFileSystem filesystem = element.getFilesystem();
					try {
						IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
						String driverPath = new Path(filesystem.getRootPath())
								.append(store.getString(OPCUAConstants.ASOPCUADriversFolder)).append(drvName)
								.toOSString();
						CreateOPCUADriverParameter evt = new CreateOPCUADriverParameter();
						evt.setFilesystem(filesystem);
						evt.setDriverpath(driverPath);
						evt.setShell(dialog.getShell());
						ExecutionEvent executionOpenDriverDPEvent = handlerService.createExecutionEvent(openDriverDPCmd,
								null);
						IEvaluationContext evalCtx = (IEvaluationContext) executionOpenDriverDPEvent
								.getApplicationContext();
						evalCtx.getParent().addVariable(AbstractOPCCreateDriverModel.PARAMETER_ID, evt);
						openDriverDPCmd.executeWithChecks(executionOpenDriverDPEvent);
						Display.getDefault().syncExec(() -> view.refresh(OPCUAServerDriversModelNode.class, false));
					} catch (NotDefinedException nde) {
						ASLogActivator.getDefault().getLogger().log(Level.SEVERE, nde.getMessage());
						// could not find driver
						MessageDialog.openError(page.getWorkbenchWindow().getShell(),
								CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
										"wizard.monitor.error"),
								CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
										"wizard.monitor.notdriver"));
					} catch (ExecutionException | NotEnabledException | NotHandledException e) {
						Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
					}
				} finally {
					monitor.done();
				}
			}
		};
		try {
			dialog.run(true, true, runnable);
		} catch (InvocationTargetException | InterruptedException e) {
			Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public static ExpandedNodeId newOPCUADriverModel(IWorkbenchPage page, NodeId startId, String drvName,
			int namespaceIndex) {
		if (NodeId.isNull(startId)) {
			return ExpandedNodeId.NULL;
		}
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

		ExpandedNodeId parentId = new ExpandedNodeId(nsTable.getUri(startId.getNamespaceIndex()), startId.getValue(),
				nsTable);
		Map<String, NodeId> nodeMapping = new HashMap<>();
		ExpandedNodeId modelId = ExpandedNodeId.NULL;
		try {
			// create driver status node
			modelId = createOPCUADriverModelStructure(
					new ExpandedNodeId(nsTable.getUri(Identifiers.ServerStatusType.getNamespaceIndex()),
							Identifiers.ServerStatusType.getValue(), nsTable),
					namespaceIndex, parentId, new QualifiedName(drvName), NodeClass.Variable,
					new LocalizedText(drvName),
					new ExpandedNodeId(nsTable.getUri(Identifiers.ServerStatusType.getNamespaceIndex()),
							Identifiers.ServerStatusType.getValue(), nsTable),
					Identifiers.Organizes, Identifiers.ServerStatusDataType, new UnsignedInteger[0], Variant.NULL,
					ValueRanks.Scalar.getValue());
			// modelId = createOPCUADriverModelStructure(
			// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
			// .toExpandedNodeId(Identifiers.ServerStatusType),
			// namespaceIndex, parentId, new QualifiedName(drvName), NodeClass.Variable,
			// new LocalizedText(drvName),
			// ServerInstance.getInstance().getServerInstance().getNamespaceUris().toExpandedNodeId(
			// Identifiers.ServerStatusType),
			// Identifiers.Organizes, Identifiers.ServerStatusDataType, new
			// UnsignedInteger[0], Variant.NULL,
			// ValueRanks.Scalar.getValue());
			// do nodemapping for driver.com file
			createOPCUADriverModelStructureMapping(
					ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(modelId), nodeMapping);
			// }
			// append created nodes to driver.com file
			if (!ExpandedNodeId.isNull(modelId)) {
				final OPCNavigationView view = (OPCNavigationView) page.findView(OPCNavigationView.ID);
				final OPCUAServerModelNode element = (OPCUAServerModelNode) view.getViewer().getInput();
				IFileSystem filesystem = element.getFilesystem();
				IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
				String driverPath = new Path(filesystem.getRootPath())
						.append(store.getString(OPCUAConstants.ASOPCUADriversFolder)).append(drvName).toOSString();
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
						BufferedReader reader1 = null;
						StringBuffer sb = new StringBuffer();
						String lineToRemove = "driverstatus";
						String currentLine;
						try {
							reader1 = new BufferedReader(new FileReader(driverCom));
							while ((currentLine = reader1.readLine()) != null) {
								// trim newline when comparing with lineToRemove
								String trimmedLine = currentLine.trim();
								if (trimmedLine.equals(lineToRemove)) {
									break;
								}
								sb.append(currentLine);
								sb.append("\n");
							}
						} catch (IOException e) {
							Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
						} finally {
							if (reader1 != null) {
								try {
									reader1.close();
								} catch (IOException e) {
									Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(),
											e);
								}
							}
						}
						BufferedWriter writer1 = null;
						try {
							writer1 = new BufferedWriter(new FileWriter(driverCom));
							writer1.append(sb.toString());
						} catch (IOException e) {
							Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
						} finally {
							if (writer1 != null) {
								try {
									writer1.close();
								} catch (IOException e) {
									Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(),
											e);
								}
							}
						}
						FileWriter writer = null;
						try {
							writer = new FileWriter(driverCom, true);
							writer.append("\n");
							writer.append("drvstatusflag\n");
							writer.append("true\n");
							writer.append("\n");
							writer.append("driverstatus\n");
							writeDriverModelLine(writer, drvName, ServerInstance.getInstance().getServerInstance()
									.getNamespaceUris().toNodeId(modelId));
							for (Entry<String, NodeId> e : nodeMapping.entrySet()) {
								writeDriverModelLine(writer, e.getKey(), e.getValue());
							}
						} catch (IOException | ServiceResultException e) {
							Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
						} finally {
							if (writer != null) {
								try {
									writer.close();
								} catch (IOException e) {
									Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(),
											e);
								}
							}
						}
					}
				}
				// refresh viewer
				ModelBrowserView opcBrowser = (ModelBrowserView) page.findView(ModelBrowserView.ID);
				opcBrowser.refresh(startId);
				opcBrowser.setDirty(true);
			}
		} catch (ServiceResultException e) {
			Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		return modelId;
	}

	public static ExpandedNodeId newOPCUAModuleModel(IWorkbenchPage page, NodeId startId, String drvName,
			int namespaceIndex) {
		if (NodeId.isNull(startId)) {
			return ExpandedNodeId.NULL;
		}
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

		ExpandedNodeId parentId = new ExpandedNodeId(nsTable.getUri(startId.getNamespaceIndex()), startId.getValue(),
				nsTable);
		Map<String, NodeId> nodeMapping = new HashMap<>();
		ExpandedNodeId modelId = ExpandedNodeId.NULL;
		try {
			// create driver status node
			modelId = createOPCUADriverModelStructure(
					new ExpandedNodeId(nsTable.getUri(Identifiers.ServerStatusType.getNamespaceIndex()),
							Identifiers.ServerStatusType.getValue(), nsTable),
					namespaceIndex, parentId, new QualifiedName(drvName), NodeClass.Variable,
					new LocalizedText(drvName),
					new ExpandedNodeId(nsTable.getUri(Identifiers.ServerStatusType.getNamespaceIndex()),
							Identifiers.ServerStatusType.getValue(), nsTable),
					Identifiers.Organizes, Identifiers.ServerStatusDataType, new UnsignedInteger[0], Variant.NULL,
					ValueRanks.Scalar.getValue());
			// modelId = createOPCUADriverModelStructure(
			// ServerInstance.getInstance().getServerInstance().getNamespaceUris()
			// .toExpandedNodeId(Identifiers.ServerStatusType),
			// namespaceIndex, parentId, new QualifiedName(drvName), NodeClass.Variable,
			// new LocalizedText(drvName),
			// ServerInstance.getInstance().getServerInstance().getNamespaceUris().toExpandedNodeId(
			// Identifiers.ServerStatusType),
			// Identifiers.Organizes, Identifiers.ServerStatusDataType, new
			// UnsignedInteger[0], Variant.NULL,
			// ValueRanks.Scalar.getValue());
			// do nodemapping for driver.com file
			createOPCUADriverModelStructureMapping(
					ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(modelId), nodeMapping);
			// }
			// append created nodes to driver.com file
			if (!ExpandedNodeId.isNull(modelId)) {
				final OPCNavigationView view = (OPCNavigationView) page.findView(OPCNavigationView.ID);
				final OPCUAServerModelNode element = (OPCUAServerModelNode) view.getViewer().getInput();
				IFileSystem filesystem = element.getFilesystem();
				IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
				String driverPath = new Path(filesystem.getRootPath())
						.append(store.getString(OPCUAConstants.ASOPCUADriversFolder)).append(drvName).toOSString();
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
						BufferedReader reader1 = null;
						StringBuffer sb = new StringBuffer();
						String lineToRemove = "driverstatus";
						String currentLine;
						try {
							reader1 = new BufferedReader(new FileReader(driverCom));
							while ((currentLine = reader1.readLine()) != null) {
								// trim newline when comparing with lineToRemove
								String trimmedLine = currentLine.trim();
								if (trimmedLine.equals(lineToRemove)) {
									break;
								}
								sb.append(currentLine);
								sb.append("\n");
							}
						} catch (IOException e) {
							Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
						} finally {
							if (reader1 != null) {
								try {
									reader1.close();
								} catch (IOException e) {
									Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(),
											e);
								}
							}
						}
						BufferedWriter writer1 = null;
						try {
							writer1 = new BufferedWriter(new FileWriter(driverCom));
							writer1.append(sb.toString());
						} catch (IOException e) {
							Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
						} finally {
							if (writer1 != null) {
								try {
									writer1.close();
								} catch (IOException e) {
									Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(),
											e);
								}
							}
						}
						FileWriter writer = null;
						try {
							writer = new FileWriter(driverCom, true);
							writer.append("\n");
							writer.append("drvstatusflag\n");
							writer.append("true\n");
							writer.append("\n");
							writer.append("driverstatus\n");
							writeDriverModelLine(writer, drvName, ServerInstance.getInstance().getServerInstance()
									.getNamespaceUris().toNodeId(modelId));
							for (Entry<String, NodeId> e : nodeMapping.entrySet()) {
								writeDriverModelLine(writer, e.getKey(), e.getValue());
							}
						} catch (IOException | ServiceResultException e) {
							Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
						} finally {
							if (writer != null) {
								try {
									writer.close();
								} catch (IOException e) {
									Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(),
											e);
								}
							}
						}
					}
				}
				// refresh viewer
				ModelBrowserView opcBrowser = (ModelBrowserView) page.findView(ModelBrowserView.ID);
				opcBrowser.refresh(startId);
				opcBrowser.setDirty(true);
			}
		} catch (ServiceResultException e) {
			Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		return modelId;
	}

	private static void createOPCUADriverModelStructureMapping(NodeId nodeId, Map<String, NodeId> nodeMapping)
			throws ServiceResultException {
		BrowseDescription description = new BrowseDescription();
		description.setBrowseDirection(BrowseDirection.Forward);
		description.setIncludeSubtypes(true);
		description.setNodeClassMask(NodeClass.getMask(NodeClass.ALL));
		description.setNodeId(nodeId);
		description.setReferenceTypeId(Identifiers.HierarchicalReferences);
		description.setResultMask(BrowseResultMask.getMask(BrowseResultMask.ALL));
		BrowseResult[] browseResults = ServerInstance.getInstance().getServerInstance().getMaster()
				.browse(new BrowseDescription[] { description }, UnsignedInteger.ZERO, null, null);
		if (browseResults != null && browseResults.length > 0) {
			for (ReferenceDescription referenceDescription : browseResults[0].getReferences()) {
				NodeId id = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
						.toNodeId(referenceDescription.getNodeId());
				nodeMapping.put(referenceDescription.getBrowseName().getName(), id);
				createOPCUADriverModelStructureMapping(id, nodeMapping);
			}
		}
	}

	private static ExpandedNodeId createOPCUADriverModelStructure(ExpandedNodeId idToFollow, int namespaceIndex,
			ExpandedNodeId parentId, QualifiedName browsename, NodeClass nodeClass, LocalizedText displayname,
			ExpandedNodeId typeId, NodeId referenceId, NodeId datatypeId, UnsignedInteger[] arrayDimensions,
			Variant value, Integer valueRank) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		NodeIdMode ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
		NodeId nextnid = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeFactory()
				.getNextNodeId(namespaceIndex, UnsignedInteger.ZERO, IdType.Numeric, ccNodeId);

		ExpandedNodeId elementId = new ExpandedNodeId(nsTable.getUri(nextnid.getNamespaceIndex()), nextnid.getValue(),
				nsTable);
		// ExpandedNodeId elementId = nsTable.toExpandedNodeId(
		// ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeFactory()
		// .getNextNodeId(namespaceIndex, UnsignedInteger.ZERO, IdType.Numeric, null,
		// ccNodeId));
		AddNodesItem node2add = new AddNodesItem();
		node2add.setBrowseName(browsename);
		node2add.setNodeClass(nodeClass);
		node2add.setParentNodeId(parentId);
		node2add.setReferenceTypeId(referenceId);
		node2add.setRequestedNewNodeId(elementId);
		node2add.setTypeDefinition(typeId);
		VariableAttributes attributes = new VariableAttributes();
		attributes.setAccessLevel(AccessLevel.getMask(AccessLevel.ALL));
		attributes.setArrayDimensions(arrayDimensions);
		attributes.setDataType(datatypeId);
		attributes.setHistorizing(false);
		attributes.setMinimumSamplingInterval(1000.0);
		attributes.setUserAccessLevel(AccessLevel.getMask(AccessLevel.ALL));
		attributes.setValue(value);
		attributes.setValueRank(valueRank);
		attributes.setDescription(new LocalizedText(""));
		attributes.setDisplayName(displayname);
		attributes.setUserWriteMask(UnsignedInteger.ZERO);
		attributes.setWriteMask(UnsignedInteger.ZERO);
		try {
			node2add.setNodeAttributes(ExtensionObject.binaryEncode(attributes, EncoderContext.getDefaultInstance()));
			// add node
			Map<ExpandedNodeId, AddNodesItem> mappedNodes = new HashMap<ExpandedNodeId, AddNodesItem>();
			mappedNodes.put(node2add.getRequestedNewNodeId(), node2add);
			AddNodesResult[] result2 = ServerInstance.getInstance().getServerInstance().getMaster()
					.addNodes(new AddNodesItem[] { node2add }, mappedNodes, false, null, false);
			if (result2 != null && result2.length > 0 && result2[0].getStatusCode().isGood()) {
				NodeId newParentId = result2[0].getAddedNodeId();
				BrowseDescription nodeToBrowse = new BrowseDescription();
				nodeToBrowse.setBrowseDirection(BrowseDirection.Forward);
				nodeToBrowse.setIncludeSubtypes(true);
				nodeToBrowse.setNodeClassMask(NodeClass.getMask(NodeClass.Variable));
				nodeToBrowse.setNodeId(nsTable.toNodeId(idToFollow));
				nodeToBrowse.setReferenceTypeId(Identifiers.HierarchicalReferences);
				nodeToBrowse.setResultMask(BrowseResultMask.getMask(BrowseResultMask.ALL));
				BrowseResult[] references2follow = ServerInstance.getInstance().getServerInstance().getMaster()
						.browse(new BrowseDescription[] { nodeToBrowse }, UnsignedInteger.ZERO, null, null);
				if (references2follow != null && references2follow.length > 0) {
					ReferenceDescription[] references = references2follow[0].getReferences();
					if (references != null) {
						TypeTable typeTable = ServerInstance.getInstance().getServerInstance().getTypeTable();
						for (ReferenceDescription refDesc : references) {
							if (!refDesc.getIsForward()) {
								continue;
							}
							ExpandedNodeId id2follow2 = refDesc.getNodeId();
							Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
									.getNodeById(id2follow2);
							ExpandedNodeId typeId2 = ExpandedNodeId.NULL;
							ReferenceNode[] references2 = node.getReferences();
							if (references != null) {
								for (ReferenceNode refNode : references2) {
									NodeId refId = refNode.getReferenceTypeId();
									boolean isTypeDef = typeTable.isTypeOf(Identifiers.HasTypeDefinition, refId);
									if (isTypeDef) {
										typeId2 = refNode.getTargetId();
										continue;
									}
								}
							}
							createOPCUADriverModelStructure(id2follow2, namespaceIndex,
									new ExpandedNodeId(nsTable.getUri(newParentId.getNamespaceIndex()),
											newParentId.getValue(), nsTable),
									node.getBrowseName(), node.getNodeClass(), node.getDisplayName(), typeId2,
									refDesc.getReferenceTypeId(), ((VariableNode) node).getDataType(),
									((VariableNode) node).getArrayDimensions(), ((VariableNode) node).getValue(),
									((VariableNode) node).getValueRank());
							// createOPCUADriverModelStructure(id2follow2, namespaceIndex,
							// nsTable.toExpandedNodeId(newParentId), node.getBrowseName(),
							// node.getNodeClass(),
							// node.getDisplayName(), typeId2, refDesc.getReferenceTypeId(),
							// ((VariableNode) node).getDataType(), ((VariableNode)
							// node).getArrayDimensions(),
							// ((VariableNode) node).getValue(), ((VariableNode) node).getValueRank());
						}
					}
				}
			} else {
				elementId = ExpandedNodeId.NULL;
			}
		} catch (EncodingException e) {
			e.printStackTrace();
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return elementId;
	}

	private static void writeDriverModelLine(FileWriter writer, String key, NodeId nodeId) {
		String ns = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
				.getUri(nodeId.getNamespaceIndex());
		String value = nodeId.toString();
		if (value.contains(";")) {
			value = value.split(";")[1];
		}
		try {
			writer.append(key + "=" + ns + ";" + value + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// public static void newOPCUADriverModelConnection(IWorkbenchPage page,
	// NodeId startId, String drvName) {
	//
	// }
	public static void newOPCUAEcmaScript(IWorkbenchPage page, String scriptName, int scriptType, int interval) {
	}

	public static void newOPCUAShellScript(IWorkbenchPage page) {
	}

	private static void createSectionProjectCertificates(IFileSystem filesystem, IPath server) throws IOException {
		// create certificate store
		IPath certPath = server.append("certificatestore");
		if (!filesystem.isDir(certPath.toOSString())) {
			filesystem.addDir(certPath.toOSString());
		}
		IPath certs = certPath.append("certs");
		if (!filesystem.isDir(certs.toOSString())) {
			filesystem.addDir(certs.toOSString());
		}
		IPath privKey = certs.append("privatekey");
		if (!filesystem.isDir(privKey.toOSString())) {
			filesystem.addDir(privKey.toOSString());
		}
		IPath publicCert = certs.append("publiccert");
		if (!filesystem.isDir(publicCert.toOSString())) {
			filesystem.addDir(publicCert.toOSString());
		}
		IPath rejected = certPath.append("rejected");
		if (!filesystem.isDir(rejected.toOSString())) {
			filesystem.addDir(rejected.toOSString());
		}
		IPath trusted = certPath.append("trusted");
		if (!filesystem.isDir(trusted.toOSString())) {
			filesystem.addDir(trusted.toOSString());
		}
		// IPath rejectedPrivKey = rejected.append("privatekey");
		// if (!filesystem.isDir(rejectedPrivKey.toOSString())) {
		// filesystem.addDir(rejectedPrivKey.toOSString());
		// }
		//
		// IPath rejectedPublicCert = rejected.append("publiccert");
		// if (!filesystem.isDir(rejectedPublicCert.toOSString())) {
		// filesystem.addDir(rejectedPublicCert.toOSString());
		// }
	}

	private static void createSectionProjectDrivers(IFileSystem filesystem, IPath server) throws IOException {
		IPreferenceStore opcuastore = OPCUAActivator.getDefault().getPreferenceStore();
		// create drivers folder
		String driver = server.append(opcuastore.getString(OPCUAConstants.ASOPCUADriversFolder)).toOSString();
		if (!filesystem.isDir(driver)) {
			filesystem.addDir(driver);
		}
	}

	private static void createSectionProjectEcmaScripts(IFileSystem filesystem, IPath server) throws IOException {
		// create ecma scripts
		IPath ecmaPath = server.append("ecmascripts");
		if (!filesystem.isDir(ecmaPath.toOSString())) {
			filesystem.addDir(ecmaPath.toOSString());
		}
		IPath internalPath = ecmaPath.append("internal");
		if (!filesystem.isDir(internalPath.toOSString())) {
			filesystem.addDir(internalPath.toOSString());
		}
		IPath outputdef = internalPath.append("internal.js");
		Bundle bundle = Platform.getBundle("com.bichler.astudio.device.opcua");
		URL url = FileLocator.find(bundle, Path.ROOT.append("runtime_opcua").append("runtime").append("opcua")
				.append("ecmascript").append("internal").append("internal.js"), null);
		URL fileUrl = FileLocator.toFileURL(url);
		File inputdef = new Path(new File(fileUrl.getFile()).getPath()).toFile();
		if (inputdef.exists()) {
			OutputStreamWriter out = null;
			try (FileReader in = new FileReader(inputdef);) {
				OutputStream stream = filesystem.writeFile(outputdef.toOSString());
				out = new OutputStreamWriter(stream);
				int c;
				while ((c = in.read()) != -1)
					out.write(c);
				/** close each buffer and stream */
				out.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private static void createSectionProjectInfomodel(IFileSystem filesystem, IPath server, String externalModel)
			throws IOException {
		File inputdef = null;
		File defaultInfoDetails = null;
		// create information model
		IPath informationPath = server.append("informationmodel");
		if (!filesystem.isDir(informationPath.toOSString())) {
			filesystem.addDir(informationPath.toOSString());
		}

		Bundle bundle = Platform.getBundle("com.bichler.astudio.device.opcua");
		URL url = FileLocator.find(bundle, Path.ROOT.append("runtime_opcua").append("runtime"), null);
		URL fileUrl = FileLocator.toFileURL(url);
		IPath outputdef = server.append("informationmodel").append("model.xml");
		if (!filesystem.isFile(outputdef.toOSString())) {
			filesystem.addFile(outputdef.toOSString());
		}
		// if we found an jar file, so create xml file from that
		if (!externalModel.isEmpty()) {
			if (externalModel.endsWith(".jar")) {
				// inputdef = new Path(externalModel).toFile();
				final OPCUADriverServer opcServer = new OPCUADriverServer();
				opcServer.getServerInstance().importModel(externalModel);

				// export model to xml
				try (FileOutputStream fis = new FileOutputStream(outputdef.toOSString());) {

					OPCInternalServer sInstance = opcServer.getServerInstance();
					new SaxNodeWriter(sInstance.getNamespaceUris(), sInstance.getServerUris(), sInstance.getTypeTable())
							.writeNodes(fis, sInstance.getAddressSpaceManager().getAllNodes(),
									sInstance.getNamespaceUris());
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				return;

			} else if (externalModel.endsWith(".xml")) {
				inputdef = new Path(externalModel).toFile();
			}
		} else {

			/** copy default information model */
			inputdef = new Path(new File(fileUrl.getFile()).getPath()).append("opcua").append("nodeset.xml").toFile();
		}
		defaultInfoDetails = new Path(new File(fileUrl.getFile()).getPath()).append("opcua")
				.append("informationmodeldetails").toFile();

		if (inputdef != null && inputdef.exists()) {

			OutputStreamWriter out = null;
			// write informationmodel file
			try (FileReader in = new FileReader(inputdef);) {
				OutputStream stream = filesystem.writeFile(outputdef.toOSString());
				out = new OutputStreamWriter(stream);
				int c;
				while ((c = in.read()) != -1) {
					out.write(c);
				}
				/** close each buffer and stream */
				out.flush();
			} catch (IOException e) {
				Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			// write informationmodel detail file
			try (FileReader in = new FileReader(defaultInfoDetails);) {
				IPath infomodeldetailoutput = server.append("informationmodel").append("informationmodeldetails");
				if (!filesystem.isFile(infomodeldetailoutput.toOSString())) {
					filesystem.addFile(infomodeldetailoutput.toOSString());
				}
				
				OutputStream stream = filesystem.writeFile(infomodeldetailoutput.toOSString());
				out = new OutputStreamWriter(stream);
				int c;
				while ((c = in.read()) != -1) {
					out.write(c);
				}
				/** close each buffer and stream */
				out.flush();
			} catch (IOException e) {
				Logger.getLogger(OPCWizardUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private static void createSectionProjectLocalization(IFileSystem filesystem, IPath server) throws IOException {
		// create localization to translate opc ua node
		// names
		IPath localizationPath = server.append("localization");
		if (!filesystem.isDir(localizationPath.toOSString())) {
			filesystem.addDir(localizationPath.toOSString());
		}
	}

	private static void createSectionProjectServerConfig(IFileSystem filesystem, IPath server) throws IOException {
		// create server configuration
		String serverConfig = server.append("serverconfig").toOSString();
		if (!filesystem.isDir(serverConfig)) {
			filesystem.addDir(serverConfig);
		}
		// create blank config file
		serverConfig = server.append("serverconfig").append("server.config.xml").toOSString();
		if (!filesystem.isFile(serverConfig)) {
			filesystem.addFile(serverConfig);
		}
		String serverConfigCom = server.append("serverconfig").append("server.config.com").toOSString();
		if (!filesystem.isFile(serverConfigCom)) {
			filesystem.addFile(serverConfigCom);
		}
		String serverConfigTxt = server.append("serverconfig").append("server.config.txt").toOSString();
		if (!filesystem.isFile(serverConfigTxt)) {
			filesystem.addFile(serverConfigTxt);
		}
		/** copy default server config */
		/** copy default information model */
		Bundle bundle = Platform.getBundle("com.bichler.astudio.device.opcua");
		URL url = FileLocator.find(bundle, Path.ROOT.append("runtime_opcua").append("runtime"), null);
		URL fileUrl = FileLocator.toFileURL(url);
		File inputdef = new Path(new File(fileUrl.getFile()).getPath()).append("opcua").append("server.config.xml")
				.toFile();
		if (inputdef.exists()) {
			FileReader in = null;
			OutputStream stream1 = null, stream2 = null;
			try {
				in = new FileReader(inputdef);
				stream1 = filesystem.writeFile(serverConfig);
				stream2 = filesystem.writeFile(serverConfigCom);
				OutputStreamWriter out1 = new OutputStreamWriter(stream1);
				OutputStreamWriter out2 = new OutputStreamWriter(stream2);
				int c;
				while ((c = in.read()) != -1) {
					out1.write(c);
					out2.write(c);
				}
				/** close each buffer and stream */
				out1.flush();
				out2.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (stream1 != null) {
					try {
						stream1.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (stream2 != null) {
					try {
						stream2.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		/** copy default server config */
		inputdef = new Path(new File(fileUrl.getFile()).getPath()).append("opcua").append("server.config.txt").toFile();
		if (inputdef.exists()) {
			FileReader in = null;
			OutputStream stream1 = null;// , stream2 = null;
			try {
				in = new FileReader(inputdef);
				stream1 = filesystem.writeFile(serverConfigTxt);
				OutputStreamWriter out1 = new OutputStreamWriter(stream1);
				int c;
				while ((c = in.read()) != -1) {
					out1.write(c);
				}
				/** close each buffer and stream */
				out1.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (stream1 != null) {
					try {
						stream1.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private static void createSectionProjectShellScripts(IFileSystem filesystem, IPath server) throws IOException {
		// create shell scripts
		IPath shellPath = server.append("shellscripts");
		if (!filesystem.isDir(shellPath.toOSString())) {
			filesystem.addDir(shellPath.toOSString());
		}
	}

	public static void createSectionProjectLogging(IFileSystem filesystem, IPath server) throws IOException {
		IPath logOutput = server.append("log.properties");
		if (!filesystem.isFile(logOutput.toOSString())) {
			try {
				filesystem.addFile(logOutput.toOSString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Bundle bundle = Platform.getBundle("com.bichler.astudio.device.opcua");
		URL url = FileLocator.find(bundle, Path.ROOT.append("runtime_opcua").append("runtime"), null);
		URL fileUrl = FileLocator.toFileURL(url);
		File logPath = new Path(new File(fileUrl.getFile()).getPath()).append("log.properties").toFile();
		if (logPath.exists()) {
			FileReader in = null;
			OutputStreamWriter out = null;
			try {
				in = new FileReader(logPath);
				OutputStream stream = filesystem.writeFile(logOutput.toOSString());
				out = new OutputStreamWriter(stream);
				int c;
				while ((c = in.read()) != -1)
					out.write(c);
				/** close each buffer and stream */
				out.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void createSectionProjectHistory(IFileSystem filesystem, IPath server, String version, String history)
			throws IOException {
		/**
		 * add version output
		 */
		IPath versionOutput = server.append("informationmodel").append("version.txt");
		if (!filesystem.isFile(versionOutput.toOSString())) {
			try {
				filesystem.addFile(versionOutput.toOSString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileReader in = null;
		OutputStreamWriter out = null;
		try {
			OutputStream stream = filesystem.writeFile(versionOutput.toOSString());
			out = new OutputStreamWriter(stream);
			out.write(version);
			/** close each buffer and stream */
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		/**
		 * add historyOutput
		 */
		IPath historyOutput = server.append("history.txt");
		if (!filesystem.isFile(historyOutput.toOSString())) {
			try {
				filesystem.addFile(historyOutput.toOSString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		in = null;
		out = null;
		try {
			OutputStream stream = filesystem.writeFile(historyOutput.toOSString());
			out = new OutputStreamWriter(stream);
			out.write(history);
			/** close each buffer and stream */
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void createSectionProjectUsers(IFileSystem filesystem, IPath server) {
		// create user management folder
		IPath usermanagementpath = server.append("users");
		if (!filesystem.isDir(usermanagementpath.toOSString())) {
			try {
				filesystem.addDir(usermanagementpath.toOSString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
