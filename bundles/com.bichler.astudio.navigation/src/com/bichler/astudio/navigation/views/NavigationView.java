package com.bichler.astudio.navigation.views;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.navigation.NavigationActivator;
import com.bichler.astudio.navigation.nodes.RootStudioModelNode;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.navigation.views.providers.StudioModelContentProvider;
import com.bichler.astudio.navigation.views.providers.StudioModelLabelProvider;
import com.bichler.astudio.utils.internationalization.CustomString;

public class NavigationView extends ViewPart implements IFileSystemNavigator {
	public static final String ID = "com.bichler.astudio.navigation.views.navigationview";

	private TreeViewer treeViewer = null;
	private IFileSystem filesystem = null;

	@Override
	public void refresh(StudioModelNode node) {
		node.refresh();
		node.setChildren(null);
		this.treeViewer.refresh(node);
	}

	public void refresh(final Class<?> c, final boolean setInput) {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(getSite().getShell());

		IRunnableWithProgress runnable = new IRunnableWithProgress() {

			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

				monitor.beginTask(
						CustomString.getString(NavigationActivator.getDefault().RESOURCE_BUNDLE, "monitor.refresh")
								+ "...",
						IProgressMonitor.UNKNOWN);

				final StudioModelNode root = (StudioModelNode) treeViewer.getInput();
				final StudioModelNode node2refresh = refresh(c, root);

				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {
						if (setInput) {
							treeViewer.setInput(root);
						} else {
							if (node2refresh != null) {
								treeViewer.refresh(node2refresh);
							} else {
								treeViewer.refresh(true);
							}
						}
					}
				});

				monitor.done();

			}
		};

		try {
			dialog.run(true, false, runnable);
		} catch (InvocationTargetException | InterruptedException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
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
			refresh(c, (StudioModelNode) child);
		}
		return null;
	}

	public void setTreeViewer(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}

	public NavigationView() {

		/**
		 * filesystem.setHmiServersRootPath(
		 * store.getString(CometConstants.CometFileSeparator) + "usr" +
		 * store.getString(CometConstants.CometFileSeparator) + "HB Studio" +
		 * store.getString(CometConstants.CometFileSeparator) + "HMI Server");
		 */
		// Studio_ResourceManager.setHmiServersRootPath(store.getString(CometConstants.CometFileSeparator)
		// + "Users/applemc207da/Documents/HB Studio/HMI Server");

		// Studio_ResourceManager.setHmiServersPath("Servers");
		// Studio_ResourceManager.setHmiRuntimePath("Runtime");

		// Studio_ResourceManager.setStudioRuntimePath("/Users/applemc207da/Documents/thomas_workspace/com.bichler.astudio_v0.0.3");
		// Studio_ResourceManager.setEditorFolder2D("svgEditor");
//		Studio_ResourceManager.setStudioConfigFolder("config");

		// Studio_ResourceManager.setEditorFolder3D("webGLEditor");
		// Studio_ResourceManager.setEditorFile2D("svg-editor.html");
		// Studio_ResourceManager.setEditorFile3D("3idw.html");
//		Studio_ResourceManager.setTemplatesFolder2D("2dTemplates");
//		Studio_ResourceManager.setTemplatesConfig2D("config.dat");

//		Studio_ResourceManager.setTemplatesFolder3D("3dTemplates");
//		Studio_ResourceManager.setTemplatesConfig2D("config.dat");
		// Studio_ResourceManager.setOpcuaRuntimePath("Runtime");

		this.loadStudioConnections();

	}

	// loads all connections to comet studio projects
	private void loadStudioConnections() {
		// IPreferenceStore store = NavigationActivator.getDefault()
		// .getPreferenceStore();
		//
		// /**
		// * File file = new
		// * File(store.getString(CometConstants.CometStudioFolder) +
		// * store.getString(CometConstants.CometFileSeparator) +
		// * store.getString(CometConstants.CometConfigFolder) // "config" +
		// * store.getString(CometConstants.CometFileSeparator) +
		// * store.getString(CometConstants.CometConnectionsFile) );
		// */
		// // if(file.exists() && file.isFile()) {
		// SAXParserFactory factory = SAXParserFactory.newInstance();
		//
		// try {
		// // SAXParser parser = factory.newSAXParser();
		//
		// // parser.parse(file.getPath(), new ConnectionsLoader());
		//
		// // } catch (IOException e) {
		// // e.printStackTrace();
		// } catch (SAXException e) {
		// e.printStackTrace();
		// } catch (ParserConfigurationException e) {
		// e.printStackTrace();
		// }
		// // }
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		createView(parent);
	}

	@Override
	public void setFocus() {
		this.treeViewer.getTree().setFocus();
	}

	private void createView(Composite parent) {
		this.treeViewer = new TreeViewer(parent, SWT.BORDER | SWT.MULTI);
		Tree tree = treeViewer.getTree();
		tree.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));

		tree.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		this.treeViewer.setContentProvider(new StudioModelContentProvider());
		this.treeViewer.setLabelProvider(new StudioModelLabelProvider());
		getSite().setSelectionProvider(this.treeViewer);
		setInput(new RootStudioModelNode());

		this.treeViewer.getTree().addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				int fontHeight = event.gc.getFontMetrics().getHeight();
				event.height = fontHeight + 5;
			}
		});

		/** we need to load all elements */

		this.treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				// node doubleclick
				TreeSelection selection = (TreeSelection) event.getSelection();
				if (!selection.isEmpty() && selection.getFirstElement() != null) {

					if (selection.getFirstElement() instanceof StudioModelNode) {
						((StudioModelNode) selection.getFirstElement()).nodeDBLClicked();
					}
				}
			}
		});

		ViewerComparator vComperator = new ViewerComparator() {

			@Override
			public int category(Object element) {
				return super.category(element);
			}

			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				String name1 = ((StudioModelNode) e1).getLabelText();
				String name2 = ((StudioModelNode) e2).getLabelText();

				if (name1 != null) {
					return name1.compareTo(name2);
				}

				return super.compare(viewer, e1, e2);
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

		this.treeViewer.setComparator(vComperator);

		this.registerContextMenu();

	}

	/**
	 * Registers contextmenu defined in the plugin.xml
	 */
	private void registerContextMenu() {
		MenuManager menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(this.treeViewer.getControl());
		this.treeViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, this.treeViewer);
	}

	public void setInput(StudioModelNode node) {
	  this.filesystem = node.getFilesystem();
		this.treeViewer.setInput(node);

	}

	public TreeViewer getViewer() {
		return this.treeViewer;
	}

  public IFileSystem getFilesystem()
  {
    return filesystem;
  }

  public void setFilesystem(IFileSystem filesystem)
  {
    this.filesystem = filesystem;
  }
}