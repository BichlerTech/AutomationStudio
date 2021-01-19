package com.bichler.astudio.device.core.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.bichler.astudio.core.user.util.UserUtils;
import com.bichler.astudio.device.core.DevCoreActivator;
import com.bichler.astudio.device.core.handler.EditDeviceHandler;
import com.bichler.astudio.device.core.preference.DevicePreferenceConstants;
import com.bichler.astudio.device.core.preference.DevicePreferenceManager;
import com.bichler.astudio.device.core.wizard.DeviceConnectionWizard;
import com.bichler.astudio.filesystem.DataHubFileSystem;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */
public class OPCUADeviceView extends ViewPart {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.bichler.astudio.device.core.view.devices";

	private TableViewer viewer;
	private DevicePreferenceManager devicePreferenceManager;

	/**
	 * The constructor.
	 */
	public OPCUADeviceView() {
		super();
		this.devicePreferenceManager = new DevicePreferenceManager();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setComparator(new ViewerComparator());
		// viewer.setSorter(new ViewerSorter());
		getSite().setSelectionProvider(this.viewer);
		setInput();
		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.bichler.astudio.device.viewer");
		hookContextMenu();
		hookDoubleClickAction();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getTable().setFocus();
	}

	public void addNewDevice(boolean showScanDevicePage) {
		DeviceConnectionWizard wizard = new DeviceConnectionWizard(true, showScanDevicePage);
		WizardDialog dialog = new WizardDialog(getSite().getShell(), wizard);
		if (WizardDialog.OK == dialog.open()) {
			devicePreferenceManager.addDevice(wizard.getFilesystem());
			viewer.refresh(true);
		}
	}

	public void refreshViewer() {
		try {
			this.devicePreferenceManager.getRoot().flush();
			this.devicePreferenceManager.getRoot().sync();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		viewer.refresh(true);
	}

	public void editDevice(Preferences obj) {
		// 0... ssh 1... simple
		int filesystemType = obj.getInt(DevicePreferenceConstants.PREFERENCE_DEVICE_FILETYPE, 0);
		String name = obj.name();
		String host = obj.get(DevicePreferenceConstants.PREFERENCE_DEVICE_HOST, "");
		int timeout = obj.getInt(DevicePreferenceConstants.PREFERENCE_DEVICE_TIMEOUT, 0);
		String user = obj.get(DevicePreferenceConstants.PREFERENCE_DEVICE_USER, "");
		String password = obj.get(DevicePreferenceConstants.PREFERENCE_DEVICE_PASSWORD, "");
		String separator = obj.get(DevicePreferenceConstants.PREFERENCE_DEVICE_FILESEPARATOR, "");
		String rootPath = obj.get(DevicePreferenceConstants.PREFERENCE_DEVICE_ROOTPATH, "");
		IFileSystem filesystem = null;
		switch (filesystemType) {
		case 0:
			filesystem = new DataHubFileSystem();
			break;
		case 1:
			filesystem = new SimpleFileSystem();
			break;
		}
		filesystem.setConnectionName(name);
		filesystem.setTimeOut(timeout);
		filesystem.setHostName(host);
		filesystem.setUser(user);
		filesystem.setPassword(password);
		filesystem.setTargetFileSeparator(separator);
		filesystem.setRootPath(rootPath);
		DeviceConnectionWizard wizard = new DeviceConnectionWizard(false, false);
		WizardDialog dialog = new WizardDialog(getSite().getShell(), wizard);
		dialog.create();
		wizard.setTargetFilesystem(filesystem);
		wizard.setEdit();
		if (WizardDialog.OK == dialog.open()) {
			devicePreferenceManager.editDeviceFilesystem(name, wizard.getFilesystem());
			viewer.refresh(true);
		}
	}

	public void removeDevice(Preferences obj) {
		this.devicePreferenceManager.removeDevice(obj);
		viewer.refresh(true);
	}

	private void setInput() {
		viewer.setInput(this.devicePreferenceManager.getRoot());
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				// OPCUADeviceView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IHandlerService handlerService = (IHandlerService) getSite().getService(IHandlerService.class);
				try {
					handlerService.executeCommand(EditDeviceHandler.ID, null);
				} catch (Exception e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
			}
		});
	}

	/*
	 * The content provider class is responsible for providing objects to the view.
	 * It can wrap existing objects in adapters or simply return objects as-is.
	 * These objects may be sensitive to the current input of the view, or ignore it
	 * and always show the same content (like Task List, for example).
	 */
	class ViewContentProvider implements IStructuredContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (parent instanceof Preferences) {
				try {
					String[] childrenNames = ((Preferences) parent).childrenNames();
					List<Preferences> preferences = new ArrayList<>();
					for (int i = 0; i < childrenNames.length; i++) {
						Preferences child = ((Preferences) parent).node(childrenNames[i]);
						int filesystem = child.getInt(DevicePreferenceConstants.PREFERENCE_DEVICE_FILETYPE, 1);
						if(!UserUtils.testUserRights(1) && DevicePreferenceConstants.PREFERENCE_TYPE_SIMPLE == filesystem) {
							continue;
						}
						preferences.add(child);
					}
					return preferences.toArray(new Preferences[0]);
				} catch (BackingStoreException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
			}
			return new String[0];
		}
	}

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		private Image server_normal;
		private Image combox;

		public ViewLabelProvider() {
			URL serverNormal = FileLocator.find(DevCoreActivator.getDefault().getBundle(),
					new Path("/icons/wizard/server_normal.png"), null);
			URL fileURL1 = null;
			try {
				fileURL1 = FileLocator.toFileURL(serverNormal);
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
			ImageDescriptor descriptor1 = ImageDescriptor.createFromURL(fileURL1);
			this.server_normal = descriptor1.createImage();
			URL comboxURL = FileLocator.find(DevCoreActivator.getDefault().getBundle(),
					new Path("/icons/img_16/datahub_16.png"), null);
			URL fileURL2 = null;
			try {
				fileURL2 = FileLocator.toFileURL(comboxURL);
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
			ImageDescriptor descriptor2 = ImageDescriptor.createFromURL(fileURL2);
			this.combox = descriptor2.createImage();
		}

		@Override
		public String getText(Object element) {
			if (element instanceof Preferences) {
				return ((Preferences) element).name();
			}
			return super.getText(element);
		}

		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			String type = ((Preferences) obj).get(DevicePreferenceConstants.PREFERENCE_DEVICE_FILETYPE, "");
			switch (type) {
			case "0":
				return this.combox;
			default:
				return this.server_normal;
			}
		}

		public Image getImage(Object obj) {
			return this.server_normal;
		}

		@Override
		public void dispose() {
			if (this.server_normal != null) {
				this.server_normal.dispose();
			}
			if (this.combox != null) {
				this.combox.dispose();
			}
			super.dispose();
		}
	}
}
