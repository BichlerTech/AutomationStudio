package com.bichler.astudio.opcua.handlers.opcua;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.EditorPart;
import org.osgi.framework.Bundle;

import com.bichler.astudio.core.user.util.UserUtils;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.log.ASLogActivator;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.driver.IOPCDataPointEditPart;
import com.bichler.astudio.opcua.handlers.events.OPCUAValidationDriverParameter;
import com.bichler.astudio.opcua.handlers.opcua.resource.OPCUAValidationHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerInfoModelsNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerModuleModelNode;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.extern.NamespaceHandler;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.extern.ParametrizedExportModelHandler;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.extern.ParametrizedImportModelHandler;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.extern.ParametrizedImportModelHandler2;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.extern.ParametrizedNewModelHandler;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.server.core.UAServerApplicationInstance;

public class OPCUAUtil {
	/**
	 * "hard close with no progress dialog"
	 * 
	 * @param window
	 */
	public static void closePerspective(final IWorkbenchWindow window, String servername) {
		// close opc model view
		ModelBrowserView modelbrowser = (ModelBrowserView) window.getActivePage().findView(ModelBrowserView.ID);
		if (modelbrowser != null) {
			modelbrowser.closeView();
		}
		// find all open editors to store
		final IEditorReference[] editorRefs = window.getActivePage().getEditorReferences();
		for (IEditorReference ref : editorRefs) {
			IEditorPart editor = ref.getEditor(false);
			// if (editor instanceof NodeEditorPart)
			{
				if (editor != null)
					editor.getEditorSite().getPage().closeEditor(editor, false);
			}
		}
		// close opc server connection
		final OPCNavigationView navigation = (OPCNavigationView) window.getActivePage().findView(OPCNavigationView.ID);
		if (navigation != null) {
			if (servername == null) {
				OPCUAServerModelNode root = (OPCUAServerModelNode) navigation.getViewer().getInput();
				if (root != null) {
					servername = root.getServerName();
				}
			}
			// close server
			if (servername != null) {
				UAServerApplicationInstance server = Studio_ResourceManager.removeOPCUAServerInstance(servername);
				if (server != null) {
					ServerInstance.closeServer();
				}
			}
			// switch perspective
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					navigation.getViewer().setInput(null);
					// switch perspective
					try {
						window.getWorkbench().showPerspective("com.bichler.astudio.perspective.studio", window);
					} catch (WorkbenchException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
					}
				}
			});
		}
		ASLogActivator.getDefault().getLogger()
				.info(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "handler.log.project.close")
						+ " " + servername);
	}

	public static void editNamespaceTable(IWorkbenchWindow window) {
		IHandlerService service = window.getService(IHandlerService.class);
		try {
			service.executeCommand(NamespaceHandler.ID, null);
		} catch (NotDefinedException | NotEnabledException | NotHandledException | ExecutionException e) {
			Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
		}
	}

	public static void doSaveAll(IWorkbenchPage page) {
		IEditorReference[] editors = page.getEditorReferences();
		for (IEditorReference editor : editors) {
			IEditorPart editor2 = editor.getEditor(true);
			boolean isDirty = editor2.isDirty();
			if (isDirty) {
				editor2.doSave(null);
			}
		}
		ModelBrowserView modelView = (ModelBrowserView) page.findView(ModelBrowserView.ID);
		boolean isDirty = modelView.isDirty();
		if (isDirty) {
			modelView.doSave();
		}
	}

	public static boolean isOPCDirty(IWorkbenchPage page) {
		IEditorReference[] editors = page.getEditorReferences();
		for (IEditorReference editor : editors) {
			IEditorPart editor2 = editor.getEditor(true);
			boolean isDirty = editor2.isDirty();
			if (isDirty) {
				return true;
			}
		}
		ModelBrowserView modelView = (ModelBrowserView) page.findView(ModelBrowserView.ID);
		boolean isDirty = modelView.isDirty();
		if (isDirty) {
			return true;
		}
		return false;
	}

	public static void openModel2(final IWorkbenchWindow window, final String path) {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(window.getShell());
		IRunnableWithProgress work = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask(
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.openlocation")
								+ "...",
						IProgressMonitor.UNKNOWN);
				try {
					IHandlerService service = window.getService(IHandlerService.class);
					ICommandService cmdService = window.getService(ICommandService.class);
					Command command = cmdService.getCommand(ParametrizedImportModelHandler2.ID);
					IParameter parameter;
					try {
						parameter = command.getParameter(ParametrizedImportModelHandler2.PARAMETER_FILE);
						Parameterization[] parameterizations = new Parameterization[1];
						parameterizations[0] = new Parameterization(parameter, path);
						final ParameterizedCommand cmd = new ParameterizedCommand(command, parameterizations);
						service.executeCommand(cmd, null);
					} catch (NotDefinedException | NotEnabledException | NotHandledException | ExecutionException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
					}
				} finally {
					monitor.done();
				}
			}
		};
		try {
			dialog.run(true, false, work);
		} catch (InvocationTargetException | InterruptedException e) {
			Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
		}
	}

	public static void openModel(final IWorkbenchWindow window, final String path) {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(window.getShell());
		IRunnableWithProgress work = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask(
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.openlocation")
								+ "...",
						IProgressMonitor.UNKNOWN);
				try {
					IHandlerService service = window.getService(IHandlerService.class);
					ICommandService cmdService = window.getService(ICommandService.class);
					Command command = cmdService.getCommand(ParametrizedImportModelHandler.ID);
					IParameter parameter;
					try {
						parameter = command.getParameter(ParametrizedImportModelHandler.PARAMETER_FILE);
						Parameterization[] parameterizations = new Parameterization[1];
						parameterizations[0] = new Parameterization(parameter, path);
						final ParameterizedCommand cmd = new ParameterizedCommand(command, parameterizations);
						service.executeCommand(cmd, null);
					} catch (NotDefinedException | NotEnabledException | NotHandledException | ExecutionException e) {
						Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
					}
				} finally {
					monitor.done();
				}
			}
		};
		try {
			dialog.run(true, false, work);
		} catch (InvocationTargetException | InterruptedException e) {
			Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
		}
	}

	public static void openProjectPerspective(final IWorkbenchWindow window, final OPCUAServerModelNode node) {
		// now verify if we have the correct studio project file version
		ASLogActivator.getDefault().getLogger()
				.info(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "handler.log.project.open")
						+ " " + node.getServerName());
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(window.getShell());
		IRunnableWithProgress work = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask(
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "handler.log.project.open"),
						IProgressMonitor.UNKNOWN);
				openProjectPerpective(monitor, window, node);
			}
		};
		try {
			dialog.run(true, true, work);
		} catch (InvocationTargetException | InterruptedException e) {
			Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
		}
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				validateOPCUAPerspective();
			}
		});
	}

	public static void openReportPerspective(final IWorkbenchWindow window, final OPCUAServerModelNode node) {
		// now verify if we have the correct studio project file version
		ASLogActivator.getDefault().getLogger()
				.info(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "handler.log.project.open")
						+ " " + node.getServerName());
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(window.getShell());
		IRunnableWithProgress work = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask(
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "handler.log.project.open"),
						IProgressMonitor.UNKNOWN);

				openReportPerpective(monitor, window, node);
			}
		};
		try {
			dialog.run(true, true, work);
		} catch (InvocationTargetException | InterruptedException e) {
			Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
		}
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				validateOPCUAPerspective();
			}
		});
	}

	public static void registerContextMenuOnDPEditor(IOPCDataPointEditPart editor) {
		if (editor.getDPControl() != null) {
			MenuManager menuMgr = new MenuManager("toolbar:" + "com.hbsoft.comet.menu.dp.editors");
			menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
			Menu menu = menuMgr.createContextMenu(editor.getDPControl());
			editor.getDPControl().setMenu(menu);
			((EditorPart) editor).getSite().registerContextMenu(menuMgr, editor);
		}
	}

	public static void resetModel(IWorkbenchWindow window, OPCNavigationView view, OPCUAServerModelNode input) {
		resetModel(window, input.getFilesystem().getRootPath());
		// refresh information model tree
		view.refresh(OPCUAServerInfoModelsNode.class, false);
	}

	public static void resetModel(final IWorkbenchWindow window, final String path) {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(window.getShell());
		IRunnableWithProgress work = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
						"handler.message.informationmodel.reset") + "...", IProgressMonitor.UNKNOWN);
				try {
					final IHandlerService service = window.getService(IHandlerService.class);
					ICommandService cmdService = window.getService(ICommandService.class);
					Command command = cmdService.getCommand(ParametrizedNewModelHandler.ID);
					IParameter parameter = null;
					try {
						parameter = command.getParameter(ParametrizedNewModelHandler.PARAMETER_PROJECT_PATH);
						Parameterization[] parameterizations = new Parameterization[1];
						parameterizations[0] = new Parameterization(parameter, path);
						final ParameterizedCommand cmd = new ParameterizedCommand(command, parameterizations);
						service.executeCommand(cmd, null);
					} catch (NotDefinedException | NotHandledException | NotEnabledException | ExecutionException e) {
						Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
					}
				} finally {
					monitor.done();
				}
			}
		};
		try {
			dialog.run(true, true, work);
		} catch (InvocationTargetException e) {
			Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
		} catch (InterruptedException e) {
			Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
		}
	}

	/**
	 * 
	 * @param window
	 * @param path   path to store opc ua information model
	 * @return
	 */
	public static Object saveModel(final IWorkbenchWindow window, final String path) {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(window.getShell());
		final ResultValue value = new ResultValue();
		IRunnableWithProgress work = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask(
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.save") + "...",
						IProgressMonitor.UNKNOWN);
				try {
					final IHandlerService service = window.getService(IHandlerService.class);
					ICommandService cmdService = window.getService(ICommandService.class);
					Command command = cmdService.getCommand(ParametrizedExportModelHandler.ID);
					IParameter parameter;
					try {
						parameter = command.getParameter(ParametrizedExportModelHandler.PARAMETER_FILE);
						Parameterization[] parameterizations = new Parameterization[1];
						parameterizations[0] = new Parameterization(parameter, path);
						final ParameterizedCommand cmd = new ParameterizedCommand(command, parameterizations);
						value.result = service.executeCommand(cmd, null);
					} catch (NotDefinedException | NotHandledException | NotEnabledException | ExecutionException e) {
						Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
					}
				} finally {
					monitor.done();
				}
			}
		};
		try {
			dialog.run(true, true, work);
		} catch (InvocationTargetException | InterruptedException e) {
			Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
		}
		return value.result;
	}

	/**
	 * Validates the whole opc ua perspective including usermanagement,drivers
	 */
	public static void validateOPCUAPerspective() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null) {
			throw new IllegalArgumentException("Cannot validate opc ua perspective");
		}
		IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			throw new IllegalArgumentException("Cannot validate opc ua perspective");
		}
		IHandlerService handlerService = (IHandlerService) window.getService(IHandlerService.class);
		Logger.getLogger(OPCUAUtil.class.getName()).log(Level.INFO,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "handler.message.validate"));
		try {
			handlerService.executeCommand(OPCUAValidationHandler.ID, new Event());
		} catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
			Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
		}
	}

	public static void validateOPCUAModule(IFileSystem fileSystem, OPCUAServerModuleModelNode module) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null) {
			throw new IllegalArgumentException("Cannot validate opc ua perspective");
		}
		IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			throw new IllegalArgumentException("Cannot validate opc ua perspective");
		}
		IHandlerService handlerService = window.getService(IHandlerService.class);
		ICommandService cmdService = window.getService(ICommandService.class);
		OPCUAServerModuleModelNode node = module;
		String type = node.getModuleType();
		String version = node.getModuleVersion();
		String drvName = node.getModuleName();
		String command = "com.bichler.astudio.editor." + type + "." + version + ".validate";
		Command openDriverDPCmd = cmdService.getCommand(command);
		OPCUAValidationDriverParameter evt = new OPCUAValidationDriverParameter();
		evt.setDrvName(drvName);
		evt.setFilesystem(fileSystem);
		evt.setModelNode(node);
		// From a view you get the site which allow to get the service
		ExecutionEvent executionOpenDriverDPEvent = handlerService.createExecutionEvent(openDriverDPCmd, null);
		// add parameter object
		IEvaluationContext evalCtx = (IEvaluationContext) executionOpenDriverDPEvent.getApplicationContext();
		evalCtx.getParent().addVariable(OPCUAValidationHandler.PARAMETER_ID, evt);
		try {
			openDriverDPCmd.executeWithChecks(executionOpenDriverDPEvent);
		} catch (Exception ex) {
			Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, ex.getMessage());
		}
	}

	public static void validateOPCUADriver(IFileSystem fileSystem, OPCUAServerDriverModelNode driver) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null) {
			throw new IllegalArgumentException("Cannot validate opc ua perspective");
		}
		IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			throw new IllegalArgumentException("Cannot validate opc ua perspective");
		}
		IHandlerService handlerService = window.getService(IHandlerService.class);
		ICommandService cmdService = window.getService(ICommandService.class);
		OPCUAServerDriverModelNode node = driver;
		String type = node.getDriverType();
		String version = node.getDriverVersion();
		String drvName = node.getDriverName();
		String command = "com.bichler.astudio.editor." + type + "." + version + ".validate";
		Command openDriverDPCmd = cmdService.getCommand(command);
		OPCUAValidationDriverParameter evt = new OPCUAValidationDriverParameter();
		evt.setDrvName(drvName);
		evt.setFilesystem(fileSystem);
		evt.setModelNode(node);
		// From a view you get the site which allow to get the service
		ExecutionEvent executionOpenDriverDPEvent = handlerService.createExecutionEvent(openDriverDPCmd, null);
		// add parameter object
		IEvaluationContext evalCtx = (IEvaluationContext) executionOpenDriverDPEvent.getApplicationContext();
		evalCtx.getParent().addVariable(OPCUAValidationHandler.PARAMETER_ID, evt);
		try {
			openDriverDPCmd.executeWithChecks(executionOpenDriverDPEvent);
		} catch (Exception ex) {
			Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, ex.getMessage());
		}
	}

	private static void openProjectPerpective(IProgressMonitor monitor, IWorkbenchWindow window,
			OPCUAServerModelNode node) {
		// time consuming work here
		openOPCUAServerProject(monitor, window, node);
		IWorkbenchPage page = switchPerspective(window,
				"com.bichler.astudio.opcua.perspective"/* OPCServerPerspective.ID */);
		// monitor is done
		if (!monitor.isCanceled()) {
			syncOPCUAServerProjectWithUI(page, node);
		}
		// monitor is cancled
		else {
			closePerspective(window, node.getServerName());
		}
		// sync with UI
		monitor.done();
	}

	private static void openReportPerpective(IProgressMonitor monitor, IWorkbenchWindow window,
			OPCUAServerModelNode node) {
		// time consuming work here
		openOPCUAServerProject(monitor, window, node);
		IWorkbenchPage page = switchPerspective(window, "org.eclipse.birt.report.designer.ui.ReportPerspective");
		// monitor is done
		if (!monitor.isCanceled()) {
		}
		// monitor is cancled
		else {
			closePerspective(window, node.getServerName());
		}
		// sync with UI
		monitor.done();
	}

	public static class CPage {
		IWorkbenchPage page;

		public IWorkbenchPage getPage() {
			return page;
		}

		public void setPage(IWorkbenchPage page) {
			this.page = page;
		}
	}

	private static IWorkbenchPage switchPerspective(final IWorkbenchWindow window, final String id) {
		final CPage cPage = new OPCUAUtil.CPage();
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					IWorkbenchPage page = window.getWorkbench().showPerspective(id, window);
					cPage.setPage(page);
					OPCNavigationView view = (OPCNavigationView) page.findView(OPCNavigationView.ID);
					view.setInput(null);
				} catch (WorkbenchException e) {
					Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
				}
			}
		});
		return cPage.getPage();
	}

	private static void syncOPCUAServerProjectWithUI(final IWorkbenchPage page, final StudioModelNode node) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				OPCNavigationView navigation = (OPCNavigationView) page.findView(OPCNavigationView.ID);
				// refresh navigation input
				navigation.setInput(node);
				final ModelBrowserView mbv = (ModelBrowserView) page.findView(ModelBrowserView.ID);
				if (mbv != null) {
					mbv.startView();
				}

				boolean isAdmin = UserUtils.testUserRights(1);
				if (isAdmin) {
					try {
						page.showView(ASLogActivator.LOGVIEW_ID);
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}else {
					IViewPart loggingView = page.findView(ASLogActivator.LOGVIEW_ID);
					page.hideView(loggingView);
				}
				
				// show properties view
				try {
					page.showView("com.bichler.astudio.properties.view");
				} catch (PartInitException e) {
					e.printStackTrace();
				}
				
			}
		});
	}

	private static void verifyConfig(String configuration) {
		if (!new File(configuration).exists()) {
			// old project found -> convert to new change server.config.xml to
			// server.config.txt
			try {
				new File(configuration).createNewFile();
				// fill with default values
				/** copy default server config */
				/** copy default server config */
				/** copy default information model */
				Bundle bundle = Platform.getBundle("com.bichler.astudio.device");
				URL url = FileLocator.find(bundle, Path.ROOT.append("runtime_opcua").append("runtime"), null);
				URL fileUrl = FileLocator.toFileURL(url);
				File inputdef = new Path(new File(fileUrl.getFile()).getPath()).append("opcua")
						.append("server.config.txt").toFile();
				if (inputdef.exists()) {
					FileReader in = null;
					OutputStream stream1 = null;
					OutputStreamWriter out1 = null;
					try {
						in = new FileReader(inputdef);
						stream1 = new FileOutputStream(configuration);
						out1 = new OutputStreamWriter(stream1);
						int c;
						while ((c = in.read()) != -1) {
							out1.write(c);
						}
						/** close each buffer and stream */
						out1.flush();
					} catch (IOException e) {
						Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
					} finally {
						if (out1 != null) {
							try {
								out1.close();
							} catch (IOException e) {
								Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
							}
						}
						if (in != null) {
							try {
								in.close();
							} catch (IOException e) {
								Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
							}
						}
						if (stream1 != null) {
							try {
								stream1.close();
							} catch (IOException e) {
								Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
							}
						}
					}
				}
			} catch (IOException e) {
				Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
			}
		}
	}

	private static void openOPCUAServerProject(IProgressMonitor monitor, IWorkbenchWindow window,
			OPCUAServerModelNode node) {
		String rootPath = node.getFilesystem().getRootPath();
		IPath rootFolder = new Path(rootPath);
		final IPath infoModelFolder = rootFolder.append("informationmodel");
		final IPath infoDocuFolder = rootFolder.append("docu");
		final IPath localizedFolder = rootFolder.append("localization");
		final String configuration = rootFolder.append("serverconfig").append("server.config.xml").toOSString();
		verifyConfig(configuration);
		final String certificates = rootFolder.toOSString();
		String pathInfomodel = infoModelFolder.toOSString();
		String pathModelDocu = infoDocuFolder.toOSString();
		// set path for saving the opc model
		Studio_ResourceManager.setServerName(node.getServerName());
		Studio_ResourceManager.setInfoModellerResource(pathInfomodel);
		Studio_ResourceManager.setInfoModellerDokuResource(pathModelDocu);
		Studio_ResourceManager.setProgressMonitor(monitor);
		try {
			Studio_ResourceManager.getOrNewOPCUAServer(node.getServerName(), configuration, certificates,
					localizedFolder.toOSString(), infoModelFolder.toFile().toURI().toURL());
			Studio_ResourceManager.loadNodeInfos();
			Studio_ResourceManager.loadNodeExtensions();
			Studio_ResourceManager.loadDatatypeDefinitions();
			Studio_ResourceManager.loadInformationModelDetails();
			Studio_ResourceManager.loadNodeCustomAttributes();
		} catch (MalformedURLException e) {
			Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
		} catch (ExecutionException e) {
			Logger.getLogger(OPCUAUtil.class.getName()).log(Level.SEVERE, e.getMessage());
			closePerspective(window, node.getServerName());
		}
	}
}
