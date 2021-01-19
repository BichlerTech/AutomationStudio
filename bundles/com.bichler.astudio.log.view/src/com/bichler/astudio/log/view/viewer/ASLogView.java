package com.bichler.astudio.log.view.viewer;

import java.util.Map;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.bichler.astudio.log.view.Activator;
import com.bichler.astudio.log.view.preferences.ASLogPreferenceConstants;
import com.bichler.astudio.log.view.preferences.ASLogPreferenceManager;
import com.bichler.astudio.log.view.provider.ASLogController;

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

public class ASLogView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.hbsoft.comet.log4j.view";

	/** table widget */
	private TableViewer viewer;
	/** controller maintaining incoming log4j events */
	private ASLogController controller;
	/** search text field */
	private Text quickSearchField;
	/** search filter */
	private QuickLogFilter quickFilter;
	/** preference change listener */
	private IPropertyChangeListener preferenceListener;
	/** preference manager */
	private ASLogPreferenceManager preferences;
	/** columns order */
	private Map<String, Integer> columnOrderMap;
	/** plugin view part lifecycle listener */
	private ViewLifecycleListener partListener;
	/** direction to display log entries */
	private boolean toggledToBottom = true;

	/**
	 * The constructor.
	 */
	public ASLogView() {
		this.controller = new ASLogController(this);
		this.quickFilter = new QuickLogFilter();
		this.preferences = Activator.getDefault().getPreferenceManager();
	}

	/**
	 * Removes all Log4j entries from the viewer table.
	 */
	public void clear() {
		this.viewer.getTable().removeAll();
		this.controller.clear();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		// layout parent
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		parent.setLayout(layout);

		// create viewer
		this.viewer = createViewer(parent);
		// set selection provider
		getSite().setSelectionProvider(this.viewer);
		// add lifecycle listener
		this.partListener = new ViewLifecycleListener();
		getViewSite().getPage().addPartListener(this.partListener);
		// add preference change listener
		this.preferenceListener = new IPropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				String property = event.getProperty();

				switch (property) {
				case ASLogPreferenceConstants.PREF_QUICKSEARCH_VISIBLE:
					setQuickSearchVisible((boolean) event.getNewValue());
					break;
				case ASLogPreferenceConstants.PREF_SERVER_REFRESH_TIME:
					int refreshTime = (int) event.getNewValue();
					break;
				case ASLogPreferenceConstants.PREF_BUFFER_SIZE:
					int buffer = (int) event.getNewValue();
					break;
				case ASLogPreferenceConstants.PREF_SERVER_PORT:
					int port = (int) event.getNewValue();
					controller.stopServer();
					controller.startServer();
					break;
				}
//				System.out.println("event changing " + event.getProperty());
			}
		};
		this.preferences.addListener(this.preferenceListener);

		// add quick search field
		this.quickSearchField = createQuickSearchField(parent);
		setQuickSearchVisible(this.preferences.isQuickSearchVisible());

		// Create the help context id for the viewer's control
		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(viewer.getControl(),
						"com.hbsoft.comet.log4j.view.viewer");
		this.controller.startServer();
	}

	@Override
	public void dispose() {
		this.controller.dispose();
		getViewSite().getPage().removePartListener(this.partListener);
		this.preferences.removeListener(this.preferenceListener);
		super.dispose();
	}

	/**
	 * Refresh view items
	 */
	public void refreshViewer() {
		if (this.viewer.getTable().isDisposed()) {
			return;
		}

		viewer.refresh(true, this.toggledToBottom);
		if (this.toggledToBottom) {
			Table table = viewer.getTable();
			int itemCount = table.getItemCount();
			table.setSelection(itemCount - 1);
		}
	}

	/**
	 * Toggle from top to bottom.
	 */
	public void toggleScrollToBottom() {
		toggledToBottom = !toggledToBottom;
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	/**
	 * Controller to manage data to display
	 * 
	 * @return Controller
	 */
	public ASLogController getController() {
		return this.controller;
	}

	private TableViewer createViewer(Composite parent) {
		TableViewer viewer = new ASLogTableWidget(parent, SWT.FULL_SELECTION
				| SWT.H_SCROLL | SWT.V_SCROLL);
		// set input collection
		viewer.setInput(getController().getLogCollection());

		final Table table = viewer.getTable();
		table.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				setFocus();
			}
		});

		// initColumns();

		viewer.addFilter(this.quickFilter);

		return viewer;
	}

	private Text createQuickSearchField(Composite parent) {
		final Text quickSearchField = new Text(parent, SWT.BORDER);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		quickSearchField.setLayoutData(gridData);

		quickSearchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent key) {
				quickFilter.setSearchText(quickSearchField.getText());
				refreshViewer();
			}
		});
		return quickSearchField;
	}

	// private void initColumns() {
	// columnOrderMap = new HashMap<>();
	// TableColumn[] columns = viewer.getTable().getColumns();
	//
	// for (int i = 0; i < columns.length; i++) {
	// columns[i].addControlListener(new ColumnResizeListener());
	// columnOrderMap.put(columns[i].getText(), i);
	// }
	// updateColumns(viewer.getTable(), this.preferences.getStructuralItems());
	// }

	private void setQuickSearchVisible(boolean visible) {
		GridData gridData = (GridData) quickSearchField.getLayoutData();
		gridData.exclude = !visible;
		quickSearchField.setVisible(visible);
		quickSearchField.getParent().layout();

		if (visible) {
			quickSearchField.selectAll();
			quickSearchField.setFocus();
		} else {
			setFocus();
		}
	}

	private class ViewLifecycleListener implements IPartListener {

		@Override
		public void partActivated(IWorkbenchPart part) {

		}

		@Override
		public void partBroughtToTop(IWorkbenchPart part) {

		}

		@Override
		public void partClosed(IWorkbenchPart part) {
			if (Activator.PLUGIN_ID.equals(part.getSite().getPluginId())) {
				if (part instanceof ASLogView) {
					controller.stopServer();
				}
			}
		}

		@Override
		public void partDeactivated(IWorkbenchPart part) {

		}

		@Override
		public void partOpened(IWorkbenchPart part) {
			if (Activator.PLUGIN_ID.equals(part.getSite().getPluginId())) {
				if (part instanceof ASLogView) {
					// if (preferences.isAutoStart()) {
					controller.startServer();
					// }
				}
			}
		}

	}

}