package com.bichler.astudio.opcua.opcmodeler.wizards.opc.export;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.constants.DesignerConstants;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ExportModelWizardPage extends WizardPage {
	private CheckboxTreeViewer treeviewer = null;
	private Text fileLocation = null;
	private Button openFile = null;
	private Button check_exportWithDefaultNodes;
	private Button check_exportClass;
	private Button check_exportCClass;

	private Button check_exportModel2;
	private String[] selectedNamespaces;

	boolean isFileLocationEditable = true;
	boolean isVersion2 = false;

	protected ExportModelWizardPage() {
		super("Export Model");
		setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.export.model"));
		// setMessage("Export");
	}

	@Override
	public void createControl(final Composite parent) {
		parent.setLayout(new GridLayout(3, false));
		Label descriptionLabel1 = new Label(parent, SWT.NONE);
		descriptionLabel1.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.export.namespace.select"));
		GridDataFactory.fillDefaults().span(3, 1).applyTo(descriptionLabel1);

		setControl(descriptionLabel1);

		this.treeviewer = new CheckboxTreeViewer(parent, SWT.BORDER | SWT.V_SCROLL);
		this.treeviewer.setLabelProvider(new WizardLabelProvider());
		this.treeviewer.setContentProvider(new WizzardArrayContentProvider());
		// The input are all namespaces, except index [0], its the default opc
		// ua namespace
		String[] namespaces = ServerInstance.getInstance().getServerInstance().getNamespaceUris().toArray();
		String[] treeViewerNamespacesInput = new String[namespaces.length - 1];
		// skip index 0 namespace (default)
		for (int i = 1; i < namespaces.length; i++) {
			treeViewerNamespacesInput[i - 1] = namespaces[i];
		}
		this.treeviewer.setInput(treeViewerNamespacesInput);
		GridDataFactory.fillDefaults().span(3, 1).hint(SWT.DEFAULT, 100).align(SWT.FILL, SWT.FILL)
				.applyTo(this.treeviewer.getControl());
		setControl(this.treeviewer.getControl());

		Label descriptionLabel2 = new Label(parent, SWT.NONE);
		descriptionLabel2
				.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.export.destination"));
		GridDataFactory.fillDefaults().span(3, 1).applyTo(descriptionLabel2);
		setControl(descriptionLabel2);

		Label descriptionLabel3 = new Label(parent, SWT.NONE);
		descriptionLabel3.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.file"));
		GridDataFactory.fillDefaults().applyTo(descriptionLabel3);
		setControl(descriptionLabel3);

		this.fileLocation = new Text(parent, SWT.BORDER);
		GridDataFactory.fillDefaults().hint(400, SWT.DEFAULT).applyTo(this.fileLocation);
		setControl(this.fileLocation);

		this.openFile = new Button(parent, SWT.PUSH);
		this.openFile.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.lookup"));
		GridDataFactory.fillDefaults().applyTo(this.openFile);
		setControl(this.openFile);

		Label descriptionLabel4 = new Label(parent, SWT.NONE);
		descriptionLabel4.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.options"));
		GridDataFactory.fillDefaults().span(3, 1).applyTo(descriptionLabel4);
		setControl(descriptionLabel4);

		this.check_exportWithDefaultNodes = new Button(parent, SWT.CHECK);
		this.check_exportWithDefaultNodes
				.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.export.defaultnodes"));
		this.check_exportWithDefaultNodes.setSelection(true);
		// this.check_exportWithDefaultNodes.setEnabled(false);
		GridDataFactory.fillDefaults().span(3, 1).applyTo(this.check_exportWithDefaultNodes);
		setControl(this.check_exportWithDefaultNodes);

		this.check_exportClass = new Button(parent, SWT.CHECK);
		this.check_exportClass
				.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.export.classes"));
		this.check_exportClass.setSelection(false);
		GridDataFactory.fillDefaults().span(3, 1).applyTo(this.check_exportClass);
		setControl(this.check_exportClass);

		this.check_exportCClass = new Button(parent, SWT.CHECK);
		this.check_exportCClass
				.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.export.cclasses"));
		this.check_exportClass.setSelection(false);
		GridDataFactory.fillDefaults().span(3, 1).applyTo(this.check_exportCClass);
		setControl(this.check_exportCClass);

		this.check_exportModel2 = new Button(parent, SWT.CHECK);
		this.check_exportModel2
				.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.export.model2"));
		this.check_exportModel2.setSelection(false);
		GridDataFactory.fillDefaults().span(3, 1).applyTo(this.check_exportModel2);
		setControl(this.check_exportModel2);

		setHandler();
		selectTreeViewer();
		setLastSelectedDestinationFile();
	}

	private void setHandler() {
		this.fileLocation.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				if (!isFileLocationEditable) {
					e.doit = false;
				}
				isFileLocationEditable = false;
			}
		});
		this.treeviewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				// add item if state is true, otherwise remove it
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});
		this.openFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(getShell(), SWT.SAVE);
				fileDialog.setOverwrite(true);
				fileDialog.setFilterNames(DesignerConstants.EXTENSION_NAME_INFORMATIONMODEL);
				fileDialog.setFilterExtensions(DesignerConstants.EXTENSION_INFORMATIONMODEL);
				String open = fileDialog.open();
				if (open != null) {
					isFileLocationEditable = true;
					fileLocation.setText(open);
					boolean complete = isPageComplete();
					setPageComplete(complete);
				}
			}
		});

		this.check_exportModel2.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				isVersion2 = check_exportModel2.getSelection();

				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});
		
		this.check_exportWithDefaultNodes.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
			
			
			
		});
	}

	private void setLastSelectedDestinationFile() {
		String lastDestinationFilePath = DesignerUtils.getPreferenceLastDestinationFile();
		this.fileLocation.setText(lastDestinationFilePath);
	}

	private void selectTreeViewer() {
		if (this.selectedNamespaces != null && this.selectedNamespaces.length > 0) {
			TreeItem[] items = this.treeviewer.getTree().getItems();
			if (items != null) {
				for (TreeItem item : items) {
					String text = item.getText();
					for (int i = 0; i < this.selectedNamespaces.length; i++) {
						if (this.selectedNamespaces[i].equals(text)) {
							item.setChecked(true);
						}
					}
				}
			}
		}
	}

	@Override
	public boolean isPageComplete() {
		// TODO: LOOK FOR CHECKBOX SELECTION NOT FOR TREE SELECTION
		boolean isExportWithDefault = check_exportWithDefaultNodes.getSelection();
		
		if (this.fileLocation.getText().isEmpty() || (!isExportWithDefault && this.treeviewer.getCheckedElements().length == 0)) {
			return false;
		}
		// check if a filepath has selected
		if (fileLocation.getText().isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Returns file path in which the model will be exported
	 * 
	 * @return model file path
	 */
	public String getFilePath() {
		return fileLocation.getText();
	}

	public List<Integer> getAllowedNamespaces() {
		// TREEVIEWER GET ALLOWED SELECTION
		Object[] values = this.treeviewer.getCheckedElements();
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		int nsLength = values.length;
		int index = 0;
		// add default namespace nodes as allowed
		if (getExportDefaultNodes()) {
			nsLength += 1;
			index += 1;
		}
		List<Integer> allowedNS = new ArrayList<>();
		if (getExportDefaultNodes()) {
			allowedNS.add(nsTable.getIndex(NamespaceTable.OPCUA_NAMESPACE));
		}
		for (Object value : values) {
			int nsIndex = nsTable.getIndex((String) value);
			allowedNS.add(nsIndex);
			index++;
		}
		return allowedNS;
	}

	private boolean getExportDefaultNodes() {
		return this.check_exportWithDefaultNodes.getSelection();
	}

	public void setSelectedNamespaces(String[] indizes2export) {
		this.selectedNamespaces = indizes2export;
	}

	public boolean getInformationModelVersion() {
		return this.isVersion2;
	}
}
