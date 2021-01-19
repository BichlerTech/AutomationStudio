package com.bichler.astudio.opcua.statemachine.wizard;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.statemachine.StatemachinePreferenceConstants;
import com.bichler.astudio.opcua.statemachine.wizard.ReverseModelNamespacePage.NamespaceTableItem;
import com.bichler.opcua.statemachine.BaseStatemachineActivator;
import com.bichler.opcua.statemachine.exception.StatemachineException;
import com.bichler.opcua.statemachine.reverse.engineering.AbstractReverseEngine;
import com.bichler.opcua.statemachine.reverse.engineering.IReverseTransformationContext;
import com.bichler.opcua.statemachine.reverse.engineering.PluginReverseStatemachineEngine;
import com.bichler.opcua.statemachine.reverse.engineering.AbstractInternalReverseEngine;

public class ReverseEngineModelNamespaceWizard extends Wizard {

	// Wizard page
	private ReverseModelNamespacePage pageOne;

	public ReverseEngineModelNamespaceWizard() {
		super();
		setWindowTitle("Reverse engine statemachine");
	}

	@Override
	public void addPages() {
		this.pageOne = new ReverseModelNamespacePage();
		addPage(this.pageOne);
	}

	@Override
	public boolean performFinish() {
		String projectname = this.pageOne.getProjectName();
		String directory = this.pageOne.getTargetPath();
		NamespaceTableItem[] nodeset = this.pageOne.getNodesetFile();
		boolean success = false;
		try {
			// OPC UA Base UML model
			File[] sourceUMLFiles = BaseStatemachineActivator.getDefault().getUMLResourceDefaultOpcUaClassFiles();
			if (sourceUMLFiles == null) {
				throw new IllegalArgumentException("No UML file with a default OPC UA types model");
			}
			// Create reverse enginee
			AbstractInternalReverseEngine engine = new PluginReverseStatemachineEngine();
			// set static uml ids mapping
			Map<String, Map<NodeId, String>> mapping = engine.fetchExternalIdsFromUML(sourceUMLFiles);
			engine.addExternalIdSet(mapping);
			engine.loadUMLResources(sourceUMLFiles);

			// create directory to export
			File projectFolder = new File(directory);
			if (!projectFolder.isDirectory()) {
				projectFolder.mkdir();
			}

			IReverseTransformationContext context = engine.createTransformationContext(directory);
			// load nodeset model files to import
			File[] nodesetFiles = new File[nodeset.length];
			for (int i = 0; i < nodeset.length; i++) {
				nodesetFiles[i] = new File(nodeset[i].filePath);
			}
			// import OPC UA nodeset
			engine.importOpcUaNodeset(nodesetFiles);
			// create namespace table from imported nodeset models
			String[] namespacesToExport = engine.generateNamespacesToExport();
			// transform
			engine.transformTypesModel(context, projectname, namespacesToExport);
			// add base model for OPC UA
			engine.createBaseModel(context);

			StatemachinePreferenceConstants.addValueToStore(
					StatemachinePreferenceConstants.REVERSE_NS_STATEMACHINE_PAGE_EXPORT_PATH, directory);
			success = true;
		} catch (StatemachineException e) {			
			((WizardPage) getContainer().getCurrentPage()).setErrorMessage(e.getMessage());
		}

		return success;
	}
}
