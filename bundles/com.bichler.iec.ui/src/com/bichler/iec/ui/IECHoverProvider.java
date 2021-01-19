package com.bichler.iec.ui;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.hover.html.DefaultEObjectHoverProvider;

public class IECHoverProvider extends DefaultEObjectHoverProvider {
	
	@Override
	protected String getHoverInfoAsHtml(EObject o) {
		//if (!hasHover(o))
		//	return null;
		StringBuffer buffer = new StringBuffer();
		buffer.append (getFirstLine(o));
		String documentation = getDocumentation(o);
		if (documentation!=null && documentation.length()>0) {
			buffer.append("<p>");
			buffer.append(documentation);
			buffer.append("</p>");
		}
		return buffer.toString();
	}
	
	protected String getFirstLine(EObject o) {
		String label = getLabel(o);
		return o.eClass().getName()+ ((label != null) ? " <b>"+label+"</b>" : ":<br/> + " );
	}
}
