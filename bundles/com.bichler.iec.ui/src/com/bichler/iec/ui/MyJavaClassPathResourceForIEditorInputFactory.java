package com.bichler.iec.ui;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.model.JavaClassPathResourceForIEditorInputFactory;

//Reenable validation
public class MyJavaClassPathResourceForIEditorInputFactory extends JavaClassPathResourceForIEditorInputFactory {

 @Override
 protected Resource createResource(java.net.URI uri) {
     XtextResource resource = (XtextResource) super.createResource(uri);
     resource.setValidationDisabled(false);
     return resource;
 }
}
