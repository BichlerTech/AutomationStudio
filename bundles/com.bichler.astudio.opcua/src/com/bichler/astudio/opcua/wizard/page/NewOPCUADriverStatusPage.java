package com.bichler.astudio.opcua.wizard.page;

import java.util.Deque;
import java.util.Iterator;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.components.ui.BrowsePathElement;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUANodeDialog;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;

public class NewOPCUADriverStatusPage extends WizardPage {

//	public static final String NAME_DRIVERSTATUS = "Driverstatus";

	private Button cb_createStatus;
	private Composite cmp_statusSettings;

	private Text txt_startnode;
	private Button btn_browse;

	private boolean isStatus = false;
	private NodeId driverId = Identifiers.ObjectsFolder;

	/**
	 * Create the wizard.
	 */
	public NewOPCUADriverStatusPage() {
		super("wizardPage");

		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.driverstatus.title"));
		setDescription(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.driverstatus.description"));
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(1, false));

		this.cb_createStatus = new Button(container, SWT.CHECK);
		this.cb_createStatus.setSelection(false);
		this.cb_createStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.cb_createStatus.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.driverstatus.create"));

		this.cmp_statusSettings = new Composite(container, SWT.NONE);
		this.cmp_statusSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		this.cmp_statusSettings.setLayout(new GridLayout(3, false));

		Label lblStartnode = new Label(cmp_statusSettings, SWT.NONE);
		lblStartnode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStartnode.setText(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.driverstatus.startnode"));

		this.txt_startnode = new Text(cmp_statusSettings, SWT.BORDER);
		this.txt_startnode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		this.btn_browse = new Button(cmp_statusSettings, SWT.NONE);
		this.btn_browse.setImage(StudioImageActivator.getImage(StudioImages.ICON_SEARCH));

		setHandler();

		fill();
	}

	private void fill() {
		txt_startnode.setText("/Root/Objects/");
	}

	private void setHandler() {
		this.btn_browse.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				OPCUANodeDialog dialog = new OPCUANodeDialog(getShell());
				dialog.setInternalServer(ServerInstance.getInstance().getServerInstance());
				dialog.setStartId(Identifiers.RootFolder);

				NodeId data = driverId;
				if (!NodeId.isNull(data)) {
					dialog.setSelectedNodeId(data);
				}
				dialog.setFormTitle(
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "dialog.nodes.browse"));

				if (dialog.open() == Dialog.OK) {
					Deque<BrowsePathElement> browsePath = dialog.getBrowsePath();
					Iterator<BrowsePathElement> iterator = browsePath.iterator();

					String path = "";
					while (iterator.hasNext()) {
						BrowsePathElement element = iterator.next();
						String name = element.getBrowsename().getName();

						path += name;
						if (iterator.hasNext()) {
							path += "/";
						}
					}
					// find driverstatus folder
					driverId = dialog.getSelectedNodeId();
					txt_startnode.setText(path);
				}
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}

		});

		this.cb_createStatus.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				isStatus = ((Button) e.getSource()).getSelection();

				if (isStatus) {
					cmp_statusSettings.setEnabled(true);
					boolean isComplete = isPageComplete();
					setPageComplete(isComplete);
				} else {
					cmp_statusSettings.setEnabled(false);
					setPageComplete(true);
				}

			}

		});

		this.txt_startnode.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				e.doit = false;
			}

			@Override
			public void keyPressed(KeyEvent e) {
				e.doit = false;
			}
		});
	}

	@Override
	public boolean isPageComplete() {
		if (this.txt_startnode.getText().isEmpty()) {
			return false;
		}

		return true;
	}

	public NodeId getRoot() {
		return this.driverId;
	}

	public boolean isStatus() {
		return this.isStatus;
	}
}
