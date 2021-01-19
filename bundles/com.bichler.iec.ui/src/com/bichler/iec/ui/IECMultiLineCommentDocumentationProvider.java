package com.bichler.iec.ui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.documentation.impl.MultiLineCommentDocumentationProvider;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.parser.IParser;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.StringInputStream;

import com.bichler.iec.IecStandaloneSetup;
import com.bichler.iec.iec.BitStringType;
import com.bichler.iec.iec.DerivedType;
import com.bichler.iec.iec.Model;
import com.bichler.iec.iec.impl.IecFactoryImpl;
import com.google.inject.Injector;

public class IECMultiLineCommentDocumentationProvider extends MultiLineCommentDocumentationProvider {

	/**
	 * Returns the nearest multi line comment node that precedes the given
	 * object.
	 * 
	 * @since 2.3
	 * @return a list with exactly one node or an empty list if the object is
	 *         undocumented.
	 */
	/* @NonNull */
	@Override
	public List<INode> getDocumentationNodes(/* @NonNull */ EObject object) {

		if (object instanceof DerivedType) {
//			IECDataTypeRegistry.refreshTypes();
			object = IECDataTypeRegistry.typeRegistry.get(((DerivedType)object).getName());
		} else if (object instanceof BitStringType) {
//			IECDataTypeRegistry.refreshTypes();
			object = IECDataTypeRegistry.typeRegistry.get(((BitStringType)object).getTypeName());
		}
		
		return super.getDocumentationNodes(object);
	}

	public IECMultiLineCommentDocumentationProvider() {
		this.startTag = "\\(\\*\\*?";
		this.endTag = "\\*\\)";
	}
}
