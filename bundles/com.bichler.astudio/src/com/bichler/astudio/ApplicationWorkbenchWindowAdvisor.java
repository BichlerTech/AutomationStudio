package com.bichler.astudio;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.bichler.astudio.language.handler.LanguageUtil;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(750, 450));
		configurer.setShowCoolBar(true);
		configurer.setShowMenuBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShowPerspectiveBar(false);
		configurer.setShowPerspectiveBar(false);
		configurer.setShowProgressIndicator(true);
		// set current theme for views and editors
	}

	@Override
	public void postWindowCreate() {
		IContributionItem[] mItems, mSubItems;
		IMenuManager mm = getWindowConfigurer().getActionBarConfigurer().getMenuManager();
		mItems = mm.getItems();
		List<IContributionItem> items2Remove = new ArrayList<>();
		for (int i = 0; i < mItems.length; i++) {
			//System.err.println("--ITEM: "+mItems[i].getId());
			boolean remove = false;
			if (mItems[i] != null && mItems[i].getId() != null
					&& mItems[i].getId().compareTo("file") == 0) {
				remove = true;
			} else if (mItems[i] != null && mItems[i].getId() != null
					&& mItems[i].getId().compareTo("help") == 0) {
				remove = true;
			} else if (mItems[i] != null && mItems[i].getId() != null
					&& mItems[i].getId().compareTo("org.eclipse.ui.run") == 0) {
				remove = true;
			} else if (mItems[i] != null && mItems[i].getId() != null
					&& mItems[i].getId().compareTo("org.eclipse.search.menu") == 0) {
				remove = true;
			}
			
			if(remove) {
				items2Remove.add(mItems[i]);
			}
			/*
			if (mItems[i] instanceof MenuManager) {
				mSubItems = ((MenuManager) mItems[i]).getItems();
				for (int j = 0; j < mSubItems.length; j++) {
					if (mItems[i].getId().equals("file") || mItems[i].getId().equals("help")
							|| mItems[i].getId().equals("search")) {
						items2Remove.add(mItems[i]);
					}
				}
			} else {
				items2Remove.add(mItems[i]);
			}*/
		}
		for (IContributionItem item : items2Remove) {
			mm.remove(item);
		}

		items2Remove.clear();
		ICoolBarManager cm = getWindowConfigurer().getActionBarConfigurer().getCoolBarManager();
		mItems = cm.getItems();
		for (int i = 0; i < mItems.length; i++) {
			if (mItems[i] != null && mItems[i].getId() != null
					&& mItems[i].getId().compareTo("org.eclipse.search.searchActionSet") == 0) {
				items2Remove.add(mItems[i]);
			}
			/*else if(mItems[i] != null && mItems[i].getId() != null
					&& mItems[i].getId().compareTo("org.eclipse.debug.ui.launchActionSet") == 0) {
				items2Remove.add(mItems[i]);
			}*/
			
		}
		for (IContributionItem item : items2Remove) {
			cm.remove(item);
		}
		LanguageUtil.initializeLanguage();

		registerLogging();
	}

	@Override
	public void postWindowClose() {
		// LicManActivator.getDefault().getLicenseManager().releaseLicense();
	}

	// private void initializeLicenseManager() {
	// /**
	// * WIBU-Systems license stick
	// */
	// long firmCode = 10l;
	// long defaultProductCode = 1001l;
	// long studentProductCode = 1002l;
	// long productCode = 0l;
	// // try to check student version
	// long hcmse =
	// LicManActivator.getDefault().getLicenseManager().accessLicense(firmCode,
	// studentProductCode);
	// if (hcmse != 0) {
	// productCode = studentProductCode;
	//
	// LicenseUtil.openDialogStartupAcademicLicense();
	// }
	// // check default license
	// else {
	// hcmse =
	// LicManActivator.getDefault().getLicenseManager().accessLicense(firmCode,
	// defaultProductCode);
	// if (hcmse == 0) {
	// boolean isActive =
	// LicManActivator.getDefault().getLicenseManager().isActive();
	// if (isActive) {
	// LicenseUtil.openDialogStartupEvaluationLicense();
	// }
	// }
	// productCode = defaultProductCode;
	// }
	// // set product license hcmse
	// LicManActivator.getDefault().getLicenseManager().setHcmse(firmCode,
	// productCode, hcmse);
	// LicenseCategory license =
	// LicManActivator.getDefault().getLicenseManager().getLicense();
	//
	// switch (license) {
	// case Enterprise:
	// break;
	// default:
	// String text = getWindowConfigurer().getWindow().getShell().getText();
	// getWindowConfigurer().getWindow().getShell().setText(text + " (" +
	// license.getName() + ")");
	// break;
	// }
	//
	//
	// /**
	// * Software license category.
	// */
	// // opc ua connections
	//// ConnectionsHostManager opcuaConnectionsManager = new
	// ConnectionsHostManager();
	//// IPreferenceStore opcuastore =
	// OPCUAActivator.getDefault().getPreferenceStore();
	//// String opcuaFolder = opcuastore.getString(OPCUAConstants.OPCUARuntime);
	//// String opcuaServers =
	// opcuastore.getString(OPCUAConstants.CometOPCUAServersPath);
	//// opcuaConnectionsManager.importHostsFromRuntimeStructure(opcuaFolder,
	// opcuaServers);
	//// // hmi connections
	//// ConnectionsHostManager hmiConnectionsManager = new
	// ConnectionsHostManager();
	//// IPreferenceStore hmistore = HMIActivator.getDefault().getPreferenceStore();
	//// String hmiFolder = hmistore.getString(HMIConstants.HMIRuntime);
	//// String hmiServers = hmistore.getString(HMIConstants.CometHMIServersPath);
	//// hmiConnectionsManager.importHostsFromRuntimeStructure(hmiFolder,
	// hmiServers);
	//// // iec61131 connections
	//// ConnectionsHostManager iec61131ConnectionsManager = new
	// ConnectionsHostManager();
	//// IPreferenceStore iec61131store =
	// IEC61131Activator.getDefault().getPreferenceStore();
	//// String iec61131Folder =
	// iec61131store.getString(IEC61131Constants.IECRuntime);
	//// String iec61131Servers =
	// iec61131store.getString(IEC61131Constants.IECProjectsPath);
	//// iec61131ConnectionsManager.importHostsFromRuntimeStructure(iec61131Folder,
	// iec61131Servers);
	//// // tsn connections
	//// ConnectionsHostManager tsnConnectionsManager = new
	// ConnectionsHostManager();
	//// IPreferenceStore tsnstore = TSNActivator.getDefault().getPreferenceStore();
	//// String tsnFolder = tsnstore.getString(TSNConstants.TSNRuntime);
	//// String tsnServers = tsnstore.getString(TSNConstants.TSNProjectsPath);
	//// tsnConnectionsManager.importHostsFromRuntimeStructure(tsnFolder,
	// tsnServers);
	//// // opc ua server count
	//// int opcserverCount =
	// opcuaConnectionsManager.getStudioConnections().getConnections().size();
	//// // hmi server count
	//// int hmiserverCount =
	// hmiConnectionsManager.getStudioConnections().getConnections().size();
	//// // hmi server count
	//// int iec61131serverCount =
	// iec61131ConnectionsManager.getStudioConnections().getConnections().size();
	//
	//// try {
	//// // check license if projects match allowed count
	//// LicManActivator.getDefault().getLicenseManager().getLicense().startServerProjects(opcserverCount,
	//// hmiserverCount);
	//// } catch (HBStudioLicenseException e) {
	//// e.printStackTrace();
	//// // Project count is invalid. Try change workspace!
	//// IHandlerService handlers =
	// getWindowConfigurer().getWindow().getService(IHandlerService.class);
	//// try {
	//// Object pick = handlers.executeCommand(SwitchWorkspaceHandler.ID, null);
	//// if (pick == null) {
	//// System.exit(-1);
	//// }
	//// } catch (ExecutionException | NotDefinedException | NotEnabledException |
	// NotHandledException e1) {
	//// e1.printStackTrace();
	//// }
	//// }
	//
	// }
	@Override
	public void postWindowOpen() {
		//UpdateUtil.checkForUpdate();
		/*
		// plugin com.bichler.astudio.p2
		try {
			P2Util.workbenchStartup(getWindowConfigurer().getWindow().getWorkbench());
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	private void registerLogging() {
		// start viewer
		// LogViewUtils.startServer();
	}

	public static void initializeDefaultPerspective(final IWorkbenchWindow window) {
		// String servername = Studio_ResourceManager.getServerName();
		// if (servername != null) {
		//// OPCUAUtil.closePerspective(window, servername);
		// }
	}
}
