package com.bichler.astudio.connections;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

public class ConnectionsView extends ViewPart {

	public static final String ID = "com.bichler.astudio.views.connections.studioconnectionview"; //$NON-NLS-1$
	private TreeViewer locationViewer;

	public ConnectionsView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		locationViewer = new TreeViewer(container, SWT.BORDER);
//		Tree tree = locationViewer.getTree();

		locationViewer.setContentProvider(new ConnectionsContentProvider());
		locationViewer.setLabelProvider(new ConnectionsLabelProvider());
		//locationViewer.setInput(ConnectionsActivator.hostManager.getStudioOPCUAConnections().getConnections().values().toArray());
		
		createActions();
		initializeToolBar();
		initializeMenu();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
//		IToolBarManager toolbarManager = getViewSite().getActionBars()
		getViewSite().getActionBars().getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		MenuManager menuMgr = new MenuManager(
				"popup:com.bichler.astudio.views.connections.studioconnectionview");
		
		Menu menu = menuMgr.createContextMenu(this.locationViewer.getControl());
		// menuMgr.add(new Iconitem);
		this.locationViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, this.locationViewer);
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
	
}
