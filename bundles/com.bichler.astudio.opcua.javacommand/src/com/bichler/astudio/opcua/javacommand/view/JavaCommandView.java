package com.bichler.astudio.opcua.javacommand.view;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.osgi.service.prefs.Preferences;

import com.bichler.astudio.opcua.javacommand.JavaCommandActivator;
import com.bichler.astudio.opcua.javacommand.handler.EditCommandHandler;
import com.bichler.astudio.opcua.javacommand.preferences.JavaCommandPreferenceManager;

public class JavaCommandView extends ViewPart {

	private TableViewer tableViewer;

	// private JavaCommandPreferenceManager manager;e

	public JavaCommandView() {
		// this.manager = new JavaCommandPreferenceManager();
		JavaCommandPreferenceManager.inititialize();
	}

	@Override
	public void createPartControl(Composite parent) {

		this.tableViewer = new TableViewer(parent, SWT.BORDER
				| SWT.FULL_SELECTION);
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setContentProvider(new ContentProvider());
		tableViewer.setSorter(new TableViewerSorter());

		setHandler();

		hookContextMenu();
		hookDoubleClick();

		fillTable();

		getSite().setSelectionProvider(this.tableViewer);
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				JavaCommandView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(this.tableViewer.getControl());
		this.tableViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, this.tableViewer);
	}

	protected void fillContextMenu(IMenuManager manager) {
		
	}

	private void hookDoubleClick() {
		this.tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				// ICommandService cmdService = (ICommandService) getSite()
				// .getService(ICommandService.class);
				IHandlerService handlerService = (IHandlerService) getSite()
						.getService(IHandlerService.class);

				// Command cmd = cmdService.getCommand(EditDeviceHandler.ID);
				try {
					handlerService.executeCommand(EditCommandHandler.ID, null);
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (NotDefinedException e) {
					e.printStackTrace();
				} catch (NotEnabledException e) {
					e.printStackTrace();
				} catch (NotHandledException e) {
					e.printStackTrace();
				}

			}
		});
	}

	public void refresh() {
		fillTable();
	}

	private void fillTable() {
		String[] commands = JavaCommandPreferenceManager.getAllCommandNames();
		this.tableViewer.setInput(commands);
	}

	private void setHandler() {

	}

	@Override
	public void setFocus() {
		this.tableViewer.getTable().setFocus();
	}

	private class TableLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {

			if (element instanceof String) {
				return JavaCommandActivator.getDefault().getRegisteredImage(
						JavaCommandActivator.IMG_STARTCMD);
			}

			return null;
		}

		public String getColumnText(Object element, int columnIndex) {

			return element.toString();
		}
	}

	private class ContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {

			if (inputElement instanceof String[]) {
				return (String[]) inputElement;
			} else if (inputElement instanceof String) {
				return new Preferences[] { JavaCommandPreferenceManager
						.getCommand((String) inputElement) };
			}

			return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	class TableViewerSorter extends ViewerSorter {

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			return super.compare(viewer, e1, e2);
		}

	}

}
