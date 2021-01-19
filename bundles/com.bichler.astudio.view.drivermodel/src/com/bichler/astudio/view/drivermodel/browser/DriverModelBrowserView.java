package com.bichler.astudio.view.drivermodel.browser;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.driver.OPCDriverUtil;
import com.bichler.astudio.opcua.driver.enums.DriverConfigProperties;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.view.drivermodel.Activator;
import com.bichler.astudio.view.drivermodel.browser.listener.IDragConverter;
import com.bichler.astudio.view.drivermodel.browser.listener.IDriverModelDragConverter;
import com.bichler.astudio.view.drivermodel.browser.listener.IDriverModelListener;
import com.bichler.astudio.view.drivermodel.dnd.DriverModelDragListener;
import com.bichler.astudio.view.drivermodel.handler.util.AbstractDriverModelViewNode;
import com.bichler.astudio.view.drivermodel.handler.util.IDriverStructResourceManager;

public class DriverModelBrowserView extends ViewPart {

	public static final String ID = "com.bichler.astudio.view.drivermodel.browser";

	private Composite emptyModelComposite;
	private TreeViewer treeViewer;
	private Map<DriverConfigProperties, IDriverModelListener> listeners = new HashMap<>();
	private String driverpath;

	private IFileSystem filesystem;

	private IDriverModelDragConverter dragSupport;

	private StackLayout layout;

	private Composite parent;

	// private IDriverStructResourceManager structureManager;

	public DriverModelBrowserView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		this.layout = new StackLayout();
		parent.setLayout(this.layout);
		this.treeViewer = new TreeViewer(parent, SWT.BORDER);
		// Tree tree = treeViewer.getTree();
		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
		this.treeViewer.addDragSupport(operations, transferTypes,
				new DriverModelDragListener(this.treeViewer, new IDragConverter() {

					@Override
					public IDriverModelDragConverter getConverter() {
						return dragSupport;
					}
				}));

		ViewerComparator vComperator = new ViewerComparator() {

			@Override
			public int category(Object element) {
				return super.category(element);
			}

			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {

				String name1 = ((AbstractDriverModelViewNode) e1).getText();
				String name2 =  ((AbstractDriverModelViewNode) e2).getText();

				if (name1 != null) {
					return name1.compareTo(name2);
				}

				return super.compare(viewer, e1, e2);

				// return 1;
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

		this.emptyModelComposite = new Composite(parent, SWT.NONE);
		emptyModelComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		Label lblDescription = new Label(emptyModelComposite, SWT.NONE);
		lblDescription
				.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "view.description.nomodel"));

		setBrowserMode(BrowserMode.Empty);

	}

	enum BrowserMode {
		Tree, Empty;
	}

	private void setBrowserMode(BrowserMode mode) {

		switch (mode) {
		case Tree:
			this.layout.topControl = this.treeViewer.getTree();
			break;
		case Empty:
			this.layout.topControl = this.emptyModelComposite;
			break;
		}

		this.parent.layout();

	}

	@Override
	public void setFocus() {
		this.treeViewer.getTree().setFocus();
	}

	/**
	 * Initializes empty view
	 */
	public void updateViewer() {
		setBrowserMode(BrowserMode.Empty);

	}

	/**
	 * Set content and labelprovider for drivermodel viewer
	 * 
	 * @param provider
	 * @param labelProvider
	 * @param driverpath
	 * @param listeners
	 */
	public void updateViewer(IContentProvider provider,
			IBaseLabelProvider labelProvider/* , Object input */, String driverpath, IFileSystem filesystem,
			Map<DriverConfigProperties, IDriverModelListener> listeners, IDriverModelDragConverter dragSupport,
			IDriverStructResourceManager structureManager) {

		setBrowserMode(BrowserMode.Tree);

		// do not update on same path
		String oldPath = getDriverPath();
		if (oldPath != null && oldPath.equals(driverpath)) {
			return;
		}

		setContentProvider(provider);
		setLabelProvider(labelProvider);
		setDriverComPath(driverpath);
		setFilesystem(filesystem);
		setModelListeners(listeners);
		setDragSupport(dragSupport);
		setModelStructureManager(structureManager);
		refresh();

	}

	private void setModelStructureManager(IDriverStructResourceManager structureManager) {

		// this.structureManager = structureManager;
	}

	private void setDragSupport(IDriverModelDragConverter dragSupport) {
		this.dragSupport = dragSupport;

	}

	private void setDriverComPath(String driverpath) {
		this.driverpath = driverpath;
	}

	private void setFilesystem(IFileSystem filesystem) {
		this.filesystem = filesystem;
	}

	protected void setContentProvider(IContentProvider provider) {
		this.treeViewer.setContentProvider(provider);
	}

	protected void setLabelProvider(IBaseLabelProvider labelProvider) {
		this.treeViewer.setLabelProvider(labelProvider);
	}

	public void setViewerInput(Object input) {
		this.treeViewer.setInput(input);

	}

	public String getDriverPath() {
		return this.driverpath;
	}

	public IFileSystem getFilesystem() {
		return this.filesystem;
	}

	private void setModelListeners(Map<DriverConfigProperties, IDriverModelListener> listeners) {
		this.listeners.clear();
		this.listeners.putAll(listeners);
	}

	/**
	 * Refreshes view when a property of a particular driver in file driver.com
	 * has changed
	 */
	public void refresh(DriverConfigProperties property, String path) {
		IDriverModelListener listener = this.listeners.get(property);
		if (listener == null) {
			return;
		}

		listener.loadModel(path);
	}

	/**
	 * Refreshes view from a particular driver model in file driver.com
	 */
	public void refresh() {
		Map<String, String> attributes = OPCDriverUtil.readDriverCom(getFilesystem(), getDriverPath());

		for (Entry<DriverConfigProperties, IDriverModelListener> entry : this.listeners.entrySet()) {
			String value = attributes.get(entry.getKey().name());
			if (value != null && !value.isEmpty()) {
				refresh(entry.getKey(), value);
			}
		}
	}

}
