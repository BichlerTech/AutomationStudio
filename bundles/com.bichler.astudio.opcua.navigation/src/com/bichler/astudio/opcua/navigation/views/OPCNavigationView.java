package com.bichler.astudio.opcua.navigation.views;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.navigation.views.IFileSystemNavigator;
import com.bichler.astudio.opcua.navigation.OPCNavigationActivator;
import com.bichler.astudio.opcua.navigation.views.provider.OPCModelContentProvider;
import com.bichler.astudio.opcua.navigation.views.provider.OPCModelLabelProvider;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCNavigationView extends ViewPart implements IFileSystemNavigator {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.bichler.astudio.opcua.navigation.views.OPCNavigationView";
	public static final String SPACE = "               ";

	private TreeViewer viewer;

	/**
	 * The constructor.
	 */
	public OPCNavigationView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		Tree tree = viewer.getTree();

		tree.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));

		// tree.setFont(SWTResourceManager.getFont("Alien Encounters Solid", 12,
		// SWT.NORMAL));

		tree.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		viewer.setContentProvider(new OPCModelContentProvider());
		viewer.setLabelProvider(new OPCModelLabelProvider());
		getSite().setSelectionProvider(this.viewer);

		ViewerComparator vComperator = new ViewerComparator() {

			@Override
			public int category(Object element) {
				return super.category(element);
			}

			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				if (((StudioModelNode) e1).useNavigationComperator()
						&& ((StudioModelNode) e2).useNavigationComperator()) {

					String name1 = ((StudioModelNode) e1).getLabelText();
					String name2 = ((StudioModelNode) e2).getLabelText();

					if (name1 != null) {
						return name1.compareTo(name2);
					}

					return super.compare(viewer, e1, e2);
				}

				return 1;
			}

			@Override
			public boolean isSorterProperty(Object element, String property) {
				return super.isSorterProperty(element, property);
			}

			@Override
			public void sort(Viewer viewer, Object[] elements) {
				super.sort(viewer, elements);
			}

		};

		this.viewer.setComparator(vComperator);

		// viewer.setInput(getViewSite());
		// makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		// contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	// private void contributeToActionBars() {
	// IActionBars bars = getViewSite().getActionBars();
	// fillLocalPullDown(bars.getMenuManager());
	// fillLocalToolBar(bars.getToolBarManager());
	// }

	// private void fillLocalPullDown(IMenuManager manager) {
	// manager.add(action1);
	// manager.add(new Separator());
	// manager.add(action2);
	// }

	// private void fillContextMenu(IMenuManager manager) {
	// manager.add(action1);
	// manager.add(action2);
	// manager.add(new Separator());
	// drillDownAdapter.addNavigationActions(manager);
	// // Other plug-ins can contribute there actions here
	// manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	// }

	// private void fillLocalToolBar(IToolBarManager manager) {
	// manager.add(action1);
	// manager.add(action2);
	// manager.add(new Separator());
	// drillDownAdapter.addNavigationActions(manager);
	// }

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				/** select command */
				TreeSelection selection = (TreeSelection) event.getSelection();

				if (!selection.isEmpty() && selection.getFirstElement() != null) {
					if (selection.getFirstElement() instanceof StudioModelNode) {
						((StudioModelNode) selection.getFirstElement()).nodeDBLClicked();
					}
				}
			}
		});
	}

	// private void showMessage(String message) {
	// MessageDialog.openInformation(viewer.getControl().getShell(),
	// "Navigation", message);
	// }

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getTree().setFocus();
	}

	public void setInput(StudioModelNode node) {
		if (node != null) {
			setPartName(node.getLabelText());
			this.viewer.setInput(node);
		} else {
			setPartName(SPACE);
			this.viewer.setInput(null);
		}
	}

	@Override
	public void refresh(StudioModelNode node) {
		node.refresh();
		node.setChildren(null);
		this.viewer.refresh(node);
	}

	public TreeViewer getViewer() {
		return this.viewer;
	}

	@Override
	public void refresh(final Class<?> c, final boolean setInput) {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(getSite().getShell());

		IRunnableWithProgress runnable = new IRunnableWithProgress() {

			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask(CustomString.getString(OPCNavigationActivator.getDefault().RESOURCE_BUNDLE,
						"editor.monitor.refresh") + "...", IProgressMonitor.UNKNOWN);
				try {
					final StudioModelNode root = (StudioModelNode) viewer.getInput();
					final StudioModelNode node2refresh = refresh(c, root);

					Display.getDefault().syncExec(new Runnable() {

						@Override
						public void run() {
							if (setInput) {
								viewer.setInput(root);
							} else {
								if (node2refresh != null) {
									viewer.refresh(node2refresh);
								} else {
									viewer.refresh(true);
								}
							}
						}
					});
				} finally {
					monitor.done();
				}
			}
		};

		try {
			dialog.run(true, false, runnable);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private StudioModelNode refresh(Class<?> c, StudioModelNode node) {
		if (node.getChildrenList() == null) {
			return null;
		}

		if (node.getClass() == c) {
			node.refresh();
			node.setChildren(null);
			node.getChildren();
			return node;
		}

		for (Object child : node.getChildren()) {
			StudioModelNode found = refresh(c, (StudioModelNode) child);

			if (found != null) {
				return found;
			}
		}
		return null;
	}

	public void update(StudioModelNode node) {
		this.viewer.update(node, null);
	}
}