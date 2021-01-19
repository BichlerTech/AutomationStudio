package com.bichler.astudio.opcua.statemachine.wizard;

import java.io.File;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;

import com.bichler.astudio.opcua.statemachine.StatemachinePreferenceConstants;
import com.bichler.opcua.statemachine.exception.StatemachineException;
import com.bichler.opcua.statemachine.transform.AbstractStateMachineToOpcTransformer;
import com.bichler.opcua.statemachine.transform.PluginStateMachineToOpcTransformer;

public class CreateStateMachineWizard extends Wizard {

	private CreateStatemachinePage pageOne;

	public CreateStateMachineWizard() {
		super();
		setWindowTitle("Create statemachine");
	}

	@Override
	public void addPages() {
		this.pageOne = new CreateStatemachinePage();
		addPage(this.pageOne);
	}

	@Override
	public boolean performFinish() {
		String statemachineUMLModel = this.pageOne.getStatemachineUMLModel();
		String targetPath = this.pageOne.getTargetPath();

		boolean success = false;
		try {
			File resourceFile = new File(statemachineUMLModel);
			File output = new File(targetPath);
			AbstractStateMachineToOpcTransformer transformer = new PluginStateMachineToOpcTransformer(true);
			transformer.transform(resourceFile, output);
			// store path to store
			StatemachinePreferenceConstants.addValueToStore(
					StatemachinePreferenceConstants.CREATE_STATEMACHINE_PAGE_UML_PATH, statemachineUMLModel);
			StatemachinePreferenceConstants
					.addValueToStore(StatemachinePreferenceConstants.CREATE_STATEMACHINE_PAGE_EXPORT_PATH, targetPath);
			success = true;
		} catch (StatemachineException e) {
			((WizardPage) getContainer().getCurrentPage()).setErrorMessage(e.getMessage());
		}
		return success;
	}

}
