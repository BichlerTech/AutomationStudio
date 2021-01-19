package com.bichler.astudio.opcua.opcmodeler.wizards.opc.load;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.opcfoundation.ua.common.ServiceResultException;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.opcmodeler.Activator;

public class ImportGeneratedIec2OpcWizard extends Wizard {
	private static final String NAME = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
			"wizard.import.title");
	private ProjectSelectionPage _pageOne;
	private ModelSelectionPage _pageTwo;

	public ImportGeneratedIec2OpcWizard() {
		super();
		setWindowTitle(NAME);
	}

	@Override
	public void addPages() {
		_pageOne = new ProjectSelectionPage();
		_pageTwo = new ModelSelectionPage();
		_pageOne.setPageTwo(_pageTwo);
		_pageTwo.setPageOne(_pageOne);
		addPage(_pageOne);
		addPage(_pageTwo);
	}

	@Override
	public boolean performFinish() {
		final StructuredSelection selection = _pageTwo.getSelection();
		final Iterator<Object> iterator = selection.iterator();
		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				try {
					monitor.beginTask(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
							"opc.message.load.modelfile"), 10);
					monitor.worked(3);
					List<File> types = new ArrayList<>();
					List<File> objects = new ArrayList<>();
					while (iterator.hasNext()) {
						IResource resource = (IResource) iterator.next();
						File modelFile = null;
						modelFile = new File(resource.getLocationURI());
						// load xml types
						String filename = modelFile.getName();
						// file extension
						String[] split = filename.split("\\.");
						if (split == null) {
							return;
						}
						String[] specificImport = split[0].split("_");
						if (specificImport == null) {
							return;
						}
						if (specificImport != null && specificImport.length > 1) {
							// _type / _objects
							String type = specificImport[1];
							switch (type) {
							case "types":
								// add nodeset xml
								types.add(modelFile);
								break;
							case "objects":
								objects.add(modelFile);
								break;
							default:
								break;
							}
						} else {
							types.add(modelFile);
						}
						monitor.worked(4);
						// insert all type files
						for (File f : types) {
							// try {
							try {
								ServerInstance.getInstance().importModel(null, f.getPath());
							} catch (ServiceResultException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						// insert object files
						for (File f : objects) {
							// try {
							try {
								ServerInstance.getInstance().importModel(null, f.getPath());
							} catch (ServiceResultException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						monitor.worked(8);
					}
				} finally {
					monitor.worked(10);
					monitor.done();
				}
			}
		};
		ServerInstance.blockExecute(runnable);
		return true;
	}
}
