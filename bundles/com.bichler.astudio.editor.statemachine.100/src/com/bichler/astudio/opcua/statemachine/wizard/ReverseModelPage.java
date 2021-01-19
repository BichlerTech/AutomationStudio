package com.bichler.astudio.opcua.statemachine.wizard;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.nebula.jface.tablecomboviewer.TableComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.opcua.statemachine.StatemachineActivator;
import com.bichler.astudio.opcua.statemachine.StatemachinePreferenceConstants;
import com.bichler.astudio.opcua.statemachine.utils.WizardUtils;
import com.bichler.opcua.statemachine.reverse.engineering.ReverseTranslationConstants;

public class ReverseModelPage extends WizardPage {
	private TableComboViewer cmbDirectoryPath;
	private Button btnDirectoryPath;
	private Combo cmbProjectname;

	public ReverseModelPage() {
		super("reversestatemachinepage");
		setTitle("Reverse UML model engineering");
		setDescription("Reverse engineers OPC UA types to UML model");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(3, false));

		Label lblPackageName = new Label(container, SWT.NONE);
		lblPackageName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPackageName.setText("Packagename");

		this.cmbProjectname = new Combo(container, SWT.BORDER);
		this.cmbProjectname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label lblTargetPath = new Label(container, SWT.NONE);
		lblTargetPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTargetPath.setText("Directory");

//		this.cmbDirectoryPath = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
//		this.cmbDirectoryPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.cmbDirectoryPath = WizardUtils.createTableComboViewer(getShell(), container,
				StatemachinePreferenceConstants.REVERSE_STATEMACHINE_PAGE_EXPORT_PATH);
		
		this.btnDirectoryPath = new Button(container, SWT.NONE);
		this.btnDirectoryPath.setImage(
				CommonImagesActivator.getImage(CommonImagesActivator.IMG_16, CommonImagesActivator.OPEN_FOLDER));
		this.btnDirectoryPath.setToolTipText("Open");
		
		setDefaultValues();
		setHandler();
	}

	private void setDefaultValues() {
		this.cmbProjectname.setItems(ReverseTranslationConstants.MODEL_DEFAULTPACKAGENAME);
		this.cmbProjectname.select(0);
		
		IPreferenceStore preferenceStore = StatemachineActivator.getDefault().getPreferenceStore();
		String pathTargets = preferenceStore.getString(StatemachinePreferenceConstants.REVERSE_STATEMACHINE_PAGE_EXPORT_PATH);
		
		String[] valuePathTargets = StatemachinePreferenceConstants.convertStringToArray(pathTargets);
		this.cmbDirectoryPath.add(valuePathTargets);
	}

	@Override
	public boolean isPageComplete() {
		if (this.cmbProjectname.getText().isEmpty()) {
			setErrorMessage("Enter a UML package name!");
			return false;
		}

		if (this.cmbDirectoryPath.getSelection().isEmpty()) {
			setErrorMessage("Enter a target directory!");
			return false;
		}

		setErrorMessage(null);

		return true;
	}

	protected String getTargetPath() {
		return this.cmbDirectoryPath.getTableCombo().getText();
	}


	protected String getProjectName() {
		return this.cmbProjectname.getText();
	}

	

	private void setHandler() {
		this.cmbDirectoryPath.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
		
		this.cmbProjectname.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
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
					if(index < 0) {
						cmbDirectoryPath.add(path);
					}
					cmbDirectoryPath.getTableCombo().setText(path);
				}
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}

		});
	}
}
