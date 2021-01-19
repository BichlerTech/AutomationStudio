package com.bichler.astudio.opcua.components.ui.dialogs;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.components.ui.ComponentsUIActivator;
import com.bichler.astudio.opcua.components.ui.dialogs.providers.OPCUAConnectionsContentProvider;
import com.bichler.astudio.opcua.components.ui.dialogs.providers.OPCUAConnectionsLabelProvider;
import com.bichler.astudio.opcua.components.ui.serverbrowser.providers.UAServerModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

// TODO: Auto-generated Javadoc
/**
 * The Class TreeViewDialog.
 */
public class OPCUATreeViewDialog extends Dialog {

	/** The files. */
	// private List<File> files;

	/** The tree view. */
	private Tree treeView;

	/** The message. */
	private String message;

	/** The root. */
	// private Root root;

	/** The tv. */
	private TreeViewer tv = null;

	/** The select tag. */
	// private DP selectTag;

	private OPCUANodeField field;

	private String selServerName;

	/**
	 * Instantiates a new tree view dialog.
	 * 
	 * @param shell
	 *            the shell
	 */
	public OPCUATreeViewDialog(Shell shell) {
		this(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}

	/**
	 * Instantiates a new tree view dialog.
	 * 
	 * @param shell
	 *            the shell
	 * @param style
	 *            the style
	 */
	public OPCUATreeViewDialog(Shell shell, int style) {
		super(shell, style);

		setText(CustomString.getString(ComponentsUIActivator.getDefault().RESOURCE_BUNDLE,
				"OPCUATreeViewerDialog.Title"));
		setMessage(CustomString.getString(ComponentsUIActivator.getDefault().RESOURCE_BUNDLE,
				"OPCUATreeViewerDialog.Message"));
	}

	/**
	 * Sets the message.
	 * 
	 * @param message
	 *            the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the message.
	 * 
	 * @param message
	 *            the new message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Open.
	 * 
	 * @return the tag
	 */
	public OPCUANodeField open() {
		// Create the dialog window
		Shell shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
		shell.setText(getText());
		shell.setSize(600, 500);
		// creates the contents for this Dialog
		createContents(shell);
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		if (this.field != null)
			return this.field;
		return null;
	}

	/**
	 * Creates the dialog's contents.
	 * 
	 * @param shell
	 *            the dialog window
	 */
	private void createContents(final Shell shell) {
		shell.setLayout(new GridLayout(3, true));

		this.treeView = new Tree(shell, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);

		// Layout of the tree
		GridData treedata = new GridData();
		treedata.horizontalSpan = 3;
		treedata.heightHint = 400;
		treedata.widthHint = 400;
		this.treeView.setLayoutData(treedata);

		// get the active editor
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		// Map<String, Comet_UA_Connection> connections = null;

		List<UAServerModelNode> servers = null;
		// Comet_UA_Connection connection = null;
		// List<String> servers = new ArrayList<String>();

		if (page != null) {
			IEditorPart part = page.getActiveEditor();
			if (part != null) {
				// if(part instanceof HMIEditor2D) {
				// servers =
				// ((HMIEditor2D)part).getEditorInput().getNode().getServerModelNodes();

				// Comet_ConnectionManager manager =
				// ((Editor2D)part).getEditorInput().getNode().getConManager();
				// String projName =
				// ((Editor2D)part).getEditorInput().getNode().getProjectName();

				// connections = manager.getConnections();

				// for(String serverId : manager.getConnections().keySet()) {
				// connection = manager.getConnections().get(serverId);
				// if(connection.isConnected()) {
				// servers.add(connection.);
				// }
				// }
				// }
			}
		}

		tv = new TreeViewer(this.treeView);

		this.tv.setContentProvider(new OPCUAConnectionsContentProvider());
		this.tv.setLabelProvider(new OPCUAConnectionsLabelProvider());
		// getSite().setSelectionProvider(this.treeViewer);

		this.tv.setInput(servers);
		this.tv.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				// TreeViewer viewer = (TreeViewer)event.getSource();

				// Control root = viewer.getTree().getChildren()[0];
				if (event.getSelection() != null) {
					setSelServerName("");
				} else {

				}
			}
		});
		// this.treeViewer.expandToLevel(2);

		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		// data.verticalSpan = 1;
		;
		Button btn_loadTag = new Button(shell, SWT.PUSH);
		btn_loadTag.setText(CustomString.getString(ComponentsUIActivator.getDefault().RESOURCE_BUNDLE,
				"OPCUATreeViewerDialog.Button.LoadTag")+"...");
		btn_loadTag.setData(data);
		// Opens an DirectoryDialog to get all *.tag files
		btn_loadTag.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// openDirectoryDialoge(shell);
				// setRoot((Root) fillTreeView());
				// tv.setInput(getRoot());
				// tv.expandToLevel(2);
				// Object source = e.getSource();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		// Create the OK button and add a handler
		// so that pressing it will set input
		// to the entered value
		Button ok = new Button(shell, SWT.PUSH);
		ok.setText(CustomString.getString(ComponentsUIActivator.getDefault().RESOURCE_BUNDLE, "OPCUATreeViewerDialog.Button.OK"));
		data = new GridData(GridData.FILL_HORIZONTAL);
		ok.setLayoutData(data);
		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				// input = new DriverField();// .getText();
				// create nodefield
				field = new OPCUANodeField();
				OPCRemoteTreeViewerItem item = (OPCRemoteTreeViewerItem) ((TreeSelection) tv.getSelection())
						.getFirstElement();
				field.setBrowsepath(item.getBrowsePath());
				field.setId(item.getNode().getNodeId().getValue().toString());
				field.setNamespace("");
				field.setServer(item.getServerName());
				shell.close();
			}
		});

		// Create the cancel button and add a handler
		// so that pressing it will set input to null
		Button cancel = new Button(shell, SWT.PUSH);
		cancel.setText(CustomString.getString(ComponentsUIActivator.getDefault().RESOURCE_BUNDLE, "OPCUATreeViewerDialog.Button.Cancel"));
		data = new GridData(GridData.FILL_HORIZONTAL);
		cancel.setLayoutData(data);
		cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				treeView = null;
				shell.close();
			}
		});

		// Set the OK button as the default, so
		// user can type input and press Enter
		// to dismiss
		shell.setDefaultButton(ok);
	}

	public String getSelServerName() {
		return selServerName;
	}

	public void setSelServerName(String selServerName) {
		this.selServerName = selServerName;
	}
}
