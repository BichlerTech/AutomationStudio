package com.bichler.astudio.opcua.statemachine.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.nebula.jface.tablecomboviewer.TableComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.opcua.statemachine.StatemachineActivator;
import com.bichler.astudio.opcua.statemachine.StatemachinePreferenceConstants;
import com.bichler.astudio.opcua.statemachine.utils.WizardUtils;

public class ReverseModelNamespacePage extends WizardPage {
//	private static final String INVALID_NAME = "<empty>";
	private static final String EXTENSION_XML = ".xml";
	private static final int COLUMN_COUNT = 1;

	private TableComboViewer cmbDirectoryPath;
	private Text txtProjectName;

	private Button btnDirectoryPath;
	private Button btnAddNodeSetModel;
	private Button btnRemoveNodeSetModel;

	private TableViewer tableNodesetModels;
	private Composite cmpButtons;

	private TableViewerColumn colPath;

	public ReverseModelNamespacePage() {
		super("reversmodel namespace page");
		setTitle("Reverse engineering UML model");
		setDescription("Reverse engineers types from OPC UA namespace to UML model");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(3, false));

		Label lblProjectname = new Label(container, SWT.NONE);
		lblProjectname.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProjectname.setText("Projectname");

		this.txtProjectName = new Text(container, SWT.BORDER);
		this.txtProjectName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(container, SWT.NONE);

		Label lblTargetPath = new Label(container, SWT.NONE);
		lblTargetPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTargetPath.setText("Target directory");

//		this.cmbDirectoryPath = new TableComboViewer(container, SWT.BORDER | SWT.READ_ONLY);
//		this.cmbDirectoryPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.cmbDirectoryPath = WizardUtils.createTableComboViewer(getShell(), container,
				StatemachinePreferenceConstants.REVERSE_NS_STATEMACHINE_PAGE_EXPORT_PATH);
		
		
		this.btnDirectoryPath = new Button(container, SWT.NONE);
//		this.btnDirectoryPath.setText("Select");
		this.btnDirectoryPath.setImage(
				CommonImagesActivator.getImage(CommonImagesActivator.IMG_16, CommonImagesActivator.OPEN_FOLDER));
		btnDirectoryPath.setToolTipText("Open");

		Label lblNodeSetModel = new Label(container, SWT.NONE);
		lblNodeSetModel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNodeSetModel.setText("OPC UA nodeset");

		tableNodesetModels = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		tableNodesetModels.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableNodesetModels.getTable().setHeaderVisible(false);
		tableNodesetModels.getTable().setLinesVisible(true);

		tableNodesetModels.setContentProvider(new ArrayContentProvider());
		tableNodesetModels.setLabelProvider(new LabelProvider());

		this.colPath = new TableViewerColumn(tableNodesetModels, SWT.NONE);
		colPath.getColumn().setWidth(200);
		colPath.getColumn().setText("OPC UA nodeset");
		colPath.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				NamespaceTableItem item = (NamespaceTableItem) element;
				return item.filePath;
			}
		});

		new Label(container, SWT.NONE);

		cmpButtons = new Composite(container, SWT.NONE);
		cmpButtons.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		cmpButtons.setLayout(new GridLayout(3, false));

		Label lblAlign = new Label(cmpButtons, SWT.NONE);
		lblAlign.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblAlign.setBounds(0, 0, 70, 20);

		this.btnAddNodeSetModel = new Button(cmpButtons, SWT.NONE);
		btnAddNodeSetModel
				.setImage(CommonImagesActivator.getImage(CommonImagesActivator.IMG_16, CommonImagesActivator.ADD));
		btnAddNodeSetModel.setToolTipText("Add nodeset");

		this.btnRemoveNodeSetModel = new Button(cmpButtons, SWT.NONE);
		btnRemoveNodeSetModel
				.setImage(CommonImagesActivator.getImage(CommonImagesActivator.IMG_16, CommonImagesActivator.DELETE));
		btnRemoveNodeSetModel.setToolTipText("Remove nodeset");

		new Label(container, SWT.NONE);

		setDefaultValues();
		setHandler();
	}

	@Override
	public boolean isPageComplete() {
		if (this.txtProjectName.getText().isEmpty()) {
			setErrorMessage("Enter an UML project name!");
			return false;
		}

		if (this.cmbDirectoryPath.getSelection().isEmpty()) {
			setErrorMessage("Enter a target directory!");
			return false;
		}

		if (this.tableNodesetModels.getTable().getItemCount() <= 0) {
			setErrorMessage("No Nodeset XML file to reverse engineer!");
			return false;
		}

		setErrorMessage(null);

		return true;
	}

	protected NamespaceTableItem[] getNodesetFile() {
		List<NamespaceTableItem> nodesetItems = new ArrayList<>();

		for (int i = 0; i < this.tableNodesetModels.getTable().getItemCount(); i++) {
			NamespaceTableItem nsItem = (NamespaceTableItem) this.tableNodesetModels.getElementAt(i);
			nodesetItems.add(nsItem);
		}

		return nodesetItems.toArray(new NamespaceTableItem[0]);
	}

	protected String getTargetPath() {
		return this.cmbDirectoryPath.getTableCombo().getText();
	}

	private void setDefaultValues() {
		this.btnRemoveNodeSetModel.setEnabled(false);

		IPreferenceStore preferenceStore = StatemachineActivator.getDefault().getPreferenceStore();
		String pathTargets = preferenceStore
				.getString(StatemachinePreferenceConstants.REVERSE_NS_STATEMACHINE_PAGE_EXPORT_PATH);

		String[] valuePathTargets = StatemachinePreferenceConstants.convertStringToArray(pathTargets);
		this.cmbDirectoryPath.add(valuePathTargets);
	}

	private void setHandler() {
		this.tableNodesetModels.getTable().addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				Rectangle rect = tableNodesetModels.getTable().getClientArea();
				if (rect.width > 0) {
					int extraSpace = rect.width / COLUMN_COUNT;
					colPath.getColumn().setWidth(extraSpace);
				}
			}

			@Override
			public void controlMoved(ControlEvent e) {
				// TODO Auto-generated method stub

			}
		});

		this.tableNodesetModels.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if (!event.getSelection().isEmpty()) {
					btnRemoveNodeSetModel.setEnabled(true);
				} else {
					btnRemoveNodeSetModel.setEnabled(false);
				}
			}
		});

		this.cmbDirectoryPath.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

				
		this.txtProjectName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		this.btnAddNodeSetModel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = WizardUtils.openFileDialog(getShell(), SWT.OPEN, new String[] { "*" + EXTENSION_XML },
						null);

				if (path != null) {
					NamespaceTableItem item = new NamespaceTableItem();
					item.filePath = path;
					tableNodesetModels.add(item);
				}

				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}

		});

		this.btnRemoveNodeSetModel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableNodesetModels.remove(((IStructuredSelection) tableNodesetModels.getSelection()).getFirstElement());

				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}

		});

		this.btnDirectoryPath.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String selection = (!cmbDirectoryPath.getSelection().isEmpty()) ? (String) cmbDirectoryPath.getStructuredSelection().getFirstElement() : null;
				String path = WizardUtils.openDirectoryDialog(getShell(), selection);
				if (path != null) {
					int index = cmbDirectoryPath.getTableCombo().indexOf(path);
					if (index < 0) {
						cmbDirectoryPath.add(path);
					}

					cmbDirectoryPath.getTableCombo().setText(path);
				}
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}

		});
	}

	class NamespaceTableItem {
		protected String filePath = "";
	}

	public String getProjectName() {
		return this.txtProjectName.getText();
	}

}
