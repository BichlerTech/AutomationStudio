package com.bichler.astudio.opcua.statemachine.wizard;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.nebula.jface.tablecomboviewer.TableComboViewer;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.opcua.statemachine.StatemachineActivator;
import com.bichler.astudio.opcua.statemachine.StatemachinePreferenceConstants;
import com.bichler.astudio.opcua.statemachine.utils.WizardUtils;

public class CreateStatemachinePage extends WizardPage {
	private TableComboViewer cmbStatemachineUmlModel;
	private TableComboViewer cmbTargetPath;

	private Button btnStatemachineUmlModel;
	private Button btnTargetPath;

	public CreateStatemachinePage() {
		super("selectstatemachinepage");
		setTitle("Statemachine");
		setDescription("Select an UML statemachine model");
	}

//	private TableComboViewer createTableComboViewer(Composite parent, String preference) {
//		TableCombo combo = new TableCombo(parent, SWT.BORDER | SWT.READ_ONLY);
//		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		combo.setShowTableHeader(false);
//		combo.setShowTableLines(true);
//		combo.setShowImageWithinSelection(true);
//		combo.defineColumns(2);
//
//		TableComboViewer viewer = new TableComboViewer(combo);
//
//		viewer.setContentProvider(new ArrayContentProvider());
//		viewer.setLabelProvider(new TableComboViewerLabelProvider());
////		viewer.setSorter(getSorter());
//
//		WizardUtils.addTableComboViewerListener(getShell(), viewer, preference);
//		
//		return viewer;
//	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(3, false));

		Label lblStatemachineUmlModel = new Label(container, SWT.NONE);
		lblStatemachineUmlModel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStatemachineUmlModel.setText("Statemachine UML model");

		cmbStatemachineUmlModel = WizardUtils.createTableComboViewer(getShell(), container,
				StatemachinePreferenceConstants.CREATE_STATEMACHINE_PAGE_UML_PATH);

		this.btnStatemachineUmlModel = new Button(container, SWT.NONE);
		btnStatemachineUmlModel.setBounds(0, 0, 90, 30);
		btnStatemachineUmlModel.setImage(
				CommonImagesActivator.getImage(CommonImagesActivator.IMG_16, CommonImagesActivator.OPEN_FOLDER));
		btnStatemachineUmlModel.setToolTipText("Open");

		Label lblTargetPath = new Label(container, SWT.NONE);
		lblTargetPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTargetPath.setText("Target path");

		cmbTargetPath = WizardUtils.createTableComboViewer(getShell(), container,
				StatemachinePreferenceConstants.CREATE_STATEMACHINE_PAGE_EXPORT_PATH);
		// new Combo(container, SWT.BORDER | SWT.READ_ONLY);
		// cmbTargetPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
		// 1, 1));

		this.btnTargetPath = new Button(container, SWT.NONE);
		btnTargetPath.setBounds(0, 0, 90, 30);
		btnTargetPath.setImage(
				CommonImagesActivator.getImage(CommonImagesActivator.IMG_16, CommonImagesActivator.OPEN_FOLDER));
		btnTargetPath.setToolTipText("Open");

		setDefaultValues();

		setHandler();
	}

	@Override
	public boolean isPageComplete() {
		if (this.cmbStatemachineUmlModel.getSelection().isEmpty()) {
			setErrorMessage("Enter an UML statemachine model!");
			return false;
		}

		if (this.cmbTargetPath.getSelection().isEmpty()) {
			setErrorMessage("Enter a target model file location!");
			return false;
		}

		setErrorMessage(null);

		return true;
	}

	protected String getStatemachineUMLModel() {
		return (String) this.cmbStatemachineUmlModel.getStructuredSelection().getFirstElement();
	}

	protected String getTargetPath() {
		return (String) this.cmbTargetPath.getStructuredSelection().getFirstElement();
	}

	private String[] getUMLExtension() {
		return new String[] { "*.uml" };
	}

	private String[] getTargetExtension() {
		return new String[] { "*.xml" };
	}

	private void setDefaultValues() {
		IPreferenceStore preferenceStore = StatemachineActivator.getDefault().getPreferenceStore();
		String pathUMLs = preferenceStore.getString(StatemachinePreferenceConstants.CREATE_STATEMACHINE_PAGE_UML_PATH);
		String pathTargets = preferenceStore
				.getString(StatemachinePreferenceConstants.CREATE_STATEMACHINE_PAGE_EXPORT_PATH);

		String[] valuePathUMLs = StatemachinePreferenceConstants.convertStringToArray(pathUMLs);
		String[] valuePathTargets = StatemachinePreferenceConstants.convertStringToArray(pathTargets);

		this.cmbStatemachineUmlModel.add(valuePathUMLs);
		this.cmbTargetPath.add(valuePathTargets);
	}

	private void setHandler() {

		this.cmbStatemachineUmlModel.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		this.cmbTargetPath.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		this.btnStatemachineUmlModel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String selection = (!cmbStatemachineUmlModel.getSelection().isEmpty())
						? (String) cmbStatemachineUmlModel.getStructuredSelection().getFirstElement()
						: null;
				String path = WizardUtils.openFileDialog(getShell(), SWT.OPEN, getUMLExtension(), selection);
				if (path != null) {
					int index = ((TableCombo) cmbStatemachineUmlModel.getControl()).indexOf(path);
					if (index < 0) {
						cmbStatemachineUmlModel.add(path);
					}
					cmbStatemachineUmlModel.getTableCombo().setText(path);

					boolean isComplete = isPageComplete();
					setPageComplete(isComplete);
				}
			}

		});

		this.btnTargetPath.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String selection = (!cmbTargetPath.getSelection().isEmpty())
						? (String) cmbTargetPath.getStructuredSelection().getFirstElement()
						: null;
				String path = WizardUtils.openFileDialog(getShell(), SWT.SAVE, getTargetExtension(), selection);
				if (path != null) {
					int index = ((TableCombo) cmbTargetPath.getControl()).indexOf(path);
					if (index < 0) {
						cmbTargetPath.add(path);
					}
					cmbTargetPath.getTableCombo().setText(path);
				}
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}

		});
	}

}
