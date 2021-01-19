package com.bichler.iec.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.bichler.iec.IecStandaloneSetup;
import com.bichler.iec.iec.GlobalVar;
import com.bichler.iec.iec.GlobalVarDecl;
import com.bichler.iec.iec.GlobalVarDeclarations;
import com.bichler.iec.iec.Model;
import com.bichler.iec.iec.ResourceDeclaration;
import com.bichler.iec.iec.impl.GlobalVarListImpl;
import com.google.inject.Injector;

public class IECGlobalVarRegistry {
	public static Map<String, EObject> varRegistry = new HashMap<>();
	public static String projectPath = "";

	public static void loadAllGlobalVars(File globVarFile) {
		Injector injector = new IecStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		if (globVarFile.exists()) {
			URI uri = URI.createFileURI(globVarFile.getAbsolutePath());
			Resource resource = resourceSet.getResource(uri, true);
			if (resource.getContents() != null && resource.getContents().size() > 0) {
				EObject object = resource.getContents().get(0);
				varRegistry.put("global", object);
			}
		}
	}

	public static List<String> getGlobalVarAsArray() {
		List<String> vars = new ArrayList<>();
		EObject obj = varRegistry.get("global");
		if (obj == null) {
			return vars;
		}
		if (obj != null && obj instanceof Model) {
			Model mod = (Model) obj;
			if (mod.getModelElement() != null && mod.getModelElement().size() > 0) {
				ResourceDeclaration resdecl = ((ResourceDeclaration) mod.getModelElement().get(0));
				GlobalVarDeclarations globvarDecls = resdecl.getGlobalVarDeclarations();
				for (GlobalVarDecl decl : globvarDecls.getGlobalVarDecl()) {
					if (decl != null && decl.getSpec() != null) {
						for (GlobalVar var : ((GlobalVarListImpl) decl.getSpec()).getVariables()) {
							vars.add(var.getName());
						}
					}
				}
			}
		}
		return vars;
	}
}
