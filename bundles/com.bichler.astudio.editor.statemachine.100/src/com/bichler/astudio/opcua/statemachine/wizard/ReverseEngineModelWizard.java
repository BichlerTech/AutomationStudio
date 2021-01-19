package com.bichler.astudio.opcua.statemachine.wizard;

import java.io.File;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.opcua.statemachine.StatemachinePreferenceConstants;
import com.bichler.opcua.statemachine.exception.StatemachineException;
import com.bichler.opcua.statemachine.reverse.engineering.AbstractReverseEngine;
import com.bichler.opcua.statemachine.reverse.engineering.IReverseTransformationContext;
import com.bichler.opcua.statemachine.reverse.engineering.PluginReverseStatemachineEngine;
import com.bichler.opcua.statemachine.reverse.engineering.AbstractInternalReverseEngine;

public class ReverseEngineModelWizard extends Wizard {

	// Wizard page
	private ReverseModelPage pageOne;

	public ReverseEngineModelWizard() {
		super();
		setWindowTitle("Reverse engine statemachine");
	}

	@Override
	public void addPages() {
		this.pageOne = new ReverseModelPage();
		addPage(this.pageOne);
	}

	@Override
	public boolean performFinish() {
		String directory = this.pageOne.getTargetPath();
		String projectName = this.pageOne.getProjectName();

		File projectFolder = new File(directory);
		if (!projectFolder.isDirectory()) {
			projectFolder.mkdir();
		}
		boolean success = false;
		try {
			AbstractReverseEngine engine = new PluginReverseStatemachineEngine();
			IReverseTransformationContext context = engine.createTransformationContext(directory);
			String[] UA_NAMESPACE = new String[] { NamespaceTable.OPCUA_NAMESPACE };

			engine.transformTypesModel(context, projectName, UA_NAMESPACE);

			StatemachinePreferenceConstants
					.addValueToStore(StatemachinePreferenceConstants.REVERSE_STATEMACHINE_PAGE_EXPORT_PATH, directory);
			success = true;
		} catch (StatemachineException e) {
			((WizardPage) getContainer().getCurrentPage()).setErrorMessage(e.getMessage());
		}

		return success;
	}
}
