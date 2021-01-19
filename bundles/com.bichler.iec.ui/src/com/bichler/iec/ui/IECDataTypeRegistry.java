package com.bichler.iec.ui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.bichler.iec.IecStandaloneSetup;
import com.google.inject.Injector;

public class IECDataTypeRegistry {

	public static Map<String, EObject> typeRegistry = null;

	public static String projectPath = "";

	static {
		typeRegistry = new HashMap<String, EObject>();
	}

	public static void loadAllTypes() {
		Injector injector = new IecStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);

		File customdatatypes = new File(projectPath + "/datatypes/custom");
		if (customdatatypes.exists()) {

			for (File file : customdatatypes.listFiles()) {
				if (file.isFile()) {
					URI uri = URI.createFileURI(file.getAbsolutePath());

					Resource resource = resourceSet.getResource(uri, true);

					if (resource.getContents() != null && resource.getContents().size() > 0) {
						EObject object = resource.getContents().get(0);

						typeRegistry.put(file.getName().replace(".iec", ""), object);
					}
				}
			}

		}

		File systemdatatypes = new File(projectPath + "/datatypes/system");
		if (systemdatatypes.exists()) {

			for (File file : systemdatatypes.listFiles()) {
				if (file.isFile()) {
					URI uri = URI.createFileURI(file.getAbsolutePath());

					Resource resource = resourceSet.getResource(uri, true);

					if (resource.getContents() != null && resource.getContents().size() > 0) {
						EObject object = resource.getContents().get(0);

						typeRegistry.put(file.getName().replace(".iec", ""), object);
					}
				}
			}

		}
		// Model modelRootElement = (Model) resource.getContents().get(0);

	}

	public static void refreshTypes() {
		loadAllTypes();
	}
}
