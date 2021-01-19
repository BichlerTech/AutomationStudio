package com.bichler.astudio.opcua.drivers.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.FillLayout;
import org.osgi.framework.Bundle;

import com.bichler.astudio.opcua.drivers.views.nodes.DriversModelNode;
import com.bichler.astudio.opcua.drivers.views.nodes.DriversNodeContentProvider;
import com.bichler.astudio.opcua.drivers.views.nodes.DriversNodeLabelProvider;

import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;

public class DriversView extends ViewPart {
	public static final String ID = "com.hbsoft.drivers.views.driversview"; //$NON-NLS-1$
	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private Table table;
	private TableViewer tv_drivers;
	private TableViewerColumn tableViewerColumn;
	private int actId = 0;

	public DriversView() {
	}

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = toolkit.createComposite(parent, SWT.NONE);
		toolkit.paintBordersFor(container);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		tv_drivers = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		table = tv_drivers.getTable();
		table.setHeaderVisible(true);
		toolkit.paintBordersFor(table);
		{
			TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tv_drivers, SWT.NONE);
			TableColumn tblclmn_id = tableViewerColumn_1.getColumn();
			tblclmn_id.setWidth(50);
			tblclmn_id.setText("New Column");
		}
		{
			tableViewerColumn = new TableViewerColumn(tv_drivers, SWT.NONE);
			TableColumn tblclmn_driver = tableViewerColumn.getColumn();
			tblclmn_driver.setWidth(150);
			tblclmn_driver.setText("drivers");
		}
		{
			TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tv_drivers, SWT.NONE);
			TableColumn tblclmn_version = tableViewerColumn_1.getColumn();
			tblclmn_version.setWidth(150);
			tblclmn_version.setText("New Column");
		}
		tv_drivers.setLabelProvider(new DriversNodeLabelProvider());
		tv_drivers.setContentProvider(new DriversNodeContentProvider());
		createActions();
		initializeToolBar();
		initializeMenu();
	}

	public void dispose() {
		toolkit.dispose();
		super.dispose();
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
		IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager manager = getViewSite().getActionBars().getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	public void addDriver(Bundle bundle) {
		DriversModelNode node = new DriversModelNode();
		node.setBundle(bundle);
		node.setId(actId++);
		this.tv_drivers.add(node);
	}
}
