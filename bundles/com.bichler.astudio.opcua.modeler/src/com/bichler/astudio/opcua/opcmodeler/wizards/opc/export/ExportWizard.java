package com.bichler.astudio.opcua.opcmodeler.wizards.opc.export;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ExportWizard extends Wizard {
	private ExportModelWizardPage exportPage = null;
	private List<Integer> allowedNamespaces;
	private String filePath;
	private boolean isVersion2 = false;

	// private String[] selectedNamespaces;
	public ExportWizard() {
		setWindowTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.lookup"));
		this.exportPage = new ExportModelWizardPage();
		addPage(this.exportPage);
	}

	@Override
	public boolean performFinish() {
		this.allowedNamespaces = this.exportPage.getAllowedNamespaces();
		this.filePath = this.exportPage.getFilePath();
		this.isVersion2 = this.exportPage.getInformationModelVersion();
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask(
							CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.export.model")
									+ "...",
							IProgressMonitor.UNKNOWN);

					// export version 1
					if (!isVersion2) {
						ServerInstance.exportModelFile2(monitor, filePath, allowedNamespaces);
					} else {
						ServerInstance.exportModel2File2(monitor, filePath, allowedNamespaces);
					}
					// export version 2
					monitor.done();
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		DesignerUtils.setPreferenceLastDestinationFile(filePath);
		return true;
	}

	public List<Integer> getAllowedNamespaces() {
		return this.allowedNamespaces;
	}

	public void setSelectedIndizes(String[] indizes2export) {
		this.exportPage.setSelectedNamespaces(indizes2export);
	}
}
